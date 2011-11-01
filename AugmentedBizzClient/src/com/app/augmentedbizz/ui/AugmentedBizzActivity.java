package com.app.augmentedbizz.ui;

import android.app.Activity;

import com.app.augmentedbizz.application.AugmentedBizzApplication;

public class AugmentedBizzActivity extends Activity {
	
	/**
	 * A project-specific getApplication()-implementation.
	 * 
	 * @return The {@link AugmentedBizzApplication}.
	 */
	public AugmentedBizzApplication getAugmentedBizzApplication() {
		return (AugmentedBizzApplication)((Activity)this).getApplication();
	}

}
