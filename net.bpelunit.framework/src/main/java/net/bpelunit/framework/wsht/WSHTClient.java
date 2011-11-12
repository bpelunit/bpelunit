package net.bpelunit.framework.wsht;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.bpelunit.framework.xml.suite.XMLAnyElement;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.lf5.util.StreamUtils;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlTokenSource;
import org.example.wsHT.api.XMLTStatus;
import org.example.wsHT.api.xsd.XMLClaimDocument;
import org.example.wsHT.api.xsd.XMLClaimDocument.Claim;
import org.example.wsHT.api.xsd.XMLCompleteDocument;
import org.example.wsHT.api.xsd.XMLCompleteDocument.Complete;
import org.example.wsHT.api.xsd.XMLGetMyTasksDocument;
import org.example.wsHT.api.xsd.XMLGetMyTasksDocument.GetMyTasks;
import org.example.wsHT.api.xsd.XMLGetMyTasksResponseDocument;
import org.example.wsHT.api.xsd.XMLGetMyTasksResponseDocument.GetMyTasksResponse;
import org.example.wsHT.api.xsd.XMLSetOutputDocument;
import org.example.wsHT.api.xsd.XMLSetOutputDocument.SetOutput;
import org.example.wsHT.api.xsd.XMLStartDocument;
import org.example.wsHT.api.xsd.XMLStartDocument.Start;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class WSHTClient {
	private static final String NAMESPACE_SOAP = "http://schemas.xmlsoap.org/soap/envelope/";
	private URL wsHtEndpoint;
	private SOAPCreator soapCreator;
	String authorizationRealm;

	private static class SOAPCreator {

		private String soapMessage;

		public SOAPCreator() throws IOException {
			soapMessage = new String(StreamUtils.getBytes(WSHTClient.class.getResourceAsStream("soap.xml")));
		}

		public String createSOAP(String xml) {
			return soapMessage.replace("%xml%", xml);
		}
	}

	public WSHTClient(URL endpoint, String username, String password) {
		this.wsHtEndpoint = endpoint;
		try {
			this.soapCreator = new SOAPCreator();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Build problem: Resource not found", e);
		}
		
		setAuthorizationRealm(username, password);
	}

	void setAuthorizationRealm(String username, String password) {
		String effectivePassword = password;
		if(effectivePassword == null) {
			effectivePassword = "";
		}
		this.authorizationRealm = new String(Base64.encodeBase64((username + ":" + effectivePassword).getBytes()));
	}

	public GetMyTasksResponse getReadyTaskList() throws IOException, XmlException, ParserConfigurationException, SAXException {
		return getReadyTaskList(null);
	}
	
	public GetMyTasksResponse getReadyTaskList(String taskName) throws IOException, XmlException,
			ParserConfigurationException, SAXException {
		XMLGetMyTasksDocument doc = XMLGetMyTasksDocument.Factory.newInstance();
		GetMyTasks getMyTasks = doc.addNewGetMyTasks();
		getMyTasks.setTaskType("TASK");
		getMyTasks.setStatusArray(new XMLTStatus.Enum[] {XMLTStatus.Enum.forString("READY")});
		if(taskName != null) {
			getMyTasks.setWhereClause("Task.Name = '" + taskName + "'");
		}
		//getMyTasks.setCreatedOnClause("Task.CreatedOn > '2011-11-09T17:40:00'");

		Node result = makeWSHTSOAPRequest(doc.xmlText());

		XMLGetMyTasksResponseDocument resDoc = XMLGetMyTasksResponseDocument.Factory
				.parse(result);

		System.out.println(resDoc.xmlText());

		return resDoc.getGetMyTasksResponse();
	}

	public void completeTaskWithOutput(String taskId, XMLAnyElement completeData) {
		claim(taskId);
		start(taskId);
		
		setOutput(taskId, completeData);
		complete(taskId);
	}

	private void setOutput(String taskId, XMLAnyElement xmlPayload) {
		try {
			XMLSetOutputDocument setOutputDoc = XMLSetOutputDocument.Factory.newInstance();
			SetOutput setOutput = setOutputDoc.addNewSetOutput();
			setOutput.setIdentifier(taskId);
			XmlObject taskData = setOutput.addNewTaskData();
			taskData.set(xmlPayload);
			
			makeWSHTSOAPRequest(setOutputDoc);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
	private void complete(String taskId) {
		try {
			XMLCompleteDocument completeDoc = XMLCompleteDocument.Factory.newInstance();
			Complete complete = completeDoc.addNewComplete();
			complete.setIdentifier(taskId);
			
			makeWSHTSOAPRequest(completeDoc);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private void start(String taskId) {
		try {
			XMLStartDocument startDoc = XMLStartDocument.Factory.newInstance();
			Start start = startDoc.addNewStart();
			start.setIdentifier(taskId);

			makeWSHTSOAPRequest(startDoc);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private void claim(String taskId) {
		try {
			XMLClaimDocument claimDoc = XMLClaimDocument.Factory.newInstance();
			Claim claim = claimDoc.addNewClaim();
			claim.setIdentifier(taskId);

			makeWSHTSOAPRequest(claimDoc);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	private Node makeWSHTSOAPRequest(XmlTokenSource request)
			throws IOException, UnsupportedEncodingException,
			ParserConfigurationException, SAXException {
		return makeWSHTSOAPRequest(request.xmlText());
	}

	private Node makeWSHTSOAPRequest(String request) throws IOException,
			UnsupportedEncodingException, ParserConfigurationException,
			SAXException {
		HttpURLConnection con = (HttpURLConnection) wsHtEndpoint
				.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic " + authorizationRealm);
		con.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
		con.setRequestProperty("Accept", "application/soap+xml, text/xml");
		con.setDoOutput(true);
		con.setDoInput(true);
		con.connect();
		OutputStream out = con.getOutputStream();

		String soapMessage = soapCreator.createSOAP(request);
		out.write(soapMessage.getBytes("UTF-8"));
		out.close();

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(con.getInputStream());
		Node soapBody = document.getElementsByTagNameNS(NAMESPACE_SOAP, "Body")
				.item(0);

		return soapBody.getFirstChild();
	}
}
