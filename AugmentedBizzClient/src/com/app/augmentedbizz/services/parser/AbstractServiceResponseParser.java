package com.app.augmentedbizz.services.parser;

import com.app.augmentedbizz.services.entity.ServiceTransferEntity;

/**
 * An abstract service parser definition based on service transfer entities
 * 
 * @author Vladi
 *
 */
public abstract class AbstractServiceResponseParser {
	/**
	 * Parses a byte array input to an service transfer entity
	 * 
	 * @param data Bytes of input from the server response
	 * @param stClass 
	 * @return
	 */
	public abstract ServiceTransferEntity parseToServiceEntityFromData(byte[] data, Class<? extends ServiceTransferEntity> stClass) throws ServiceParserException;
	
}
