/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.exception;

/**
 * 
 * Thrown if the test suite is instructed to only run certain test cases, but the test cases are
 * unknown.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class TestCaseNotFoundException extends BPELUnitException {

	private static final long serialVersionUID= 4339387409248483260L;

	public TestCaseNotFoundException(String message) {
		super(message);
	}

}
