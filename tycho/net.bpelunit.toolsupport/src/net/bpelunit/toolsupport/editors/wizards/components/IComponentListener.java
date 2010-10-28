/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.wizards.components;

import net.bpelunit.toolsupport.editors.wizards.fields.DialogField;

/**
 * Interface to be implemented by classes who wish to be informed about changes in a component.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public interface IComponentListener {

	/**
	 * Informs the listener that the value of the given field has changed.
	 * 
	 * @param field
	 */
	public void valueChanged(DialogField field);

}
