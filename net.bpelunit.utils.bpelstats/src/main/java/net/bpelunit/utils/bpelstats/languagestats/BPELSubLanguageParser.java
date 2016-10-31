package net.bpelunit.utils.bpelstats.languagestats;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import es.uca.webservices.xquery.parser.util.XQueryParsingException;

public class BPELSubLanguageParser extends DefaultHandler {

	private static final String BPEL_URN_XQUERY = "urn:active-endpoints:expression-language:xquery1.0";
	private static final String BPEL_URN_XPATH = "urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0";
	private File baseDirectory;
	
	public static class LanguageFragment {
		String language;
		String fragment;
		QName elementName;
	}
	
	private static final String[] XQUERY_IMPORT_TYPES = new String[] { "http://modules.active-endpoints.com/2009/07/xquery" };
	private static final String[] XSLT_IMPORT_TYPES = new String[] { "http://www.w3.org/1999/XSL/Transform" };
	
	private StringBuilder textContent = new StringBuilder();
	private String defaultQueryLanguage = BPEL_URN_XPATH;
	private String defaultExpressionLanguage = BPEL_URN_XPATH;
	
	private List<LanguageFragment> queryFragments = new ArrayList<BPELSubLanguageParser.LanguageFragment>();
	private List<LanguageFragment> expressionFragments = new ArrayList<BPELSubLanguageParser.LanguageFragment>();
	
	private List<Import> imports = new ArrayList<Import>();
	
	private LanguageFragment currentFragment = null;
	
	private int xquerySimpleExpressionLOCs = 0;
	private int xqueryComplexExpressionLOCs = 0;
	private int xquerySimpleQueryLOCs = 0;
	private int xqueryComplexQueryLOCs = 0;
	private int xpathQueryLOCs = 0;
	private int xpathExpressionLOCs = 0;
	private int xquerySimpleExpressionOccurrences = 0;
	private int xqueryComplexExpressionOccurrences = 0;
	private int xquerySimpleQueryOccurrences = 0;
	private int xqueryComplexQueryOccurrences = 0;
	private int xpathQueryOccurrences = 0;
	private int xpathExpressionOccurrences = 0;
	private int xqueryComplexityQuery = 0;
	private int xqueryComplexityExpression = 0;
	private String bpelNamespace;
	
	public BPELSubLanguageParser(File baseDirectory) {
		this.baseDirectory = baseDirectory;
	}
	
