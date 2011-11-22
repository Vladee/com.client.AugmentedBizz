#include "RenderManager.h"
#include "../logging/DebugLog.h"
#include "../application/ApplicationStateManager.h"

#include <QCAR/QCAR.h>
#include <QCAR/CameraDevice.h>
#include <QCAR/Renderer.h>
#include <QCAR/VideoBackgroundConfig.h>
#include <QCAR/Trackable.h>
#include <QCAR/Tool.h>
#include <QCAR/Tracker.h>
#include <QCAR/Image.h>
#include <QCAR/CameraCalibration.h>
#include "../Utils.h"
#include "CubeShaders.h"

RenderManager::RenderManager(ApplicationStateManager* applicationStateManager,
		ObjectLoader* objectLoader,
		jobject jrenderManager) {
	this->applicationStateManager = applicationStateManager;
	this->renderManagerJavaInterface = new RenderManagerJavaInterface(objectLoader, jrenderManager);

	this->numPixels = 0;
	this->pixelArray = 0;

	this->screenWidth = 0;
	this->screenHeight = 0;

	this->maxTrackableCount = 1;

	this->texture = 0;

	this->numModelElementsToDraw = 0;
	this->vertices = 0;
	this->normals = 0;
	this->texcoords = 0;
	this->indices = 0;
	this->hasIndices = false;
	this->scaleFactor = 0;
}

RenderManager::~RenderManager() {
	delete this->renderManagerJavaInterface;
	this->renderManagerJavaInterface = 0;
}

void RenderManager::initizializeNative(unsigned short screenWidth, unsigned short screenHeight) {
	this->setScreenDimensions(screenWidth, screenHeight);
	QCAR::setHint(QCAR::HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, this->maxTrackableCount);

	// Initialize the camera:
	if(!QCAR::CameraDevice::getInstance().init())
		return;

	// Configure the video background
	configureVideoBackground();

	// Select the default mode:
	if(!QCAR::CameraDevice::getInstance().selectVideoMode(
								QCAR::CameraDevice::MODE_DEFAULT))
		return;

	this->shaderProgramID = SampleUtils::createProgramFromBuffer(cubeMeshVertexShader,
	                                            cubeFragmentShader);
	this->vertexHandle = glGetAttribLocation(shaderProgramID,
	                                            "vertexPosition");
	this->normalHandle = glGetAttribLocation(shaderProgramID,
												"vertexNormal");
	this->textureCoordHandle = glGetAttribLocation(shaderProgramID,
												"vertexTexCoord");
	this->mvpMatrixHandle = glGetUniformLocation(shaderProgramID,
												"modelViewProjectionMatrix");

	// TODO
}

void RenderManager:: setScreenDimensions(unsigned short screenWidth, unsigned short screenHeight) {
	this->screenWidth = screenWidth;
	this->screenHeight = screenHeight;
}

void RenderManager::updateRendering(unsigned short screenWidth, unsigned short screenHeight) {
	this->setScreenDimensions(screenWidth, screenHeight);
	//TODO
}

void RenderManager::startCamera() {
	// Start the camera:
	if(!QCAR::CameraDevice::getInstance().start())
		return;

	// Set the focus mode
	QCAR::CameraDevice::getInstance().setFocusMode(QCAR::CameraDevice::FOCUS_MODE_AUTO);

	// Start the tracker:
	QCAR::Tracker::getInstance().start();

	// Cache the projection matrix:
	const QCAR::Tracker& tracker = QCAR::Tracker::getInstance();
	const QCAR::CameraCalibration& cameraCalibration = tracker.getCameraCalibration();
	projectionMatrix = QCAR::Tool::getProjectionGL(cameraCalibration, 2.0f, 2000.0f);

	DebugLog::logi("Camera started.");
	//this->applicationStateManager->setApplicationState(TRACKING);
}

void RenderManager::stopCamera() {
	QCAR::Tracker::getInstance().stop();
	QCAR::CameraDevice::getInstance().stop();
	QCAR::CameraDevice::getInstance().deinit();

	DebugLog::logi("Camera stopped.");
}

