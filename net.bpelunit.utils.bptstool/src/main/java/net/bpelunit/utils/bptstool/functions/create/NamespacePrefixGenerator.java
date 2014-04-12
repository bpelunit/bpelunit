package net.bpelunit.utils.bptstool.functions.create;

import java.util.HashMap;
import java.util.Map;

public class NamespacePrefixGenerator {

	private int counter = 1;
	private static final String NSPREFIX = "ns";
	
	private Map<String, String> namespaceMap = new HashMap<String, String>();
	
	public synchronized String getNamespacePrefix(String namespace) {
		if(!namespaceMap.containsKey(namespace)) {
			namespaceMap.put(namespace, NSPREFIX + (counter++));
		} 
		return namespaceMap.get(namespace);
	}
	
}
