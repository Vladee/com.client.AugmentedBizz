package com.app.augmentedbizz.services.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.app.augmentedbizz.logging.DebugLog;
import com.app.augmentedbizz.services.parser.AbstractServiceResponseParser;
import com.app.augmentedbizz.services.response.ServiceResponseListener;
import com.app.augmentedbizz.services.response.ServiceResponseProxyListener;
import com.app.augmentedbizz.services.service.BaseHttpService;

/**
 * A HTTP service handler for predefined HTTP services with servie transfer entities as responses
 * 
 * @author Vladi
 *
 */
public class HttpServiceHandler {
	private String baseUrl;
	private HttpClient httpClient;
	private AbstractServiceResponseParser serviceParser;
	private List<HttpGetTask> activeTasksList = new ArrayList<HttpGetTask>();
	
	public HttpServiceHandler(String baseUrl, AbstractServiceResponseParser serviceParser) {
		this.baseUrl = baseUrl;
		this.serviceParser = serviceParser;
		httpClient = HttpClientFactory.createHttpClient();
	}
	
	/**
	 * Processes the http request in an asynchronous way and returns the result via the response listener
	 * 
	 * @param service The service which should be called
	 * @param responseListener The listener which should be invoked when the result is available
	 * @throws ServiceHandlingException
	 */
	public void processRequestAsynch(BaseHttpService service, ServiceResponseListener responseListener) {
        HttpGet getMethod = new HttpGet(baseUrl + (!baseUrl.endsWith("/") ? "/" : "") + service.generateUrlExtension());
        
        HttpGetTask task = null;
        task = new HttpGetTask(httpClient, service, serviceParser, new ServiceResponseProxyListener(this, responseListener, task));
        activeTasksList.add(task);
        
        task.execute(getMethod);
    }

	/**
	 * Stops all currently active HTTP service calls.
	 */
	public void stopAll() {
		for(HttpGetTask task : activeTasksList) {
			task.cancel(true);
			DebugLog.logi("Service task forced to stop.");
		}
		activeTasksList.clear();
	}
	
	/**
	 * Removes a task from the active task list
	 * 
	 * @param task The task to be removed
	 */
	public void removeTask(HttpGetTask task) {
		activeTasksList.remove(task);
	}
}
