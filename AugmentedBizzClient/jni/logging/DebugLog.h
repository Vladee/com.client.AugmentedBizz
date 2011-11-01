#ifndef _DEBUGLOG_H_
#define _DEBUGLOG_H_

#include <jni.h>
#include <android/log.h>
#include <string>

#define LOG_TAG "ARBIZZ"
#define LOG_PREFIX "Native: "

class DebugLog {
	public:
		static void logd(std::string message);
		static void loge(std::string message);
		static void logi(std::string message);
		static void logv(std::string message);
		static void logw(std::string message);
	private:
		static void log(android_LogPriority, std::string);
		static char* makeMessage(std::string message);
};

#endif // _DEBUGLOG_H_
