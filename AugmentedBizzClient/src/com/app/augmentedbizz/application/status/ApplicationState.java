package com.app.augmentedbizz.application.status;

public enum ApplicationState {
	/** Volatile initial state indicating that nothing happened yet. */
	UNINITIATED,
    /** The application is starting, splash screen is being shown,
     * and the Java/C++ components are being initialized. */
	INITIALIZING,
    /** Initialization finished, camera active and tracking. */
	TRACKING,
    /** ImageTarget captured. */
	CAPTURED,
    /** The QRCode has been identified and read. */
	SCANNED,
	/** No matching model was found in the cache, all data needs to be
     * retrieved from the server and is being loaded now. */
	LOADING,
    /** The corresponding model was found in the cache and will be displayed.
     * Simultaneously the server is requested for updates of the model. */
	SHOWING_CACHE,
    /** Data synchronization finished and the defect indicator information is
     * being loaded. */
	LOADING_INDICATORS,
	/** All data is available now, the application is displaying the model
     * with its indicators. */
	SHOWING,
    /** The application is about to be closed and thus freeing resources. */
	DEINITIALIZING,
    /** Volatile state indicating that memory has been freed and the
     * app is closing now. */
	EXITING;
	
	public String toString() {
		// "Pretty Printing"
		String res = this.name().replace('_', ' ');
		return res.substring(0, 1).toUpperCase() + res.substring(1).toLowerCase();
	}
	
	public int getIndex() {
		for(int i = 0; i < ApplicationState.values().length; i++) {
			if(ApplicationState.values()[i].equals(this)) {
				return i;
			}
		}
		return -1;
	}
}
