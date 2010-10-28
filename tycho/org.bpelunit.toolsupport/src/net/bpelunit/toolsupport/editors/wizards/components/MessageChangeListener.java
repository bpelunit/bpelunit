package org.bpelunit.toolsupport.editors.wizards.components;

import org.bpelunit.toolsupport.util.schema.nodes.Element;

public interface MessageChangeListener {
	public abstract void messageChanged(Element message);
}
