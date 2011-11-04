package com.app.augmentedbizz.services.service.repository;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.services.entity.ServiceTransferEntity;
import com.app.augmentedbizz.services.entity.transfer.ModelServiceEntity;
import com.app.augmentedbizz.services.service.BaseHttpService;

/**
 * HTTP GET service for model information
 * 
 * @author Vladi
 *
 */
public class ModelHttpService extends BaseHttpService
{
	private Context context;
	
	public ModelHttpService(Context context, Long modelId)
	{
		this.context = context;
		
		queryKeyValuePairs.add(new BasicNameValuePair("model", Long.toString(modelId)));
	}
	
	@Override
	public String getServiceUrlExtension()
	{
		return context.getString(R.string.modelServiceUrlExtension);
	}

	@Override
	public Class<? extends ServiceTransferEntity> getServiceTransferEntityClass()
	{
		return ModelServiceEntity.class;
	}

}
