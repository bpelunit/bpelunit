package net.bpelunit.framework.control.deploy.activebpel;

/**
 * This exception implies that there have been an error building the BPR file
 * @author Antonio García Domínguez
 */
public class BPELPackagingException extends Exception {

	private static final long serialVersionUID = 7169103227031634985L;

        /**
         * Constructor from a String
         * @param arg0  The message string
         */
	public BPELPackagingException(String arg0) {
		super(arg0);
	}

        /**
         * Constructor from a throwable object
         * @param arg0  The object to be nested
         */
	public BPELPackagingException(Throwable arg0) {
		super(arg0);
	}

        /**
         * Constructor from a string and a throwable object
         * @param arg0  The message string
         * @param arg1  The object to be nested
         */
	public BPELPackagingException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
