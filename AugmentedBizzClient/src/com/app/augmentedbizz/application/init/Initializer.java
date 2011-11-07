package com.app.augmentedbizz.application.init;

import android.os.AsyncTask;

import com.app.augmentedbizz.application.AugmentedBizzApplication;
import com.app.augmentedbizz.application.ApplicationFacade;
import com.app.augmentedbizz.application.status.ApplicationState;
import com.app.augmentedbizz.cache.DummyCacheData;
import com.app.augmentedbizz.logging.DebugLog;
import com.qualcomm.QCAR.QCAR;

/**
 * Initializing tasks and components of the application
 * @author Vladi
 *
 */
public class Initializer extends AsyncTask<Object, Integer, Object>
{
	private static String LIBRARY_QCAR = "QCAR";
	private static String LIBRARY_AUGBIZZ = "AugmentedBizzClient";
	
	private ApplicationFacade facade;
	
	public Initializer(ApplicationFacade facade)
	{
		this.facade = facade;
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
	
    /**
	 * Loads necessary application libraries as shared objects.
	 */
	private void loadSharedLibraries()
	{
		loadLibrary(LIBRARY_QCAR);
    	loadLibrary(LIBRARY_AUGBIZZ);
	}
	
	/**
     * Native component initializing.
     */
    private native void initializeApplicationNative();
    
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
    	facade.getApplicationStateManager().setApplicationState(ApplicationState.INITIALIZING);
    }
    
	@Override
	protected Object doInBackground(Object... arg0)
	{
		try
		{
			while(facade.getUIManager().getMainActivity() == null)
			{
				Thread.sleep(10);
			}
			publishProgress(1);
			loadSharedLibraries();
			initializeApplicationNative();
			initializeMainQCARComponents();
			publishProgress(2);
			facade.getUIManager().getMainActivity().getRenderManager().initialize();
			initializeQCARTracker();
		}
		catch(Exception e)
		{
			return e;
		}

		return new Integer(100);
	}
	
	@Override
	protected void onProgressUpdate(Integer... values)
	{
		if(values[0] == 1)
		{
			facade.getUIManager().getMainActivity().showSplashScreen();
		}
		else if(values[0] == 2)
		{
			facade.getUIManager().getMainActivity().showMainScreen();
		}
	}
	
	@Override
	protected void onPostExecute(Object result)
	{
		if(result instanceof Integer)
		{
			facade.getApplicationStateManager().setApplicationState(ApplicationState.TRACKING);
		}
		else
		{
			facade.getApplicationStateManager().setApplicationState(ApplicationState.DEINITIALIZING);
		}
		
	}
	
	/**
	 * Initializes the main QCAR components.
	 * 
	 * @throws Exception Thrown on initialization error.
	 */
	private void initializeMainQCARComponents() throws Exception
	{
		QCAR.setInitParameters(facade.getUIManager().getMainActivity(), QCAR.GL_20);
		
		int process = 0;
		while(process >= 0 && process < 100)
		{
			process = QCAR.init();
		}
		if(process < 0)
		{
			throw(new Exception("Main QCAR initialization failed"));
		}
	}
	
	/**
	 * Initializes the QCAR tracker component.
	 * 
	 * @throws Exception Thrown on initialization error.
	 */
	private void initializeQCARTracker() throws Exception
	{
		int process = 0;
		while(process >= 0 && process < 100)
		{
			process = QCAR.load();
		}
		if(process < 0)
		{
			throw(new Exception("Main QCAR initialization failed"));
		}
	}
}
