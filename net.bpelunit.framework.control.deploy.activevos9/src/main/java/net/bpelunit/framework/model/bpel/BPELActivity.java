package net.bpelunit.framework.model.bpel;

import org.w3c.dom.Element;

public abstract class BPELActivity {

	private static final String ATTRIBUTE_NAME = "name";
	private Element element;
	private BPELProcess process;

	BPELActivity(Element bpelElement, BPELProcess bpelProcess) {
		this.element = bpelElement;
		this.process = bpelProcess;
	}
	
	public String getName() {
		return element.getAttribute(ATTRIBUTE_NAME);
	}
	
	public void setName(String newName) {
		element.setAttribute(ATTRIBUTE_NAME, newName);
	}
	
	public BPELActivity getParent() {
		return process.getActivityForElement(element.getParentNode());
	}
	
	public String getActivityType() {
		return element.getLocalName();
	}
	
	/**
	 * Must be a setter because the process cannot put "this" in the
	 * super constructor call (not allowed: super(..., this) );
	 * 
	 * @param bpelProcess
	 */
	void setProcess(BPELProcess bpelProcess) {
		this.process = bpelProcess; 
	}
	
	Element getElement() {
		return element;
	}
}
