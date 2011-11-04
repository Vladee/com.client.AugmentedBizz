package com.app.augmentedbizz.services.service.repository;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.services.entity.ServiceTransferEntity;
import com.app.augmentedbizz.services.entity.transfer.IndicatorServiceEntity;
import com.app.augmentedbizz.services.entity.transfer.ModelServiceEntity;
import com.app.augmentedbizz.services.service.BaseHttpService;

/**
 * HTTP GET service for indicator information
 * 
 * @author Vladi
 *
 */
public class IndicatorHttpService extends BaseHttpService
{
	private Context context;
	
	public IndicatorHttpService(Context context, Long targetId)
	{
		this.context = context;
		
		queryKeyValuePairs.add(new BasicNameValuePair("target", Long.toString(targetId)));
	}
	
	@Override
	public String getServiceUrlExtension()
	{
		return context.getString(R.string.indicatorServiceExtension);
	}

	@Override
	public Class<? extends ServiceTransferEntity> getServiceTransferEntityClass()
	{
		return IndicatorServiceEntity.class;
	}

}
