package com.app.augmentedbizz.ui.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.qualcomm.QCAR.QCAR;

import android.opengl.GLSurfaceView.Renderer;

/**
 * Implements the Java-side representation of the renderer for the camera frames and augmented objects
 * The renderer is managed by a render manager
 * @author Vladi
 *
 */
public class AugmentedRenderer implements Renderer {
	protected boolean active = true;
	
	public AugmentedRenderer() {
	}
	
    /** 
     * Native function to update the renderer. 
     */
    public native void updateRendering(int width, int height);
    
    /** 
     * The native render function. 
     */
    public native void renderFrame();

	@Override
	public void onDrawFrame(GL10 arg0) {
		if(active) {
			//invoke native rendering
			renderFrame();
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
