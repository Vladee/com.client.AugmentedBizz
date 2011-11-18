#ifndef _AUGMENTEDBIZZAPPLICATION_H_
#define _AUGMENTEDBIZZAPPLICATION_H_

#include <jni.h>
#include "../logging/DebugLog.h"
#include "ObjectLoader.h"
#include "ApplicationStateManager.h"
#include "../renderer/RenderManager.h"

class AugmentedBizzApplication;
class IApplicationFacade;
class AugmentedBizzApplicationJavaInterface;

class IApplicationFacade {
	public:
		virtual ApplicationStateManager* getApplicationStateManager() = 0;
		virtual AugmentedBizzApplication* getAugmentedBizzApplication() = 0;
		virtual RenderManager* getRenderManager() = 0;
};

class AugmentedBizzApplication: public IApplicationFacade {
	public:
	    AugmentedBizzApplication(JNIEnv*, jobject);
		virtual ApplicationStateManager* getApplicationStateManager();
		virtual AugmentedBizzApplication* getAugmentedBizzApplication();
		virtual RenderManager* getRenderManager();
		~AugmentedBizzApplication();
	private:
		AugmentedBizzApplicationJavaInterface* augmentedBizzApplicationJavaInterface;
		ObjectLoader* objectLoader;
		ApplicationStateManager* applicationStateManager;
		RenderManager* renderManager;
		void initializeApplication(JNIEnv*, jobject);
};

class AugmentedBizzApplicationJavaInterface: JavaInterface {
	public:
		AugmentedBizzApplicationJavaInterface(jobject, ObjectLoader*);
		jobject getJavaApplicationStateManager();
	protected:
		virtual jclass getClass();
	private:
		jobject javaAugmentedBizzApplication;
		jmethodID getJavaGetApplicationStateManagerMethodID();
};

#endif // _AUGMENTEDBIZZAPPLICATION_H_
