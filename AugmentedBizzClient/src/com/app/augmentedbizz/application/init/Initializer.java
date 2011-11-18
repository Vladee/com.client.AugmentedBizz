package com.app.augmentedbizz.application.init;

import android.os.AsyncTask;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.application.ApplicationFacade;
import com.app.augmentedbizz.application.status.ApplicationState;
import com.app.augmentedbizz.logging.DebugLog;
import com.qualcomm.QCAR.QCAR;

/**
 * Initializing tasks and components of the application
 * @author Vladi
 *
 */
public class Initializer extends AsyncTask<Object, Integer, Object> {
	private static int minSplashMillis = 2000;
	private static String LIBRARY_QCAR = "QCAR";
	private static String LIBRARY_AUGBIZZ = "AugmentedBizzClient";
	
	private ApplicationFacade facade;
	
	public Initializer(ApplicationFacade facade) {
		this.facade = facade;
	}
	
	/**
     * Loads the library with the given name.
     * 
     * @param libraryName Name of the library to load.
     */
    private static void loadLibrary(String libraryName) throws Exception {
    	try {
            System.loadLibrary(libraryName);
        }
        catch(UnsatisfiedLinkError ulee) {
            throw(new Exception("The library lib" + libraryName + ".so could not be loaded"));
        }
        catch (SecurityException se) {
        	throw(new Exception("The library lib" + libraryName + ".so was not allowed to be loaded"));
        }
    }
	
    /**
	 * Loads necessary application libraries as shared objects.
	 */
	public static void loadSharedLibraries() {
		try {
			loadLibrary(LIBRARY_QCAR);
			loadLibrary(LIBRARY_AUGBIZZ);
		}
		catch (Exception e) {
			DebugLog.logw(e.getMessage());
		}
	}
	
	/**
     * Native component initializing.
     */
    private native void initializeApplicationNative(ApplicationFacade facade);
    
    /**
     * Initialites all necessary application components.
     */
    public void initializeApplication() {
    	
    	if(facade.getApplicationStateManager().getApplicationState().equals(ApplicationState.UNINITIATED))
    	{
    		facade.getApplicationStateManager().setApplicationState(ApplicationState.INITIALIZING);
    		execute();
    	}
    	
    }
    
    @Override
    protected void onPreExecute()
    {
    	DebugLog.logi("Begin application initialization.");
    	facade.getApplicationStateManager().setApplicationState(ApplicationState.INITIALIZING);
    }
    
	@Override
	protected Object doInBackground(Object... arg0) {
		try {
			while(facade.getUIManager().getMainActivity() == null) {
				Thread.sleep(10);
			}
			publishProgress(1);
			Thread.sleep(10);
			long preTimePoint = System.currentTimeMillis();
			initializeApplicationNative(facade);
			initializeMainQCARComponents();
			initializeQCARTracker();
			System.gc();
			long postTimePoints = System.currentTimeMillis();
			if(postTimePoints - preTimePoint < 2000) {
				Thread.sleep(2000 - (postTimePoints - preTimePoint));
			}
			publishProgress(2);
			while(!facade.getUIManager().getMainActivity().getRenderManager().initialize()) {
				Thread.sleep(10);
			}
			
		}
		catch(Exception e) {
			return e;
		}

		return new Integer(100);
	}
	
	@Override
	protected void onProgressUpdate(Integer... values) {
		if(values[0] == 1) {
			facade.getUIManager().getMainActivity().showSplashScreen();
		}
		else if(values[0] == 2) {
			facade.getUIManager().getMainActivity().showMainScreen();
		}
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if(result instanceof Integer) {
			DebugLog.logi("Application initialized.");
			facade.getApplicationStateManager().setApplicationState(ApplicationState.TRACKING);
		}
		else {
			DebugLog.loge("Application initialization failed", (Exception)result);
			facade.getUIManager().showErrorDialog(R.string.errorInitialization, true);
		}
		
	}
	
	/**
	 * Initializes the main QCAR components.
	 * 
	 * @throws Exception Thrown on initialization error.
	 */
	private void initializeMainQCARComponents() throws Exception {
		QCAR.setInitParameters(facade.getUIManager().getMainActivity(), QCAR.GL_20);
		
		int process = 0;
		while(process >= 0 && process < 100) {
			process = QCAR.init();
		}
		if(process < 0) {
			throw(new Exception("Main QCAR initialization failed"));
		}
	}
	
	/**
	 * Initializes the QCAR tracker component.
	 * 
	 * @throws Exception Thrown on initialization error.
	 */
	private void initializeQCARTracker() throws Exception {
		int process = 0;
		while(process >= 0 && process < 100) {
			process = QCAR.load();
		}
		//gives negative number too if already initialized 
		if(process < -1) {
			throw(new Exception("QCAR Tracker initialization failed"));
		}
	}
}
