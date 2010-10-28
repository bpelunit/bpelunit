/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.exception;


/**
 * An error while reading the test specification, i.e. the test suite document.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SpecificationException extends BPELUnitException {

	private static final long serialVersionUID= 2804089365346290814L;

	public SpecificationException(String message, Throwable e) {
		super(message, e);
	}

	public SpecificationException(String message) {
		super(message);
	}

}
