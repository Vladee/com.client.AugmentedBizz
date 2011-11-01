package com.app.augmentedbizz.util;

import android.graphics.Bitmap;

/**
 * Utility class that provides methods to convert various
 * basic types to and from byte arrays.
 * 
 * @author Cudmore (http://www.daniweb.com/software-development/java/code/216874)
 *
 */
public class TypeConversion {
	
	/**
	 * Converts a single float number to an array of bytes.
	 * 
	 * @param floatNumber The floating point number to convert.
	 * @return Array of bytes holding the float number data.
	 */
	public static byte[] toByteArrayFrom(float floatNumber) {
	    return TypeConversion.toByteArrayFrom(Float.floatToRawIntBits(floatNumber));
	}
	
	/**
	 * Converts an array of float to an array of byte.
	 * 
	 * @param floatArray The float array to convert.
	 * @return Array of bytes holding the float array data.
	 */
	public static byte[] toByteArrayFrom(float[] floatArray) {
	    if (floatArray == null) return null;
	    // ----------
	    byte[] byts = new byte[floatArray.length * 4];
	    for (int i = 0; i < floatArray.length; i++)
	        System.arraycopy(TypeConversion.toByteArrayFrom(floatArray[i]), 0, byts, i * 4, 4);
	    return byts;
	}
	
	/**
	 * Converts single integer to an array of byte.
	 * 
	 * @param intNumber The integer to convert.
	 * @return Array of bytes holding the integer data.
	 */
	public static byte[] toByteArrayFrom(int intNumber) {
	    return new byte[] {
	        (byte)((intNumber >> 24) & 0xff),
	        (byte)((intNumber >> 16) & 0xff),
	        (byte)((intNumber >> 8) & 0xff),
	        (byte)((intNumber >> 0) & 0xff),
	    };
	}
	
	/**
	 * Converts an array of integer to an array of byte.
	 * 
	 * @param intArray The integer array to convert.
	 * @return Array of bytes holding the integer array data.
	 */
	public static byte[] toByteArrayFrom(int[] intArray) {
	    if (intArray == null) return null;
	    // ----------
	    byte[] byts = new byte[intArray.length * 4];
	    for (int i = 0; i < intArray.length; i++)
	        System.arraycopy(TypeConversion.toByteArrayFrom(intArray[i]), 0, byts, i * 4, 4);
	    return byts;
	}
	
	/**
	 * Converts single short to an array of byte.
	 * 
	 * @param shortNumber The short number to convert.
	 * @return Array of bytes holding the short number data.
	 */
	public static byte[] toByteArrayFrom(short shortNumber) {
	    return new byte[] {
	        (byte)((shortNumber >> 8) & 0xff),
	        (byte)((shortNumber >> 0) & 0xff),
	    };
	}
	
	/**
	 * Converts an array of short to an array of byte.
	 * 
	 * @param shortArray The integer array to convert.
	 * @return Array of bytes holding the short array number data.
	 */
	public static byte[] toByteArrayFrom(short[] shortArray) {
	    if (shortArray == null) return null;
	    // ----------
	    byte[] byts = new byte[shortArray.length * 2];
	    for (int i = 0; i < shortArray.length; i++)
	        System.arraycopy(TypeConversion.toByteArrayFrom(shortArray[i]), 0, byts, i * 2, 2);
	    return byts;
	}
	
	/**
	 * Converts a regular bitmap to a byte array with its RGBA channels split.
	 * 
	 * @param bitmap The bitmap to split.
	 * @return Array of bytes holding the split RGBA information.
	 */
	public static byte[] splitRGBAChannelsOf(Bitmap bitmap) {
		int[] data = new int[bitmap.getWidth() * bitmap.getHeight()];
		bitmap.getPixels(data, 0, bitmap.getWidth(), 0, 0,
				bitmap.getWidth(), bitmap.getHeight());
        
        // Convert:
        byte[] dataBytes = new byte[bitmap.getWidth() *
                                    bitmap.getHeight() * 4];
        for (int p = 0; p < bitmap.getWidth() * bitmap.getHeight(); ++p)
        {
            int colour = data[p];
            dataBytes[p * 4]        = (byte)(colour >>> 16);    // R
            dataBytes[p * 4 + 1]    = (byte)(colour >>> 8);     // G
            dataBytes[p * 4 + 2]    = (byte) colour;            // B
            dataBytes[p * 4 + 3]    = (byte)(colour >>> 24);    // A
        }
        
        return dataBytes;
	}
	
}