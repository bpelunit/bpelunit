/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.client.eclipse.dialog.validate;

import org.bpelunit.framework.client.eclipse.dialog.DialogFieldValidator;

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
