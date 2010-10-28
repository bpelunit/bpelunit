/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors;

/**
 * 
 * Interface to be implemented by UI parts who wish to be informed about model changes.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public interface IModelChangedListener {

	/**
	 * Called when there is a change in the model this listener is registered with.
	 */
	public void modelChanged();
}
