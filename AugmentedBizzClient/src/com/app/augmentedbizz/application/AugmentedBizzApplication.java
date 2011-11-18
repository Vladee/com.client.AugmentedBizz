package com.app.augmentedbizz.application;

import android.app.Application;
import android.content.Context;

import com.app.augmentedbizz.application.data.DataManager;
import com.app.augmentedbizz.application.init.Initializer;
import com.app.augmentedbizz.application.status.ApplicationStateManager;
import com.app.augmentedbizz.ui.UIManager;

/**
 * Represents the application context and facade for other application components
 * 
 * @author Vladi
 *
 */
public class AugmentedBizzApplication extends Application implements ApplicationFacade {
	
	private UIManager uiManager;
	private ApplicationStateManager stateManager;
	private DataManager dataManager;
	
	static {
		Initializer.loadSharedLibraries();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		//init managers
		stateManager = new ApplicationStateManager();
		uiManager = new UIManager(this);
		dataManager = new DataManager(this);
	}
	
	/**
	 * Starts the application initialization.
	 */
	public void startInitialization() {
		new Initializer(this).initializeApplication();
	}
	
	@Override
	public ApplicationStateManager getApplicationStateManager() {
		return stateManager;
	}

	@Override
	public Context getContext() {
		return this;
	}

	@Override
	public UIManager getUIManager() {
		return uiManager;
	}

	@Override
	public DataManager getDataManager() {
		return dataManager;
	}
	
}
