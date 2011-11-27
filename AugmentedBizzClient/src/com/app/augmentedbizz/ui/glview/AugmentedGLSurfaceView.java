package com.app.augmentedbizz.ui.glview;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import com.app.augmentedbizz.application.ApplicationFacade;
import com.app.augmentedbizz.ui.renderer.AugmentedRenderer;

public class AugmentedGLSurfaceView extends GLSurfaceView {
	private AugmentedRenderer renderer;
	
	public AugmentedGLSurfaceView(Context context) {

		super(context);
	}
	
	/**
	 * XML constructor
	 * 
	 * @param context
	 * @param attrs
	 */
	public AugmentedGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/** 
	 * Initialization of the surface view
	 */
    public void setup(boolean translucent, int depth, int stencil, ApplicationFacade application) {
        setTranslucent(translucent);
        setEGLContextFactory(createContextFactory());
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setEGLConfigChooser(createConfigChooser(translucent, depth, stencil));
        setupRenderer(application);
    }
    
    /**
	 * @return the renderer for the GL surface augmentation
	 */
	public AugmentedRenderer getRenderer() {
		return renderer;
	}

	/**
     * Setup for the translucency, e.g. if background views should be visible behind the GL Surface 
     * @param translucent
     */
    public void setTranslucent(boolean translucent) {
    	this.getHolder().setFormat(translucent ? PixelFormat.TRANSLUCENT : PixelFormat.RGB_565);
    }
    
    /**
     * Creates a context factory for OpenGL 1.x and 2.0 depending on the flags
     * @param flags
     * @return Instance of an OpenGL ES context factory
     */
    protected GLSurfaceView.EGLContextFactory createContextFactory() {
    	return new GLSurfaceView.EGLContextFactory() {
    		private int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
    		
            public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig) {
            	int[] attribListGL20 = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};
                return egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attribListGL20);
            }

            public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context) {
                egl.eglDestroyContext(display, context);
            }
		};
    }
    
    protected AbstractConfigChooser createConfigChooser(boolean transclucent, int depth, int stencil) {
    	AbstractConfigChooser configChooser = null;
    	if(transclucent) {
    		configChooser = new RGB8888ConfigChooser(depth, stencil);
    	}
    	else {
    		configChooser = new RGB565ConfigChooser(depth, stencil);
    	}
    	
    	return configChooser;
    }
    
    private void setupRenderer(ApplicationFacade application) {
    	if(renderer == null) {
    		renderer = new AugmentedRenderer(application);
    		this.setRenderer(renderer);
    	}
    }
}
