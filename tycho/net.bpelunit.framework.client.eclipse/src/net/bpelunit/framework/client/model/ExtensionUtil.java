/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Utilities for storing the deployers configuration options in the plugin preference store
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ExtensionUtil {

	public static String serializeMap(Map<String, String> map) {
		if (map == null)
			return ""; //$NON-NLS-1$

		StringBuffer buffer= new StringBuffer();
		int i= 0;
		for (String key : map.keySet()) {
			String value= map.get(key);

			if (i > 0)
				buffer.append(",");

			buffer.append(key);
			buffer.append("=");
			buffer.append(value);

			i++;
		}
		return buffer.toString();
	}

	public static Map<String, String> deserializeMap(String string) {

		Map<String, String> map= new HashMap<String, String>();

		String[] elements= string.split(",");
		for (String string2 : elements) {
			String key= null;
			String value= null;
			String[] elements2= string2.split("=");
			if (elements2.length == 2) {
				key= elements2[0];
				value= elements2[1];
			}
			if ( (key != null) && (value != null))
				map.put(key, value);
		}
		return map;
	}
}
