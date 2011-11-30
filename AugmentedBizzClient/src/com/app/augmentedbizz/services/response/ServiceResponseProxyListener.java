package com.app.augmentedbizz.services.response;

import com.app.augmentedbizz.services.entity.ServiceTransferEntity;
import com.app.augmentedbizz.services.handler.HttpGetTask;
import com.app.augmentedbizz.services.handler.HttpServiceHandler;
import com.app.augmentedbizz.services.handler.ServiceHandlingException;
import com.app.augmentedbizz.services.service.BaseHttpService;

/**
 * A proxy class for service response listeners.
 * 
 * @author Vladi
 *
 */
public class ServiceResponseProxyListener implements ServiceResponseListener {
	private ServiceResponseListener listener;
	private HttpServiceHandler handler;
	private HttpGetTask task;
	
	public ServiceResponseProxyListener(HttpServiceHandler handler, 
										ServiceResponseListener listener,
										HttpGetTask task) {
		this.handler = handler;
		this.listener = listener;
		this.task = task;
	}
	
	@Override
	public void onServiceResponse(ServiceTransferEntity stEntity,
			BaseHttpService calledService) {
		handler.removeTask(task);
		listener.onServiceResponse(stEntity, calledService);
	}

	@Override
	public void onServiceFailed(ServiceHandlingException exception,
			BaseHttpService calledService) {
		handler.removeTask(task);
		listener.onServiceFailed(exception, calledService);
	}

}
