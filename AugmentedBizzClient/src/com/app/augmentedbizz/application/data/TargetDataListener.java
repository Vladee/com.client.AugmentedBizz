package com.app.augmentedbizz.application.data;


public interface TargetDataListener {
	
	void onTargetData(Target target);
	void onTargetError(Exception e);
}
