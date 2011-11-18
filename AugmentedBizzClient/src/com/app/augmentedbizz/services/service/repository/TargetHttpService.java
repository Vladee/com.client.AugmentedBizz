package com.app.augmentedbizz.services.service.repository;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.services.entity.ServiceTransferEntity;
import com.app.augmentedbizz.services.entity.transfer.TargetServiceEntity;
import com.app.augmentedbizz.services.service.BaseHttpService;

/**
 * HTTP GET service for target information
 * 
 * @author Vladi
 *
 */
public class TargetHttpService extends BaseHttpService {
	private Context context;
	
	public TargetHttpService(Context context, Long targetId) {
		this.context = context;
		
		queryKeyValuePairs.add(new BasicNameValuePair("target", Long.toString(targetId)));
	}
	
	@Override
	public String getServiceUrlExtension() {
		return context.getString(R.string.targetServiceUrlExtension);
	}

	@Override
	public Class<? extends ServiceTransferEntity> getServiceTransferEntityClass() {
		return TargetServiceEntity.class;
	}

}
