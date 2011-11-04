#include "ApplicationStateManager.h"
#include "AugmentedBizzApplication.h"

ApplicationStateManager::ApplicationStateManager(ObjectLoader *objectLoader, jobject javaApplicationStateManager) {
	this->currentState = INITIALIZING;

	this->applicationStateManagerJavaInterface = \
			new ApplicationStateManagerJavaInterface(objectLoader, javaApplicationStateManager);

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

ApplicationStateManager::~ApplicationStateManager() {
	delete this->applicationStateManagerJavaInterface;
	this->applicationStateManagerJavaInterface = 0;
}

void ApplicationStateManager::setApplicationState(ApplicationState nextState) {
	this->fireApplicationStateChangedEvent(nextState);
	this->applicationStateManagerJavaInterface->setJavaApplicationState(nextState);
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
	this->getObjectLoader()->callVoidMethod(javaApplicationStateManager, \
			this->getJavaFireApplicationStateChangedEventMethodID(), nextState);
}
