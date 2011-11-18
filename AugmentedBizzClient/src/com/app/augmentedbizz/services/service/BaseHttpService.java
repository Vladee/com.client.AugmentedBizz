package com.app.augmentedbizz.services.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.app.augmentedbizz.services.entity.ServiceTransferEntity;

/**
 * A basic HTTP service defintion class
 * 
 * @author Vladi
 *
 */
public abstract class BaseHttpService {
    protected List<BasicNameValuePair> queryKeyValuePairs = new ArrayList<BasicNameValuePair>();
    
    public BaseHttpService() 
    {
    }

    /**
     * @return Returns the service url extension after the base url
     */
    public abstract String getServiceUrlExtension();

    /**
     * @return Corresponding service transfer entity class which saves the information from the response
     */
    public abstract Class<? extends ServiceTransferEntity> getServiceTransferEntityClass();
    
    /**
     * @return The method's key-value-pairs in form of a list
     */
    public List<BasicNameValuePair> getServiceQueryList()
    {
        return queryKeyValuePairs;
    }
    
    /**
     * Generates the HTTP url extension for this service, e.g. 'servicename?name1=value&name2=value'
     * based upon the the name value pairs
     * 
     * @return The string representation of the url extension
     */
    public String generateUrlExtension()
    {
    	String query = "";
    	for(BasicNameValuePair pair : queryKeyValuePairs)
    	{
    		query += (query.length() > 0 ? "&" : "") + pair.getName() + "=" + pair.getValue();
    	}
    	return getServiceUrlExtension() + (query.length() > 0 ? "?" + query : "");
    }

}
