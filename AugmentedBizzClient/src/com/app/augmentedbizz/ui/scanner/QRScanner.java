package com.app.augmentedbizz.ui.scanner;

import java.nio.ByteBuffer;
import java.util.Formatter.BigDecimalLayoutForm;

import org.apache.http.util.ByteArrayBuffer;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import android.graphics.Bitmap;
import android.text.Html.ImageGetter;
import android.util.Log;

/**
 * A QRScanner for bitmaps
 * 
 * @author Vladi
 *
 */
public class QRScanner
{
	private BarcodeScannerTask scannerTask;
	
	public QRScanner(Bitmap.Config bitmapConfig)
	{
		this.scannerTask = new BarcodeScannerTask(bitmapConfig);
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
	public void scanForQRCode(int width, int height, byte[] bitmapData, ScannerResultListener listener)
	{	
		if(bitmapData != null && listener != null)
		{
			ByteArrayBuffer dataBuffer = new ByteArrayBuffer(bitmapData.length);
			dataBuffer.append(bitmapData, 0, bitmapData.length);
			scannerTask.execute(new Integer(width), new Integer(height), dataBuffer, listener);
		}
	}
	
	/**
	 * @return true, if scanner is currently scanning and decoding an image
	 */
	public boolean isScanning()
	{
		return scannerTask.isProcessing();
	}
}
