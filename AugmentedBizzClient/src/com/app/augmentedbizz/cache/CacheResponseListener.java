package com.app.augmentedbizz.cache;

import com.app.augmentedbizz.ui.renderer.OpenGLModelConfiguration;

/**
 * Response listener for the internal database cache.
 * 
 * @author Vladi
 *
 */
public interface CacheResponseListener {
	/**
	 * Gets called when a model was successfully fetched from the cache.
	 * 
	 * @param model The model object which was retrieved.
	 */
	public void onLoadedModelConfig(OpenGLModelConfiguration model);
	
	/**
	 * Gets called when a model retrieval couldn't be satsfied by the cache.
	 * 
	 * @param modelId The id of the model which failed to be fetched.
	 */
	public void onFailedModelConfigLoading(int modelId);
}
