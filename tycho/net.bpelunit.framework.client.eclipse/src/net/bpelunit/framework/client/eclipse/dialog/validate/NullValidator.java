/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.dialog.validate;

import net.bpelunit.framework.client.eclipse.dialog.DialogFieldValidator;

/**
 * A validator which doesn't check anything.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class NullValidator extends DialogFieldValidator {

	@Override
	public String validate(String value) {
		return null;
	}

}
