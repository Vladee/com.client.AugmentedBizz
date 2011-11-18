package com.app.augmentedbizz.ui.scanner;

import java.nio.ByteBuffer;

import org.apache.http.util.ByteArrayBuffer;

import com.app.augmentedbizz.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Asynchronous QR barcode scanning of bitmaps.
 * 
 * @author Vladi
 *
 */
public class BarcodeScannerTask extends AsyncTask<Object, Object, Object> {
	private volatile boolean processing = false;
	private Context context = null;
	private ScannerResultListener resultListener = null;
	private QRCodeReader qrReader = new QRCodeReader();
	private Bitmap cameraImage = null;
	private Bitmap.Config bitmapConfig;
	
	public BarcodeScannerTask(Context context, Bitmap.Config config) {
		this.context = context;
		this.bitmapConfig = config;
	}

	@Override
	protected void onPreExecute() {
		processing = true;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		try {
			Integer width = (Integer)params[0];
			Integer height = (Integer)params[1];
			ByteArrayBuffer data = (ByteArrayBuffer)params[2];
			resultListener = (ScannerResultListener)params[3];
			
			createBitmapFromData(width, height, data.toByteArray());
			BinaryBitmap binBitmap = createBinaryBitmapFromRGBBitmap(cameraImage);
			
			Result result = qrReader.decode(binBitmap);
			return result.getText();
		}
		catch(Exception e) {
			return e;
		}
	}
	
	 @Override
	protected void onPostExecute(Object result) {
		processing = false;
		if(result instanceof String) {
			String barcodeText = (String)result;
			String codePrefix = context.getString(R.string.prefixBarcode);
			if(barcodeText.length() > 0 && barcodeText.startsWith(codePrefix)) {
				try {
					Integer targetId = new Integer(barcodeText.substring(codePrefix.length()));
					resultListener.onScanningSuccess(targetId);
				}
				catch(Exception e) {
					resultListener.onScanningResultless();
				}
			}
			else {
				resultListener.onScanningResultless();
			}
		}
		else if(result instanceof Exception) {
			resultListener.onScanningFailed();
		}
	}

	/**
	 * @return the processing
	 */
	public boolean isProcessing() {
		return processing;
	}
	
	/**
	 * Creates a binary bitmap from a RGB bitmap
	 * @param bitmap The rgb bitmap
	 * @return A binary bitmap
	 */
	private BinaryBitmap createBinaryBitmapFromRGBBitmap(Bitmap bitmap) {
		HybridBinarizer binarizer = new HybridBinarizer(new RGBLuminanceSource(cameraImage));
		return new BinaryBitmap(binarizer);
	}
	
	private void createBitmapFromData(Integer width, Integer height, byte[] bitmapData) {
		if(cameraImage == null || width != cameraImage.getWidth() || height != cameraImage.getHeight()) {
			cameraImage = Bitmap.createBitmap(width, height, bitmapConfig);
		}
		cameraImage.copyPixelsFromBuffer(ByteBuffer.wrap(bitmapData));
	}
}
