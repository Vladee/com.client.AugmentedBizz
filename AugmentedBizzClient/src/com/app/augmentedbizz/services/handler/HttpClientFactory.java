package com.app.augmentedbizz.services.handler;

import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import com.app.augmentedbizz.logging.DebugLog;

/**
 * A factory class that will build instances of HttpClient that are ready
 * to use to communicate with the server
 * 
 * @author Vladi
 */
public class HttpClientFactory 
{

    /**
     * Creates a new HttpClient that is ready to for use
     *  
     * @return instance an HttpClient
     */
    public static synchronized HttpClient createHttpClient() 
    {
    	//register necessary schemes
        SchemeRegistry schemeReg = new SchemeRegistry();
        schemeReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        
        //create a http client from a threadsafe connection manager
        ThreadSafeClientConnManager connMgr = new ThreadSafeClientConnManager(new BasicHttpParams(), schemeReg);
        DefaultHttpClient httpClient = new DefaultHttpClient(connMgr, new BasicHttpParams());
        httpClient.setHttpRequestRetryHandler(new RetryHandler());
        
        //setup proxy if available
        String proxyHostname = android.net.Proxy.getDefaultHost();
        int proxyPort = android.net.Proxy.getDefaultPort();
        if (proxyHostname != null && proxyPort != -1)
        {
        	HttpHost proxyHttpHost = new HttpHost(proxyHostname, proxyPort);
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxyHttpHost);
        }

        return httpClient;
    }

    /**
     * Shut down the HttpClient built by this factory.
     */
    public static synchronized void shutdownHttpClient(HttpClient httpClient) 
    {
        if (httpClient != null && httpClient.getConnectionManager() != null)
        {
            httpClient.getConnectionManager().shutdown();
        }
    }
        
    /**
     * This handler class is invoked in the event that an HTTP request results in
     * an IOException. These exceptions are produced in situations where it is
     * possible to attempt to recover.
     */
    private static class RetryHandler implements HttpRequestRetryHandler
    {

        @Override
		public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {

            DebugLog.logw("Http retry " + exception.toString() + " " + executionCount);
            
            //do not retry if over max retry count
            if(executionCount >= 5) 
            {
                return false;
            }
            //retry if the server dropped connection on us
            if(exception instanceof NoHttpResponseException) 
            {
                
                return true;
            }
            //limit retries to requests that have not yet been sent.
            Boolean requestSent = (Boolean) context.getAttribute(ExecutionContext.HTTP_REQ_SENT);
            if(!requestSent)
            {
                return true;
            }
            
            return false;
        }
    };
}
