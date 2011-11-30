#include "DebugLog.h"
#include <cstring>
#include "android/log.h"

DebugLogJavaInterface* DebugLog::debugLogJavaInterface = 0;

void DebugLog::initialize(ObjectLoader* objectLoader) {
	DebugLog::debugLogJavaInterface = new DebugLogJavaInterface(objectLoader);
	atexit(DebugLog::destroy);
}

void DebugLog::logd(std::string message) {
	if(DebugLog::debugLogJavaInterface == 0) return;
	//DebugLog::debugLogJavaInterface->callLogd(message);
}

void DebugLog::loge(std::string message) {
	if(DebugLog::debugLogJavaInterface == 0) return;
	//DebugLog::debugLogJavaInterface->callLoge(message);
}

void DebugLog::logi(std::string message) {
	if(DebugLog::debugLogJavaInterface == 0) return;
	//DebugLog::debugLogJavaInterface->callLogi(message);
}

void DebugLog::logv(std::string message) {
	if(DebugLog::debugLogJavaInterface == 0) return;
	//DebugLog::debugLogJavaInterface->callLogv(message);
}

void DebugLog::logw(std::string message) {
	if(DebugLog::debugLogJavaInterface == 0) return;
	//DebugLog::debugLogJavaInterface->callLogw(message);
}

void DebugLog::destroy() {
	if(DebugLog::debugLogJavaInterface != 0) {
		delete DebugLog::debugLogJavaInterface;
		DebugLog::debugLogJavaInterface = 0;
	}
}

// **********************************************************************************************

DebugLogJavaInterface::DebugLogJavaInterface(ObjectLoader *objectLoader) \
		: JavaInterface(objectLoader) {
}

jclass DebugLogJavaInterface::getClass() {
	return this->getObjectLoader()->findClass("com/app/augmentedbizz/logging/DebugLog");
}

jmethodID DebugLogJavaInterface::getLogdMethodID() {
	return this->getLogMethodID('d');
}

jmethodID DebugLogJavaInterface::getLogeMethodID() {
	return this->getLogMethodID('e');
}

jmethodID DebugLogJavaInterface::getLogiMethodID() {
	return this->getLogMethodID('i');
}

jmethodID DebugLogJavaInterface::getLogvMethodID() {
	return this->getLogMethodID('v');
}

jmethodID DebugLogJavaInterface::getLogwMethodID() {
	return this->getLogMethodID('w');
}

jmethodID DebugLogJavaInterface::getLogMethodID(char methodType) {
	__android_log_print(ANDROID_LOG_DEBUG, "ARBIZZ", "%s", "getLogMethodID()");
	jmethodID methodID = this->getStaticMethodID("nativeLog" + methodType, "(Ljava/lang/String;)V");
	__android_log_print(ANDROID_LOG_DEBUG, "ARBIZZ", "%s", "methodID is there");
	__android_log_print(ANDROID_LOG_DEBUG, "ARBIZZ", "%s", methodID == 0 ? "0" : "1");
	return methodID;
}

void DebugLogJavaInterface::callLogd(std::string message) {
	this->getObjectLoader()->getJNIEnv()->CallStaticVoidMethod(this->getClass(),
				this->getLogdMethodID(), message.c_str());
}

void DebugLogJavaInterface::callLoge(std::string message) {
	this->getObjectLoader()->getJNIEnv()->CallStaticVoidMethod(this->getClass(),
				this->getLogeMethodID(), message.c_str());
}
void DebugLogJavaInterface::callLogi(std::string message) {
	this->getObjectLoader()->getJNIEnv()->CallStaticVoidMethod(this->getClass(),
				this->getLogiMethodID(), message.c_str());
}
void DebugLogJavaInterface::callLogv(std::string message) {
	this->getObjectLoader()->getJNIEnv()->CallStaticVoidMethod(this->getClass(),
				this->getLogvMethodID(), message.c_str());
}
void DebugLogJavaInterface::callLogw(std::string message) {
	this->getObjectLoader()->getJNIEnv()->CallStaticVoidMethod(this->getClass(),
				this->getLogwMethodID(), message.c_str());
}
