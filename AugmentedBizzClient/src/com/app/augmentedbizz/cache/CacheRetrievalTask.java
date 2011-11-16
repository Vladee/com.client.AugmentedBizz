package com.app.augmentedbizz.cache;

import android.os.AsyncTask;

import com.app.augmentedbizz.ui.renderer.OpenGLModelConfiguration;

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
			OpenGLModelConfiguration model = null;
			//fetch model
			try {
				dbAdapter.open();
				model = dbAdapter.fetchModel(new Long(modelId));
				dbAdapter.close();
			}
			catch(Exception e) {
				return (OpenGLModelConfiguration)null;
			}
			return model;
		}
		
		return (Object)null;
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if (result instanceof OpenGLModelConfiguration && responseListener != null) {
			OpenGLModelConfiguration modelConfig = (OpenGLModelConfiguration)result;
			responseListener.onLoadedModelConfig(modelConfig);
		} else {
			responseListener.onFailedModelConfigLoading(modelId);
		}
	}

}
