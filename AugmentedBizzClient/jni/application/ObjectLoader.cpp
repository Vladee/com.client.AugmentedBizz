#include "ObjectLoader.h"
#include <stdarg.h>
#include "../logging/DebugLog.h"
#include "../Utils.h"

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

jmethodID ObjectLoader::getStaticMethodID(jclass clazz, std::string methodName, std::string methodSignature) {
	jmethodID methodID = this->getJNIEnv()->GetMethodID(clazz, methodName.c_str(), methodSignature.c_str());
	if(methodID == 0) {
		DebugLog::loge("Static method " + methodName + methodSignature + " could not be found.");
	}
}

jbyteArray ObjectLoader::createByteArray(unsigned int size) {
	return this->getJNIEnv()->NewByteArray(size);
}

void ObjectLoader::setByteArrayRegion(jbyteArray array, unsigned int start, unsigned int end, const jbyte* bytes) {
	return this->getJNIEnv()->SetByteArrayRegion(array, start, end, bytes);
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

jmethodID JavaInterface::getStaticMethodID(std::string methodName, std::string methodSignature) {
	jclass clazz = this->getClass();
	return this->objectLoader->getMethodID(clazz, methodName, methodSignature);
}

ObjectLoader* JavaInterface::getObjectLoader() {
	return this->objectLoader;
}
