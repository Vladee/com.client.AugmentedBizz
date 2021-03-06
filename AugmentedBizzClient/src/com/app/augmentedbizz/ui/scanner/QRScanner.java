package com.app.augmentedbizz.ui.scanner;

import org.apache.http.util.ByteArrayBuffer;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * A QRScanner for bitmaps
 * 
 * @author Vladi
 *
 */
public class QRScanner {
	private Context context;
	private Bitmap.Config bitmapConfig;
	
	public QRScanner(Context context, Bitmap.Config bitmapConfig) {
		this.context = context;
		this.bitmapConfig = bitmapConfig;
	}
	
	/**
	 * Initiates the scanning for a QR barcode in a bitmap specified by the width, height and image data.
	 * Returns the scanning result by invocation of a result listener.
	 * 
	 * @param width of the image to be scanned
	 * @param height of the image to be scanned
	 * @param bitmapData image data
	 * @param listener Gets invoked when the asynchronous processing is finished a result is available
	 */
	public void scanForQRCode(int width, int height, byte[] bitmapData, ScannerResultListener listener) {
		if(bitmapData != null && listener != null) {
			ByteArrayBuffer dataBuffer = new ByteArrayBuffer(bitmapData.length);
			dataBuffer.append(bitmapData, 0, bitmapData.length);
			new BarcodeScannerTask(this.context, this.bitmapConfig).execute(new Integer(width), new Integer(height), dataBuffer, listener);
		}
	}
}
