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
	this->scanCounter = 0;

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
	this->scaleFactor = 5.0f;
}

RenderManager::~RenderManager() {
	delete this->renderManagerJavaInterface;
	this->renderManagerJavaInterface = 0;

	releaseModel();
}

void RenderManager::inititializeNative(unsigned short screenWidth, unsigned short screenHeight) {
	this->setScreenDimensions(screenWidth, screenHeight);
	QCAR::setHint(QCAR::HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, this->maxTrackableCount);

	// Initialize the camera:
	if(!QCAR::CameraDevice::getInstance().init())
		return;

	// Select the default mode:
	if(!QCAR::CameraDevice::getInstance().selectVideoMode(QCAR::CameraDevice::MODE_DEFAULT))
		return;

	// Configure the video background
	configureVideoBackground();

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
	// Define clear color
	glClearColor(0.0f, 0.0f, 0.0f, QCAR::requiresAlpha() ? 0.0f : 1.0f);

	SampleUtils::checkGlError("Augmented Bizz GL init error");
}

void RenderManager:: setScreenDimensions(unsigned short screenWidth, unsigned short screenHeight) {
	this->screenWidth = screenWidth;
	this->screenHeight = screenHeight;
}

void RenderManager::updateRendering(unsigned short screenWidth, unsigned short screenHeight) {
	this->setScreenDimensions(screenWidth, screenHeight);

	// Configure the video background
	configureVideoBackground();
}

void RenderManager::initRendering() {
    // Define clear color
    glClearColor(0.0f, 0.0f, 0.0f, QCAR::requiresAlpha() ? 0.0f : 1.0f);

	// Load the shader and create the handles
    shaderProgramID     = SampleUtils::createProgramFromBuffer(cubeMeshVertexShader,
                                                            cubeFragmentShader);

    vertexHandle        = glGetAttribLocation(shaderProgramID,
                                                "vertexPosition");
    normalHandle        = glGetAttribLocation(shaderProgramID,
                                                "vertexNormal");
    textureCoordHandle  = glGetAttribLocation(shaderProgramID,
                                                "vertexTexCoord");
    mvpMatrixHandle     = glGetUniformLocation(shaderProgramID,
                                                "modelViewProjectionMatrix");
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
}

void RenderManager::stopCamera() {
	QCAR::Tracker::getInstance().stop();
	QCAR::CameraDevice::getInstance().stop();
	QCAR::CameraDevice::getInstance().deinit();

	DebugLog::logi("Camera stopped.");
}

void RenderManager::setModel(JNIEnv *env, jfloatArray jvertices, jfloatArray jnormals, jfloatArray jtexcoords, jshortArray jindices) {
	this->jVertices = jvertices;
	this->jNormals = jnormals;
	this->jTexcoords = jtexcoords;
	this->jIndices = jindices;

	jboolean copyArrays = true;

	this->vertices = env->GetFloatArrayElements(jvertices, &copyArrays);
	this->normals = env->GetFloatArrayElements(jnormals, &copyArrays);
	this->texcoords = env->GetFloatArrayElements(jtexcoords, &copyArrays);

	this->hasIndices = false;

	if(jindices) {
		this->indices = (unsigned short*)env->GetShortArrayElements(jindices, &copyArrays);
		this->hasIndices = env->GetArrayLength(jindices) > 0;
	}

	if(hasIndices) {
		DebugLog::logi("<GL> Loaded model with indices");
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
	QCAR::Frame frame = state.getFrame();
	for(int i = 0; i < frame.getNumImages(); i++) {
		const QCAR::Image *frameImage = frame.getImage(i);
		if(frameImage->getFormat() == QCAR::RGB565) {
			jbyteArray pixelArray = NULL;
			const char* pixels = (const char*) frameImage->getPixels();
			int imageWidth = frameImage->getWidth();
			int imageHeight = frameImage->getHeight();
			int curNumPixels = imageWidth * imageHeight;
			//build up the pixel array
			if(pixelArray == NULL || curNumPixels != numPixels) {
				pixelArray = this->renderManagerJavaInterface->getObjectLoader()->createByteArray(curNumPixels * 2);
			}
			//fill the pixel array
			this->renderManagerJavaInterface->getObjectLoader()->setByteArrayRegion(pixelArray, 0, curNumPixels * 2, (const jbyte*)pixels);
			this->renderManagerJavaInterface->callScanner(imageWidth, imageHeight, pixelArray);
			//release pixel data
			this->renderManagerJavaInterface->getObjectLoader()->getJNIEnv()->ReleaseByteArrayElements(pixelArray, (jbyte*)pixels, 0);
		}
	}
}

void RenderManager::renderModel(QCAR::State& state) {
	if(this->vertices &&
	   this->normals &&
	   this->texcoords &&
	   this->texture) {
		// Get the trackable (only one available)
		const QCAR::Trackable* trackable = state.getActiveTrackable(0);
		QCAR::Matrix44F modelViewMatrix =
			QCAR::Tool::convertPose2GLMatrix(trackable->getPose());

		QCAR::Matrix44F modelViewProjection;

		SampleUtils::translatePoseMatrix(0.0f, 0.0f, this->scaleFactor,
										 &modelViewMatrix.data[0]);
		SampleUtils::scalePoseMatrix(this->scaleFactor, this->scaleFactor, this->scaleFactor,
									 &modelViewMatrix.data[0]);
		SampleUtils::multiplyMatrix(&projectionMatrix.data[0],
									&modelViewMatrix.data[0] ,
									&modelViewProjection.data[0]);
		glUseProgram(shaderProgramID);

		glVertexAttribPointer(vertexHandle, 3, GL_FLOAT, GL_FALSE, 0,
							  (const GLvoid*) this->vertices);
		glVertexAttribPointer(normalHandle, 3, GL_FLOAT, GL_FALSE, 0,
							  (const GLvoid*) this->normals);
		glVertexAttribPointer(textureCoordHandle, 2, GL_FLOAT, GL_FALSE, 0,
							  (const GLvoid*) this->texcoords);

		glEnableVertexAttribArray(vertexHandle);
		glEnableVertexAttribArray(normalHandle);
		glEnableVertexAttribArray(textureCoordHandle);

		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texture->mTextureID);

		glUniformMatrix4fv(mvpMatrixHandle, 1, GL_FALSE,
						   (GLfloat*)&modelViewProjection.data[0] );

		if(hasIndices) {
			glDrawElements(GL_TRIANGLES, this->numModelElementsToDraw, GL_UNSIGNED_SHORT, (const GLvoid*) this->indices);
		} else {
			glDrawArrays(GL_TRIANGLES, 0, this->numModelElementsToDraw);
		}
	}
}

