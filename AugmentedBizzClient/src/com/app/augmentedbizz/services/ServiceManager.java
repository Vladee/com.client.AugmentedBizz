package com.app.augmentedbizz.services;

import android.content.Context;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.application.IApplicationFacade;
import com.app.augmentedbizz.services.handler.HttpServiceHandler;
import com.app.augmentedbizz.services.parser.json.JSONServiceResponseParser;

/**
 * The service manager enables the communication with a server, data exchange and also data parsing into entities
 * 
 * @author Vladi
 *
 */
public class ServiceManager
{
	private HttpServiceHandler serviceHandler;
	
	public ServiceManager(Context context)
	{
		serviceHandler = new HttpServiceHandler(context.getString(R.string.baseUrl), new JSONServiceResponseParser());
	}
	
	//TODO access to services and data manager interaction
}
