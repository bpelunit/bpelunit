/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.dialog.validate;

import net.bpelunit.framework.client.eclipse.dialog.DialogFieldValidator;

/**
 * A validator which simply checks whether the given value is empty.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class NotEmptyValidator extends DialogFieldValidator {

	private String fName;

	public NotEmptyValidator(String name) {
		fName= name;
	}

	@Override
	public String validate(String value) {
		if (value.equals(""))
			return fName + " may not be empty.";
		else
			return null;
	}

}
