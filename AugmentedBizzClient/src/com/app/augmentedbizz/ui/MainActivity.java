package com.app.augmentedbizz.ui;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.cache.DummyCacheData;
import com.app.augmentedbizz.states.StateManager;

import android.app.Activity;
import android.os.Bundle;

public class MainActivity extends Activity {
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.initializeApplication();
        
        setContentView(R.layout.main);
    }
    
    private void initializeApplication() {
    	StateManager.getInstance().setApplicationState(StateManager.APPSTATUS_INITIALIZING);
    	
    	DummyCacheData.storeDummyModelsInDatabase(this);
    }

	@Override
	protected void onDestroy() {
		StateManager.getInstance().setApplicationState(StateManager.APPSTATUS_DEINIALIZING);
		super.onDestroy();
	}
    
}