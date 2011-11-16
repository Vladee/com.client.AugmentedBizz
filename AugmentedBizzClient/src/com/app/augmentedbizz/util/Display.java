package com.app.augmentedbizz.util;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Utilities for display information retrieval.
 * 
 * @author Vladi
 *
 */
public class Display
{
	public static int getScreenWidth(Activity activity)
	{
		DisplayMetrics metrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
	}
	
	public static int getScreenHeight(Activity activity)
	{
		DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
	}
}
