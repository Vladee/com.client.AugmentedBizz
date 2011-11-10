package com.app.augmentedbizz.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.application.init.Initializer;
import com.app.augmentedbizz.application.status.ApplicationState;
import com.app.augmentedbizz.logging.DebugLog;
import com.app.augmentedbizz.ui.glview.AugmentedGLSurfaceView;
import com.app.augmentedbizz.ui.renderer.RenderManager;
import com.app.augmentedbizz.ui.widget.InfoPanelSlidingDrawer;
import com.app.augmentedbizz.ui.widget.InfoPanelSlidingDrawer.StateIndicatorValue;
import com.qualcomm.QCAR.QCAR;

public class MainActivity extends AugmentedBizzActivity {
	
	private RenderManager renderManager = null;
	private RelativeLayout mainLayout = null;
	private AugmentedGLSurfaceView glSurfaceView = null;
	private InfoPanelSlidingDrawer infoPanelSlider = null;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	DebugLog.logi("onCreate()");
        super.onCreate(savedInstanceState);
        
        renderManager = new RenderManager(this);
        getAugmentedBizzApplication().getUIManager().setMainActivity(this);
        
        getAugmentedBizzApplication().startInitialization();
    }
    
	@Override
	protected void onDestroy() {
		DebugLog.logi("onDestroy()");
		getAugmentedBizzApplication().getApplicationStateManager().setApplicationState(ApplicationState.UNINITIATED);
		
		QCAR.deinit();
		
		super.onDestroy();
	}
    
	@Override
	protected void onResume()
	{
		DebugLog.logi("onResume()");
		super.onResume();
		
		QCAR.onResume();
		
		if(getAugmentedBizzApplication().getApplicationStateManager().getApplicationState().equals(ApplicationState.DEINITIALIZING))
			getRenderManager().startCamera();
		
		if(mainLayout != null)
		{
			mainLayout.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onStop()
	{
		DebugLog.logi("onStop()");
		super.onStop();
		
		getAugmentedBizzApplication().getApplicationStateManager().setApplicationState(ApplicationState.DEINITIALIZING);
	}
	
	@Override
	protected void onPause()
	{
		DebugLog.logi("onPause()");
		super.onPause();
	
		//hide the main screen
		if(mainLayout != null)
		{
			mainLayout.setVisibility(View.GONE);
			getRenderManager().getGlSurfaceView().onPause();
		}
		
		QCAR.onPause();
		
		//stop camera if running
		if(!getAugmentedBizzApplication().getApplicationStateManager().getApplicationState().equals(ApplicationState.DEINITIALIZING))
		{
			getRenderManager().stopCamera();
		}
	}
	
	/**
	 * Shows the splash screen on the display.
	 */
	public void showSplashScreen()
	{
		DebugLog.logi("Showing splash screen");
		setContentView(R.layout.splash);
	}
	
	/**
	 * Shows the main screen of the app.
	 */
	public void showMainScreen()
	{
		DebugLog.logi("Showing main screen");
		setContentView(R.layout.main);
		
		//load necessary UI elements
		mainLayout = (RelativeLayout)findViewById(R.id.relativeLayoutMain);
		glSurfaceView = (AugmentedGLSurfaceView)findViewById(R.id.augmentedGLSurfaceView);
		infoPanelSlider = (InfoPanelSlidingDrawer)findViewById(R.id.slidingDrawerInfoPanel);
	}
	
	/**
	 * @return the renderManager
	 */
	public RenderManager getRenderManager()
	{
		return renderManager;
	}
	
}