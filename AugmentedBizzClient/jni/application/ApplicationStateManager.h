#ifndef _APPLICATIONSTATEMANAGER_H_
#define _APPLICATIONSTATEMANAGER_H_

#include <jni.h>
#include <map>
#include <list>
#include <string>
#include "../logging/DebugLog.h"
#include "ObjectLoader.h"

class ApplicationStateManager;
class IApplicationStateListener;
class ApplicationStateManagerJavaInterface;

// See Java side for state documentation
// Use struct as a namespace
enum ApplicationState {
	// Enum values used as array indices
	UNINITIATED = 0,
	INITIALIZING,
	INITIALIZED,
	TRACKING,
	TRACKED,
	SCANNING,
	SCANNED,
	LOADING,
	SHOWING_CACHE,
	LOADING_INDICATORS,
	SHOWING,
	DEINITIALIZING,
	EXITING,
	// Must be the last one
	NUMBER_OF_APPLICATION_STATES
};

class IApplicationStateListener {
	public:
		virtual void onApplicationStateChange(ApplicationState) = 0;
};

class ApplicationStateManager : IApplicationStateListener {
   public:
	ApplicationStateManager(ObjectLoader*, jobject);
	~ApplicationStateManager();
    void setApplicationState(ApplicationState);
    ApplicationState getCurrentApplicationState();
    void setNativeApplicationStateOnly(int);
    void addApplicationStateListener(IApplicationStateListener*);
    void removeApplicationStateListener(IApplicationStateListener*);
    virtual void onApplicationStateChange(ApplicationState);

   private:
    std::string applicationStateNames[NUMBER_OF_APPLICATION_STATES];
    ApplicationState currentState;
    std::list<IApplicationStateListener*> applicationStateListeners;
    ApplicationStateManagerJavaInterface* applicationStateManagerJavaInterface;

    void fireApplicationStateChangedEvent(ApplicationState);
    JNIEXPORT void JNICALL Java_com_app_augmentedbizz_application_status_ApplicationStateManger_fireApplicationStateChangedEventNative(JNIEnv *, jobject, jstring);
};

class ApplicationStateManagerJavaInterface: JavaInterface {
	public:
		ApplicationStateManagerJavaInterface(ObjectLoader*, jobject);
		void setJavaApplicationState(ApplicationState);
	protected:
		virtual jclass getClass();
	private:
		jmethodID getJavaFireApplicationStateChangedEventMethodID();

		jobject	javaApplicationStateManager;
};

#endif // _APPLICATIONSTATEMANAGER_H_
