package com.app.augmentedbizz.application.data;

import com.app.augmentedbizz.ui.renderer.OpenGLModelConfiguration;

public interface ModelDataListener {
	
	void onModelData(OpenGLModelConfiguration openGLModelConfiguration, boolean retrievingNewerVersion);
	void onModelError(Exception e);

}
