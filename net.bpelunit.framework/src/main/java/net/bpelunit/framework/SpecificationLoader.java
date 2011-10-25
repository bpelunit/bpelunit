/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.bpelunit.framework.control.datasource.DataSourceUtil;
import net.bpelunit.framework.control.ext.IBPELDeployer;
import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.control.util.ActivityUtil;
import net.bpelunit.framework.control.util.ActivityUtil.ActivityConstant;
import net.bpelunit.framework.control.util.BPELUnitConstants;
import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.coverage.ICoverageMeasurementTool;
import net.bpelunit.framework.exception.ConfigurationException;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.model.Partner;
import net.bpelunit.framework.model.ProcessUnderTest;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.activity.ReceiveAsync;
import net.bpelunit.framework.model.test.activity.ReceiveSendAsync;
import net.bpelunit.framework.model.test.activity.ReceiveSendSync;
import net.bpelunit.framework.model.test.activity.SendAsync;
import net.bpelunit.framework.model.test.activity.SendReceiveAsync;
import net.bpelunit.framework.model.test.activity.SendReceiveSync;
import net.bpelunit.framework.model.test.activity.TwoWayAsyncActivity;
import net.bpelunit.framework.model.test.activity.Wait;
import net.bpelunit.framework.model.test.data.DataCopyOperation;
import net.bpelunit.framework.model.test.data.ReceiveCondition;
import net.bpelunit.framework.model.test.data.ReceiveDataSpecification;
import net.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import net.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;
import net.bpelunit.framework.model.test.data.SendDataSpecification;
import net.bpelunit.framework.xml.suite.XMLActivity;
import net.bpelunit.framework.xml.suite.XMLAnyElement;
import net.bpelunit.framework.xml.suite.XMLCondition;
import net.bpelunit.framework.xml.suite.XMLCopy;
import net.bpelunit.framework.xml.suite.XMLDeploymentSection;
import net.bpelunit.framework.xml.suite.XMLHeaderProcessor;
import net.bpelunit.framework.xml.suite.XMLMapping;
import net.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLProperty;
import net.bpelunit.framework.xml.suite.XMLReceiveActivity;
import net.bpelunit.framework.xml.suite.XMLSendActivity;
import net.bpelunit.framework.xml.suite.XMLSetUp;
import net.bpelunit.framework.xml.suite.XMLSoapActivity;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestCasesSection;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.framework.xml.suite.XMLTrack;
import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;
import net.bpelunit.framework.xml.suite.XMLWaitActivity;

