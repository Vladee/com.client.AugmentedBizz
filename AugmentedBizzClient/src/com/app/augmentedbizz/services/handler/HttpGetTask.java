package com.app.augmentedbizz.services.handler;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.app.augmentedbizz.logging.DebugLog;
import com.app.augmentedbizz.services.entity.ServiceTransferEntity;
import com.app.augmentedbizz.services.parser.AbstractServiceResponseParser;
import com.app.augmentedbizz.services.response.ServiceResponseListener;
import com.app.augmentedbizz.services.service.BaseHttpService;
import com.app.augmentedbizz.util.TypeConversion;

import android.os.AsyncTask;

/**
 * Asynchronous task for HTTP GET requests and response processing
 * 
 * @author Vladi
 *
 */
public class HttpGetTask extends AsyncTask<HttpGet, Integer, Object> {
	private HttpClient httpClient;
	private BaseHttpService service;
	private AbstractServiceResponseParser serviceParser;
	private ServiceResponseListener responseListener;
	
	public HttpGetTask(HttpClient httpClient, BaseHttpService service, AbstractServiceResponseParser serviceParser, ServiceResponseListener responseListener) {
		this.httpClient = httpClient;
		this.service = service;
		this.serviceParser = serviceParser;
		this.responseListener = responseListener;
	}

	@Override
	protected Object doInBackground(HttpGet... get) {
		try {
            validateGetMethod(get[0]);
            
            HttpEntity entity = executeHttpGetForEntity(httpClient, get[0]);
            
            byte[] data = TypeConversion.toByteArrayFrom(entity.getContent());
            entity.consumeContent();
            
            return serviceParser.parseToServiceEntityFromData(data, service.getServiceTransferEntityClass());
        } 
		catch(Exception e)  {
            DebugLog.logw("Exception while service processing: " + e.getMessage());
            return e;
        }
	}
	
	@Override
	protected void onPostExecute(Object result) {
		if(responseListener == null) {
			return;
		}
		
		if(result instanceof ServiceTransferEntity) {
			responseListener.onServiceResponse((ServiceTransferEntity)result, service);
		}
		else if(result instanceof Exception) {
			String msg = ((Exception)(result)).getMessage();
			responseListener.onServiceFailed(new ServiceHandlingException(msg), service);
		}
	}
	
	/**
	 * Validates a HTTP GET method
	 * 
	 * @param get The instance of the HTTP GET which should be validated
	 * @throws Exception Thrown if the GET method is invalid
	 */
	private void validateGetMethod(HttpGet get) throws Exception {
		if(get == null || get.isAborted() || !get.getURI().isAbsolute())
        {
            throw(new Exception("HTTP GET is invalid."));
        }
	}

	/**
	 * Executes a HTTP GET method and returns the resulting HTTP entity
	 * 
	 * @param client The HTTP client whoch should execute the GET request
	 * @param get The instance of the GET request
	 * @return An instance of an HTTP entity containing the response information from the request
	 * @throws Exception
	 */
	private HttpEntity executeHttpGetForEntity(HttpClient client, HttpGet get) throws Exception {
        HttpResponse httpResponse = client.execute(get);
        
        if(httpResponse == null) 
        {
        	throw(new Exception("HTTP response is null."));
        }
        
        HttpEntity entity = httpResponse.getEntity();
        if(entity == null)
        {
        	throw(new Exception("HTTP entity is null."));
        }
        
        return entity;
	}
}
