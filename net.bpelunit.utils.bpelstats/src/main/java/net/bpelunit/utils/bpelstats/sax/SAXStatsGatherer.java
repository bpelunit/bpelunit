package net.bpelunit.utils.bpelstats.sax;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import net.bpelunit.utils.bpelstats.BpelStatsGatherer;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class SAXStatsGatherer implements BpelStatsGatherer {

	private SAXStatsHandler handler;
	private XMLReader xmlReader;

	public SAXStatsGatherer() {
		try {
			SAXParserFactory spf = SAXParserFactory.newInstance();
		    spf.setNamespaceAware(true);
		    SAXParser saxParser = spf.newSAXParser();
		    xmlReader = saxParser.getXMLReader();
		    handler = new SAXStatsHandler();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public SAXStatsHandler gather(String bpelFilename) throws IOException, SAXException {
		xmlReader.setContentHandler(handler);
	    xmlReader.parse(convertToFileURL(bpelFilename));
	    return handler;
	}
	
	private static String convertToFileURL(String filename) {
        String path = new File(filename).getAbsolutePath();
        if (File.separatorChar != '/') {
        	path = path.replace(File.separatorChar, '/');
        }

        if (!path.startsWith("/")) {
            path = "/" + path;
        }
        return "file:" + path;
    }
	
}
