#ifndef _RENDERMANAGER_H_
#define _RENDERMANAGER_H_

#include <QCAR/QCAR.h>
#include <QCAR/Tool.h>
#include <QCAR/Renderer.h>
#include <QCAR/Trackable.h>
#include <QCAR/Tracker.h>
#include "Texture.h"
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include "../application/ApplicationStateManager.h"

class RenderManager;
class RenderManagerJavaInterface;

class RenderManager {
	public:
		RenderManager(ApplicationStateManager*, ObjectLoader*, jobject);
		~RenderManager();
		void initizializeNative(unsigned short, unsigned short);
		void updateRendering(unsigned short, unsigned short);
		void scanFrameForBarcode(QCAR::State& state);
		void renderFrame();
		void startCamera();
		void stopCamera();
		void setTexture(jobject);
		void setModel(JNIEnv*, jfloatArray, jfloatArray, jfloatArray, jshortArray);
		void setScaleFactor(float);
	private:
		void configureVideoBackground();
		void setScreenDimensions(unsigned short, unsigned short);

		ApplicationStateManager* applicationStateManager;
		RenderManagerJavaInterface* renderManagerJavaInterface;

		unsigned int numPixels;
		jbyteArray pixelArray;

		unsigned short screenWidth;
		unsigned short screenHeight;
		Texture* texture;
		QCAR::Matrix44F projectionMatrix;

		unsigned int shaderProgramID;
		GLint vertexHandle;
		GLint normalHandle;
		GLint textureCoordHandle;
		GLint mvpMatrixHandle;

		unsigned short maxTrackableCount;
		int numModelElementsToDraw;
		float *vertices;
		float *normals;
		float *texcoords;
		unsigned short *indices;
		bool hasIndices;
		float scaleFactor;
};

class RenderManagerJavaInterface: public JavaInterface {
	public:
		RenderManagerJavaInterface(ObjectLoader*, jobject);
		void callScanner(unsigned int, unsigned int, jbyteArray);
	protected:
		virtual jclass getClass();
	private:
		jobject javaRenderManager;

		jmethodID getCallScannerMethodID();
};

#endif // _RENDERMANAGER_H_
