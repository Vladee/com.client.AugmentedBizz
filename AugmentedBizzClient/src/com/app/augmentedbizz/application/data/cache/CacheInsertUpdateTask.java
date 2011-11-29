package com.app.augmentedbizz.application.data.cache;

import com.app.augmentedbizz.logging.DebugLog;
import com.app.augmentedbizz.ui.renderer.OpenGLModel;

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
		if(params[0] instanceof OpenGLModel) {
			OpenGLModel model = (OpenGLModel)params[0];
			try {
				//insert or update model
				dbAdapter.open();
				if(!dbAdapter.isModelExisting(model.getId())) {
					dbAdapter.insertModel(model);
				}
				else {
					dbAdapter.updateModel(model);
				}
			}
			catch(Exception e) {
				DebugLog.loge("An error ocurred while inserting model with id " + model.getId() + ".", e);
				return 1;
			}
			finally {
				dbAdapter.close();
			}
		}
		
		return 1;
	}

}
