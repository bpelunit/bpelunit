package net.bpelunit.utils.bpelstats.hertis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

public class HandlerMultiplexer extends DefaultHandler {
	private List<DefaultHandler> handlers = new ArrayList<DefaultHandler>();

	public HandlerMultiplexer(DefaultHandler... handlersToMultiplex) {
		for(DefaultHandler dh : handlersToMultiplex) {
			handlers.add(dh);
		}
	}
	
	public HandlerMultiplexer(List<DefaultHandler> handlersToMultiplex) {
		handlers.addAll(handlersToMultiplex);
	}
	
	public InputSource resolveEntity(String publicId, String systemId)
			throws IOException, SAXException {
		for(DefaultHandler dh : handlers) {
			try {
				InputSource s = dh.resolveEntity(publicId, systemId);
				if(s != null) {
					return s;
				}
			} catch(Exception e) {
				// ignore
			}
		}
		
		return super.resolveEntity(publicId, systemId);
	}

	@Override
	public void notationDecl(String name, String publicId, String systemId)
			throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.notationDecl(name, publicId, systemId);
		}
	}

	@Override
	public void unparsedEntityDecl(String name, String publicId,
			String systemId, String notationName) throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.unparsedEntityDecl(name, publicId, systemId, notationName);
		}
	}

	@Override
	public void setDocumentLocator(Locator locator) {
		for(DefaultHandler dh : handlers) {
			dh.setDocumentLocator(locator);
		}
	}

	@Override
	public void startDocument() throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.startDocument();
		}
	}

	@Override
	public void endDocument() throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.endDocument();
		}
	}

	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.startPrefixMapping(prefix, uri);
		}
	}

	@Override
	public void endPrefixMapping(String prefix) throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.endPrefixMapping(prefix);
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.startElement(uri, localName, qName, attributes);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		for(DefaultHandler dh : handlers) { 
			dh.endElement(uri, localName, qName);
		}
	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.characters(ch, start, length);
		}
	}

	@Override
	public void ignorableWhitespace(char[] ch, int start, int length)
			throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.ignorableWhitespace(ch, start, length);
		}
	}

	@Override
	public void processingInstruction(String target, String data)
			throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.processingInstruction(target, data);
		}
	}

	@Override
	public void skippedEntity(String name) throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.skippedEntity(name);
		}
	}

	@Override
	public void warning(SAXParseException e) throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.warning(e);
		}
	}

	@Override
	public void error(SAXParseException e) throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.error(e);
		}
	}

	@Override
	public void fatalError(SAXParseException e) throws SAXException {
		for(DefaultHandler dh : handlers) {
			dh.fatalError(e);
		}
	}
	
	
}
