package com.app.augmentedbizz.ui.scanner;

import java.nio.ByteBuffer;

import android.graphics.Bitmap;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

/**
 * A QRScanner for bitmaps
 * 
 * @author Vladi
 *
 */
public class QRScanner
{
	private QRCodeReader qrReader = null;
	private int imageWidth = -1;
	private int imageHeight = -1;
	private Bitmap cameraImage = null;
	private Bitmap.Config bitmapConfig;
	
	public QRScanner(Bitmap.Config bitmapConfig)
	{
		qrReader = new QRCodeReader();
		this.bitmapConfig = bitmapConfig;
	}
	
	public String scanRGBBitmapForQRCode(int width, int height, byte[] pixelData) throws NoBarcodeFoundException
	{
		try
		{
			updateBuffer(width, height, pixelData);
			BinaryBitmap binaryBitmap = createBinaryBitmapFromRGBLuminanceSource(cameraImage);
			
			Result result = qrReader.decode(binaryBitmap);
			return result.getText();
		} 
		catch(Exception e)
		{
			throw new NoBarcodeFoundException(e.getMessage());
		}
	}
	
	private void updateBuffer(int width, int height, byte[] pixelData)
	{
		if(imageWidth != width || imageHeight != height || cameraImage == null)
		{
			cameraImage = Bitmap.createBitmap(width, height, bitmapConfig);
		}
		cameraImage.copyPixelsFromBuffer(ByteBuffer.wrap(pixelData));
	}
	
	private BinaryBitmap createBinaryBitmapFromRGBLuminanceSource(Bitmap bitmap)
	{
		HybridBinarizer binarizer = new HybridBinarizer(new RGBLuminanceSource(cameraImage));
		return new BinaryBitmap(binarizer);
	}
	
}
