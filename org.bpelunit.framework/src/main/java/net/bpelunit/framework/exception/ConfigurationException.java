/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.exception;

/**
 * A configuration exception indicates a serious problem with the configuration of BPELUnit itself,
 * or a class loading problem.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ConfigurationException extends BPELUnitException {

	private static final long serialVersionUID= -5971584431646197004L;

	public ConfigurationException(String message) {
		super(message);
	}

	public ConfigurationException(String message, Throwable e) {
		super(message, e);
	}

}
