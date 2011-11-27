#include "DebugLog.h"
#include <cstring>

void DebugLog::logd(std::string message) {
	DebugLog::log(ANDROID_LOG_DEBUG, message);
}

void DebugLog::loge(std::string message) {
	DebugLog::log(ANDROID_LOG_ERROR, message);
}

void DebugLog::logi(std::string message) {
	DebugLog::log(ANDROID_LOG_INFO, message);
}

void DebugLog::logv(std::string message) {
	DebugLog::log(ANDROID_LOG_VERBOSE, message);
}

void DebugLog::logw(std::string message) {
	DebugLog::log(ANDROID_LOG_WARN, message);
}

void DebugLog::log(android_LogPriority logPriority, std::string message) {
//	__android_log_print(logPriority,LOG_TAG,DebugLog::makeMessage(message));
}

char* DebugLog::makeMessage(std::string message) {
//	std::string messageWithPrefix = LOG_PREFIX + message;
//	char *result = new char[messageWithPrefix.length()];
//	strcpy(result,messageWithPrefix.c_str());
//	return result;
}
