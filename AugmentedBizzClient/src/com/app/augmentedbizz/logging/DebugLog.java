package com.app.augmentedbizz.logging;

import android.util.Log;

public class DebugLog {
	
	private static final String LOG_PREFIX = "Java: ";
	private static final String LOG_TAG = "ARBIZZ";
	
	public static void logv(String message) {
		Log.v(DebugLog.LOG_TAG, LOG_PREFIX + message);
	}
	
	public static void loge(String message) {
		Log.e(DebugLog.LOG_TAG, LOG_PREFIX + message);
	}
	
	public static void loge(String message, Throwable reason) {
		Log.e(DebugLog.LOG_TAG, LOG_PREFIX + message, reason);
	}
	
	public static void logi(String message) {
		Log.i(DebugLog.LOG_TAG, LOG_PREFIX + message);
	}
	
	public static void logd(String message) {
		Log.d(DebugLog.LOG_TAG, LOG_PREFIX + message);
	}
	
	public static void logw(String message) {
		Log.w(DebugLog.LOG_TAG, LOG_PREFIX + message);
	}

}
