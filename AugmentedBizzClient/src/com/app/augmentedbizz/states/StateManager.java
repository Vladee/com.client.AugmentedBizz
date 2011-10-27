package com.app.augmentedbizz.states;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.app.augmentedbizz.logging.DebugLog;


/**
 * @author Michael Jess
 * 
 * A StateManager class that enables Java and C++ components to keep track
 * of application state changes.
 *
 */
public class StateManager implements ApplicationStateListener {
	
	/** Volatile initial state indicating that nothing happened yet. */
    public static final int APPSTATUS_UNINITIATED				= -1;
    /** The application is starting, splash screen is being shown,
     * and the Java/C++ components are being initialized. */
    public static final int APPSTATUS_INITIALIZING			= 0;
    /** Initialization finished, camera active and tracking. */
    public static final int APPSTATUS_TRACKING				= 1;
    /** ImageTarget captured. */
    public static final int APPSTATUS_CAPTURED				= 2;
    /** The QRCode has been identified and read. */
    public static final int APPSTATUS_SCANNED				= 3;
    /** No matching model was found in the cache, all data needs to be
     * retrieved from the server and is being loaded now. */
    public static final int APPSTATUS_LOADING				= 4;
    /** The corresponding model was found in the cache and will be displayed.
     * Simultaneously the server is requested for updates of the model. */
    public static final int APPSTATUS_SHOWING_CACHE			= 5;
    /** Data synchronization finished and the defect indicator information is
     * being loaded. */
    public static final int APPSTATUS_LOADING_INDICATORS	= 6;
    /** All data is available now, the application is displaying the model
     * with its indicators. */
    public static final int APPSTATUS_SHOWING				= 7;
    /** The application is about to be closed and thus freeing resources. */
    public static final int APPSTATUS_DEINIALIZING			= 8;
    /** Volatile state indicating that memory has been freed and the
     * app is closing now. */
    public static final int APPSTATUS_EXITING				= 9;
    
    /**
     * Variable that stores the current application state.
     */
    private int currentState = APPSTATUS_UNINITIATED;
    
    private Map<Integer, String> stateNameMap = new HashMap<Integer, String>();
    
    /**
     * List of listeners that will be notified when a state change takes place.
     */
    private List<ApplicationStateListener> applicationStateListener = new ArrayList<ApplicationStateListener>();
    
    private static StateManager instance = new StateManager();
    private StateManager() {
    	this.addApplicationStateListener(this);
    	
    	// Brief state descriptions for logging purposes
    	this.stateNameMap.put(StateManager.APPSTATUS_UNINITIATED, "UNINITIATED");
    	this.stateNameMap.put(StateManager.APPSTATUS_INITIALIZING, "INITIALIZING");
    	this.stateNameMap.put(StateManager.APPSTATUS_TRACKING, "TRACKING");
    	this.stateNameMap.put(StateManager.APPSTATUS_CAPTURED, "CAPTURED");
    	this.stateNameMap.put(StateManager.APPSTATUS_SCANNED, "SCANNED");
    	this.stateNameMap.put(StateManager.APPSTATUS_LOADING, "LOADING");
    	this.stateNameMap.put(StateManager.APPSTATUS_SHOWING_CACHE, "SHOWING FROM CACHE");
    	this.stateNameMap.put(StateManager.APPSTATUS_LOADING_INDICATORS, "LOADING INDICATORS");
    	this.stateNameMap.put(StateManager.APPSTATUS_SHOWING, "SHOWING FULL MODEL");
    	this.stateNameMap.put(StateManager.APPSTATUS_DEINIALIZING, "DEINITIALIZING");
    	this.stateNameMap.put(StateManager.APPSTATUS_EXITING, "EXITING");
    }
    
    public static StateManager getInstance() {
    	return StateManager.instance;
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
    public void setApplicationState(int nextState) {
    	this.fireApplicationStateChangedEvent(nextState);
    }

	/* (non-Javadoc)
	 * @see com.app.augmentedbizz.states.ApplicationStateListener#onApplicationStateChange(int)
	 */
	@Override
	public void onApplicationStateChange(int nextState) {
		DebugLog.logi("Application state changed. Moving from " +
				this.stateNameMap.get(this.currentState) +" to " +
				this.stateNameMap.get(nextState) + ".");
		this.currentState = nextState;
	}
	
	/**
	 * Used to notify all listeners that the application state changed. 
	 * 
	 * @param nextState The next state that will be entered.
	 */
	private void fireApplicationStateChangedEvent(int nextState) {
		Iterator<ApplicationStateListener> it = this.applicationStateListener.iterator();
		
		while(it.hasNext()) {
			it.next().onApplicationStateChange(nextState);
		}
	}
    
}
