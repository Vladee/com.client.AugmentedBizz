#include "ApplicationStateManager.h"
#include "AugmentedBizzApplication.h"

ApplicationStateManager::ApplicationStateManager() {
	this->currentState = UNINITIATED;

	this->applicationStateNames[0] = "Uninitiated";
	this->applicationStateNames[1] = "Initializing";
	this->applicationStateNames[2] = "Tracking";
	this->applicationStateNames[3] = "Captured";
	this->applicationStateNames[4] = "Scanned";
	this->applicationStateNames[5] = "Loading";
	this->applicationStateNames[6] = "Showing cache";
	this->applicationStateNames[7] = "Loading indicators";
	this->applicationStateNames[8] = "Showing";
	this->applicationStateNames[9] = "Deinitializing";
	this->applicationStateNames[10] = "Exiting";

	this->applicationStateListeners.push_back(this);
}

ApplicationStateManager& ApplicationStateManager::getInstance() {
	static ApplicationStateManager instance;
	return instance;
}

void ApplicationStateManager::setApplicationState(ApplicationState nextState) {
	DebugLog::logi("In setApplicationState()");
	this->fireApplicationStateChangedEvent(nextState);

	DebugLog::logi("Still in setApplicationState()");

	JNIEnv* env = AugmentedBizzApplication::getInstance().getJNIEnv();

	if(env != 0) {
		DebugLog::logi("JNIEnv found.");
	} else {
		DebugLog::loge("JNIEnv not found.");
	}

	jclass javaApplicationStateManager = env->FindClass("com/app/augmentedbizz/application/status/ApplicationStateManager");

	if(javaApplicationStateManager != 0) {
		DebugLog::logi("javaApplicationStateManager found.");
	} else {
		DebugLog::loge("javaApplicationStateManager not found.");
	}

	jmethodID fireApplicationStateChangedEventID = env->GetMethodID(javaApplicationStateManager, "fireApplicationStateChangedEvent", "(I)V");

	if(fireApplicationStateChangedEventID != 0) {
		DebugLog::logi("fireApplicationStateChangedEventID found.");
	} else {
		DebugLog::loge("fireApplicationStateChangedEventID not found.");
	}

	env->CallVoidMethod(javaApplicationStateManager, fireApplicationStateChangedEventID, nextState);
}

void ApplicationStateManager::setNativeApplicationStateOnly(int nextState) {
	this->fireApplicationStateChangedEvent(ApplicationState(nextState));
}

void ApplicationStateManager::onApplicationStateChange(ApplicationState nextState) {
	DebugLog::logi("Application state changed. Moved from " + \
			this->applicationStateNames[this->currentState] + " to " + \
			this->applicationStateNames[nextState] + ".");
	this->currentState = nextState;
}

void ApplicationStateManager::addApplicationStateListener(IApplicationStateListener* listener) {
	this->applicationStateListeners.push_back(listener);
}

void ApplicationStateManager::removeApplicationStateListener(IApplicationStateListener* listener) {
	this->applicationStateListeners.remove(listener);
}

ApplicationState ApplicationStateManager::getCurrentApplicationState() {
	return this->currentState;
}

void ApplicationStateManager::fireApplicationStateChangedEvent(ApplicationState nextState) {
	 for(std::list<IApplicationStateListener*>::iterator i = this->applicationStateListeners.begin();
			 i != this->applicationStateListeners.end(); ++i) {
		 (*i)->onApplicationStateChange(nextState);
	 }
}
