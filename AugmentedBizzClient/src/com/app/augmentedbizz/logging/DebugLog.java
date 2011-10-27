package com.app.augmentedbizz.logging;

import android.util.Log;

public class DebugLog {
	
	private static final String TAG = "ARBIZZ";
	
	public static void logv(String message) {
		Log.v(DebugLog.TAG, message);
	}
	
	public static void loge(String message) {
		Log.e(DebugLog.TAG, message);
	}
	
	public static void logi(String message) {
		Log.i(DebugLog.TAG, message);
	}
	
	public static void logd(String message) {
		Log.d(DebugLog.TAG, message);
	}
	
	public static void logw(String message) {
		Log.w(DebugLog.TAG, message);
	}

}
