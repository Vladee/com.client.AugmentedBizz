package com.app.augmentedbizz.ui;

import android.app.Activity;
import android.graphics.Bitmap;

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
	
}
