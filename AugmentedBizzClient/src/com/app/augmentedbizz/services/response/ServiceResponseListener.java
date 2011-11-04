package com.app.augmentedbizz.services.response;

import com.app.augmentedbizz.services.entity.ServiceTransferEntity;
import com.app.augmentedbizz.services.handler.ServiceHandlingException;
import com.app.augmentedbizz.services.service.BaseHttpService;

/**
 * A basic service response listener for service requests
 * 
 * @author Vladi
 *
 */
public interface ServiceResponseListener
{
	/**
	 * Gets called when a service response was received and parsed successfully into an entity
	 * 
	 * @param stEntity The created transfer entity by a handler which can be processed
	 * @param calledService A reference to the called service
	 */
	public void onServiceResponse(ServiceTransferEntity stEntity, BaseHttpService calledService);
	
	/**
	 * Gets called when a service processing failed because of several reasons 
	 * (wrong url or parameters, missing connectivity, parser error and others)
	 * 
	 * @param exception The exception which was thrown while service processing
	 * @param calledService A reference to the called service
	 */
	public void onServiceFailed(ServiceHandlingException exception, BaseHttpService calledService);
}