import org.apache.log4j.Logger;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The specificaton loader reads test suite documents and creates the in-memory
 * test run data structure, which is made ready for test execution.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SpecificationLoader {

	private Logger fLogger;
	private BPELUnitRunner fRunner;

	protected SpecificationLoader(BPELUnitRunner runner) {
		fRunner = runner;
		fLogger = Logger.getLogger(getClass());
	}

	protected TestSuite loadTestSuite(File suite) throws SpecificationException {
		try {
			fLogger.info("Loading test suite from file " + suite);

			String path = suite.getCanonicalFile().getParent() + File.separator;
			fLogger.info("Using base path: " + path);

			XMLTestSuiteDocument doc = XMLTestSuiteDocument.Factory
					.parse(suite);
			validateTestSuite(doc);
			TestSuite testSuite = parseSuite(path, doc);

			fLogger.info("Loaded test suite with name \"" + testSuite.getName()
					+ "\" and " + testSuite.getTestCaseCount() + " test cases.");

			if (BPELUnitRunner.measureTestCoverage()) {
				ICoverageMeasurementTool tool = BPELUnitRunner
						.getCoverageMeasurmentTool();
				try {

					String encodingStyle = tool.getEncodingStyle();
					if (encodingStyle != null) {
						tool.setSOAPEncoder(fRunner
								.createNewSOAPEncoder(encodingStyle));
					}
				} catch (Exception e) {
					tool.setErrorStatus("CoverageTool: " + e.getMessage());

				}

			}
			return testSuite;

		} catch (XmlException e) {
			throw new SpecificationException(
					"An XML reading error occurred when reading the test suite file.",
					e);
		} catch (IOException e) {
			throw new SpecificationException(
					"An I/O error occurred when reading the test suite file.",
					e);
		}
	}

	private void validateTestSuite(final XMLTestSuiteDocument doc)
			throws SpecificationException {
		ArrayList<Object> validationErrors = new ArrayList<Object>();
		XmlOptions options = new XmlOptions();
		options.setErrorListener(validationErrors);

		if (!doc.validate(options)) {
			StringWriter sW = new StringWriter();
			for (Object o : validationErrors) {
				sW.append(o + "\n");
			}
			throw new SpecificationException("BPTS is invalid:\n"
					+ sW.toString());
		}
	}

	private TestSuite parseSuite(String testDirectory,
			XMLTestSuiteDocument xmlTestSuiteDocument)
			throws SpecificationException {

		XMLTestSuite xmlTestSuite = xmlTestSuiteDocument.getTestSuite();
		if (xmlTestSuite == null)
			throw new SpecificationException(
					"Could not find testSuite root element in the test suite XML file.");

		// Name
		String xmlSuiteName = xmlTestSuite.getName();
		if (xmlSuiteName == null)
			throw new SpecificationException(
					"No name found for the test suite.");

		URL suiteBaseURL = getBaseURL(xmlTestSuite);

		// Load deployment information
		XMLDeploymentSection xmlDeployment = xmlTestSuite.getDeployment();
		if (xmlDeployment == null)
			throw new SpecificationException(
					"Could not find deployment section inside test suite document.");

		// A map for the partners to re-identify them when reading
		// PartnerTracks.
		Map<String, Partner> suitePartners = new HashMap<String, Partner>();

		/*
		 * The Process Under Test and his Deployer.
		 */

		XMLPUTDeploymentInformation xmlPut = xmlDeployment.getPut();
		if (xmlPut == null)
			throw new SpecificationException(
					"Expected a Process Under Test (PUT) in the test suite.");
		String xmlPutName = xmlPut.getName();
		String xmlPutWSDL = xmlPut.getWsdl();
		String xmlPutType = xmlPut.getType();

		if ((xmlPutName == null) || (xmlPutWSDL == null)
				|| (xmlPutType == null))
			throw new SpecificationException(
					"Process Under Test must have attributes name, type, wsdl, and a deployment section specified.");

		ProcessUnderTest processUnderTest = new ProcessUnderTest(xmlPutName,
				testDirectory, xmlPutWSDL, suiteBaseURL.toString());

		for (XMLProperty property : xmlPut.getPropertyList()) {
			processUnderTest.setXMLDeploymentOption(property.getName(),
					property.getStringValue());
		}

		IBPELDeployer suitePutDeployer = fRunner.createNewDeployer(xmlPutType);
		processUnderTest.setDeployer(suitePutDeployer);
		processUnderTest.setGlobalConfiguration(fRunner
				.getGlobalConfigurationForDeployer(suitePutDeployer));

		// Add the put to the partners in case of running in test mode
		suitePartners.put(processUnderTest.getName(), processUnderTest);

		/*
		 * Add partners of the process to put in order to facilitate end point
		 * replacement
		 */
		processUnderTest.setPartners(suitePartners);

		/*
		 * The Client. Note that the client uses the PUT's WSDL. This is
		 * intended as the clients activities will all deal with the partners
		 * operations.
		 */

		Partner suiteClient = new Partner(BPELUnitConstants.CLIENT_NAME,
				testDirectory, xmlPutWSDL, suiteBaseURL.toString());

		/*
		 * The Partners. Each partner is initialized with the attached WSDL
		 * information, which allows retrieving operations from this partner
		 * later on.
		 */

		for (XMLPartnerDeploymentInformation xmlPDI : xmlDeployment
				.getPartnerList()) {
			String name = xmlPDI.getName();
			String wsdl = xmlPDI.getWsdl();
			if ((name == null) || (wsdl == null))
				throw new SpecificationException(
						"Name and WSDL attributes of a partner must not be empty.");
			Partner p = new Partner(xmlPDI.getName(), testDirectory,
					xmlPDI.getWsdl(), suiteBaseURL.toString());
			suitePartners.put(p.getName(), p);
		}

		// Create the suite.
		TestSuite suite = new TestSuite(xmlSuiteName, suiteBaseURL,
				processUnderTest);

		// Process the contents of the setUp block, if any
		try {
			readTestSuiteSetUpBlock(suite, xmlTestSuite);
		} catch (Exception ex) {
			throw new SpecificationException("Error during test suite set up",
					ex);
		}

		/*
		 * The Test Cases. Each test case consists of a number of Partner
		 * Tracks, which in turn consist of an ordered collection of Activities.
		 */

		XMLTestCasesSection xmlTestCases = xmlTestSuite.getTestCases();
		if (xmlTestCases == null)
			throw new SpecificationException(
					"No test case section found in test suite document.");

		List<XMLTestCase> xmlTestCaseList = xmlTestCases.getTestCaseList();

		if (xmlTestCaseList == null || xmlTestCaseList.size() == 0)
			throw new SpecificationException("No test cases found.");

		int currentNumber = 0;
		for (XMLTestCase xmlTestCase : xmlTestCaseList) {

			String xmlTestCaseName = xmlTestCase.getName();
			if (xmlTestCaseName == null)
				xmlTestCaseName = "Test Case " + currentNumber;
			currentNumber++;

			boolean isVary = xmlTestCase.getVary();
			int rounds = computeNumberOfRounds(xmlTestSuiteDocument, isVary);
			fLogger.info("Varying: " + isVary + " (Rounds: " + rounds + ")");

			IDataSource dataSource;
			try {
				dataSource = DataSourceUtil.createDataSource(xmlTestSuite,
						xmlTestCase, new File(testDirectory), fRunner);
			} catch (DataSourceException e) {
				throw new SpecificationException("There was a problem while "
						+ "initializing the specified data source.", e);
			}

			final int nRows = dataSource != null ? dataSource.getNumberOfRows()
					: 1;
			final int nRounds = isVary && rounds > 0 ? rounds : 1;
			for (int iRow = 0; iRow < nRows; ++iRow) {
				for (int iRound = 0; iRound < nRounds; iRound++) {
					String currentTestCaseName = xmlTestCaseName;
					if (dataSource != null) {
						currentTestCaseName = currentTestCaseName + " (Row "
								+ (iRow + 1) + ")";
					}
					if (isVary && rounds > 0) {
						// Create a non-computer-science name ;)
						currentTestCaseName = currentTestCaseName + " (Round "
								+ (iRound + 1) + ")";
					}
					if (!xmlTestCase.getAbstract()) {
						TestCase test = createTestCase(suitePartners,
								suiteClient, suite, xmlTestCase,
								currentTestCaseName, iRound, testDirectory);
						test.setDataSource(dataSource);
						test.setRowIndex(iRow);
						suite.addTestCase(test);
					}
				}
			}
		}

		return suite;
	}

	private URL getBaseURL(XMLTestSuite xmlTestSuite)
			throws SpecificationException {
		try {
			// Use the local base URL, and if that doesn't work, try the default
			// one
			String xmlUrl = xmlTestSuite.getBaseURL();
			if (xmlUrl == null)
				xmlUrl = BPELUnitConstants.DEFAULT_BASE_URL;
			URL suiteBaseURL = new URL(xmlUrl);

			// Normalize the URL: add port and trailing slash if missing
			int port = suiteBaseURL.getPort();
			if (port == -1) {
				port = BPELUnitConstants.DEFAULT_BASE_PORT;
			}
			String path = suiteBaseURL.getPath();
			if (!path.endsWith("/")) {
				path += "/";
			}
			suiteBaseURL = new URL(suiteBaseURL.getProtocol(),
					suiteBaseURL.getHost(), port, path);

			return suiteBaseURL;
		} catch (MalformedURLException e) {
			throw new SpecificationException(
					"Could not create a valid URL from specified base URL.", e);
		}
	}

	private int computeNumberOfRounds(
			XMLTestSuiteDocument xmlTestSuiteDocument, boolean isVary)
			throws SpecificationException {
		int rounds;
		rounds = 0;

		if (isVary) {
			/*
			 * This is a varying test case. Each activity may have an arbitrary
			 * number of delay times specified. These lists SHOULD be of equal
			 * length, although no varying also also okay.
			 * 
			 * We use XPath to find all delay sequences and find the highest
			 * size. This will be the size expected from all activities.
			 */
			XPath xpath = XPathFactory.newInstance().newXPath();
			NamespaceContextImpl nsContext = new NamespaceContextImpl();
			nsContext.setNamespace("ts",
					BPELUnitConstants.BPELUNIT_TESTSUITE_NAMESPACE);
			xpath.setNamespaceContext(nsContext);
			try {
				NodeList set = (NodeList) xpath.evaluate("//@delaySequence",
						xmlTestSuiteDocument.getDomNode(),
						XPathConstants.NODESET);
				int currentMax = 0;
				for (int i = 0; i < set.getLength(); i++) {
					if (set.item(i) instanceof Attr) {
						Attr attr = (Attr) set.item(i);
						List<Integer> ints = getRoundInformation(attr
								.getValue());
						if (ints != null)
							currentMax = ints.size();
					}
				}
				rounds = currentMax;
			} catch (XPathExpressionException e) {
				// This should not happen.
				throw new SpecificationException(
						"There was a problem finding delay sequences. This most likely indicates a bug in the framework.");
			}
		}
		return rounds;
	}

	private void readTestSuiteSetUpBlock(TestSuite testSuite,
			XMLTestSuite xmlTestSuite) throws Exception {
		if (!xmlTestSuite.isSetSetUp())
			return;

		XMLSetUp xmlSetUp = xmlTestSuite.getSetUp();
		if (xmlSetUp.isSetScript()) {
			testSuite.setSetUpVelocityScript(xmlSetUp.getScript());
		}
	}

	private TestCase createTestCase(Map<String, Partner> suitePartners,
			Partner suiteClient, TestSuite suite, XMLTestCase xmlTestCase,
			String xmlTestCaseName, int round, String testDirectory) throws SpecificationException {

		TestCase test = new TestCase(suite, xmlTestCaseName);

		// Load metadata
		for (XMLProperty data : xmlTestCase.getPropertyList()) {
			String xmlPropertyName = data.getName();
			String xmlPropertyData = data.getStringValue();
			if ((xmlPropertyName == null) || (xmlPropertyData == null))
				throw new SpecificationException("Metadata in Test Case "
						+ xmlTestCaseName
						+ " must have both property and value.");

			test.addProperty(xmlPropertyName, xmlPropertyData);
		}

		// Set up block
		readTestCaseSetUpBlock(test, xmlTestCase);

		// Client Partner Track
		XMLTrack xmlClientTrack = xmlTestCase.getClientTrack();

		if (xmlClientTrack == null)
			throw new SpecificationException(
					"Could not find clientTrack in test case "
							+ xmlTestCase.getName());

		PartnerTrack track = new PartnerTrack(test, suiteClient);
		readActivities(track, xmlTestCase, xmlClientTrack, round, testDirectory);
		track.setNamespaceContext(getNamespaceMap(xmlClientTrack.newCursor()));
		test.addPartnerTrack(track);

		// Partners Partner Track
		List<XMLPartnerTrack> partnerTrackList = xmlTestCase
				.getPartnerTrackList();
		// There might be no partners.
		if (partnerTrackList != null)
			for (XMLPartnerTrack xmlPartnerTrack : partnerTrackList) {

				String xmlPartnerTrackName = xmlPartnerTrack.getName();
				if (xmlPartnerTrackName == null)
					throw new SpecificationException(
							"A partnertrack has been specified without a partner name.");

				Partner realPartner = suitePartners.get(xmlPartnerTrackName);
				if (realPartner == null)
					throw new SpecificationException(
							"Could not find partner or client with the name "
									+ xmlPartnerTrack.getName());

				PartnerTrack pTrack = new PartnerTrack(test, realPartner);
				readActivities(pTrack, xmlTestCase, xmlPartnerTrack, round, testDirectory);
				pTrack.setNamespaceContext(getNamespaceMap(xmlPartnerTrack
						.newCursor()));
				if (xmlPartnerTrack.isSetAssume()) {
					pTrack.setAssumption(xmlPartnerTrack.getAssume());
				}
				test.addPartnerTrack(pTrack);
			}
		return test;
	}

	private void readTestCaseSetUpBlock(TestCase test, XMLTestCase xmlTestCase) {
		if (!xmlTestCase.isSetSetUp())
			return;

		XMLSetUp xmlSetUp = xmlTestCase.getSetUp();
		if (xmlSetUp.isSetScript()) {
			test.setSetUpVelocityScript(xmlSetUp.getScript());
		}
	}

	/**
	 * Reads a list of XML activities from inside a partnerTrack and adds them
	 * to the partnerTrack.
	 * 
	 * @param partnerTrack
	 *            the track to which the activites belong.
	 * @param xmlTestCase
	 * @param xmlTrack
	 *            the list of XML activities
	 * @param round
	 * @throws SpecificationException
	 * @throws ConfigurationException
	 */
	private void readActivities(PartnerTrack partnerTrack,
			XMLTestCase xmlTestCase, XMLTrack xmlTrack, int round,
			String testDirectory)
			throws SpecificationException {

		List<XMLActivity> xmlActivities = ActivityUtil.getActivities(xmlTrack);

		if (xmlActivities.isEmpty()) {

			// Activity list is empty. If the test case is based on another
			// test case, check the activities of this test case.
			if (xmlTestCase.getBasedOn() != null
					&& !"".equals(xmlTestCase.getBasedOn())) {

				// Find name of track
				String trackName = null;
				if (xmlTrack instanceof XMLPartnerTrack)
					trackName = ((XMLPartnerTrack) xmlTrack).getName();
				else
					trackName = BPELUnitConstants.CLIENT_NAME;

				// Find the first test case with has a non-empty track like ours
				XMLTestCase basedOnTestCase = findInHierarchy(xmlTestCase,
						trackName);
				if (basedOnTestCase != null) {
					XMLTrack trackInNewTestCase = getPartnerTrack(
							basedOnTestCase, trackName);
					if (trackInNewTestCase != null) {
						readActivities(partnerTrack, basedOnTestCase,
								trackInNewTestCase, round, testDirectory);
						return;
					}
				}
			}

			partnerTrack.setActivities(new ArrayList<Activity>());

		} else {
			List<Activity> activities = new ArrayList<Activity>();
			for (XMLActivity event : xmlActivities) {

				/*
				 * Each activity is one of the seven specified activites:
				 * ReceiveOnly, SendOnly, ReceiveSendSync, SendReceiveSync,
				 * ReceiveSendAsync, SendReceiveAsync, and Wait.
				 */

				if (event instanceof XMLWaitActivity) {
					XMLWaitActivity xmlWait = (XMLWaitActivity) event;
					Wait activity = new Wait(partnerTrack);
					activity.setWaitDuration(xmlWait.getWaitForMilliseconds());
					activity.setAssumption(event.getAssume());
					activities.add(activity);
					continue;
				}

				if (event instanceof XMLReceiveActivity) {
					XMLReceiveActivity xmlReceive = (XMLReceiveActivity) event;
					ReceiveAsync activity = new ReceiveAsync(partnerTrack);
					ReceiveDataSpecification spec = createReceiveSpecificationStandalone(
							activity, xmlReceive,
							SOAPOperationDirectionIdentifier.INPUT);
					activity.initialize(spec);
					activity.setAssumption(event.getAssume());

					activities.add(activity);
					continue;
				}

				if (event instanceof XMLSendActivity) {
					XMLSendActivity xmlSend = (XMLSendActivity) event;
					SendAsync activity = new SendAsync(partnerTrack);
					SendDataSpecification spec = createSendSpecificationFromStandalone(
							activity, xmlSend,
							SOAPOperationDirectionIdentifier.INPUT, round,
							testDirectory);
					activity.initialize(spec);
					activity.setAssumption(event.getAssume());

					activities.add(activity);
					continue;
				}

				if (event instanceof XMLTwoWayActivity) {
					XMLTwoWayActivity op = (XMLTwoWayActivity) event;
					if (ActivityUtil.isActivity(op,
							ActivityConstant.RECEIVE_SEND_SYNC)) {
						Activity activity = createReceiveSendSynchronous(op,
								partnerTrack, round, testDirectory);
						activity.setAssumption(event.getAssume());
						activities.add(activity);
					}
					if (ActivityUtil.isActivity(op,
							ActivityConstant.SEND_RECEIVE_SYNC)) {
						Activity activity = createSendReceiveSynchronous(op,
								partnerTrack, round, testDirectory);
						activity.setAssumption(event.getAssume());
						activities.add(activity);
					}
					if (ActivityUtil.isActivity(op,
							ActivityConstant.RECEIVE_SEND_ASYNC)) {
						ReceiveSendAsync activity = new ReceiveSendAsync(
								partnerTrack);
						fillAsyncTwoWay(activity, op, round, testDirectory);
						activity.setAssumption(event.getAssume());
						activities.add(activity);
					}
					if (ActivityUtil.isActivity(op,
							ActivityConstant.SEND_RECEIVE_ASYNC)) {
						SendReceiveAsync activity = new SendReceiveAsync(
								partnerTrack);
						fillAsyncTwoWay(activity, op, round, testDirectory);
						activity.setAssumption(event.getAssume());
						activities.add(activity);
					}
					continue;
				}

				throw new SpecificationException(
						"No activity found when reading event list for "
								+ partnerTrack);
			}

			partnerTrack.setActivities(activities);
		}
	}

	/**
	 * 
	 * Creates a synchronous send/receive activity.
	 * 
	 */
	private Activity createSendReceiveSynchronous(
			XMLTwoWayActivity xmlSendReceiveSync, PartnerTrack partnerTrack,
			int round, String testDirectory) throws SpecificationException {

		SendReceiveSync activity = new SendReceiveSync(partnerTrack);

		XMLSendActivity xmlSend = xmlSendReceiveSync.getSend();
		XMLReceiveActivity xmlReceive = xmlSendReceiveSync.getReceive();

		if ((xmlSend == null) || (xmlReceive == null))
			throw new SpecificationException(
					"A synchronous send/receive activity must have both receive and send children.");

		XMLHeaderProcessor xmlHeaderProcessor = xmlSendReceiveSync
				.getHeaderProcessor();

		// Always send to an input element
		SendDataSpecification sSpec = createSendSpecificationFromParent(
				activity, xmlSendReceiveSync, xmlSend,
				SOAPOperationDirectionIdentifier.INPUT, round, testDirectory);

		// Receive may expect a fault - or receive from an output element
		SOAPOperationDirectionIdentifier receiveDirection = SOAPOperationDirectionIdentifier.OUTPUT;
		if (xmlReceive.getFault())
			receiveDirection = SOAPOperationDirectionIdentifier.FAULT;
		ReceiveDataSpecification rSpec = createReceiveSpecificationFromParent(
				activity, xmlSendReceiveSync, xmlReceive, receiveDirection);

		IHeaderProcessor proc = getHeaderProcessor(xmlHeaderProcessor);
		ArrayList<net.bpelunit.framework.model.test.data.DataCopyOperation> mapping = getCopyOperations(
				activity, xmlSendReceiveSync);

		activity.initialize(sSpec, rSpec, proc, mapping);
		return activity;
	}

	/**
	 * Creates a synchronous receive/send activity.
	 * 
	 */
	private Activity createReceiveSendSynchronous(
			XMLTwoWayActivity xmlReceiveSendSync, PartnerTrack partnerTrack,
			int round, String testDirectory) throws SpecificationException {

		ReceiveSendSync activity = new ReceiveSendSync(partnerTrack);
		activity.setAssumption(xmlReceiveSendSync.getAssume());

		XMLSendActivity xmlSend = xmlReceiveSendSync.getSend();
		XMLReceiveActivity xmlReceive = xmlReceiveSendSync.getReceive();

		if ((xmlSend == null) || (xmlReceive == null))
			throw new SpecificationException(
					"A synchronous receive/send activity must have both receive and send children.");

		XMLHeaderProcessor xmlHeaderProcessor = xmlReceiveSendSync
				.getHeaderProcessor();

		// Always receive to the input element.
		ReceiveDataSpecification rSpec = createReceiveSpecificationFromParent(
				activity, xmlReceiveSendSync, xmlReceive,
				SOAPOperationDirectionIdentifier.INPUT);

		// The "send" part may send a fault here, or to an output element
		SOAPOperationDirectionIdentifier sendDirection = SOAPOperationDirectionIdentifier.OUTPUT;
		if (xmlSend.getFault())
			sendDirection = SOAPOperationDirectionIdentifier.FAULT;
		SendDataSpecification sSpec = createSendSpecificationFromParent(
				activity, xmlReceiveSendSync, xmlSend, sendDirection, round, testDirectory);

		IHeaderProcessor proc = getHeaderProcessor(xmlHeaderProcessor);
		ArrayList<net.bpelunit.framework.model.test.data.DataCopyOperation> mapping = getCopyOperations(
				activity, xmlReceiveSendSync);

		activity.initialize(sSpec, rSpec, proc, mapping);
		return activity;
	}

	/**
	 * Fills an already-existing asynchronous two-way activity with the relevant
	 * information from the test suite XML document.
	 * 
	 * @param twoWayActivity
	 *            the given asynchronous activity
	 * @param xmlAsyncTwoWay
	 *            the XML data
	 * @param round
	 * @throws SpecificationException
	 * @throws ConfigurationException
	 */
	private void fillAsyncTwoWay(TwoWayAsyncActivity twoWayActivity,
			XMLTwoWayActivity xmlAsyncTwoWay, int round, String testDirectory)
			throws SpecificationException {

		XMLSendActivity xmlSend = xmlAsyncTwoWay.getSend();
		XMLReceiveActivity xmlReceive = xmlAsyncTwoWay.getReceive();

		if ((xmlSend == null) || (xmlReceive == null))
			throw new SpecificationException(
					"An asynchronous receive/send or send/receive activity must have both receive and send children.");

		XMLHeaderProcessor xmlHeaderProcessor = xmlAsyncTwoWay
				.getHeaderProcessor();
		ArrayList<net.bpelunit.framework.model.test.data.DataCopyOperation> mapping = getCopyOperations(
				twoWayActivity, xmlAsyncTwoWay);

		SendAsync sendAct = new SendAsync(twoWayActivity);
		SendDataSpecification sSpec = createSendSpecificationFromStandalone(
				sendAct, xmlSend, SOAPOperationDirectionIdentifier.INPUT, round, testDirectory);
		sendAct.initialize(sSpec);

		ReceiveAsync receiveAct = new ReceiveAsync(twoWayActivity);
		ReceiveDataSpecification rSpec = createReceiveSpecificationStandalone(
				receiveAct, xmlReceive, SOAPOperationDirectionIdentifier.INPUT);
		receiveAct.initialize(rSpec);

		IHeaderProcessor proc = getHeaderProcessor(xmlHeaderProcessor);

		twoWayActivity.initialize(sendAct, receiveAct, proc, mapping);
	}

	// ******************** Specifications *******************

	/**
	 * Creates a send specification for an asnychronous send-only. In this case,
	 * service information must be stored directly on the send activity itself.
	 * 
	 */
	private SendDataSpecification createSendSpecificationFromStandalone(
			Activity parentActivity, XMLSendActivity xmlSend,
			SOAPOperationDirectionIdentifier direction, int round, String testDirectory)
			throws SpecificationException {

		SOAPOperationCallIdentifier operation = getOperationCallIdentifier(
				parentActivity, getService(parentActivity, xmlSend),
				xmlSend.getPort(), xmlSend.getOperation(), direction);
		return createSendSpecification(parentActivity, operation, xmlSend,
				round, testDirectory);
	}

	/**
	 * Creates a send specficiation for a synchronous send/receive. In this
	 * case, service information must be stored on the send/receive activity.
	 * 
	 */
	private SendDataSpecification createSendSpecificationFromParent(
			Activity parentActivity, XMLTwoWayActivity xmlSendReceiveSync,
			XMLSendActivity xmlSend,
			SOAPOperationDirectionIdentifier direction, int round,
			String testDirectory)
			throws SpecificationException {

		SOAPOperationCallIdentifier operation = getOperationCallIdentifier(
				parentActivity, getService(parentActivity, xmlSendReceiveSync),
				xmlSendReceiveSync.getPort(),
				xmlSendReceiveSync.getOperation(), direction);
		return createSendSpecification(parentActivity, operation, xmlSend,
				round, testDirectory);
	}

	/**
	 * Creates a send specification for the given activity and operation and
	 * from the given XML Send Specification.
	 * 
	 */
	private SendDataSpecification createSendSpecification(Activity activity,
			SOAPOperationCallIdentifier operation, XMLSendActivity xmlSend,
			int round, String testDirectory) throws SpecificationException {

		// Namespaces
		NamespaceContext context = getNamespaceMap(xmlSend.newCursor());

		SendDataSpecification spec = new SendDataSpecification(activity,
				context);

		String encodingStyle = operation.getEncodingStyle();
		String targetURL = operation.getTargetURL();
		String soapAction = operation.getSOAPHTTPAction();
		ISOAPEncoder encoder = fRunner.createNewSOAPEncoder(encodingStyle);

		// Check that we have everything
		int xmlSourceCount = 0;
		if (xmlSend.isSetData())
			xmlSourceCount++;
		if (xmlSend.isSetTemplate())
			xmlSourceCount++;
		if (xmlSend.isSetXmlFile())
			xmlSourceCount++;
		if (xmlSourceCount != 1) {
			throw new SpecificationException(
					"Send Element must have exactly one of <data>, <template>, or <xmlFile> ");
		}

		if (xmlSend.isSetXmlFile()) {
			importXmlFileAndSetAsData(xmlSend, testDirectory);
		}

		XMLAnyElement xmlData = xmlSend.getData();
		XMLAnyElement xmlTemplate = xmlSend.getTemplate();
		
		// "delay" attribute
		if (xmlSend.isSetDelay() && xmlSend.isSetDelaySequence()) {
			throw new SpecificationException(
					"Send Element can only have exactly one of {delay, delaySequence}, and not both");
		}
		final String delayExpression = xmlSend.getDelay();
		// Import namespaces in the BPTS file to the root elements of the
		// <data> or <template> element, and convert the <template> contents
		// to text.
		Element rawDataRoot = null;
		String templateText = null;

		try {
			if (xmlData != null)
				rawDataRoot = copyAsRootWithNamespaces(xmlData);
			if (xmlTemplate != null) {
				Element templateRoot = copyAsRootWithNamespaces(xmlTemplate);
				templateText = XmlObject.Factory.parse(templateRoot).xmlText();
			}
		} catch (XmlException e) {
			throw new SpecificationException(
					"An error occurred when reading the literal data or "
							+ "template of send for activity "
							+ activity.getName() + ": " + e.getMessage(), e);
		}

		/*
		 * Get round data
		 */
		String delaySequence = xmlSend.getDelaySequence();
		List<Integer> sequence = getRoundInformation(delaySequence);
		int currentDelay = 0;
		if (sequence != null && sequence.size() > round)
			currentDelay = sequence.get(round);

		// If the user hasn't specified any fault code or string, use these
		// default values
		final QName faultCode = xmlSend.isSetFaultcode() ? xmlSend
				.getFaultcode() : BPELUnitConstants.SOAP_FAULT_CODE_CLIENT;
		final String faultString = xmlSend.isSetFaultstring() ? xmlSend
				.getFaultstring() : BPELUnitConstants.SOAP_FAULT_DESCRIPTION;
		if (activity instanceof ReceiveSendSync) {
			// This send specification will be used to send back
			// a response to a HTTP request inside the same channel
			// It does not need targetURL and soapAction.
			spec.initialize(operation, currentDelay, delayExpression, null,
					null, encodingStyle, encoder, rawDataRoot, templateText,
					faultCode, faultString);
		} else
			spec.initialize(operation, currentDelay, delayExpression,
					targetURL, soapAction, encodingStyle, encoder, rawDataRoot,
					templateText, faultCode, faultString);

		return spec;
	}

	private void importXmlFileAndSetAsData(XMLSendActivity xmlSend,
			String testDirectory) throws SpecificationException {
		File xmlFile = new File(testDirectory, xmlSend.getXmlFile());
		xmlSend.setData(XMLAnyElement.Factory.newInstance());
		xmlSend.unsetXmlFile();
		try {
			xmlSend.getData().set(XmlObject.Factory.parse(xmlFile));
		} catch (Exception e) {
			throw new SpecificationException(
					"Error while loading imported XML File: "
							+ e.getMessage(), e);
		}
	}

	private Element copyAsRootWithNamespaces(XMLAnyElement xmlData)
			throws XmlException, DOMException {
		Element rawDataRoot;
		rawDataRoot = BPELUnitUtil.generateDummyElementNode();
		// Use the internal namespace mechanism of XMLBeans to
		// sort out namespaces and add them to the top-level element,
		// ready to be copied by importNode().
		XmlObject test = XmlObject.Factory.parse(xmlData.xmlText());
		NodeList cn = test.getDomNode().getChildNodes();
		for (int i = 0; i < cn.getLength(); i++) {
			Node currentItem = cn.item(i);
			// must be elements. There might be comments flying around,
			// filter them.
			if (currentItem instanceof Element) {
				Element element = (Element) currentItem;
				rawDataRoot.appendChild(rawDataRoot.getOwnerDocument()
						.importNode(element, true));
			}
		}
		return rawDataRoot;
	}

	/**
	 * Creates a receive data specification for an asynchronous receive. In this
	 * case, the service information must be stored on the asynchronous receive
	 * itself.
	 */
	private ReceiveDataSpecification createReceiveSpecificationStandalone(
			Activity parentActivity, XMLReceiveActivity xmlReceive,
			SOAPOperationDirectionIdentifier direction)
			throws SpecificationException {

		SOAPOperationCallIdentifier operation = getOperationCallIdentifier(
				parentActivity, getService(parentActivity, xmlReceive),
				xmlReceive.getPort(), xmlReceive.getOperation(), direction);
		return createReceiveSpecification(parentActivity, operation, xmlReceive);
	}

	/**
	 * Creates a receive data specification for a synchronous receive/send. In
	 * this case, service information must be stored on the receive/send
	 * activity.
	 */
	private ReceiveDataSpecification createReceiveSpecificationFromParent(
			Activity parentActivity, XMLTwoWayActivity xmlReceiveSendSync,
			XMLReceiveActivity xmlReceive,
			SOAPOperationDirectionIdentifier direction)
			throws SpecificationException {

		SOAPOperationCallIdentifier operation = getOperationCallIdentifier(
				parentActivity, getService(parentActivity, xmlReceiveSendSync),
				xmlReceiveSendSync.getPort(),
				xmlReceiveSendSync.getOperation(), direction);
		return createReceiveSpecification(parentActivity, operation, xmlReceive);
	}

	/**
	 * Creates a receive specification for the given activity and operation from
	 * the information stored in the XML Receive.
	 */
	private ReceiveDataSpecification createReceiveSpecification(
			Activity activity, SOAPOperationCallIdentifier operation,
			XMLReceiveActivity xmlReceive) throws SpecificationException {

		// Namespaces
		NamespaceContext context = getNamespaceMap(xmlReceive.newCursor());

		ReceiveDataSpecification spec = new ReceiveDataSpecification(activity,
				context);

		String encodingStyle = operation.getEncodingStyle();
		ISOAPEncoder encoder = fRunner.createNewSOAPEncoder(encodingStyle);

		// get conditions
		List<XMLCondition> xmlConditionList = xmlReceive.getConditionList();
		List<ReceiveCondition> cList = new ArrayList<ReceiveCondition>();
		if (xmlConditionList != null)
			for (XMLCondition xmlCondition : xmlConditionList) {
				cList.add(new ReceiveCondition(spec, xmlCondition
						.getExpression(), xmlCondition.getTemplate(),
						xmlCondition.getValue()));
			}

		// Add fault code and string. These will be only checked if this message
		// is a fault and if
		// they are not null.
		QName faultCode = xmlReceive.getFaultcode();
		String faultString = xmlReceive.getFaultstring();

		spec.initialize(operation, encodingStyle, encoder, cList, faultCode,
				faultString);
		return spec;
	}

	// *********** HELPERS *******************

	private ArrayList<DataCopyOperation> getCopyOperations(Activity activity,
			XMLTwoWayActivity xmlTwoWayType) throws SpecificationException {

		ArrayList<DataCopyOperation> copyDataOperations = new ArrayList<DataCopyOperation>();
		XMLMapping xmlMapping = xmlTwoWayType.getMapping();
		if (xmlMapping != null) {
			List<XMLCopy> xmlCopyList = xmlMapping.getCopyList();
			if (xmlCopyList != null)
				for (XMLCopy xmlCopy : xmlCopyList) {
					String xmlCopyFrom = xmlCopy.getFrom();
					String xmlCopyTo = xmlCopy.getTo();
					if ((xmlCopyFrom == null) || (xmlCopyTo == null))
						throw new SpecificationException(
								"Copy operations need both copy-from and copy-to specifications.");

					copyDataOperations.add(new DataCopyOperation(activity,
							xmlCopyFrom, xmlCopyTo));
				}
		}

		return copyDataOperations;
	}

	private IHeaderProcessor getHeaderProcessor(
			XMLHeaderProcessor xmlHeaderProcessor)
			throws SpecificationException {

		if (xmlHeaderProcessor == null)
			return null;

		String xmlHeaderProcessorName = xmlHeaderProcessor.getName();
		if (xmlHeaderProcessorName == null)
			throw new SpecificationException("Header Processor needs a name.");

		List<XMLProperty> propertyList = xmlHeaderProcessor.getPropertyList();

		IHeaderProcessor proc = fRunner
				.createNewHeaderProcessor(xmlHeaderProcessorName);
		if (propertyList != null)
			for (XMLProperty property : propertyList) {
				String xmlPropertyName = property.getName();
				String xmlPropertyData = property.getStringValue();
				if ((xmlPropertyName == null) || (xmlPropertyData == null))
					throw new SpecificationException(
							"Properties in Header Processor "
									+ xmlHeaderProcessorName
									+ " need both property name and value.");

				proc.setProperty(xmlPropertyName, xmlPropertyData);
			}
		return proc;
	}

	private SOAPOperationCallIdentifier getOperationCallIdentifier(
			Activity activity, QName service, String port, String operation,
			SOAPOperationDirectionIdentifier direction)
			throws SpecificationException {

		Partner partner = activity.getPartner();

		if (service == null)
			throw new SpecificationException(
					"Expected a service specification in activity "
							+ activity.getName()
							+ " (PartnerTrack for partner " + partner + ").");
		if (port == null)
			throw new SpecificationException(
					"Expected a port specification in activity "
							+ activity.getName()
							+ " (PartnerTrack for partner " + partner + ").");
		if (operation == null)
			throw new SpecificationException(
					"Expected a operation specification in activity "
							+ activity.getName()
							+ " (PartnerTrack for partner " + partner + ").");

		return partner.getOperation(service, port, operation, direction);
	}

	private QName getService(Activity parentActivity,
			XMLSoapActivity xmlActivity) throws SpecificationException {
		QName service = null;
		try {
			service = xmlActivity.getService();
		} catch (Exception e) {
		}
		if (service == null)
			throw new SpecificationException(
					"Could not find service for activity "
							+ parentActivity.getName()
							+ ": not specified or wrong prefix.");
		return service;
	}

	/**
	 * Get namespaces from send object (include all those specified higher up
	 * the chain). We could get these from the top-level element, if the only
	 * way of specifiying the test suite document were the tool support...
	 * 
	 */
	private NamespaceContextImpl getNamespaceMap(XmlCursor newCursor) {

		Map<String, String> namespaces = new HashMap<String, String>();
		newCursor.getAllNamespaces(namespaces);
		NamespaceContextImpl context = new NamespaceContextImpl();
		for (String prefix : namespaces.keySet()) {
			context.setNamespace(prefix, namespaces.get(prefix));
		}
		return context;
	}

	/**
	 * Finds a track with a non-empty partner track in the test case inheritance
	 * hierarchy
	 * 
	 */
	private XMLTestCase findInHierarchy(XMLTestCase xmlTestCase,
			String partnerTrackName) {

		XmlCursor cursor = xmlTestCase.newCursor();
		String basedOn = xmlTestCase.getBasedOn();
		if (cursor.toParent()) {
			XMLTestCasesSection section = (XMLTestCasesSection) cursor
					.getObject();
			for (XMLTestCase xmlTestCaseFor : section.getTestCaseList()) {
				if (basedOn.equals(xmlTestCaseFor.getName())) {
					if (hasNonEmptyPartnerTrack(xmlTestCaseFor,
							partnerTrackName)) {
						return xmlTestCaseFor;
					} else {
						if (xmlTestCaseFor.getBasedOn() != null
								&& !"".equals(xmlTestCaseFor.getBasedOn()))
							return findInHierarchy(xmlTestCaseFor,
									partnerTrackName);
					}
				}
			}
		}
		return null;
	}

	private boolean hasNonEmptyPartnerTrack(XMLTestCase xmlTestCase,
			String partnerTrackName) {

		XMLTrack track = getPartnerTrack(xmlTestCase, partnerTrackName);
		if (track != null)
			return !ActivityUtil.getActivities(track).isEmpty();

		return false;
	}

	private XMLTrack getPartnerTrack(XMLTestCase xmlTestCase, String trackName) {
		XMLTrack track = null;
		if (trackName.equalsIgnoreCase(BPELUnitConstants.CLIENT_NAME)) {
			track = xmlTestCase.getClientTrack();
		} else {
			for (XMLPartnerTrack pTrack : xmlTestCase.getPartnerTrackList()) {
				if (trackName.equals(pTrack.getName())) {
					track = pTrack;
					break;
				}
			}
		}
		return track;
	}

	private List<Integer> getRoundInformation(String roundsAsText) {
		List<Integer> list = new ArrayList<Integer>();
		if (roundsAsText != null && !"".equals(roundsAsText)) {
			String[] values = roundsAsText.split(",");
			for (int j = 0; j < values.length; j++) {
				try {
					list.add(Integer.parseInt(values[j].trim()));
				} catch (NumberFormatException e) {
					return null;
				}
			}
			return list;
		}
		return null;
	}

}
