package net.bpelunit.utils.bpelstats.languagestats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XSLTSubLanguageParser extends DefaultHandler {

	private static final List<String> XSLT_COMPLEXITY_STRUCTURES = Collections.unmodifiableList(Arrays.asList("template", "if", "switch", "for-each")); 
	
	private final String XSLT_NAMESPACE = "http://www.w3.org/1999/XSL/Transform";

	private List<Import> imports = new ArrayList<Import>();
	private int complexity = 0;

	private File baseDirectory;
	
	public XSLTSubLanguageParser(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	@Override
	public void startDocument() throws SAXException {
		imports.clear();
		complexity = 0;
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {

		if (!XSLT_NAMESPACE.equals(uri)) return;
		
		if (localName.equals("import")) {
			Import i = new Import();
			i.importType = "XSLT";
			try {
				i.location = new File(baseDirectory, attributes.getValue("href")).getCanonicalFile();
			} catch (IOException e) {
				throw new SAXException(e);
			}
			imports.add(i);
		}
		
		if (XSLT_COMPLEXITY_STRUCTURES.contains(localName)) {
			complexity++;
		}
	}

	public List<? extends Import> getImports() {
		return Collections.unmodifiableList(imports);
	}
	
	public int getComplexity() {
		return complexity;
	}
}
