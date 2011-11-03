package com.app.augmentedbizz.services.parser.json;

import com.app.augmentedbizz.services.entity.ServiceTransferEntity;
import com.app.augmentedbizz.services.parser.AbstractServiceResponseParser;
import com.app.augmentedbizz.services.parser.ServiceParserException;
import com.google.gson.Gson;

/**
 * JSON parser for service responses
 * 
 * @author Vladi
 *
 */
public class JSONServiceResponseParser extends AbstractServiceResponseParser
{
	protected Gson gson = new Gson();
	
	@Override
	public ServiceTransferEntity parseToServiceEntityFromData(byte[] data, Class<? extends ServiceTransferEntity> stClass) throws ServiceParserException
	{
		try
		{
			return gson.fromJson(new String(data, "UTF-8"), stClass);
		}
		catch(Exception e)
		{
			throw(new ServiceParserException(e.getMessage()));
		}
	}

}
