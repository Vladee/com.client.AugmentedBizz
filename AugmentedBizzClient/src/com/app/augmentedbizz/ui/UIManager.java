package com.app.augmentedbizz.ui;

import android.app.Activity;

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
	private Activity mainActivity;
	private ApplicationFacade facade;
	
	public UIManager(ApplicationFacade facade)
	{
		this.facade = facade;
	}

	/**
	 * @return the qrScanner
	 */
	public QRScanner getQRScanner()
	{
		return qrScanner;
	}

	/**
	 * @param qrScanner the qrScanner to set
	 */
	public void setQRScanner(QRScanner qrScanner)
	{
		this.qrScanner = qrScanner;
	}

	/**
	 * @return the mainActivity
	 */
	public Activity getMainActivity()
	{
		return mainActivity;
	}

	/**
	 * @param mainActivity the mainActivity to set
	 */
	public void setMainActivity(Activity mainActivity)
	{
		this.mainActivity = mainActivity;
	}
	
}
