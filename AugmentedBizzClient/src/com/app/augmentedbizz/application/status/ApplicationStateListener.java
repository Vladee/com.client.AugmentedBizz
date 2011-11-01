package com.app.augmentedbizz.application.status;


/**
 * @author Miffels
 *
 * Interface that defines events applicaton modules should listen to in order to
 * be notified when the application state changes.
 *
 */
public interface ApplicationStateListener {
	
	public void onApplicationStateChange(ApplicationState nextState);

}
