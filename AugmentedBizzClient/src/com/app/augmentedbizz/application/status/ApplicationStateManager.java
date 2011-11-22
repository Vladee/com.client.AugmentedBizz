package com.app.augmentedbizz.application.status;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.os.Handler;
import android.os.Looper;

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
    
    public ApplicationStateManager() {
    	this.addApplicationStateListener(this);
    }
    /**
     * A handler for non-UI thread state changement invocations
     */
    public Handler nativeStateHandler = new Handler();
    
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
    public synchronized void setApplicationState(ApplicationState nextState) {
    	if(!nextState.equals(currentState)) {
	    	if(nextState != ApplicationState.INITIALIZING) {
	    		// Initializing is the default state in native
	    		// and cannot be changed before it is initialized.
	    		this.fireApplicationStateChangedEventNative(nextState.getIndex());
	    	}
	    	this.fireApplicationStateChangedEvent(nextState);
    	}
    }
    
    /**
     * @return Current application state.
     */
    public synchronized ApplicationState getApplicationState() {
    	return currentState;
    }

	/* (non-Javadoc)
	 * @see com.app.augmentedbizz.states.ApplicationStateListener#onApplicationStateChange(int)
	 */
	@Override
	public void onApplicationStateChange( ApplicationState lastState, ApplicationState nextState) {
		DebugLog.logi("Application state changed. Moved from " +
				lastState +" to " +
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
		ApplicationState currentState = this.currentState;
		while(it.hasNext()) {
			it.next().onApplicationStateChange(currentState, nextState);
		}
	}
	
	private synchronized void fireApplicationStateChangedEvent(final int nextState) {
		nativeStateHandler.post(new Runnable() {
			@Override
			public void run() {
				synchronized(this) {
					fireApplicationStateChangedEvent(ApplicationState.values()[nextState]);
				}
				synchronized(ApplicationStateManager.this) {
					ApplicationStateManager.this.notify();
				}
			}
		});
		
		try {
			synchronized(this) {
				wait();
			}
		} 
		catch (InterruptedException e) {
		}
		
	}
	
	/**
	 * Used to notify native listeners that the application state changed.
	 * 
	 * @param nextState The next state that will be entered.
	 */
	private native void fireApplicationStateChangedEventNative(int jnextState);
    
}
