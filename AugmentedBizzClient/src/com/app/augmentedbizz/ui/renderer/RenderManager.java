package com.app.augmentedbizz.ui.renderer;

import android.util.DisplayMetrics;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.application.data.ModelDataListener;
import com.app.augmentedbizz.application.status.ApplicationState;
import com.app.augmentedbizz.application.status.ApplicationStateListener;
import com.app.augmentedbizz.ui.MainActivity;
import com.app.augmentedbizz.ui.glview.AugmentedGLSurfaceView;
import com.app.augmentedbizz.util.Display;
import com.qualcomm.QCAR.QCAR;

/**
 * Manager for graphical rendering.
 * 
 * @author Vladi
 *
 */
public class RenderManager implements ModelDataListener, ApplicationStateListener
{
	public static int depthSize = 16;
	public static int stencilSize = 0;
	private MainActivity mainActivity;
	private boolean initialized = false;
	
	public RenderManager(MainActivity mainActivity) {
		this.mainActivity = mainActivity;
		mainActivity.getAugmentedBizzApplication().getApplicationStateManager().addApplicationStateListener(this);
	}
	
	/**
	 * Initializes the render manager by setting up the GL surface view 
	 * and passing necessary data to the native side.
	 * 
	 * @return true, if the initialization was successful
	 */
	public boolean initialize()
	{
		if(getGlSurfaceView() == null)
		{
			return false;
		}
		
		if(!initialized)
		{
			getGlSurfaceView().setup(QCAR.requiresAlpha(), depthSize, stencilSize);
			setupScreenDimensions(Display.getScreenWidth(mainActivity), Display.getScreenHeight(mainActivity));
			initialized = true;
		}
		return true;
	}
	
	/**
	 * @return The GL surface view of the main screen.
	 */
	public AugmentedGLSurfaceView getGlSurfaceView()
	{
		return (AugmentedGLSurfaceView)mainActivity.findViewById(R.id.augmentedGLSurfaceView);
	}
	
	/**
	 * @return The renderer for the 3D augmentation
	 */
	public AugmentedRenderer getRenderer()
	{
		return getGlSurfaceView().getRenderer();
	}
	
	/**
	 * Setup of screen dimensions on the native side.
	 * 
	 * @param width of the screen
	 * @param height of the screen
	 */
	private native void setupScreenDimensions(int width, int height);
	
	/** 
	 * Native methods for starting and stoping the camera. 
	 */ 
    public native void startCamera();
    public native void stopCamera();

	@Override
	public void onModelData(OpenGLModelConfiguration openGLModelConfiguration)
	{
		// TODO
	}

	@Override
	public void onModelError(Exception e)
	{
		// TODO
	}

	@Override
	public void onApplicationStateChange(ApplicationState nextState)
	{
		// TODO
		if(nextState.equals(ApplicationState.TRACKING))
		{
			startCamera();
		}
		else if(nextState.equals(ApplicationState.DEINITIALIZING))
		{
			stopCamera();
		}
	}
}
