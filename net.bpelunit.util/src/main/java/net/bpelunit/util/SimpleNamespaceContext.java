package net.bpelunit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.NamespaceContext;

public class SimpleNamespaceContext implements NamespaceContext {

	private Map<String, String> prefix2Namespaces = new HashMap<String, String>();
	private Map<String, String> namespace2Prefix = new HashMap<String, String>();
	
	public void addNamespace(String prefix, String namespaceURI) {
		prefix2Namespaces.put(prefix, namespaceURI);
		namespace2Prefix.put(namespaceURI, prefix);
	}
	
	@Override
	public String getNamespaceURI(String prefix) {
		return prefix2Namespaces.get(prefix);
	}

	@Override
	public String getPrefix(String namespaceURI) {
		return namespace2Prefix.get(namespaceURI);
	}

	@Override
	public Iterator<String> getPrefixes(String namespaceURI) {
		List<String> result = new ArrayList<String>();
		
		for(Entry<String, String> e : prefix2Namespaces.entrySet()) {
			if(namespaceURI.equals(e.getValue())) {
				result.add(e.getKey());
			}
		}
		
		return result.iterator();
	}

}
