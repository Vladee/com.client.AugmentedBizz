package com.app.augmentedbizz.ui.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

import com.app.augmentedbizz.application.ApplicationFacade;
import com.app.augmentedbizz.application.status.ApplicationState;
import com.qualcomm.QCAR.QCAR;

/**
 * Implements the Java-side representation of the renderer for the camera frames and augmented objects
 * The renderer is managed by a render manager
 * @author Vladi
 *
 */
public class AugmentedRenderer implements Renderer {
	protected boolean active = true;
	ApplicationFacade application;
	
	public AugmentedRenderer(ApplicationFacade application) {
		this.application = application;
	}
	
    /** 
     * Native function to update the renderer. 
     */
    public native void updateRendering(int width, int height);
    
    /** 
     * The native render function. 
     */
    private native void renderFrame();
    private native void scanFrame();

	@Override
	public void onDrawFrame(GL10 arg0) {
		ApplicationState currentState = this.application.getApplicationStateManager().getApplicationState();
		if(currentState.equals(ApplicationState.TRACKING)) {
			this.scanFrame();
		} else
		if(currentState.equals(ApplicationState.SHOWING_CACHE) ||
				currentState.equals(ApplicationState.LOADING_INDICATORS) ||
				currentState.equals(ApplicationState.SHOWING)) {
			this.renderFrame();
		}
	}

	@Override
	public void onSurfaceChanged(GL10 gl, int width, int height)
	{	
		//call native function to update rendering
        updateRendering(width, height);

        // Call QCAR function to handle render surface size changes:
        QCAR.onSurfaceChanged(width, height);
	}

	@Override
	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        //call QCAR function to (re)initialize rendering after first use
        //or after OpenGL ES context was lost (e.g. after onPause/onResume)
        QCAR.onSurfaceCreated();
	}

	/**
	 * @return whether the renderer is actively rendering or not
	 */
	public boolean isActive() {
		return active;
	}

	/**
	 * @param active True, if the renderer should actively render the data
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

}
