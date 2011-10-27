package com.app.augmentedbizz.cache;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.app.augmentedbizz.logging.DebugLog;
import com.app.augmentedbizz.model.OpenGLModel;
import com.app.augmentedbizz.model.Texture;
import com.app.augmentedbizz.util.TypeConversion;

/**
 * Holds some dummy objects that can be used for testing.
 * 
 * @author Miffels
 *
 */
public class DummyCacheData {
	
	/**
	 * Loads the texture from the APK assets.
	 * 
	 * @param fileName File name of the texture to load.
	 * @param assetManager Android asset manager used to access the assets.
	 * @return RGBA texture created from the image file.
	 */
	private static final Texture loadTexture(String fileName, AssetManager assetManager) {
		InputStream inputStream = null;
        try
        {
            inputStream = assetManager.open(fileName, AssetManager.ACCESS_BUFFER);
             
            BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
            Bitmap bitmap = BitmapFactory.decodeStream(bufferedStream);
            
            return new Texture(bitmap.getWidth(),
            		bitmap.getHeight(),
            		TypeConversion.splitRGBAChannelsOf(bitmap));
            
        } catch (IOException e) {
            DebugLog.loge("Failed to load texture '" + fileName + "' from APK.Error message: " + e.getMessage());
            return null;
        }
	}
	
