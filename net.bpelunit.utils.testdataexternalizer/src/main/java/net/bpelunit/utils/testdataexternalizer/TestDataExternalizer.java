package net.bpelunit.utils.testdataexternalizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.TransformerException;

import net.bpelunit.framework.xml.suite.XMLActivity;
import net.bpelunit.framework.xml.suite.XMLAnyElement;
import net.bpelunit.framework.xml.suite.XMLSendActivity;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.framework.xml.suite.XMLTrack;
import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import net.bpelunit.util.XMLUtil;
import net.bpelunit.utils.testdataexternalizer.io.IFileWriter;
import net.bpelunit.utils.testdataexternalizer.io.IFileWriter.FileAlreadyExistsException;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Node;

public class TestDataExternalizer {

	private Map<String, String> xmlToFileNameMap = new HashMap<String, String>();
	private Map<String, String> fileNameToXmlMap = new HashMap<String, String>();

	private String relPath = "";
	
	public TestDataExternalizer() {
		this("");
	}
	
	public TestDataExternalizer(String newRelPath) {
		if(!StringUtils.isEmpty(newRelPath)) {
			if(!newRelPath.endsWith("/")) {
				newRelPath += "/";
			}
			
			this.relPath = newRelPath;
		}
	}

	static String getDataElementName(XMLAnyElement anyElement) {
		try {
			Node child = getFirstElement(anyElement);

			return child.getLocalName();
		} catch (Exception e) {
			return null;
		}
	}

	private static Node getFirstElement(XMLAnyElement anyElement) {
		Node child = anyElement.getDomNode().getFirstChild();

		while (child.getNodeType() != Node.ELEMENT_NODE) {
			child = child.getNextSibling();
		}
		return child;
	}

	public String register(XMLAnyElement anyElement) {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		try {
			XMLUtil.writeXML(getFirstElement(anyElement), byteStream);
		} catch (TransformerException e) {
			// Ignore: Should not happen
		}
		String xml = null;
		try {
			xml = new String(byteStream.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// UTF-8 is standard encoding
		}

		if (xmlToFileNameMap.containsKey(xml)) {
			return xmlToFileNameMap.get(xml);
		} else {
			String elementName = getDataElementName(anyElement);
			String fileName = elementName + ".xml";

			int counter = 0;
			while (fileNameToXmlMap.containsKey(fileName)) {
				counter++;
				fileName = elementName + "-" + counter + ".xml";
			}

			xmlToFileNameMap.put(xml, fileName);
			fileNameToXmlMap.put(fileName, xml);
			return fileName;
		}
	}

	public void externalize(IFileWriter fw) throws FileAlreadyExistsException {
		for (String fileName : fileNameToXmlMap.keySet()) {
			String xmlContent = fileNameToXmlMap.get(fileName);
			
			fw.write(new ByteArrayInputStream(xmlContent.getBytes()), this.relPath + fileName);
		}
	}

	public void replaceContentsWithSrc(XMLAnyElement e) {
		if (e.getSrc() == null) {
			String fileName = register(e);
			Node anyNode = e.getDomNode();
			while (anyNode.hasChildNodes()) {
				anyNode.removeChild(anyNode.getFirstChild());
			}
			e.setSrc(this.relPath + fileName);
		}
	}

	public void replaceContentsWithSrc(XMLTestSuiteDocument testSuite) {
		List<XMLTestCase> testCases = testSuite.getTestSuite().getTestCases()
				.getTestCaseList();

		for (XMLTestCase tc : testCases) {
			replaceContentsWithSrc(tc);
		}
	}

	private void replaceContentsWithSrc(XMLTestCase tc) {

		replaceContentsWithSrc(tc.getClientTrack());
		for (XMLTrack t : tc.getPartnerTrackList()) {
			replaceContentsWithSrc(t);
		}
	}

	private void replaceContentsWithSrc(XMLTrack t) {
		if (t != null) {
			replaceContentsWithSrc(t.getReceiveSendList());
			replaceContentsWithSrc(t.getReceiveSendAsynchronousList());
			replaceContentsWithSrc(t.getSendReceiveList());
			replaceContentsWithSrc(t.getSendReceiveAsynchronousList());
			replaceContentsWithSrc(t.getSendOnlyList());
		}
	}

	private void replaceContentsWithSrc(List<? extends XMLActivity> activities) {
		if (activities != null) {
			for (XMLActivity a : activities) {
				replaceContentsWithSrc(a);
			}
		}
	}

	private void replaceContentsWithSrc(XMLActivity a) {
		if (a instanceof XMLTwoWayActivity) {
			replaceContentsWithSrc((XMLTwoWayActivity) a);
		} else {
			replaceContentsWithSrc((XMLSendActivity) a);
		}
	}

	private void replaceContentsWithSrc(XMLSendActivity a) {
		replaceContentsWithSrc(a.getData());
	}

	private void replaceContentsWithSrc(XMLTwoWayActivity a) {
		replaceContentsWithSrc(a.getSend());
	}
}
