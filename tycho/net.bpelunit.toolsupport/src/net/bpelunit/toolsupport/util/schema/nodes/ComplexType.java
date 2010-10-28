package net.bpelunit.toolsupport.util.schema.nodes;

import java.util.List;

public interface ComplexType extends Type {

	public abstract void addElement(Element element);

	public abstract List<Element> getElements();

	public abstract void addAttribute(Attribute attribute);

	public abstract List<Attribute> getAttributes();

}