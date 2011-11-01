#include <jni.h>

#include "logging/DebugLog.h"
#include "application/AugmentedBizzApplication.h"
#include "application/ApplicationStateManager.h"

#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_application_status_ApplicationStateManager_fireApplicationStateChangedEventNative(JNIEnv, jobject, jint jnextState) {
	ApplicationStateManager::getInstance().setNativeApplicationStateOnly(jnextState);
}

JNIEXPORT void JNICALL Java_com_app_augmentedbizz_application_AugmentedBizzApplication_initializeApplicationNative(JNIEnv *env, jobject) {
	AugmentedBizzApplication::getInstance().initializeApplication(env);
	// Check if C++ --> Java calls work
//	ApplicationStateManager::getInstance().setApplicationState(EXITING);
}

#ifdef __cplusplus
}
#endif
