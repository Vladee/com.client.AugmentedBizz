package com.app.augmentedbizz.application.data.cache;

import android.os.AsyncTask;

import com.app.augmentedbizz.ui.renderer.OpenGLModel;

/**
 * Handles the asynchronous cache data retrieval of models.
 *  
 * @author Vladi
 *
 */
public class CacheRetrievalTask extends AsyncTask<Object, Integer, Object> {
	private CacheDbAdapter dbAdapter;
	private int modelId;
	private CacheResponseListener responseListener;
	
	public CacheRetrievalTask(CacheDbAdapter dbAdapter, CacheResponseListener responseListener) {
		this.dbAdapter = dbAdapter;
		this.responseListener = responseListener;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		if(params[0] instanceof Integer) {
			modelId = (Integer)params[0];
			OpenGLModel model = null;
			//fetch model
			try {
				dbAdapter.open();
				model = dbAdapter.fetchModel(new Long(modelId));
			}
			catch(Exception e) {
				return e;
			}
			finally {
				dbAdapter.close();
			}
			if(model != null) {
				return model;
			}
		}
		
		return new Exception();
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if (result instanceof OpenGLModel && responseListener != null) {
			OpenGLModel model = (OpenGLModel)result;
			responseListener.onModelConfigFromCache(model);
		} else {
			responseListener.onCacheFailure(modelId);
		}
	}

}