void RenderManager::setModel(JNIEnv *env, jfloatArray jvertices, jfloatArray jnormals, jfloatArray jtexcoords, jshortArray jindices) {
	jboolean copyArrays = true;

	this->vertices = env->GetFloatArrayElements(jvertices, &copyArrays);
	this->normals = env->GetFloatArrayElements(jnormals, &copyArrays);
	this->texcoords = env->GetFloatArrayElements(jtexcoords, &copyArrays);

	this->hasIndices = false;

	if(jindices) {
		this->indices = (unsigned short*)env->GetShortArrayElements(jindices, &copyArrays);
		this->hasIndices = env->GetArrayLength(jindices) > 0;
	}

	this->numModelElementsToDraw = this->hasIndices ?
			env->GetArrayLength(jindices) :
			env->GetArrayLength(jvertices) / 3;
}

void RenderManager::setTexture(jobject jtexture) {
	this->texture = Texture::create(this->renderManagerJavaInterface->getObjectLoader()->getJNIEnv(), jtexture);

	glGenTextures(1, &(this->texture->mTextureID));
	glBindTexture(GL_TEXTURE_2D, this->texture->mTextureID);
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this->texture->mWidth,
			this->texture->mHeight, 0, GL_RGBA, GL_UNSIGNED_BYTE,
		(GLvoid*)  this->texture->mData);
}

void RenderManager::setScaleFactor(float scaleFactor) {
	this->scaleFactor = scaleFactor;
}

void RenderManager::scanFrameForBarcode(QCAR::State& state) {
	// Override private member variable due to
	// some mystery ArrayIndexOutOufBoundsException
	jbyteArray pixelArray = NULL;

	QCAR::Frame frame = state.getFrame();
	for(int i = 0; i < frame.getNumImages(); i++)
	{
		const QCAR::Image *frameImage = frame.getImage(i);
		if(frameImage->getFormat() == QCAR::RGB565)
		{
			const short* pixels = (const short*) frameImage->getPixels();
			int imageWidth = frameImage->getWidth();
			int imageHeight = frameImage->getHeight();
			int curNumPixels = imageWidth * imageHeight;
			//build up the pixel array
			if(pixelArray == NULL || curNumPixels != numPixels)
			{
				numPixels = curNumPixels;
				pixelArray = this->renderManagerJavaInterface->getObjectLoader()->createByteArray(numPixels * 2);
			}
			//fill the pixel array
			this->renderManagerJavaInterface->getObjectLoader()->setByteArrayRegion(pixelArray, 0, numPixels * 2, (const jbyte*)pixels);
			this->renderManagerJavaInterface->callScanner(imageWidth, imageHeight, pixelArray);
		}
	}
}

