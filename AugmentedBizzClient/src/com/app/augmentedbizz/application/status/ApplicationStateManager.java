package com.app.augmentedbizz.application.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.app.augmentedbizz.logging.DebugLog;


/**
 * @author Michael Jess
 * 
 * A StateManager class that enables Java and C++ components to keep track
 * of application state changes.
 *
 */
public class ApplicationStateManager implements ApplicationStateListener {
	
    /**
     * Variable that stores the current application state.
     */
    private ApplicationState currentState = ApplicationState.UNINITIATED;
    /**
     * List of listeners that will be notified when a state change takes place.
     */
    private List<ApplicationStateListener> applicationStateListener = new ArrayList<ApplicationStateListener>();
    
    private static ApplicationStateManager instance = new ApplicationStateManager();
    private ApplicationStateManager() {
    	this.addApplicationStateListener(this);
    }
    
    public static ApplicationStateManager getInstance() {
    	return ApplicationStateManager.instance;
    }
    
    /**
     * Adds a new {@link ApplicationStateListener} to the listener list.
     * 
     * @param listener The listener to add.
     */
    public void addApplicationStateListener(ApplicationStateListener listener) {
    	this.applicationStateListener.add(listener);
    }
    
    /**
     * Removes a {@link ApplicationStateListener} from the listener list.
     * 
     * @param listener The listener to remove.
     */
    public void removeApplicationStateListener(ApplicationStateListener listener) {
    	this.applicationStateListener.remove(listener);
    }
    
    /**
     * This method allows the caller to change the global application state.
     * 
     * @param nextState The next application state that should be entered.
     */
    public void setApplicationState(ApplicationState nextState) {
    	this.fireApplicationStateChangedEvent(nextState);
    	if(nextState != ApplicationState.INITIALIZING) {
    		// Initializing is the default state in native
    		// and cannot be changed before it is initialized.
    		this.fireApplicationStateChangedEventNative(nextState.getIndex());
    	}
    }

	/* (non-Javadoc)
	 * @see com.app.augmentedbizz.states.ApplicationStateListener#onApplicationStateChange(int)
	 */
	@Override
	public void onApplicationStateChange(ApplicationState nextState) {
		DebugLog.logi("Application state changed. Moved from " +
				this.currentState +" to " +
				nextState + ".");
		this.currentState = nextState;
	}
	
	/**
	 * Used to notify java listeners that the application state changed. 
	 * 
	 * @param nextState The next state that will be entered.
	 */
	private void fireApplicationStateChangedEvent(ApplicationState nextState) {
		Iterator<ApplicationStateListener> it = this.applicationStateListener.iterator();
		
		while(it.hasNext()) {
			it.next().onApplicationStateChange(nextState);
		}
	}
	
	private void fireApplicationStateChangedEvent(int nextState) {
		this.fireApplicationStateChangedEvent(ApplicationState.values()[nextState]);
	}
	
	/**
	 * Used to notify native listeners that the application state changed.
	 * 
	 * @param nextState The next state that will be entered.
	 */
	private native void fireApplicationStateChangedEventNative(int jnextState);
    
}
