/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.exception;

/**
 * A problem while deploying or undeploying the BPEL PUT
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class DeploymentException extends BPELUnitException {

	private static final long serialVersionUID= -5810936363158504970L;

	public DeploymentException(String message) {
		super(message);
	}

	public DeploymentException(String string, Exception e) {
		super(string, e);
	}

}
