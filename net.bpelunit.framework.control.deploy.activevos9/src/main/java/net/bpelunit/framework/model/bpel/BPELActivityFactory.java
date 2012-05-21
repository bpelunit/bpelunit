package net.bpelunit.framework.model.bpel;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;

class BPELActivityFactory {

	private Map<String, Class<?extends BPELActivity>> activities = new HashMap<String, Class<? extends BPELActivity>>();
	
	public BPELActivityFactory() {
		activities.put("wait", Wait.class);
	}
	
	public BPELActivity createActivityFor(String localName, Element node, BPELProcess bpelProcess) {
		Class<?extends BPELActivity> clazz = activities.get(localName);
		
		try {
			Constructor<? extends BPELActivity> c = clazz.getConstructor(Element.class, BPELProcess.class);
			
			return c.newInstance(node, bpelProcess);
		} catch (Exception e) {
			return null;
		}
	}

}
