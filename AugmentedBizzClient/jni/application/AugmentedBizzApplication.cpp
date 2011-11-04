#include "AugmentedBizzApplication.h"
#include "../logging/DebugLog.h"

AugmentedBizzApplication::AugmentedBizzApplication(JNIEnv *env, jobject jAugmentedBizzApplication) {
	this->initializeApplication(env, jAugmentedBizzApplication);
}

void AugmentedBizzApplication::initializeApplication(JNIEnv *env, jobject jAugmentedBizzApplication) {
	DebugLog::logi("Initializing application.");

	this->objectLoader = new ObjectLoader(env);
	this->augmentedBizzApplicationJavaInterface = \
			new AugmentedBizzApplicationJavaInterface(jAugmentedBizzApplication, this->objectLoader);

	this->applicationStateManager = new ApplicationStateManager(this->objectLoader, \
			this->augmentedBizzApplicationJavaInterface->getJavaApplicationStateManager());
}

AugmentedBizzApplication::~AugmentedBizzApplication() {
	delete this->objectLoader;
	delete this->applicationStateManager;
	this->objectLoader = 0;
	this->applicationStateManager = 0;
}

ApplicationStateManager* AugmentedBizzApplication::getApplicationStateManager() {
	return this->applicationStateManager;
}

AugmentedBizzApplication* AugmentedBizzApplication::getAugmentedBizzApplication() {
	return this;
}

// --------------------------------------------------------------------

AugmentedBizzApplicationJavaInterface::AugmentedBizzApplicationJavaInterface(jobject javaAugmentedBizzApplication, \
		ObjectLoader* objectLoader) : JavaInterface(objectLoader) {
	this->javaAugmentedBizzApplication = javaAugmentedBizzApplication;
}

jclass AugmentedBizzApplicationJavaInterface::getClass() {
	return this->getObjectLoader()->getObjectClass(this->javaAugmentedBizzApplication);
}

jobject AugmentedBizzApplicationJavaInterface::getJavaApplicationStateManager() {
	return this->getObjectLoader()->callObjectMethod(this->javaAugmentedBizzApplication, \
			this->getJavaGetApplicationStateManagerMethodID());
}

jmethodID AugmentedBizzApplicationJavaInterface::getJavaGetApplicationStateManagerMethodID() {
	return this->getMethodID("getApplicationStateManager", \
			"()Lcom/app/augmentedbizz/application/status/ApplicationStateManager;");
}
