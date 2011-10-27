/*==============================================================================
            Copyright (c) 2010-2011 QUALCOMM Incorporated.
            All Rights Reserved.
            Qualcomm Confidential and Proprietary
==============================================================================*/

package com.app.augmentedbizz.ui.renderer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * Exposes functionality for loading a texture
 */
public class Texture
{
    public int width;
    public int height;
    public int channels;
    public byte[] data;
    
    protected Texture()
    {
    }
    
    public byte[] getData()
    {
        return data;
    }    
    
    /**
	 * Factory function to load a texture from the APK
	 */
	public static Texture loadTextureFromApk(String fileName, AssetManager assets) throws TextureLoadingFailedException
	{
		InputStream inputStream = null;
		try
		{
			inputStream = assets.open(fileName, AssetManager.ACCESS_BUFFER);
		} 
		catch (IOException e)
		{
			Log.d("AR", "Failed to load texture from APK: " + fileName);
			throw(new TextureLoadingFailedException(e.getMessage()));
		}
		return loadTextureFromInputBuffer(inputStream);
	}
    
	 /**
	  * Factory function to load a texture from an input stream
	  */
    public static Texture loadTextureFromInputBuffer(InputStream inputStream) throws TextureLoadingFailedException
    {
    	 try
    	 {
    		 if(inputStream == null)
    		 {
    			 throw(new NullPointerException());
    		 }
    		 BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
             Bitmap bitMap = BitmapFactory.decodeStream(bufferedStream);
             
             int[] data = new int[bitMap.getWidth() * bitMap.getHeight()];
             bitMap.getPixels(data, 0, bitMap.getWidth(), 0, 0,
                                 bitMap.getWidth(), bitMap.getHeight());
             
             //convert data to 
             byte[] dataBytes = new byte[bitMap.getWidth() * bitMap.getHeight() * 4];
             for(int p = 0; p < bitMap.getWidth() * bitMap.getHeight(); ++p)
             {
                 int colour = data[p];
                 dataBytes[p * 4]        = (byte)(colour >>> 16);    // R
                 dataBytes[p * 4 + 1]    = (byte)(colour >>> 8);     // G
                 dataBytes[p * 4 + 2]    = (byte) colour;            // B
                 dataBytes[p * 4 + 3]    = (byte)(colour >>> 24);    // A
             }
             
             Texture texture = new Texture();
             texture.width      = bitMap.getWidth();
             texture.height     = bitMap.getHeight();
             texture.channels   = 4;
             texture.data       = dataBytes;
             
             return texture;
         }
         catch(Exception e)
         {
             Log.d("AR", "Failed to load texture from InputStream.");
             
             throw new TextureLoadingFailedException(e.getMessage());
         }
    }

}
