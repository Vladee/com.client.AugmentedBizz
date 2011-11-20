package com.app.augmentedbizz.application;

import android.content.Context;

import com.app.augmentedbizz.application.data.DataManager;
import com.app.augmentedbizz.application.status.ApplicationStateManager;
import com.app.augmentedbizz.ui.UIManager;
import com.app.augmentedbizz.ui.renderer.RenderManager;

public interface ApplicationFacade {
	
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
	
	/**
	 * Returns the application UI manager.
	 * 
	 * @return The application UI manager.
	 */
	public UIManager getUIManager();
	
	/**
	 * Returns the application data manager.
	 * 
	 * @return The application data manager.
	 */
	public DataManager getDataManager();
	
	/**
	 * Returns the application render manager.
	 * 
	 * @return The application render manager.
	 */
	public RenderManager getRenderManager();

}