void RenderManager::renderFrame() {
    // Clear color and depth buffer
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// Render video background:
	QCAR::State state = QCAR::Renderer::getInstance().begin();

	if(state.getNumActiveTrackables() > 0) {
		if(this->applicationStateManager->getCurrentApplicationState() == TRACKING) {
			this->applicationStateManager->setApplicationState(TRACKED);
			//focus the trackable
			QCAR::CameraDevice::getInstance().startAutoFocus();
		} else if(this->applicationStateManager->getCurrentApplicationState() == TRACKED) {
			scanFrameForBarcode(state);
		} else if(this->applicationStateManager->getCurrentApplicationState() == SHOWING_CACHE ||
				  this->applicationStateManager->getCurrentApplicationState() == LOADING_INDICATORS ||
				  this->applicationStateManager->getCurrentApplicationState() == SHOWING) {

			glEnable(GL_DEPTH_TEST);
			glEnable(GL_CULL_FACE);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

			// Get the trackable (only one available)
			const QCAR::Trackable* trackable = state.getActiveTrackable(0);
			QCAR::Matrix44F modelViewMatrix =
				QCAR::Tool::convertPose2GLMatrix(trackable->getPose());

			const Texture* const thisTexture = this->texture;

			QCAR::Matrix44F modelViewProjection;

			SampleUtils::translatePoseMatrix(0.0f, 0.0f, this->scaleFactor,
											 &modelViewMatrix.data[0]);
			SampleUtils::scalePoseMatrix(this->scaleFactor, this->scaleFactor, this->scaleFactor,
										 &modelViewMatrix.data[0]);
			SampleUtils::multiplyMatrix(&projectionMatrix.data[0],
										&modelViewMatrix.data[0] ,
										&modelViewProjection.data[0]);
			glUseProgram(shaderProgramID);

			if(this->vertices != NULL &&
			   this->normals != NULL &&
			   this->texcoords != NULL &&
			   thisTexture != NULL) {

				glVertexAttribPointer(vertexHandle, 3, GL_FLOAT, GL_FALSE, 0,
									  (const GLvoid*) &this->vertices[0]);
				glVertexAttribPointer(normalHandle, 3, GL_FLOAT, GL_FALSE, 0,
									  (const GLvoid*) &this->normals[0]);
				glVertexAttribPointer(textureCoordHandle, 2, GL_FLOAT, GL_FALSE, 0,
									  (const GLvoid*) &this->texcoords[0]);

				glEnableVertexAttribArray(vertexHandle);
				glEnableVertexAttribArray(normalHandle);
				glEnableVertexAttribArray(textureCoordHandle);

				glActiveTexture(GL_TEXTURE0);
				glBindTexture(GL_TEXTURE_2D, thisTexture->mTextureID);
				glUniformMatrix4fv(mvpMatrixHandle, 1, GL_FALSE,
								   (GLfloat*)&modelViewProjection.data[0] );


				if(hasIndices) {
					glDrawElements(GL_TRIANGLES, this->numModelElementsToDraw, GL_UNSIGNED_SHORT, (const GLvoid*) &(this->indices[0]));
				} else {
					glDrawArrays(GL_TRIANGLES, 0, this->numModelElementsToDraw);
				}

			}

			glDisable(GL_DEPTH_TEST);

			glDisableVertexAttribArray(vertexHandle);
			glDisableVertexAttribArray(normalHandle);
			glDisableVertexAttribArray(textureCoordHandle);
		}

	} else if(this->applicationStateManager->getCurrentApplicationState() != TRACKING) {
		this->applicationStateManager->setApplicationState(TRACKING);
	}

	QCAR::Renderer::getInstance().end();

	glFinish();
}


void RenderManager::configureVideoBackground() {
    //get the default video mode:
    QCAR::CameraDevice& cameraDevice = QCAR::CameraDevice::getInstance();
    QCAR::VideoMode videoMode = cameraDevice.getVideoMode(QCAR::CameraDevice::MODE_OPTIMIZE_QUALITY);

    //set frame format
    QCAR::setFrameFormat(QCAR::RGB565, true);

    //configure the video background
    QCAR::VideoBackgroundConfig config;
    config.mEnabled = true;
    config.mSynchronous = true;
    config.mPosition.data[0] = 0.0f;
    config.mPosition.data[1] = 0.0f;

    config.mSize.data[0] = screenWidth;
    config.mSize.data[1] = videoMode.mHeight * (screenWidth / (float)videoMode.mWidth);

    //set the config:
    QCAR::Renderer::getInstance().setVideoBackgroundConfig(config);
}

// *************************************************************

RenderManagerJavaInterface::RenderManagerJavaInterface(ObjectLoader* objectLoader, jobject javaRenderManager): \
		JavaInterface(objectLoader) {
	this->javaRenderManager = javaRenderManager;
}

jclass RenderManagerJavaInterface::getClass() {
	return this->getObjectLoader()->getObjectClass(this->javaRenderManager);
}

void RenderManagerJavaInterface::callScanner(unsigned int width, unsigned int height, jbyteArray pixels) {
	this->getObjectLoader()->getJNIEnv()->CallVoidMethod(this->javaRenderManager, \
				this->getCallScannerMethodID(), width, height, pixels);
}

jmethodID RenderManagerJavaInterface::getCallScannerMethodID() {
	return this->getMethodID("callScanner", \
			"(II[B)V");
}
