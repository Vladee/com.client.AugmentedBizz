package com.app.augmentedbizz.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.application.status.ApplicationState;
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
        super.onCreate(savedInstanceState);
        
        renderManager = new RenderManager(this);
        getAugmentedBizzApplication().getUIManager().setMainActivity(this);
    }
    
	@Override
	protected void onDestroy() {
		// Application::onTerminate() isn't called reliably, hence set the status here
		this.getAugmentedBizzApplication().
			getApplicationStateManager().
			setApplicationState(ApplicationState.DEINITIALIZING);
		
		super.onDestroy();
	}
    
	@Override
	protected void onResume()
	{
		super.onResume();
		
		QCAR.onResume();
		if(mainLayout != null)
		{
			mainLayout.setVisibility(View.VISIBLE);
		}
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	
		QCAR.onPause();
		if(mainLayout != null)
		{
			mainLayout.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * Shows the splash screen on the display.
	 */
	public void showSplashScreen()
	{
		setContentView(R.layout.splash);
	}
	
	/**
	 * Shows the main screen of the app.
	 */
	public void showMainScreen()
	{
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