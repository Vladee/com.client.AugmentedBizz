#include <jni.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include <QCAR/QCAR.h>
#include <QCAR/CameraDevice.h>
#include <QCAR/Tracker.h>
#include <QCAR/Tool.h>
#include <QCAR/VideoBackgroundConfig.h>
#include <QCAR/Renderer.h>

#include "logging/DebugLog.h"
#include "application/AugmentedBizzApplication.h"
#include "application/ApplicationStateManager.h"

#ifdef __cplusplus
extern "C" {
#endif

const unsigned short MAX_TRACKABLE_COUNT = 1;

IApplicationFacade* application;
QCAR::Matrix44F projectionMatrix;
unsigned int screenWidth = 0;
unsigned int screenHeight = 0;

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_application_status_ApplicationStateManager_fireApplicationStateChangedEventNative(JNIEnv, jobject, jint jnextState) {
	application->getApplicationStateManager()->setNativeApplicationStateOnly(jnextState);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_application_init_Initializer_initializeApplicationNative(JNIEnv *env, jobject, jobject jAugmentedBizzApplication) {
	application = new AugmentedBizzApplication(env, jAugmentedBizzApplication);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_MainActivity_onDestroyNative(JNIEnv, jobject) {
	delete application;
	application = 0;
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_AugmentedRenderer_initRendering(JNIEnv *env, jobject) {
	QCAR::setHint(QCAR::HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, MAX_TRACKABLE_COUNT);

	//TODO
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_AugmentedRenderer_updateRendering(JNIEnv* env, jobject obj, jint width, jint height) {
	screenWidth = width;
	screenHeight = height;

	//TODO
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_AugmentedRenderer_renderFrame(JNIEnv *env, jobject obj) {
   // Clear color and depth buffer
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

    // Render video background:
    QCAR::State state = QCAR::Renderer::getInstance().begin();

    glEnable(GL_DEPTH_TEST);
    glEnable(GL_CULL_FACE);

    QCAR::Renderer::getInstance().end();
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_setupScreenDimensions(JNIEnv* env, jobject obj, jint width, jint height) {
	screenWidth = width;
	screenHeight = height;
}

void configureVideoBackground()
{
    //get the default video mode:
    QCAR::CameraDevice& cameraDevice = QCAR::CameraDevice::getInstance();
    QCAR::VideoMode videoMode = cameraDevice.getVideoMode(QCAR::CameraDevice::MODE_DEFAULT);

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

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_startCamera(JNIEnv *, jobject) {
	DebugLog::logi("Camera started.");
    // Initialize the camera:
    if(!QCAR::CameraDevice::getInstance().init())
        return;

    // Configure the video background
    configureVideoBackground();

    // Select the default mode:
    if(!QCAR::CameraDevice::getInstance().selectVideoMode(
                                QCAR::CameraDevice::MODE_DEFAULT))
        return;

    // Start the camera:
    if(!QCAR::CameraDevice::getInstance().start())
        return;

    // Start the tracker:
    QCAR::Tracker::getInstance().start();

    // Cache the projection matrix:
    const QCAR::Tracker& tracker = QCAR::Tracker::getInstance();
    const QCAR::CameraCalibration& cameraCalibration = tracker.getCameraCalibration();
    projectionMatrix = QCAR::Tool::getProjectionGL(cameraCalibration, 2.0f, 2000.0f);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_stopCamera(JNIEnv *, jobject) {
    QCAR::Tracker::getInstance().stop();
    QCAR::CameraDevice::getInstance().stop();
    QCAR::CameraDevice::getInstance().deinit();
}

#ifdef __cplusplus
}
#endif
