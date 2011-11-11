package com.app.augmentedbizz.cache;

import android.content.Context;

import com.app.augmentedbizz.application.ApplicationFacade;
import com.app.augmentedbizz.application.data.DataManager;
import com.app.augmentedbizz.ui.renderer.OpenGLModelConfiguration;

/**
 * Implements a cache manager which handles data in- and ouput in the internal
 * SQLite database.
 * 
 * @author Vladi
 *
 */
public class CacheManager {
	private CacheDbAdapter dbAdapter;
	
	public CacheManager(Context context) {
		dbAdapter = new CacheDbAdapter(context);
	}
	
	/**
	 * Inserts or updates an already existing model in the db cache.
	 * 
	 * @param model The model object which sould be inserted or updated.
	 */
	public void insertOrUpdateModelAsync(OpenGLModelConfiguration model) {
		new CacheInsertUpdateTask(dbAdapter).execute(model);
	}
	
	/**
	 * Tries to read a model from the db cache of the application.
	 * 
	 * @param modelId The id of the model which should be fetched.
	 * @param responseListener The response listener which handles successful or failed fetches.
	 */
	public void readModelAsync(int modelId, CacheResponseListener responseListener) {
		new CacheRetrievalTask(dbAdapter, responseListener).execute(modelId);
	}
}
