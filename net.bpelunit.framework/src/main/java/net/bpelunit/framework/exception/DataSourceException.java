package net.bpelunit.framework.exception;

/**
 * There was a problem while loading contents into a data source, customizing
 * its options or initializing a context from its contents.
 * 
 * @author Antonio García-Domínguez
 */
public class DataSourceException extends BPELUnitException {

	private static final long serialVersionUID = -2393693115120941956L;

	public DataSourceException(String message, Throwable e) {
		super(message, e);
	}

	public DataSourceException(String message) {
		super(message);
	}

	public DataSourceException(Throwable cause) {
		super(cause);
	}

}
