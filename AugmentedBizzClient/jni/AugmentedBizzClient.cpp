#include <jni.h>

#include "application/AugmentedBizzApplication.h"
#include "application/ApplicationStateManager.h"

#ifdef __cplusplus
extern "C" {
#endif

IApplicationFacade* application;

// ************ AugmentedBizzApplication ************

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_application_init_Initializer_initializeApplicationNative(JNIEnv *env, jobject, jobject jAugmentedBizzApplication) {
	application = new AugmentedBizzApplication(env, jAugmentedBizzApplication);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_MainActivity_onDestroyNative(JNIEnv, jobject) {
	delete application;
	application = 0;
}



// ************ ApplicationStateManager *************

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_application_status_ApplicationStateManager_fireApplicationStateChangedEventNative(JNIEnv, jobject, jint jnextState) {
	application->getApplicationStateManager()->setNativeApplicationStateOnly(jnextState);
}



// ************ RenderManager ***********************

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_initializeNative(JNIEnv *env, jobject, jshort width, jshort height) {
	application->getRenderManager()->initizializeNative(width, height);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_AugmentedRenderer_updateRendering(JNIEnv*, jobject, jshort width, jshort height) {
	application->getRenderManager()->updateRendering(width, height);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_AugmentedRenderer_renderFrame(JNIEnv, jobject) {
	application->getRenderManager()->renderFrame();
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_startCamera(JNIEnv *, jobject) {
	application->getRenderManager()->startCamera();
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_stopCamera(JNIEnv *, jobject) {
	application->getRenderManager()->stopCamera();
}

#ifdef __cplusplus
}
#endif
