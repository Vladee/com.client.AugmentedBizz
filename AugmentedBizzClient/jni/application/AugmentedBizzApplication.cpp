#include "AugmentedBizzApplication.h"
#include "../logging/DebugLog.h"

AugmentedBizzApplication& AugmentedBizzApplication::getInstance() {
	static AugmentedBizzApplication instance;
	return instance;
}

void AugmentedBizzApplication::initializeApplication(JNIEnv *env) {
	DebugLog::logi("Initializing application.");
	JavaVM **ptr = &(this->javaVM);

	jint result = env->GetJavaVM(ptr);
	if(result < 0) {
		DebugLog::loge("Unable to retrieve JavaVM: GetJavaVM() returned with error code " + result);
	} else {
		DebugLog::logi("JavaVM successfully retrieved.");
	}
}

JavaVM* AugmentedBizzApplication::getJavaVM() {
	return this->javaVM;
}

JNIEnv* AugmentedBizzApplication::getJNIEnv() {
	JNIEnv *env;
	jint result = this->javaVM->GetEnv((void**)&(this->javaVM), 0x00010001);
	if(result < 0) {
		DebugLog::loge("Unable to retrieve JNIEnv: GetEnv() returned with error code " + result);
	}
	return env;
}
