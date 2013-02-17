package net.bpelunit.toolsupport.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlCursor.TokenType;
import net.bpelunit.framework.xml.suite.XMLTestSuite;

public class NamespaceEditor {

	private XMLTestSuite baseSuite;

	public NamespaceEditor(XMLTestSuite suite) {
		this.baseSuite = suite;
	}

	public String getPrefix(String url) {
		for (Entry<String, String> entry : this.getNamespacesFromSuite().entrySet()) {
			if (entry.getValue() != null && entry.getValue().equals(url)) {
				return entry.getKey();
			}
		}
		return this.addNamespaceToSuite(url);
	}

	public void getNamespacesFromSuite(Map<String, String> namespaces) {
		this.baseSuite.newCursor().getAllNamespaces(namespaces);
	}

	public Map<String, String> getNamespacesFromSuite() {
		Map<String, String> namespaces = new HashMap<String, String>();
		this.baseSuite.newCursor().getAllNamespaces(namespaces);
		return namespaces;
	}

	public boolean addNamespaceToSuite(String prefix, String url) {
		if (!"".equals(prefix) && !"".equals(url)) {
			XmlCursor cursor = this.baseSuite.newCursor();
			cursor.toNextToken();
			cursor.insertNamespace(prefix, url);
			cursor.dispose();
			return true;
		} else {
			return false;
		}
	}

	public String addNamespaceToSuite(String url) {
		if (url != null) {
			String prefix = this.generatePrefix(url);
			this.addNamespaceToSuite(prefix, url);
			return prefix;
		}
		return null;
	}

	public boolean editNamespaceInSuite(NamespaceDeclaration current, String prefix, String url) {
		XmlCursor cursor = this.getPlacedCursor(current);
		try {
			if (cursor != null) {
				// These two operations effectively change the prefix for the
				// complete document
				// Upon re-serialization, the new prefix for the given URL is
				// used
				// in all declarations
				cursor.removeXml();
				// cursor.currentTokenType();
				this.addNamespaceToSuite(prefix, url);
				// cursor.insertNamespace(prefix, url);
				return true;
			}
			return false;
		} finally {
			cursor.dispose();
		}
	}

	public boolean removeNamespaceFromSuite(NamespaceDeclaration prop) {
		XmlCursor cursor = this.getPlacedCursor(prop);
		try {
			if (cursor != null) {
				cursor.removeXml();
				return true;
			}
			return false;
		} finally {
			cursor.dispose();
		}
	}

	private String generatePrefix(String url) {
		int protocolPosition = url.indexOf("://www.");
		if (protocolPosition == -1) {
			protocolPosition = url.indexOf("://");
			if (protocolPosition == -1) {
				protocolPosition = 0;
			} else {
				protocolPosition += 3;
			}
		} else {
			protocolPosition += 7;
		}

		int endPosition = Math.min(protocolPosition + 3, url.length());
		String prefix = url.substring(protocolPosition, endPosition);
		Map<String, String> namespaces = this.getNamespacesFromSuite();
		if (namespaces.containsKey(prefix)) {
			int suffix = 0;
			while (namespaces.containsKey(prefix + suffix)) {
				suffix++;
			}
			prefix += suffix;
		}
		return prefix;
	}

	/**
	 * Caller must dispose() the delivered cursor
	 * 
	 * @param current
	 * @return
	 */
	private XmlCursor getPlacedCursor(NamespaceDeclaration current) {
		XmlCursor cursor = this.baseSuite.newCursor();

		while (true) {
			// navigate to the next namespace
			TokenType type = cursor.toNextToken();

			// Skip attributes
			while (type.equals(TokenType.ATTR)) {
				type = cursor.toNextToken();
			}

			// Current token is no attribute and no namespace -> end of suite
			// element
			if (!type.equals(TokenType.NAMESPACE)) {
				break;
			}

			// Must be namespace element
			String declarationPrefix = cursor.getName().getLocalPart();
			if (current.getPrefix().equals(declarationPrefix)) {
				return cursor;
			}
		}

		return null;
	}

}
