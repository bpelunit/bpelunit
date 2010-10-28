/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.formwidgets;

public class EntryAdapter implements IEntryListener {
	private ContextPart contextPart;

	public EntryAdapter(ContextPart contextPart) {
		this.contextPart= contextPart;
	}

	public void textDirty(TextEntry entry) {
		contextPart.fireSaveNeeded();
	}

	public void textValueChanged(TextEntry entry) {
	}

}