	@Override
	public void startDocument() throws SAXException {
		textContent = new StringBuilder();
		defaultQueryLanguage = BPEL_URN_XPATH;
		defaultExpressionLanguage = BPEL_URN_XPATH;
		imports.clear();
		queryFragments.clear();
		expressionFragments.clear();
		xquerySimpleExpressionLOCs = 0;
		xpathQueryLOCs = 0;
		xquerySimpleQueryLOCs = 0;
		xpathExpressionLOCs = 0;
		xqueryComplexityQuery = 0;
		xqueryComplexityExpression = 0;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(bpelNamespace==null) {
			bpelNamespace = uri;
		}
		
		if(!bpelNamespace.equals(uri)) {
			return; // skip non-BPEL elements
		}
		
		textContent.delete(0, textContent.length());
		if(localName.equals("process")) {
			String expressionLanguage = attributes.getValue("expressionLanguage");
			if(expressionLanguage != null) {
				defaultExpressionLanguage = expressionLanguage;
			}
			String queryLanguage = attributes.getValue("queryLanguage");
			if(queryLanguage != null) {
				defaultQueryLanguage = queryLanguage;
			}
		}
		
		if(localName.equals("import")) {
			String type = attributes.getValue("importType");
			if(Arrays.asList(XQUERY_IMPORT_TYPES).contains(type)) {
				Import i = new Import();
				i.importType = "XQUERY";
				try {
					i.location = new File(baseDirectory, attributes.getValue("location")).getCanonicalFile();
				} catch (IOException e) {
					throw new SAXException(e);
				}
				imports.add(i);
			}
			
			if(Arrays.asList(XSLT_IMPORT_TYPES).contains(type)) {
				Import i = new Import();
				i.importType = "XSLT";
				i.location = new File(baseDirectory, attributes.getValue("location"));
				imports.add(i);
			}
		}
		
		if(
				localName.equals("condition") || 
				localName.equals("for") || 
				localName.equals("until") || 
				localName.equals("repeatEvery") || 
				localName.equals("from") || 
				localName.equals("startCounterValue") || 
				localName.equals("finalCounterValue") || 
				localName.equals("completionCondition") || 
				localName.equals("branches") || 
				localName.equals("joinCondition") || 
				localName.equals("transitionCondition") || 
				localName.equals("to")
			) {
			String expressionLanguage = attributes.getValue("expressionLanguage");
			currentFragment = new LanguageFragment();
			currentFragment.language = expressionLanguage;
			currentFragment.elementName = new QName(uri, localName);
			if(expressionLanguage == null) {
				currentFragment.language = defaultExpressionLanguage;
			}
			expressionFragments.add(currentFragment);
		}
		
		if(localName.equals("literal")) {
			// assign/from/literal must not be counted
			expressionFragments.remove(currentFragment);
			queryFragments.remove(currentFragment);
			currentFragment = null;
		}
		
		if(localName.equals("query")) {
			String queryLanguage = attributes.getValue("queryLanguage");
			currentFragment = new LanguageFragment();
			currentFragment.language = queryLanguage;
			currentFragment.elementName = new QName(uri, localName);
			if(queryLanguage == null) {
				currentFragment.language = defaultQueryLanguage;
			}
			queryFragments.add(currentFragment);
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(currentFragment != null) {
			currentFragment.fragment = textContent.toString();
		
			if(currentFragment.fragment == null) {
				expressionFragments.remove(currentFragment);
				queryFragments.remove(currentFragment);
			}
		
			currentFragment = null;
		}
		textContent.delete(0, textContent.length());
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
		textContent.append(ch, start, length);
	}
	
	@Override
	public void endDocument() throws SAXException {
		removeEmptyFragments();
		resetLOCCounts();
		XQuerySubLanguageParser xqueryParser = new XQuerySubLanguageParser();
		
		for(LanguageFragment lf : queryFragments) {
			if(BPEL_URN_XPATH.equals(lf.language)) {
				xpathQueryOccurrences++;
				xpathQueryLOCs += calculateLOC(lf);
			} else if(BPEL_URN_XQUERY.equals(lf.language)) {
				try {
					xqueryParser.parse(lf.fragment);
				} catch (XQueryParsingException e) {
					throw new SAXException(e);
				}
				boolean isComplex = xqueryParser.isComplex();
				int loc = calculateLOC(lf);
				xqueryComplexityQuery +=  xqueryParser.getComplexity();
				if(isComplex) {
					xqueryComplexQueryOccurrences++;
					xqueryComplexQueryLOCs += loc; 
				} else {
					xquerySimpleQueryOccurrences++;
					xquerySimpleQueryLOCs += loc;
				}
			}
		}
		
		for(LanguageFragment lf : expressionFragments) {
			if(BPEL_URN_XPATH.equals(lf.language)) {
				xpathExpressionOccurrences++;
				xpathExpressionLOCs += calculateLOC(lf);
			} else if(BPEL_URN_XQUERY.equals(lf.language)) {
				try {
					xqueryParser.parse(lf.fragment);
				} catch (XQueryParsingException e) {
					throw new SAXException(e);
				}
				boolean isComplex = xqueryParser.isComplex();
				int loc = xqueryParser.getLoc();
				xqueryComplexityExpression  += xqueryParser.getComplexity();
				if(isComplex) {
					xqueryComplexExpressionOccurrences++;
					xqueryComplexExpressionLOCs += loc; 
				} else {
					xquerySimpleExpressionOccurrences++;
					xquerySimpleExpressionLOCs += loc;
				}
			}
		}
		
	}

	private void resetLOCCounts() {
		xqueryComplexExpressionLOCs = 0;
		xqueryComplexQueryLOCs = 0;
		xquerySimpleExpressionLOCs = 0;
		xquerySimpleQueryLOCs = 0;
		xpathExpressionLOCs = 0;
		xpathQueryLOCs = 0;
	}

	private void removeEmptyFragments() {
		List<LanguageFragment> fragmentsToDelete = new ArrayList<LanguageFragment>();
		for(LanguageFragment f : queryFragments) {
			if(f.fragment == null || "".equals(f.fragment.trim())) {
				fragmentsToDelete.add(f);
			}
		}
		for(LanguageFragment f : expressionFragments) {
			if(f.fragment == null || "".equals(f.fragment.trim())) {
				fragmentsToDelete.add(f);
			}
		}
		
		for(LanguageFragment f : fragmentsToDelete) {
			expressionFragments.remove(f);
			queryFragments.remove(f);
		}
	}
	
	private int calculateLOC(LanguageFragment f) {
		return LOCCalculator.calculateLOC(f.fragment);
	}
	

	public List<Import> getImports() {
		return Collections.unmodifiableList(imports);
	}

	public int getXPathQueryLOCs() {
		return xpathQueryLOCs;
	}
	public int getXPathExpressionLOCs() {
		return xpathExpressionLOCs;
	}

	public int getXQuerySimpleExpressionLOCs() {
		return xquerySimpleExpressionLOCs;
	}

	public int getXQueryComplexExpressionLOCs() {
		return xqueryComplexExpressionLOCs;
	}

	public int getXQuerySimpleQueryLOCs() {
		return xquerySimpleQueryLOCs;
	}

	public int getXQueryComplexQueryLOCs() {
		return xqueryComplexQueryLOCs;
	}
	
	public int getXQuerySimpleExpressionOccurrences() {
		return xquerySimpleExpressionOccurrences;
	}

	public int getXQueryComplexExpressionOccurrences() {
		return xqueryComplexExpressionOccurrences;
	}

	public int getXQuerySimpleQueryOccurrences() {
		return xquerySimpleQueryOccurrences;
	}

	public int getXQueryComplexQueryOccurrences() {
		return xqueryComplexQueryOccurrences;
	}

	public int getXPathQueryOccurrences() {
		return xpathQueryOccurrences;
	}

	public int getXPathExpressionOccurrences() {
		return xpathExpressionOccurrences;
	}
	
	public int getXQueryComplexityExpression() {
		return xqueryComplexityExpression;
	}
	
	public int getXQueryComplexityQuery() {
		return xqueryComplexityQuery;
	}
}
