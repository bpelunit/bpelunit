/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.soap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;


/**
 * Simple hashmap-backed implementation NamespaceContext.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class NamespaceContextImpl implements NamespaceContext {

	/**
	 * Map. Note that the KEY is **NOT** the prefix, but the namespace URI, as the URI is unique and
	 * may have more than one registered prefix.
	 */
	private Map<String, Collection<String>> fNamespaceMap;


	public NamespaceContextImpl() {
		fNamespaceMap= new HashMap<String, Collection<String>>();
	}

	public void setNamespace(String prefix, String namespaceURI) {
		Collection<String> prefixes= fNamespaceMap.get(namespaceURI);
		if (prefixes == null) {
			prefixes= new ArrayList<String>();
			fNamespaceMap.put(namespaceURI, prefixes);
		}
		prefixes.add(prefix);
	}


	public String getNamespaceURI(String prefix) {
		for (String namespaceURI : fNamespaceMap.keySet()) {
			Collection<String> prefixes= fNamespaceMap.get(namespaceURI);
			for (String string : prefixes) {
				if (string.equals(prefix)) {
					return namespaceURI;
				}
			}
		}
		return null;
	}

	public String getPrefix(String namespaceURI) {
		Collection<String> name= fNamespaceMap.get(namespaceURI);
		if (name != null && name.size() > 0) {
			return name.iterator().next();
		} else {
			return null;
		}
	}

	public Iterator<String> getPrefixes(String namespaceURI) {
		Collection<String> name= fNamespaceMap.get(namespaceURI);
		if (name != null) {
			return name.iterator();
		} else {
			return null;
		}
	}
}
