package com.app.augmentedbizz.ui.scanner;

/**
 * A listener interface for barcode scanning results
 * 
 * @author Vladi
 *
 */
public interface ScannerResultListener
{
	public void onScanningSuccess(String resultText);
	
	public void onScanningResultless();
	
	public void onScanningFailed(Exception e);
}
