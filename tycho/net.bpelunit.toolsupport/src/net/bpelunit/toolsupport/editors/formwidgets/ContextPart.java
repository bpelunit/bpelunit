/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.formwidgets;

/**
 * 
 * Implemented by GUI elements wishing to be informed about new data.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public interface ContextPart {

	void fireSaveNeeded();

}
