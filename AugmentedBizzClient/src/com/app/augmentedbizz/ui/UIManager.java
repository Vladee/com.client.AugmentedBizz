package com.app.augmentedbizz.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.app.augmentedbizz.application.ApplicationFacade;
import com.app.augmentedbizz.ui.scanner.QRScanner;

/**
 * Central point of UI management, renderer setups and scanning.
 * 
 * @author Vladi
 *
 */
public class UIManager
{
	private QRScanner qrScanner;
	private volatile MainActivity mainActivity;
	private ApplicationFacade facade;
	
	public UIManager(ApplicationFacade facade)
	{
		this.facade = facade;
		qrScanner = new QRScanner(Bitmap.Config.RGB_565);
	}

	/**
	 * @return the qrScanner
	 */
	public QRScanner getQRScanner()
	{
		return qrScanner;
	}

	/**
	 * @return the mainActivity
	 */
	public MainActivity getMainActivity()
	{
		return mainActivity;
	}

	/**
	 * @param mainActivity the mainActivity to set
	 */
	public void setMainActivity(MainActivity mainActivity)
	{
		this.mainActivity = mainActivity;
	}
	
	/**
	 * Shows a warning toast with a given string id.
	 * 
	 * @param stringId The id of the warning text which should be shown.
	 */
	public void showWarningToast(int stringId)
	{
		Toast.makeText(mainActivity, stringId, Toast.LENGTH_SHORT).show();
	}
	
	/**
	 * Shows an error dialog with the given string id as the referenced content.
	 * 
	 * @param stringId The id of the error string which should be shown.
	 */
	public void showErrorDialog(int stringId, final boolean forceClose)
	{
		final AlertDialog errorDialog = new AlertDialog.Builder(mainActivity).create();   
		errorDialog.setTitle(mainActivity.getResources().getString(com.app.augmentedbizz.R.string.errorTitle));   
		errorDialog.setMessage(mainActivity.getResources().getString(stringId));   
		errorDialog.setCancelable(false);
		errorDialog.setButton(mainActivity.getResources().getString(com.app.augmentedbizz.R.string.errorButtonOK), new DialogInterface.OnClickListener() 
		{   
	
			public void onClick(DialogInterface dialog, int which) 
	    	{   
				errorDialog.dismiss();
				if(forceClose) 
				{
					getMainActivity().finish();
				}
	    	}
		});
		errorDialog.show();
	}
}
