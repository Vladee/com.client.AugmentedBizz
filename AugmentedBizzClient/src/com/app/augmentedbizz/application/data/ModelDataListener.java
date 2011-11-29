package com.app.augmentedbizz.application.data;

import com.app.augmentedbizz.ui.renderer.OpenGLModel;

public interface ModelDataListener {
	
	void onModelData(OpenGLModel openGLModelConfiguration, boolean retrievingNewerVersion);
	void onModelError(Exception e);

}
