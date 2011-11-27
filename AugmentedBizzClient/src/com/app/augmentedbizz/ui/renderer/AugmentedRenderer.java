package com.app.augmentedbizz.ui.renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLSurfaceView.Renderer;

import com.app.augmentedbizz.application.ApplicationFacade;
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
     * Native function to init and update the rendering. 
     */
    private native void initRendering();
    public native void updateRendering(int width, int height);
    
    /** 
     * The native render function. 
     */
    private native void renderFrame();

	@Override
	public void onDrawFrame(GL10 arg0) {
		if(active) {
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
		
		//TODO
		initRendering();
        
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
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}
	
}
