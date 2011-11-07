package com.app.augmentedbizz.ui;

import android.os.Bundle;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.application.status.ApplicationState;
import com.app.augmentedbizz.ui.widget.InfoPanelSlidingDrawer;

public class MainActivity extends AugmentedBizzActivity {
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);
        
        InfoPanelSlidingDrawer slider = (InfoPanelSlidingDrawer)findViewById(R.id.slidingDrawerInfoPanel);
        slider.showLoadingSymbol();
    }
    
	@Override
	protected void onDestroy() {
		// Application::onTerminate() isn't called reliably, hence set the status here
		this.getAugmentedBizzApplication().
			getApplicationStateManager().
			setApplicationState(ApplicationState.DEINITIALIZING);
		
		super.onDestroy();
	}
    
}