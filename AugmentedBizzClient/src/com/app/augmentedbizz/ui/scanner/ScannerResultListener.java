package com.app.augmentedbizz.ui.scanner;

/**
 * A listener interface for barcode scanning results
 * 
 * @author Vladi
 *
 */
public interface ScannerResultListener {
	public void onScanningSuccess(int targetId);
	
	public void onScanningResultless();
	
	public void onScanningFailed();
}
