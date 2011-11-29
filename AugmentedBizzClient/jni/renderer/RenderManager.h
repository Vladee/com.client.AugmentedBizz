#ifndef _RENDERMANAGER_H_
#define _RENDERMANAGER_H_

#include <QCAR/QCAR.h>
#include <QCAR/Tool.h>
#include <QCAR/Renderer.h>
#include <QCAR/Trackable.h>
#include <QCAR/Tracker.h>
#include <pthread.h>
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include "../application/ApplicationStateManager.h"
#include "Texture.h"

class RenderManager;
class RenderManagerJavaInterface;

class RenderManager {
	public:
		RenderManager(ApplicationStateManager*, ObjectLoader*, jobject);
		~RenderManager();
		void inititializeNative(unsigned short, unsigned short);
		void initRendering();
		void updateRendering(unsigned short, unsigned short);
		void scanFrameForBarcode(QCAR::State& state);
		void renderFrame();
		void renderModel(QCAR::State& state);
		void renderIndicators(QCAR::State& state);
		void releaseData();
		void startCamera();
		void stopCamera();
		void setTexture(jobject);
		void setIndicatorTexture(jobject);
		void setModel(JNIEnv*, jfloatArray, jfloatArray, jfloatArray, jshortArray);
		void setIndicators(JNIEnv*, jfloatArray);
		void setScaleFactor(float);
		int getTrackableWidth();
		int getTrackableHeight();
	private:
		void configureVideoBackground();
		void setScreenDimensions(unsigned short, unsigned short);
		void addTexture(Texture* texture);

		ApplicationStateManager* applicationStateManager;
		RenderManagerJavaInterface* renderManagerJavaInterface;

		unsigned int scanCounter;

		unsigned short screenWidth;
		unsigned short screenHeight;
		Texture* modelTexture;
		Texture* indicatorTexture;
		QCAR::Matrix44F projectionMatrix;
		int trackableWidth;
		int trackableHeight;

		unsigned int shaderProgramID;
		GLint vertexHandle;
		GLint normalHandle;
		GLint textureCoordHandle;
		GLint mvpMatrixHandle;

		unsigned short maxTrackableCount;
		int numModelElementsToDraw;
		jfloatArray jVertices;
		jfloatArray jNormals;
		jfloatArray jTexcoords;
		jshortArray jIndices;
		float* vertices;
		float* normals;
		float* texcoords;
		unsigned short *indices;
		bool hasIndices;
		float scaleFactor;
		bool newTextureAvailable;
		jfloatArray jIndicators;
		float* indicators;
		int numIndicators;
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
