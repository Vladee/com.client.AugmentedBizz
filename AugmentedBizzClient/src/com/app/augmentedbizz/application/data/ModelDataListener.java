package com.app.augmentedbizz.application.data;

import com.app.augmentedbizz.ui.renderer.OpenGLModelConfiguration;

public interface ModelDataListener {
	
	void onModelData(OpenGLModelConfiguration openGLModelConfiguration);
	void onModelError(Exception e);

}
