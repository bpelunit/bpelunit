/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.dialog;

/**
 * 
 * A dialog field validator.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class DialogFieldValidator {

	/**
	 * Validates the given value. Must return an error message in case the value is wrong, or null
	 * in case everything is okay.
	 * 
	 * @param value
	 * @return
	 */
	public abstract String validate(String value);

}