	public static final void storeDummyModelsInDatabase(Context context) {
		
		AssetManager assetManager = context.getAssets();
		
		OpenGLModel cube = new OpenGLModel(99999, 1, new float[] { // vertices
				    -1.00f,  -1.00f,   1.00f, // front
				     1.00f,  -1.00f,   1.00f,
				     1.00f,   1.00f,   1.00f,
				    -1.00f,   1.00f,   1.00f,

				    -1.00f,  -1.00f,  -1.00f, // back
				     1.00f,  -1.00f,  -1.00f,
				     1.00f,   1.00f,  -1.00f,
				    -1.00f,   1.00f,  -1.00f,

				    -1.00f,  -1.00f,  -1.00f, // left
				    -1.00f,  -1.00f,   1.00f,
				    -1.00f,   1.00f,   1.00f,
				    -1.00f,   1.00f,  -1.00f,

				     1.00f,  -1.00f,  -1.00f, // right
				     1.00f,  -1.00f,   1.00f,
				     1.00f,   1.00f,   1.00f,
				     1.00f,   1.00f,  -1.00f,

				    -1.00f,   1.00f,   1.00f, // top
				     1.00f,   1.00f,   1.00f,
				     1.00f,   1.00f,  -1.00f,
				    -1.00f,   1.00f,  -1.00f,

				    -1.00f,  -1.00f,   1.00f, // bottom
				     1.00f,  -1.00f,   1.00f,
				     1.00f,  -1.00f,  -1.00f,
				    -1.00f,  -1.00f,  -1.00f
				}, new float[] { // normals
					0, 0, 1,
				    0, 0, 1,
				    0, 0, 1,
				    0, 0, 1,

				    0, 0, -1,
				    0, 0, -1,
				    0, 0, -1,
				    0, 0, -1,

				    0, -1, 0,
				    0, -1, 0,
				    0, -1, 0,
				    0, -1, 0,

				    0, 1, 0,
				    0, 1, 0,
				    0, 1, 0,
				    0, 1, 0,

				    1, 0, 0,
				    1, 0, 0,
				    1, 0, 0,
				    1, 0, 0,

				    -1, 0, 0,
				    -1, 0, 0,
				    -1, 0, 0,
				    -1, 0, 0
				}, new float[] { // texture coordinates
				    0, 0,
				    1, 0,
				    1, 1,
				    0, 1,

				    1, 0,
				    0, 0,
				    0, 1,
				    1, 1,

				    0, 0,
				    1, 0,
				    1, 1,
				    0, 1,

				    1, 0,
				    0, 0,
				    0, 1,
				    1, 1,

				    0, 0,
				    1, 0,
				    1, 1,
				    0, 1,

				    1, 0,
				    0, 0,
				    0, 1,
				    1, 1
				}, new short[] {
				     0,  1,  2,  0,  2,  3, // front
				     4,  6,  5,  4,  7,  6, // back
				     8,  9, 10,  8, 10, 11, // left
				    12, 14, 13, 12, 15, 14, // right
				    16, 17, 18, 16, 18, 19, // top
				    20, 22, 21, 20, 23, 22  // bottom
				}, DummyCacheData.loadTexture("DummyCube.png", assetManager), 50.f);
		
		OpenGLModel box = new OpenGLModel(99998, 1, new float[] { // vertices
				-3.937000f, -3.937000f, 3.937000f, -3.937000f, -3.937000f, -3.937000f, 3.937000f, -3.937000f, -3.937000f,
				3.937000f, -3.937000f, -3.937000f, 3.937000f, -3.937000f, 3.937000f, -3.937000f, -3.937000f, 3.937000f,
				-3.937000f, 3.937000f, 3.937000f, 3.937000f, 3.937000f, 3.937000f, 3.937000f, 3.937000f, -3.937000f,
				3.937000f, 3.937000f, -3.937000f, -3.937000f, 3.937000f, -3.937000f, -3.937000f, 3.937000f, 3.937000f,
				-3.937000f, -3.937000f, 3.937000f, 3.937000f, -3.937000f, 3.937000f, 3.937000f, 3.937000f, 3.937000f,
				3.937000f, 3.937000f, 3.937000f, -3.937000f, 3.937000f, 3.937000f, -3.937000f, -3.937000f, 3.937000f,
				3.937000f, -3.937000f, 3.937000f, 3.937000f, -3.937000f, -3.937000f, 3.937000f, 3.937000f, -3.937000f,
				3.937000f, 3.937000f, -3.937000f, 3.937000f, 3.937000f, 3.937000f, 3.937000f, -3.937000f, 3.937000f,
				3.937000f, -3.937000f, -3.937000f, -3.937000f, -3.937000f, -3.937000f, -3.937000f, 3.937000f, -3.937000f,
				-3.937000f, 3.937000f, -3.937000f, 3.937000f, 3.937000f, -3.937000f, 3.937000f, -3.937000f, -3.937000f,
				-3.937000f, -3.937000f, -3.937000f, -3.937000f, -3.937000f, 3.937000f, -3.937000f, 3.937000f, 3.937000f,
				-3.937000f, 3.937000f, 3.937000f, -3.937000f, 3.937000f, -3.937000f, -3.937000f, -3.937000f, -3.937000f
			}, new float[] { // normals
				0.000000f, -1.000000f, -0.000000f, 0.000000f, -1.000000f, -0.000000f, 0.000000f, -1.000000f, -0.000000f,
				0.000000f, -1.000000f, -0.000000f, 0.000000f, -1.000000f, -0.000000f, 0.000000f, -1.000000f, -0.000000f,
				0.000000f, 1.000000f, -0.000000f, 0.000000f, 1.000000f, -0.000000f, 0.000000f, 1.000000f, -0.000000f,
				0.000000f, 1.000000f, -0.000000f, 0.000000f, 1.000000f, -0.000000f, 0.000000f, 1.000000f, -0.000000f,
				0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
				0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
				1.000000f, 0.000000f, -0.000000f, 1.000000f, 0.000000f, -0.000000f, 1.000000f, 0.000000f, -0.000000f,
				1.000000f, 0.000000f, -0.000000f, 1.000000f, 0.000000f, -0.000000f, 1.000000f, 0.000000f, -0.000000f,
				0.000000f, 0.000000f, -1.000000f, 0.000000f, 0.000000f, -1.000000f, 0.000000f, 0.000000f, -1.000000f,
				0.000000f, 0.000000f, -1.000000f, 0.000000f, 0.000000f, -1.000000f, 0.000000f, 0.000000f, -1.000000f,
				-1.000000f, 0.000000f, -0.000000f, -1.000000f, 0.000000f, -0.000000f, -1.000000f, 0.000000f, -0.000000f,
				-1.000000f, 0.000000f, -0.000000f, -1.000000f, 0.000000f, -0.000000f, -1.000000f, 0.000000f, -0.000000f
			}, new float[] { // texture coordinates
				7.874000f, 1.000000f, 7.874000f, -6.874000f, 0.000000f, -6.874000f, 0.000000f, -6.874000f, 0.000000f, 1.000000f, 7.874000f, 1.000000f,
				0.000000f, 1.000000f, 7.874000f, 1.000000f, 7.874000f, -6.874000f, 7.874000f, -6.874000f, 0.000000f, -6.874000f, 0.000000f, 1.000000f,
				0.000000f, 1.000000f, 7.874000f, 1.000000f, 7.874000f, -6.874000f, 7.874000f, -6.874000f, 0.000000f, -6.874000f, 0.000000f, 1.000000f,
				0.000000f, 1.000000f, 7.874000f, 1.000000f, 7.874000f, -6.874000f, 7.874000f, -6.874000f, 0.000000f, -6.874000f, 0.000000f, 1.000000f,
				0.000000f, 1.000000f, 7.874000f, 1.000000f, 7.874000f, -6.874000f, 7.874000f, -6.874000f, 0.000000f, -6.874000f, 0.000000f, 1.000000f,
				0.000000f, 1.000000f, 7.874000f, 1.000000f, 7.874000f, -6.874000f, 7.874000f, -6.874000f, 0.000000f, -6.874000f, 0.000000f, 1.000000f
			}, new short[] { // indices
				0, 1, 2, 2, 3, 0, 4, 5, 6, 6, 7, 4,
				0, 3, 5, 5, 4, 0, 3, 2, 6, 6, 5, 3,
				2, 1, 7, 7, 6, 2, 1, 0, 4, 4, 7, 1
			}, DummyCacheData.loadTexture("DummyBox.png", assetManager), 5.0f);
		
		CacheDbAdapter adapter = new CacheDbAdapter(context);
		adapter.open();
		
		adapter.insertModel(cube);
		adapter.insertModel(box);
		
		adapter.close();
	}
}
