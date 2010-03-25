package org.bpelunit.framework.datasource;


@SuppressWarnings("serial")
public class InvalidDataSourceException extends Exception {

	public InvalidDataSourceException(String message, Exception cause) {
		super(message, cause);
	}

	public InvalidDataSourceException(String message) {
		super(message);
	}

}
