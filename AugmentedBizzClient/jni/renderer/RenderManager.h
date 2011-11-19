#ifndef _RENDERMANAGER_H_
#define _RENDERMANAGER_H_

#include <QCAR/Tool.h>
#include "Texture.h"
#include <GLES2/gl2.h>
#include <GLES2/gl2ext.h>
#include "../application/ApplicationStateManager.h"

class RenderManager;

class RenderManager {
	public:
		RenderManager(ApplicationStateManager*);
		~RenderManager();
		void initizializeNative(unsigned short, unsigned short);
		void updateRendering(unsigned short, unsigned short);
		void renderFrame();
		void startCamera();
		void stopCamera();
		void setTexture(JNIEnv*, jobject);
		void setModel(JNIEnv*, jfloatArray, jfloatArray, jfloatArray, jshortArray);
		void setScaleFactor(float);
	private:
		void configureVideoBackground();
		void setScreenDimensions(unsigned short, unsigned short);

		ApplicationStateManager* applicationStateManager;

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

#endif // _RENDERMANAGER_H_
