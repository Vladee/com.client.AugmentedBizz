package com.app.augmentedbizz.services;

import com.app.augmentedbizz.R;
import com.app.augmentedbizz.application.ApplicationFacade;
import com.app.augmentedbizz.services.handler.HttpServiceHandler;
import com.app.augmentedbizz.services.parser.json.JSONServiceResponseParser;
import com.app.augmentedbizz.services.response.ServiceResponseListener;
import com.app.augmentedbizz.services.service.repository.IndicatorHttpService;
import com.app.augmentedbizz.services.service.repository.ModelHttpService;
import com.app.augmentedbizz.services.service.repository.TargetHttpService;

/**
 * The service manager serves as the central point for communication with a server, data exchange and 
 * also data parsing into transfer entities. It opens up interfaces for the application data management.
 * 
 * @author Vladi
 *
 */
public class ServiceManager {
	private HttpServiceHandler serviceHandler;
	private ApplicationFacade facade;
	
	public ServiceManager(ApplicationFacade facade) {
		this.facade = facade;
		serviceHandler = new HttpServiceHandler(facade.getContext().getString(R.string.baseUrl), new JSONServiceResponseParser());
	}
	
	/**
	 * Sends a target service request to the server and returns the result information by invocation of the response listener
	 * 
	 * @param targetId The id of the captured target
	 * @param responseListener A response listener which gets invoked when the result is available
	 */
	public void callTargetInformationService(int targetId, ServiceResponseListener responseListener) {
		serviceHandler.processRequestAsynch(new TargetHttpService(facade.getContext(), new Long(targetId)), responseListener);
	}
	
	/**
	 * Sends a model service request to the server and returns the result information by invocation of the response listener
	 * 
	 * @param modelId The id of the model
	 * @param responseListener A response listener which gets invoked when the result is available
	 */
	public void callModelInformationService(int modelId, ServiceResponseListener responseListener) {
		serviceHandler.processRequestAsynch(new ModelHttpService(facade.getContext(), new Long(modelId)), responseListener);
	}
	
	/**
	 * Sends a indicator service request to the server and returns the result information by invocation of the response listener
	 * 
	 * @param targetId The id of the context target
	 * @param responseListener A response listener which gets invoked when the result is available
	 */
	public void callIndicatorInformationService(int targetId, ServiceResponseListener responseListener) {
		serviceHandler.processRequestAsynch(new IndicatorHttpService(facade.getContext(), new Long(targetId)), responseListener);
	}
}
