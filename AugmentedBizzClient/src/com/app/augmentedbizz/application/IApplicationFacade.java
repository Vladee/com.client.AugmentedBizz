package com.app.augmentedbizz.application;

import com.app.augmentedbizz.application.status.ApplicationStateManager;

public interface IApplicationFacade
{
	
	/**
	 * Returns the application state manager.
	 * 
	 * @return The application state manager.
	 */
	public ApplicationStateManager getApplicationStateManager();
}
