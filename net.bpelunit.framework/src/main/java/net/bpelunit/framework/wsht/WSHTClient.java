package net.bpelunit.framework.wsht;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import org.example.wsHT.api.xsd.XMLGetInputDocument;
import org.example.wsHT.api.xsd.XMLGetInputDocument.GetInput;
import org.example.wsHT.api.xsd.XMLGetInputResponseDocument;
import org.example.wsHT.api.xsd.XMLGetInputResponseDocument.GetInputResponse;
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
	private String authorizationRealm;

	@SuppressWarnings("serial")
	public class WSHTException extends RuntimeException {
		public WSHTException(String msg, Throwable t) {
			super(msg, t);
		}
	}

	private static class SOAPCreator {

		private String soapMessage;

		public SOAPCreator() throws IOException {
			soapMessage = new String(StreamUtils.getBytes(WSHTClient.class
					.getResourceAsStream("soap.xml")));
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
			throw new WSHTException("Build problem: Resource not found", e);
		}

		setAuthorizationRealm(username, password);
	}

	final void setAuthorizationRealm(String username, String password) {
		String effectivePassword = password;
		if (effectivePassword == null) {
			effectivePassword = "";
		}
		this.authorizationRealm = new String(Base64.encodeBase64((username
				+ ":" + effectivePassword).getBytes()));
	}

	public GetMyTasksResponse getReadyTaskList() throws IOException,
			XmlException, ParserConfigurationException, SAXException {
		return getReadyTaskList(null);
	}

	public GetMyTasksResponse getTaskList(String taskName,
			XMLTStatus.Enum[] state) throws IOException, XmlException,
			ParserConfigurationException, SAXException {
		XMLGetMyTasksDocument doc = XMLGetMyTasksDocument.Factory.newInstance();
		GetMyTasks getMyTasks = doc.addNewGetMyTasks();
		getMyTasks.setTaskType("TASK");
		getMyTasks.setStatusArray(state);
		if (taskName != null) {
			getMyTasks.setWhereClause("Task.Name = '" + taskName + "'");
		}

		Node result = makeWSHTSOAPRequest(doc.xmlText());

		XMLGetMyTasksResponseDocument resDoc = XMLGetMyTasksResponseDocument.Factory
				.parse(result);

		return resDoc.getGetMyTasksResponse();
	}

	public GetMyTasksResponse getReadyTaskList(String taskName)
			throws IOException, XmlException, ParserConfigurationException,
			SAXException {
		return getTaskList(taskName,
				new XMLTStatus.Enum[] { XMLTStatus.Enum.forString("READY") });
	}

	public void completeTaskWithOutput(String taskId, XmlObject completeData) {
		claim(taskId);
		start(taskId);

		setOutput(taskId, completeData);
		complete(taskId);
	}

	public GetInputResponse getInput(String taskId) {
		try {
			XMLGetInputDocument getInputDoc = XMLGetInputDocument.Factory
					.newInstance();
			GetInput getInput = getInputDoc.addNewGetInput();
			getInput.setIdentifier(taskId);

			Node response = makeWSHTSOAPRequest(getInputDoc);

			XMLGetInputResponseDocument getInputResponseDoc = XMLGetInputResponseDocument.Factory
					.parse(response);
			return getInputResponseDoc.getGetInputResponse();
		} catch (Exception e) {
			throw new WSHTException(e.getMessage(), e);
		}
	}

	private void setOutput(String taskId, XmlObject xmlPayload) {
		try {
			XMLSetOutputDocument setOutputDoc = XMLSetOutputDocument.Factory
					.newInstance();
			SetOutput setOutput = setOutputDoc.addNewSetOutput();
			setOutput.setIdentifier(taskId);
			XmlObject taskData = setOutput.addNewTaskData();
			taskData.set(xmlPayload);

			makeWSHTSOAPRequest(setOutputDoc);
		} catch (Exception e) {
			throw new WSHTException(e.getMessage(), e);
		}
	}

	private void complete(String taskId) {
		try {
			XMLCompleteDocument completeDoc = XMLCompleteDocument.Factory
					.newInstance();
			Complete complete = completeDoc.addNewComplete();
			complete.setIdentifier(taskId);

			makeWSHTSOAPRequest(completeDoc);
		} catch (Exception e) {
			throw new WSHTException(e.getMessage(), e);
		}
	}

	private void start(String taskId) {
		try {
			XMLStartDocument startDoc = XMLStartDocument.Factory.newInstance();
			Start start = startDoc.addNewStart();
			start.setIdentifier(taskId);

			makeWSHTSOAPRequest(startDoc);
		} catch (Exception e) {
			throw new WSHTException(e.getMessage(), e);
		}
	}

	private void claim(String taskId) {
		try {
			XMLClaimDocument claimDoc = XMLClaimDocument.Factory.newInstance();
			Claim claim = claimDoc.addNewClaim();
			claim.setIdentifier(taskId);

			makeWSHTSOAPRequest(claimDoc);
		} catch (Exception e) {
			throw new WSHTException(e.getMessage(), e);
		}
	}

	private Node makeWSHTSOAPRequest(XmlTokenSource request)
			throws IOException, ParserConfigurationException, SAXException {
		return makeWSHTSOAPRequest(request.xmlText());
	}

	private Node makeWSHTSOAPRequest(String request) throws IOException,
			ParserConfigurationException, SAXException {
		HttpURLConnection con = (HttpURLConnection) wsHtEndpoint
				.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Authorization", "Basic "
				+ getAuthorizationRealm());
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

	/**
	 * For test purposes only
	 */
	String getAuthorizationRealm() {
		return authorizationRealm;
	}
}
