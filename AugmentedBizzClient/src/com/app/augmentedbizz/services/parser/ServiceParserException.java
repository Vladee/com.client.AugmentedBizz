package com.app.augmentedbizz.services.parser;

/**
 * An exception type for errors while data parsing inside of services
 * @author Vladi
 *
 */
public class ServiceParserException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ServiceParserException(String msg) {
		super(msg);
	}
}