void RenderManager::releaseModel() {
	JNIEnv* env = this->renderManagerJavaInterface->getObjectLoader()->getJNIEnv();

	if(this->vertices) {
		env->ReleaseFloatArrayElements(this->jVertices, this->vertices, 0);
		this->vertices = 0;
		this->jVertices = 0;
	}
	if(this->normals) {
		env->ReleaseFloatArrayElements(this->jNormals, this->normals, 0);
		this->normals = 0;
		this->jNormals;
	}
	if(this->texcoords) {
		env->ReleaseFloatArrayElements(this->jTexcoords, this->texcoords, 0);
		this->texcoords = 0;
		this->jTexcoords = 0;
	}
	if(this->indices) {
		env->ReleaseShortArrayElements(this->jIndices, (short*)this->indices, 0);
		this->indices = 0;
		this->jIndices = 0;
	}
	if(this->texture) {
		glBindTexture(GL_TEXTURE_2D, 0);
		glDeleteTextures(1, &(this->texture->mTextureID));

		delete this->texture;
		this->texture = 0;
	}
	setScaleFactor(1.0f);
}

void RenderManager::renderFrame() {
    // Clear color and depth buffer
	glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	// Render video background:
	QCAR::State state = QCAR::Renderer::getInstance().begin();

	if(state.getNumActiveTrackables() > 0) {
		if(this->applicationStateManager->getCurrentApplicationState() == TRACKING) {
			this->applicationStateManager->setApplicationState(TRACKED);
			this->scanCounter = 0;
			//focus the trackable
			QCAR::CameraDevice::getInstance().startAutoFocus();
		} else if(this->applicationStateManager->getCurrentApplicationState() == TRACKED) {
			++this->scanCounter;
			if(scanCounter > 10) {
				QCAR::CameraDevice::getInstance().startAutoFocus();
				this->scanCounter = 0;
			}
			scanFrameForBarcode(state);
		} else if(this->applicationStateManager->getCurrentApplicationState() == SHOWING_CACHE ||
				  this->applicationStateManager->getCurrentApplicationState() == LOADING_INDICATORS ||
				  this->applicationStateManager->getCurrentApplicationState() == SHOWING) {

			glEnable(GL_DEPTH_TEST);
			glEnable(GL_CULL_FACE);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

			renderModel(state);
		}

	} else if(this->applicationStateManager->getCurrentApplicationState() != TRACKING) {
		this->applicationStateManager->setApplicationState(TRACKING);
		releaseModel();
	}

	SampleUtils::checkGlError("Augmented Bizz GL error");

	glDisable(GL_DEPTH_TEST);

	glDisableVertexAttribArray(vertexHandle);
	glDisableVertexAttribArray(normalHandle);
	glDisableVertexAttribArray(textureCoordHandle);

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
