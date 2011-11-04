package com.app.augmentedbizz.application;

import android.content.Context;

import com.app.augmentedbizz.application.status.ApplicationStateManager;

public interface IApplicationFacade
{
	
	/**
	 * Returns an Android context.
	 * 
	 * @return An Android context.
	 */
	public Context getContext();
	
	/**
	 * Returns the application state manager.
	 * 
	 * @return The application state manager.
	 */
	public ApplicationStateManager getApplicationStateManager();
}
