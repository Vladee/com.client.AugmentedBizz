#include <jni.h>

#include "logging/DebugLog.h"
#include "application/AugmentedBizzApplication.h"
#include "application/ApplicationStateManager.h"

#ifdef __cplusplus
extern "C" {
#endif

IApplicationFacade* application;


JNIEXPORT void JNICALL Java_com_app_augmentedbizz_application_status_ApplicationStateManager_fireApplicationStateChangedEventNative(JNIEnv, jobject, jint jnextState) {
	application->getApplicationStateManager()->setNativeApplicationStateOnly(jnextState);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_application_init_Initializer_initializeApplicationNative(JNIEnv *env, jobject jAugmentedBizzApplication) {
	application = new AugmentedBizzApplication(env, jAugmentedBizzApplication);
	// Check if C++ --> Java calls work
	//application->getApplicationStateManager()->setApplicationState(EXITING);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_ui_MainActivity_onDestroyNative(JNIEnv, jobject) {
	delete application;
	application = 0;
}

#ifdef __cplusplus
}
#endif
