/*==============================================================================
            Copyright (c) 2010-2011 QUALCOMM Incorporated.
            All Rights Reserved.
            Qualcomm Confidential and Proprietary
            
@file 
    Texture.cpp

@brief
    Implementation of class Texture.

==============================================================================*/

// Include files
#include "Texture.h"
#include "../Utils.h"
#include "../logging/DebugLog.h"

#include <string.h>

int jjasdhhKIadhasdjHKJkJADSKJ = 0;

Texture::Texture() :
mWidth(0),
mHeight(0),
mChannelCount(0),
mData(0),
mTextureID(0){};


Texture::~Texture()
{
    if (mData != 0)
        delete[]mData;
}


Texture*
Texture::create(JNIEnv* env, jobject textureObject)
{

    Texture* newTexture = new Texture();

    // Handle to the Texture class:
    jclass textureClass = env->GetObjectClass(textureObject);

    // Get width:
    jfieldID widthID = env->GetFieldID(textureClass, "width", "I");
    if (!widthID)
    {
        DebugLog::loge("Field width not found.");
        return 0;
    }
    newTexture->mWidth = env->GetIntField(textureObject, widthID);

    // Get height:
    jfieldID heightID = env->GetFieldID(textureClass, "height", "I");
    if (!heightID)
    {
        DebugLog::loge("Field height not found.");
        return 0;
    }
    newTexture->mHeight = env->GetIntField(textureObject, heightID);

    // Always use RGBA channels:
    newTexture->mChannelCount = 4;

    // Get data:
    jmethodID texBufferMethodId = env->GetMethodID(textureClass , "getRGBAdata", "()[B");
    if (!texBufferMethodId)
    {
        DebugLog::loge("Function getData() not found.");
        return 0;
    }
    
    jbyteArray pixelBuffer = (jbyteArray)env->CallObjectMethod(textureObject, texBufferMethodId);    
    if (pixelBuffer == NULL)
    {
        DebugLog::loge("Get image buffer returned zero pointer");
        return 0;
    }

    jboolean isCopy = JNI_TRUE;
    jbyte* pixels = env->GetByteArrayElements(pixelBuffer, &isCopy);
    if (pixels == NULL)
    {
        DebugLog::loge("Failed to get texture buffer.");
        return 0;
    }

    newTexture->mData = new unsigned char[newTexture->mWidth * newTexture->mHeight * newTexture->mChannelCount];

    unsigned char* textureData = (unsigned char*) pixels;

    int rowSize = newTexture->mWidth * newTexture->mChannelCount;
    for (int r = 0; r < newTexture->mHeight; ++r)
    {
        memcpy(newTexture->mData + rowSize * r, pixels + rowSize * (newTexture->mHeight - 1 - r), newTexture->mWidth * 4);
    }

    // Release:
    env->ReleaseByteArrayElements(pixelBuffer, pixels, 0);

    newTexture->mTextureID = jjasdhhKIadhasdjHKJkJADSKJ++;

    return newTexture;
}

