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
	this->renderManager = new RenderManager(this->applicationStateManager, this->objectLoader, \
			this->augmentedBizzApplicationJavaInterface->getJavaRenderManager());
}

AugmentedBizzApplication::~AugmentedBizzApplication() {
	delete this->objectLoader;
	delete this->applicationStateManager;
	delete this->renderManager;
	this->objectLoader = 0;
	this->applicationStateManager = 0;
	this->renderManager = 0;
}

ApplicationStateManager* AugmentedBizzApplication::getApplicationStateManager() {
	return this->applicationStateManager;
}

AugmentedBizzApplication* AugmentedBizzApplication::getAugmentedBizzApplication() {
	return this;
}

RenderManager* AugmentedBizzApplication::getRenderManager() {
	return this->renderManager;
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
	return this->getObjectLoader()->getJNIEnv()->CallObjectMethod(this->javaAugmentedBizzApplication, \
			this->getJavaGetApplicationStateManagerMethodID());
}

jobject AugmentedBizzApplicationJavaInterface::getJavaRenderManager() {
	return this->getObjectLoader()->getJNIEnv()->CallObjectMethod(this->javaAugmentedBizzApplication, \
			this->getJavaGetRenderManagerMethodID());
}

jmethodID AugmentedBizzApplicationJavaInterface::getJavaGetApplicationStateManagerMethodID() {
	return this->getMethodID("getApplicationStateManager", \
			"()Lcom/app/augmentedbizz/application/status/ApplicationStateManager;");
}

jmethodID AugmentedBizzApplicationJavaInterface::getJavaGetRenderManagerMethodID() {
	return this->getMethodID("getRenderManager", \
			"()Lcom/app/augmentedbizz/ui/renderer/RenderManager;");
}
