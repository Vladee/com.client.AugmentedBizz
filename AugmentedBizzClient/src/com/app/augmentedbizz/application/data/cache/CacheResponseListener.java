package com.app.augmentedbizz.application.data.cache;

import com.app.augmentedbizz.ui.renderer.OpenGLModel;

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
	public void onModelConfigFromCache(OpenGLModel model);
	
	/**
	 * Gets called when a model retrieval couldn't be satsfied by the cache.
	 * 
	 * @param modelId The id of the model which failed to be fetched.
	 */
	public void onCacheFailure(int modelId);
}
