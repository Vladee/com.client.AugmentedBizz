#ifndef _AUGMENTEDBIZZAPPLICATION_H_
#define _AUGMENTEDBIZZAPPLICATION_H_

#include <jni.h>
#include "../logging/DebugLog.h"

class AugmentedBizzApplication {
public:
  static AugmentedBizzApplication& getInstance();
  void initializeApplication(JNIEnv*);
  JNIEnv* getJNIEnv();
  JavaVM* getJavaVM();
  //~AugmentedBizzApplication();

private:
  AugmentedBizzApplication() {};
  AugmentedBizzApplication(const AugmentedBizzApplication&);

  // Pointer to the thread-independant JavaVM
  JavaVM* javaVM;
};

#endif // _AUGMENTEDBIZZAPPLICATION_H_
