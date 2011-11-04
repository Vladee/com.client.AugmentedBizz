#include "ObjectLoader.h"
#include <stdarg.h>
#include "../logging/DebugLog.h"

ObjectLoader::ObjectLoader(JNIEnv *env) {
	JavaVM *javaVM;

	jint result = env->GetJavaVM(&javaVM);
	if(result < 0) {
		DebugLog::loge("Unable to retrieve JavaVM: GetJavaVM() returned with error code " + result);
	} else {
		DebugLog::logi("JavaVM successfully retrieved.");
	}

	this->javaVM = javaVM;
}

ObjectLoader::~ObjectLoader() {
	this->javaVM = 0;
}

jclass ObjectLoader::findClass(std::string className) {
	jclass clazz = this->getJNIEnv()->FindClass(className.c_str());

	if(clazz == NULL) {
		DebugLog::loge("Class " + className + " could not be found.");
	}

	return clazz;
}

jclass ObjectLoader::getObjectClass(jobject object) {
	jclass clazz = this->getJNIEnv()->GetObjectClass(object);
}

jmethodID ObjectLoader::getMethodID(jclass clazz, std::string methodName, std::string methodSignature) {
	jmethodID methodID = this->getJNIEnv()->GetMethodID(clazz, methodName.c_str(), methodSignature.c_str());

	if(methodID == 0) {
		DebugLog::loge("Method " + methodName + methodSignature + " could not be found.");
	}
}

void ObjectLoader::callVoidMethod(jobject object, jmethodID methodID, ...) {
	// Get parameter list
	va_list vl;
	va_start(vl, methodID);

	this->getJNIEnv()->CallVoidMethod(object, methodID, vl);
}

jobject ObjectLoader::callObjectMethod(jobject object, jmethodID methodID, ...) {
	// Get parameter list
	va_list vl;
	va_start(vl, methodID);

	return this->getJNIEnv()->CallObjectMethod(object, methodID, vl);
}

JNIEnv* ObjectLoader::getJNIEnv() {
	JNIEnv *env;

	int result = this->javaVM->GetEnv((void**) &env, JNI_VERSION_1_6);
	if(result != JNI_OK) {
		DebugLog::loge("Unable to retrieve JNIEnv.");
	}

	return env;
}

// --------------------------------- Java Interface ------------------------------

JavaInterface::JavaInterface(ObjectLoader* objectLoader) {
	this->objectLoader = objectLoader;
}

jmethodID JavaInterface::getMethodID(std::string methodName, std::string methodSignature) {
	jclass clazz = this->getClass();
	return this->objectLoader->getMethodID(clazz, methodName, methodSignature);
}

ObjectLoader* JavaInterface::getObjectLoader() {
	return this->objectLoader;
}
