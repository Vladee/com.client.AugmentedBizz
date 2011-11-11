package com.app.augmentedbizz.application.data;

import java.util.List;

import com.app.augmentedbizz.services.entity.transfer.IndicatorServiceEntity.TargetIndicator;

public interface IndicatorDataListener {
	
	void onIndicatorData(List<TargetIndicator> targetIndicators);
	void onIndicatorError(Exception e);
}
