package com.app.augmentedbizz.services.handler;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;

import com.app.augmentedbizz.services.parser.AbstractServiceResponseParser;
import com.app.augmentedbizz.services.parser.ServiceParserException;
import com.app.augmentedbizz.services.response.ServiceResponseListener;
import com.app.augmentedbizz.services.service.BaseHttpService;

/**
 * A HTTP service handler for predefined HTTP services with servie transfer entities as responses
 * 
 * @author Vladi
 *
 */
public class HttpServiceHandler
{
	private String baseUrl;
	private HttpClient httpClient;
	private AbstractServiceResponseParser serviceParser;
	
	public HttpServiceHandler(String baseUrl, AbstractServiceResponseParser serviceParser)
	{
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
	public void processRequestAsynch(BaseHttpService service, ServiceResponseListener responseListener)
	{
        HttpGet getMethod = new HttpGet(baseUrl + (!baseUrl.endsWith("/") ? "/" : "") + service.generateUrlExtension());

        new HttpGetTask(httpClient, service, serviceParser, responseListener).execute(getMethod);
    }

	
}
