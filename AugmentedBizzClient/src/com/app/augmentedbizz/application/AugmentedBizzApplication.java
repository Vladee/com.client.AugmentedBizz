package com.app.augmentedbizz.application;

import android.app.Application;
import android.content.Context;

import com.app.augmentedbizz.application.init.Initializer;
import com.app.augmentedbizz.application.status.ApplicationState;
import com.app.augmentedbizz.application.status.ApplicationStateManager;
import com.app.augmentedbizz.cache.DummyCacheData;
import com.app.augmentedbizz.logging.DebugLog;
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
	private Initializer initializer;
	
	static {
		Initializer.loadSharedLibraries();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		uiManager = new UIManager(this);
		stateManager = new ApplicationStateManager();
		
		initializer = new Initializer(this);
		initializer.initializeApplication();
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
	
	
}
