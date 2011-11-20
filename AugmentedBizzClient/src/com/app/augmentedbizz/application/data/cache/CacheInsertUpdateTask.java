package com.app.augmentedbizz.application.data.cache;

import com.app.augmentedbizz.logging.DebugLog;
import com.app.augmentedbizz.ui.renderer.OpenGLModelConfiguration;

import android.os.AsyncTask;

/**
 * Handles the asynchronous cache data insertion and update of models.
 *  
 * @author Vladi
 *
 */
public class CacheInsertUpdateTask extends AsyncTask<Object, Integer, Object> {
	private CacheDbAdapter dbAdapter;
	
	public CacheInsertUpdateTask(CacheDbAdapter dbAdapter) {
		this.dbAdapter = dbAdapter;
	}
	
	@Override
	protected Object doInBackground(Object... params) {
		if(params[0] instanceof OpenGLModelConfiguration) {
			OpenGLModelConfiguration modelConfig = (OpenGLModelConfiguration)params[0];
			try {
				//insert or update model
				dbAdapter.open();
				if(!dbAdapter.isModelExisting(modelConfig.getOpenGLModel().getId())) {
					dbAdapter.insertModel((OpenGLModelConfiguration)params[0]);
				}
				else {
					dbAdapter.updateModel(modelConfig);
				}
				dbAdapter.close();
			}
			catch(Exception e) {
				DebugLog.loge("An error ocurred inserting a model.", e);
				return 1;
			}
		}
		
		return 1;
	}

}
