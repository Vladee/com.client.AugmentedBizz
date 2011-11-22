#include "ApplicationStateManager.h"
#include "AugmentedBizzApplication.h"

ApplicationStateManager::ApplicationStateManager(ObjectLoader *objectLoader, jobject javaApplicationStateManager) {
	this->currentState = INITIALIZING;
	DebugLog::logi("Current State is: Initializing.");

	this->applicationStateManagerJavaInterface = \
			new ApplicationStateManagerJavaInterface(objectLoader, javaApplicationStateManager);

	this->applicationStateNames[0] = "Uninitiated";
	this->applicationStateNames[1] = "Initializing";
	this->applicationStateNames[2] = "Initialized";
	this->applicationStateNames[3] = "Tracking";
	this->applicationStateNames[4] = "Tracked";
	this->applicationStateNames[5] = "Scanning";
	this->applicationStateNames[6] = "Scanned";
	this->applicationStateNames[7] = "Loading";
	this->applicationStateNames[8] = "Showing cache";
	this->applicationStateNames[9] = "Loading indicators";
	this->applicationStateNames[10] = "Showing";
	this->applicationStateNames[11] = "Deinitializing";
	this->applicationStateNames[12] = "Exiting";

	this->applicationStateListeners.push_back(this);

	//initialize the state mutex
	pthread_mutex_init(this->stateMutex, NULL);
}

ApplicationStateManager::~ApplicationStateManager() {
	delete this->applicationStateManagerJavaInterface;
	this->applicationStateManagerJavaInterface = 0;

	//destroy the state mutex
	pthread_mutex_destroy(this->stateMutex);
}

void ApplicationStateManager::setApplicationState(ApplicationState nextState) {
	if(nextState != currentState) {
		pthread_mutex_lock(this->stateMutex);
		this->fireApplicationStateChangedEvent(nextState);
		this->applicationStateManagerJavaInterface->setJavaApplicationState(nextState);
		pthread_mutex_unlock(this->stateMutex);
	}
}

void ApplicationStateManager::setNativeApplicationStateOnly(int nextState) {
	if(nextState != currentState) {
		this->fireApplicationStateChangedEvent(ApplicationState(nextState));
	}
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



// --------------------------------- Java Interface -----------------------------------

ApplicationStateManagerJavaInterface::ApplicationStateManagerJavaInterface(ObjectLoader *objectLoader, \
		jobject javaApplicationStateManager) : JavaInterface(objectLoader) {
	this->javaApplicationStateManager = javaApplicationStateManager;
}

jclass ApplicationStateManagerJavaInterface::getClass() {
	return this->getObjectLoader()->getObjectClass(this->javaApplicationStateManager);
}

jmethodID ApplicationStateManagerJavaInterface::getJavaFireApplicationStateChangedEventMethodID() {
	return this->getMethodID("fireApplicationStateChangedEvent", "(I)V");
}

void ApplicationStateManagerJavaInterface::setJavaApplicationState(ApplicationState nextState) {
	this->getObjectLoader()->getJNIEnv()->CallVoidMethod(javaApplicationStateManager, \
			this->getJavaFireApplicationStateChangedEventMethodID(), nextState);
}
