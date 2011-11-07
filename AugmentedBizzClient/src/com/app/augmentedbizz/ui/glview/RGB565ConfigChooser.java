package com.app.augmentedbizz.ui.glview;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;


public class RGB565ConfigChooser extends AbstractConfigChooser
{

	public RGB565ConfigChooser(int depth, int stencil)
	{
		super(5, 6, 5, 0, depth, stencil);
	}


}
