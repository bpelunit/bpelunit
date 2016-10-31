package net.bpelunit.utils.bpelstats.extensions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ExtensionHandler extends DefaultHandler {

	private Set<String> extensionsMustUnderstand = new HashSet<String>();
	private Set<String> extensionsNotMustUnderstand = new HashSet<String>();
	private Set<String> extensionActivities = new HashSet<String>();
	private String bpelNamespace;
	private boolean isAtStartOfExtensionActivity = false;
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		if(bpelNamespace == null) {
			bpelNamespace = uri;
			return;
		}
		
		if(bpelNamespace.equals(uri) && "extension".equals(localName)) {
			String extensionNamespace = attributes.getValue("namespace");
			if(attributes.getValue("mustUnderstand").equals("yes")) {
				extensionsMustUnderstand.add(extensionNamespace);
			} else {
				extensionsNotMustUnderstand.add(extensionNamespace);
			}
		}
		
		if(isAtStartOfExtensionActivity) {
			isAtStartOfExtensionActivity = false;
			extensionActivities.add("{" + uri + "}" + localName);
		}
		
		if(bpelNamespace.equals(uri) && "extensionActivity".equals(localName)) {
			isAtStartOfExtensionActivity = true;
		}
		
	}
	
	public Set<String> getExtensionsMustUnderstand() {
		return Collections.unmodifiableSet(extensionsMustUnderstand);
	}
	
	public Set<String> getExtensionsNotMustUnderstand() {
		return Collections.unmodifiableSet(extensionsNotMustUnderstand);
	}

	public Set<String> getExtensionActivities() {
		return Collections.unmodifiableSet(extensionActivities);
	}
}
