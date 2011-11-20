package com.app.augmentedbizz.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.RelativeLayout;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.application.status.ApplicationState;
import com.app.augmentedbizz.logging.DebugLog;
import com.app.augmentedbizz.ui.renderer.RenderManager;
import com.app.augmentedbizz.ui.widget.InfoPanelSlidingDrawer;
import com.qualcomm.QCAR.QCAR;

public class MainActivity extends AugmentedBizzActivity {
	
	private RenderManager renderManager = null;
	private RelativeLayout mainLayout = null;
	private InfoPanelSlidingDrawer infoPanelSlider = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	DebugLog.logi("MainActivity.onCreate()");
        super.onCreate(savedInstanceState);
        
        renderManager = new RenderManager(this);
        getAugmentedBizzApplication().getUIManager().setMainActivity(this);
        
        getAugmentedBizzApplication().startInitialization(this.renderManager);
    }
    
	@Override
	protected void onDestroy() {
		DebugLog.logi("MainActivity.onDestroy()");
		getAugmentedBizzApplication().getApplicationStateManager().setApplicationState(ApplicationState.UNINITIATED);
		
		QCAR.deinit();
		
		System.gc();
		super.onDestroy();
	}
    
	@Override
	protected void onResume() {
		DebugLog.logi("MainActivity.onResume()");
		super.onResume();
		
		QCAR.onResume();
		
		if(getAugmentedBizzApplication().getApplicationStateManager().getApplicationState().equals(ApplicationState.DEINITIALIZING)) {
			getRenderManager().startCamera();
		}
		
		if(mainLayout != null) {
			getRenderManager().getGlSurfaceView().onResume();
		}
	}
	
	@Override
	protected void onStop() {
		DebugLog.logi("MainActivity.onStop()");
		super.onStop();
		
		getAugmentedBizzApplication().getApplicationStateManager().setApplicationState(ApplicationState.DEINITIALIZING);
		
		//small hack as the onDestroy() method isn't called when leaving via home button
		onDestroy();
		System.exit(0);
	}
	
	@Override
	protected void onPause() {
		DebugLog.logi("MainActivity.onPause()");
		super.onPause();
	
		//hide the main screen
		if(mainLayout != null) {
			getRenderManager().getGlSurfaceView().onPause();
		}
		
		QCAR.onPause();
		
		//stop camera if running
		if(!getAugmentedBizzApplication().getApplicationStateManager().getApplicationState().equals(ApplicationState.DEINITIALIZING)) {
			getRenderManager().stopCamera();
		}
	}
	
	/**
	 * Shows the splash screen on the display.
	 */
	public void showSplashScreen() {
		DebugLog.logi("Showing splash screen");
		setContentView(R.layout.splash);
	}
	
	/**
	 * Shows the main screen of the app.
	 */
	public void showMainScreen() {
		DebugLog.logi("Showing main screen");
		setContentView(R.layout.main);
		
		//load necessary UI elements
		mainLayout = (RelativeLayout)findViewById(R.id.relativeLayoutMain);
		infoPanelSlider = (InfoPanelSlidingDrawer)findViewById(R.id.slidingDrawerInfoPanel);
		infoPanelSlider.getWidth();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_HOME) {
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * @return the renderManager
	 */
	public RenderManager getRenderManager() {
		return renderManager;
	}
}