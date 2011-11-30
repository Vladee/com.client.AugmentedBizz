#ifndef _DEBUGLOG_H_
#define _DEBUGLOG_H_

#include <jni.h>
#include <android/log.h>
#include <string>
#include "../application/ObjectLoader.h"

#define LOG_TAG "ARBIZZ"
#define LOG_PREFIX "Native: "

class DebugLog;
class DebugLogJavaInterface;

class DebugLog {
	public:
		static void initialize(ObjectLoader*);
		static void logd(std::string);
		static void loge(std::string);
		static void logi(std::string);
		static void logv(std::string);
		static void logw(std::string);
	private:
		static DebugLogJavaInterface* debugLogJavaInterface;
		static void destroy();
};

class DebugLogJavaInterface: public JavaInterface {
	public:
	DebugLogJavaInterface(ObjectLoader*);
		void callLogd(std::string);
		void callLoge(std::string);
		void callLogi(std::string);
		void callLogv(std::string);
		void callLogw(std::string);
	protected:
		virtual jclass getClass();
	private:
		jmethodID getLogdMethodID();
		jmethodID getLogeMethodID();
		jmethodID getLogiMethodID();
		jmethodID getLogvMethodID();
		jmethodID getLogwMethodID();
		jmethodID getLogMethodID(char);
};

#endif // _DEBUGLOG_H_
