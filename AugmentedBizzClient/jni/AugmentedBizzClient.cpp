#include <jni.h>

#include "application/AugmentedBizzApplication.h"
#include "application/ApplicationStateManager.h"

#ifdef __cplusplus
extern "C" {
#endif

IApplicationFacade* application;

// ************ AugmentedBizzApplication ************

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_application_init_Initializer_initializeApplicationNative(JNIEnv* env, jobject, jobject jAugmentedBizzApplication) {
	application = new AugmentedBizzApplication(env, jAugmentedBizzApplication);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_MainActivity_onDestroyNative(JNIEnv*, jobject) {
	delete application;
	application = 0;
}



// ************ ApplicationStateManager *************

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_application_status_ApplicationStateManager_fireApplicationStateChangedEventNative(JNIEnv*, jobject, jint jnextState) {
	if(application != NULL && application->getApplicationStateManager() != NULL) {
		application->getApplicationStateManager()->setNativeApplicationStateOnly(jnextState);
	}
}



// ************ RenderManager ***********************

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_initializeNative(JNIEnv* env, jobject, jshort width, jshort height) {
	application->getRenderManager()->inititializeNative(width, height);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_AugmentedRenderer_initRendering(JNIEnv*, jobject) {
	application->getRenderManager()->initRendering();
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_AugmentedRenderer_updateRendering(JNIEnv*, jobject, jshort width, jshort height) {
	application->getRenderManager()->updateRendering(width, height);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_AugmentedRenderer_renderFrame(JNIEnv*, jobject) {
	application->getRenderManager()->renderFrame();
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_startCamera(JNIEnv *, jobject) {
	application->getRenderManager()->startCamera();
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_stopCamera(JNIEnv *, jobject) {
	application->getRenderManager()->stopCamera();
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_setModel(JNIEnv* env, jobject,
			jfloatArray jvertices,
			jfloatArray jnormals,
			jfloatArray jtexcoords,
			jshortArray jindices) {
	application->getRenderManager()->setModel(env, jvertices, jnormals, jtexcoords, jindices);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_setTexture(JNIEnv*, jobject, jobject jtexture) {
	application->getRenderManager()->setTexture(jtexture);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_renderer_RenderManager_setScaleFactor(JNIEnv*, jobject, jfloat jscaleFactor) {
	application->getRenderManager()->setScaleFactor(jscaleFactor);
}

#ifdef __cplusplus
}
#endif
