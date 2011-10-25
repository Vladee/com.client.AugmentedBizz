package com.app.augmentedbizz.application;

import android.app.Application;

/**
 * Represents the application context and facade for other application components
 * 
 * @author Vladi
 *
 */
public class AugmentedBizzApplication extends Application implements IApplicationFacade
{
	@Override
	public void onCreate()
	{
		super.onCreate();
		
		//TODO setup managers
		//TODO update state
	}
}
