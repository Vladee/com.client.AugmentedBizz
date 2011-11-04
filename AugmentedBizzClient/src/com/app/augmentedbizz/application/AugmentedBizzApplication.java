package com.app.augmentedbizz.application;

import android.app.Application;
import android.content.Context;

import com.app.augmentedbizz.application.status.ApplicationState;
import com.app.augmentedbizz.application.status.ApplicationStateManager;
import com.app.augmentedbizz.cache.DummyCacheData;
import com.app.augmentedbizz.logging.DebugLog;

/**
 * Represents the application context and facade for other application components
 * 
 * @author Vladi
 *
 */
public class AugmentedBizzApplication extends Application implements IApplicationFacade {
	
	private static String LIBRARY_QCAR = "QCAR";
	private static String LIBRARY_AUGBIZZ = "AugmentedBizzClient";
	
	@Override
	public void onCreate() {
		super.onCreate();
		
    	this.loadLibrary(AugmentedBizzApplication.LIBRARY_QCAR);
    	this.loadLibrary(AugmentedBizzApplication.LIBRARY_AUGBIZZ);
		
		this.initializeApplication();
		
	}
	
    private void initializeApplication() {
    	this.initializeApplicationNative();
    	
    	// Deinitialization state is set in MainActivity, as Application::onTerminate() is not reliable
    	this.getApplicationStateManager().setApplicationState(ApplicationState.INITIALIZING);
    	
    	DummyCacheData.storeDummyModelsInDatabase(this);
    }
    
    /**
     * Loads the library with the given name.
     * 
     * @param libraryName Name of the library to load.
     */
    private void loadLibrary(String libraryName) {
    	try {
            System.loadLibrary(libraryName);
            DebugLog.logi("Native library lib" + libraryName + ".so loaded");
        }
        catch (UnsatisfiedLinkError ulee) {
            DebugLog.loge("The library lib" + libraryName +
                            ".so could not be loaded", ulee);
        }
        catch (SecurityException se) {
            DebugLog.loge("The library lib" + libraryName +
                            ".so was not allowed to be loaded");
        }
    }
    
    private native void initializeApplicationNative();
	
	@Override
	public ApplicationStateManager getApplicationStateManager() {
		return ApplicationStateManager.getInstance();
	}

	@Override
	public Context getContext()
	{
		return this;
	}
}
