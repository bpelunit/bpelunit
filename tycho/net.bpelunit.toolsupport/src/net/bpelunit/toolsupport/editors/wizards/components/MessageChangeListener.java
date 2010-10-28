package net.bpelunit.toolsupport.editors.wizards.components;

import net.bpelunit.toolsupport.util.schema.nodes.Element;

public interface MessageChangeListener {
	public abstract void messageChanged(Element message);
}
