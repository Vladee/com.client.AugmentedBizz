package com.app.augmentedbizz.ui.glview;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import com.qualcomm.QCAR.QCAR;


import android.content.Context;
import android.graphics.PixelFormat;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

public class AugmentedGLSurfaceView extends GLSurfaceView
{

	public AugmentedGLSurfaceView(Context context)
	{
		super(context);
	}
	
	/**
	 * XML constructor
	 * 
	 * @param context
	 * @param attrs
	 */
	public AugmentedGLSurfaceView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	/** 
	 * Initialization of the surface view
	 */
    public void init(boolean translucent, int depth, int stencil)
    {
        setTranslucent(translucent);
        setEGLContextFactory(createContextFactory());

        setEGLConfigChooser(createConfigChooser(translucent, depth, stencil));
    }
    
    /**
     * Setup for the translucency, e.g. if background views should be visible behind the GL Surface 
     * @param translucent
     */
    public void setTranslucent(boolean translucent)
    {
    	this.getHolder().setFormat(translucent ? PixelFormat.TRANSLUCENT : PixelFormat.RGB_565);
    }
    
    /**
     * Creates a context factory for OpenGL 1.x and 2.0 depending on the flags
     * @param flags
     * @return Instance of an OpenGL ES context factory
     */
    protected GLSurfaceView.EGLContextFactory createContextFactory()
    {
    	return new GLSurfaceView.EGLContextFactory()
		{
    		private int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
    		
            public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig)
            {
            	int[] attribListGL20 = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};
                return egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attribListGL20);
            }

            public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context)
            {
                egl.eglDestroyContext(display, context);
            }
		};
    }
    
    protected AbstractConfigChooser createConfigChooser(boolean transclucent, int depth, int stencil)
    {
    	AbstractConfigChooser configChooser = null;
    	if(transclucent)
    	{
    		configChooser = new RGB8888ConfigChooser(depth, stencil);
    	}
    	else
    	{
    		configChooser = new RGB565ConfigChooser(depth, stencil);
    	}
    	
    	return configChooser;
    }
}
