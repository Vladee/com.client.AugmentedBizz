package com.app.augmentedbizz.ui.glview;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

import android.opengl.GLSurfaceView;


public abstract class AbstractConfigChooser implements GLSurfaceView.EGLConfigChooser {
	protected int redSize;
	protected int greenSize;
	protected int blueSize;
	protected int alphaSize;
	protected int depthSize;
	protected int stencilSize;
	
	public AbstractConfigChooser(int r, int g, int b, int a, int depth, int stencil) {
		this.redSize = r;
		this.greenSize = g;
		this.blueSize = b;
		this.alphaSize = a;
		this.depthSize = depth;
		this.stencilSize = stencil;
	}
	
	protected EGLConfig getMatchingConfig(EGL10 egl, EGLDisplay display, int[] configAttribs)
    {
        //get the number of minimally matching EGL configurations
        int[] numConfigs = new int[1];
        egl.eglChooseConfig(display, configAttribs, null, 0, numConfigs);

        if(numConfigs[0] <= 0)
        {
            throw(new IllegalArgumentException());
        }
        //allocate then read the array of minimally matching EGL configs
        EGLConfig[] configs = new EGLConfig[numConfigs[0]];
        egl.eglChooseConfig(display, configAttribs, configs, numConfigs[0], numConfigs);

        return chooseConfig(egl, display, configs);
    }
	
	public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display)
    {
        final int[] configAttribsGL20 =
        {
            EGL10.EGL_RED_SIZE, 4,
            EGL10.EGL_GREEN_SIZE, 4,
            EGL10.EGL_BLUE_SIZE, 4,
            EGL10.EGL_RENDERABLE_TYPE, 0x0004,
            EGL10.EGL_NONE
        };

        return getMatchingConfig(egl, display, configAttribsGL20);
    }
	
	public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display, EGLConfig[] configs) {
		for(EGLConfig config : configs)
        {
            int configDepthSize = findConfigAttrib(egl, display, config, EGL10.EGL_DEPTH_SIZE, 0);
            int configStencilSize = findConfigAttrib(egl, display, config, EGL10.EGL_STENCIL_SIZE, 0);

            if (configDepthSize < depthSize || configStencilSize < stencilSize)
                continue;

            //we want an *exact* match for red/green/blue/alpha
            int r = findConfigAttrib(egl, display, config, EGL10.EGL_RED_SIZE, 0);
            int g = findConfigAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE, 0);
            int b = findConfigAttrib(egl, display, config, EGL10.EGL_BLUE_SIZE, 0);
            int a = findConfigAttrib(egl, display, config, EGL10.EGL_ALPHA_SIZE, 0);

            if (r == redSize && g == greenSize && b == blueSize && a == alphaSize)
            {
                return config;
            }
        }

        return null;
	}

	protected int findConfigAttrib(EGL10 egl, EGLDisplay display, EGLConfig config, int attribute, int defaultValue)
    {
		int[] value = new int[1];
        if(egl.eglGetConfigAttrib(display, config, attribute, value))
        {
        	return value[0];
        }
        return defaultValue;
    }
}
