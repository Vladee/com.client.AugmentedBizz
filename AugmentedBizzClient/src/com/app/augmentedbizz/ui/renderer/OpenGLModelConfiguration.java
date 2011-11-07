package com.app.augmentedbizz.ui.renderer;

/**
 * Holds an {@link OpenGLModel} along with its preferred rendering settings.
 * 
 * @author Miffels
 *
 */
public class OpenGLModelConfiguration {
	
	private OpenGLModel openGLModel;
	private float preferredScaleFactor;
	
	public OpenGLModelConfiguration(OpenGLModel openGLModel, float preferredScaleFactor) {
		super();
		this.openGLModel = openGLModel;
		this.preferredScaleFactor = preferredScaleFactor;
	}

	public OpenGLModel getOpenGLModel() {
		return openGLModel;
	}

	public float getPreferredScaleFactor() {
		return preferredScaleFactor;
	}
	
}
