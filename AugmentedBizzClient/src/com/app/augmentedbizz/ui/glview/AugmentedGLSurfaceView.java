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
    public void init(int flags, boolean translucent, int depth, int stencil)
    {
        setTranslucent(translucent);
        setEGLContextFactory(createContextFactory(flags));

        setEGLConfigChooser(createConfigChooser(flags, translucent, depth, stencil));
    }
    
    /**
     * @param flags
     * @return Whether OpenGL ES 2.0 can be used or not
     */
    public boolean canUseOpenGLES2(final int flags)
    {
    	return (flags & QCAR.GL_20) != 0;
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
    protected GLSurfaceView.EGLContextFactory createContextFactory(final int flags)
    {
    	return new GLSurfaceView.EGLContextFactory()
		{
    		private int EGL_CONTEXT_CLIENT_VERSION = 0x3098;
    		
            public EGLContext createContext(EGL10 egl, EGLDisplay display, EGLConfig eglConfig)
            {
                EGLContext context;
                if(canUseOpenGLES2(flags))
                {
                    int[] attribListGL20 = {EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE};
                    context = egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attribListGL20);
                }
                else
                {
                    int[] attribListGL1x = {EGL_CONTEXT_CLIENT_VERSION, 1, EGL10.EGL_NONE};
                    context = egl.eglCreateContext(display, eglConfig, EGL10.EGL_NO_CONTEXT, attribListGL1x);
                }
                return context;
            }

            public void destroyContext(EGL10 egl, EGLDisplay display, EGLContext context)
            {
                egl.eglDestroyContext(display, context);
            }
		};
    }
    
    protected AbstractConfigChooser createConfigChooser(int flags, boolean transclucent, int depth, int stencil)
    {
    	AbstractConfigChooser configChooser = null;
    	if(transclucent)
    	{
    		configChooser = new RGB8888ConfigChooser(depth, stencil, canUseOpenGLES2(flags));
    	}
    	else
    	{
    		configChooser = new RGB565ConfigChooser(depth, stencil, canUseOpenGLES2(flags));
    	}
    	
    	return configChooser;
    }
}
