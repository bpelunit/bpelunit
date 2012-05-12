package net.bpelunit.util;

import java.util.regex.Pattern;

import javax.xml.namespace.QName;

public final class QNameUtil {
	
	private QNameUtil() {
	}
	
	private static final Pattern QNAME_PATTERN = Pattern.compile("^\\{[^\\}]*\\}(.+)");

	/**
	 * Checks on whether the string conforms to a String representation of a QName.
	 * A QName is denoted as {namespace}local-name
	 * 
	 * @param name String to be checked
	 * @return true if the string represents a QName, false if not
	 */
	public static boolean isQName(String name) {
		return (name != null) && QNAME_PATTERN.matcher(name).matches();
	}

	/**
	 * Converts the given String to a QName
	 * 
	 * @param name String represention of a QName
	 * @return QName with namespace and local-name parts
	 * @see #isQName(String)
	 */
	public static QName parseQName(String name) {
		// TODO Switch to Pattern and groups 
		int indexOfClosingBrace = name.indexOf('}');
		
		String namespace =  name.substring(1, indexOfClosingBrace);
		String localName = name.substring(indexOfClosingBrace + 1);
		return new QName(namespace, localName);
	}
}
