package net.bpelunit.utils.bpelstats.languagestats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import es.uca.webservices.xquery.parser.util.XQueryParsingException;

public class BpelSubLanguageStatsGatherer {

	public List<FileStats> gather(File bpelFile, String[] reusePaths) throws IOException, SAXException, ParserConfigurationException, XQueryParsingException {
		BPELSubLanguageParser handler = getBpelSubLanguageStats(bpelFile);
		
		List<Import> remainingImports = new ArrayList<Import>();
		List<Import> processedImports = new ArrayList<Import>();
		List<FileStats> fileStats = new ArrayList<FileStats>();
		remainingImports.addAll(handler.getImports());
		
		FileStats bpelFileStats = new FileStats();
		bpelFileStats.fileType = "BPEL";
		bpelFileStats.absoluteFileName = bpelFile.getAbsoluteFile();
		bpelFileStats.xquerySimpleExpressionLOCs = handler.getXQuerySimpleExpressionLOCs();
		bpelFileStats.xqueryComplexExpressionLOCs = handler.getXQueryComplexExpressionLOCs();
		bpelFileStats.xquerySimpleQueryLOCs = handler.getXQuerySimpleQueryLOCs();
		bpelFileStats.xqueryComplexQueryLOCs = handler.getXQueryComplexQueryLOCs();
		bpelFileStats.xpathExpressionsLOCs = handler.getXPathExpressionLOCs();
		bpelFileStats.xpathQueriesLOCs = handler.getXPathQueryLOCs();
		bpelFileStats.xquerySimpleExpressionOccurrences = handler.getXQuerySimpleExpressionOccurrences();
		bpelFileStats.xqueryComplexExpressionOccurrences = handler.getXQueryComplexExpressionOccurrences();
		bpelFileStats.xquerySimpleQueryOccurrences = handler.getXQuerySimpleQueryOccurrences();
		bpelFileStats.xqueryComplexQueryOccurrences = handler.getXQueryComplexQueryOccurrences();
		bpelFileStats.xpathQueryOccurrences = handler.getXPathQueryOccurrences();
		bpelFileStats.xpathExpressionOccurrences = handler.getXPathExpressionOccurrences();
		bpelFileStats.xqueryComplexity = handler.getXQueryComplexityExpression() + handler.getXQueryComplexityQuery();
		fileStats.add(bpelFileStats);
		
		XQuerySubLanguageParser xqueryParser = new XQuerySubLanguageParser();
		
		while(remainingImports.size() > 0) {
			Import imp = remainingImports.remove(0);
			if(!processedImports.contains(imp)) {
				if(imp.importType.equals("XSLT")) {
					XSLTSubLanguageParser xsltSubLangauageStats = getXsltSubLangauageStats(imp.location);
					remainingImports.addAll(xsltSubLangauageStats.getImports());
					FileStats xsltFileStats = new FileStats();
					xsltFileStats.fileType = "XSLT";
					xsltFileStats.absoluteFileName = imp.location.getAbsoluteFile();
					if(isInReusablePath(reusePaths, xsltFileStats)) {
						xsltFileStats.xsltReuseLOCs = calculateLOCs(xsltFileStats.absoluteFileName);
					} else {
						xsltFileStats.xsltExternalLOCs = calculateLOCs(xsltFileStats.absoluteFileName);
					}
					xsltFileStats.xsltComplexity = xsltSubLangauageStats.getComplexity();
					fileStats.add(xsltFileStats);
				} else if(imp.importType.equals("XQUERY")) {
					FileStats xqueryFileStats = new FileStats();
					xqueryFileStats.fileType = "XQUERY";
					xqueryFileStats.absoluteFileName = imp.location.getAbsoluteFile();
					
					String xquery = FileUtils.readFileToString(xqueryFileStats.absoluteFileName);
					xqueryParser.parse(xquery);
					
					if(isInReusablePath(reusePaths, xqueryFileStats)) {
						xqueryFileStats.xqueryReuseLOCs = xqueryParser.getLoc();
					} else {
						xqueryFileStats.xqueryExternalLOCs = xqueryParser.getLoc();
					}
					remainingImports.addAll(getXQueryImports(xqueryFileStats.absoluteFileName));
					xqueryFileStats.xqueryComplexity = xqueryParser.getComplexity();
					fileStats.add(xqueryFileStats);
				}
				processedImports.add(imp);
			}
		}
		
		return fileStats;
	}

	private boolean isInReusablePath(String[] reusePaths, FileStats fileStats) {
		if(reusePaths == null) {
			return false;
		} else {
			for(String reusePath : reusePaths) {
				if(fileStats.absoluteFileName.getAbsolutePath().contains(File.separator + reusePath + File.separator)) {
					return true;
				}
			}
		}
		return false;
	}

	private List<? extends Import> getXQueryImports(File fileName) throws IOException {
		List<Import> imports = new ArrayList<Import>();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		try {
			while(reader.ready()) {
				String line = reader.readLine().trim();
				if(line.startsWith("import")) {
					line = line.replaceAll("\\s+", " ");
					int pos = line.indexOf(" at ");
					if(pos > 0) {
						String location = line.substring(pos + " at ".length());
						if(location.endsWith(";")) {
							location = location.substring(0, location.length() - 1);
							location = location.replaceAll("'", "");
							location = location.replaceAll("\"", "");
						}
						
						Import i = new Import();
						i.importType = "XQUERY";
						i.location = new File(fileName.getParentFile(), location).getCanonicalFile();
						imports.add(i);
					}
				}
			}
			
			return imports;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// quiet close
			}
		}
	}

	private int calculateLOCs(File fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		try {
			int locs = 0;
			
			while(reader.ready()) {
				String line = reader.readLine();
				if(!"".equals(line.trim())) {
					locs++;
				}
			}
			
			return locs;
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// quiet close
			}
		}
	}

	private XSLTSubLanguageParser getXsltSubLangauageStats(File location) throws SAXException, ParserConfigurationException, IOException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		XSLTSubLanguageParser handler = new XSLTSubLanguageParser(location.getParentFile());
		xmlReader.setContentHandler(handler);
		xmlReader.parse(new InputSource(new FileInputStream(location)));
		
		return handler;
	}

	private BPELSubLanguageParser getBpelSubLanguageStats(File f) throws IOException, SAXException, ParserConfigurationException {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		spf.setNamespaceAware(true);
		SAXParser saxParser = spf.newSAXParser();
		XMLReader xmlReader = saxParser.getXMLReader();
		BPELSubLanguageParser handler = new BPELSubLanguageParser(f.getParentFile());
		xmlReader.setContentHandler(handler);
		xmlReader.parse(new InputSource(new FileInputStream(f)));
		
		return handler;
	}
}
