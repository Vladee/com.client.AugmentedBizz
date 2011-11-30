#ifndef _OBJECTLOADER_H_
#define _OBJECTLOADER_H_

#include <jni.h>
#include <string>

class ObjectLoader;
class JavaInterface;

//	Type Signatures - http://download.oracle.com/javase/1.3/docs/guide/jni/spec/types.doc.html
//
//	The JNI uses the Java VM's representation of type signatures. Table 3-2 shows these type signatures.
//
//	Table 3-2	 Java VM Type Signatures
//
//	Type Signature	 Java Type
//	Z	 boolean
//	B	 byte
//	C	 char
//	S	 short
//	I	 int
//	J	 long
//	F	 float
//	D	 double
//	L fully-qualified-class ;	 fully-qualified-class
//	[ type	 type[]
//	( arg-types ) ret-type	 method type
//	For example, the Java method:
//
//	    long f (int n, String s, int[] arr);
//	has the following type signature:
//	    (ILjava/lang/String;[I)J

class ObjectLoader {
	public:
		ObjectLoader(JNIEnv*);
		~ObjectLoader();
		jclass findClass(std::string);
		jclass getObjectClass(jobject);
		JNIEnv* getJNIEnv();
		jmethodID getMethodID(jclass, std::string, std::string);
		jmethodID getStaticMethodID(jclass, std::string, std::string);
		void callVoidMethod(jobject, jmethodID, ...);
		void callStaticVoidMethod(jclass, jmethodID, ...);
		jobject callObjectMethod(jobject, jmethodID, ...);

		jbyteArray createByteArray(unsigned int);
		void setByteArrayRegion(jbyteArray, unsigned int, unsigned int, const jbyte*);
	private:
		JavaVM *javaVM;
};

class JavaInterface {
	public:
		JavaInterface(ObjectLoader*);
		ObjectLoader* getObjectLoader();
	protected:
		jmethodID getMethodID(std::string, std::string);
		jmethodID getStaticMethodID(std::string, std::string);
		virtual jclass getClass() = 0;
	private:
		ObjectLoader* objectLoader;
};

#endif // _OBJECTLOADER_H_
