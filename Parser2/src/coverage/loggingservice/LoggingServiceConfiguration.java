package coverage.loggingservice;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FilenameUtils;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.soap.NamespaceContextImpl;
import org.bpelunit.framework.control.util.BPELUnitConstants;
import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.Partner;
import org.bpelunit.framework.model.ProcessUnderTest;
import org.bpelunit.framework.model.test.PartnerTrack;
import org.bpelunit.framework.model.test.TestCase;
import org.bpelunit.framework.model.test.TestSuite;
import org.bpelunit.framework.model.test.activity.ReceiveAsync;
import org.bpelunit.framework.model.test.data.ReceiveDataSpecification;
import org.bpelunit.framework.model.test.data.SOAPOperationCallIdentifier;
import org.bpelunit.framework.model.test.data.SOAPOperationDirectionIdentifier;
import org.bpelunit.framework.xml.suite.XMLDeploymentSection;
import org.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation;
import org.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
import org.bpelunit.framework.xml.suite.XMLProperty;
import org.bpelunit.framework.xml.suite.XMLTestCase;
import org.bpelunit.framework.xml.suite.XMLTestCasesSection;
import org.bpelunit.framework.xml.suite.XMLTestSuite;
import org.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.w3c.dom.Attr;
import org.w3c.dom.NodeList;

public class LoggingServiceConfiguration {

	private static final String LOGGING_SERVICE_CONFIG_ELEMENT = "loggingServiceConfiguration";

	private static final String WSDL_FILE_PROPERTY = "wsdl";

	private static final String LOGGING_SERVICE_PROPERTY = "service";

	private static final String LOGGING_OPERATION_PROPERTY = "operation";

	private static final String PARTNER_NAME_FOR_LOG_SERVICE_PROPERTY = "name";

	private static final String SIMULATED_URL_PROPERTY = "simulatedUrl";

	/**
	 * The URL which this partner simulates (base url plus partner name)
	 */
	private static String fSimulatedURL;

	/**
	 * The name of the partner, identifying it in the test suite document and in
	 * the URLs of the partner WSDL.
	 * 
	 */
	private static String fName;

	/**
	 * Path to the WSDL file (including file name) of this partner, relative to
	 * the test base path, which denotes the location of the .bpts file.
	 */
	private static String fPathToWSDL;

	private static String fServiceName;

	private static String fOparationName;

	public static void loadConfiguration(String directory, String configFile)
			throws ConfigurationException, SpecificationException {
		File file = new File(FilenameUtils.concat(directory, configFile));
		SAXBuilder builder = new SAXBuilder();
		try {
			Document config = builder.build(file);
			Element configElement = config.getRootElement();
			if (!configElement.equals(LOGGING_SERVICE_CONFIG_ELEMENT)) {
				throw new ConfigurationException(
						"An error occurred parsing the coverage-logging configuration from file"
								+ file.getAbsolutePath());
			}
			fName = configElement
					.getChildText(PARTNER_NAME_FOR_LOG_SERVICE_PROPERTY);
			fSimulatedURL = configElement.getChildText(SIMULATED_URL_PROPERTY);
			String wsdlName = configElement.getChildText(WSDL_FILE_PROPERTY);
			fServiceName = configElement.getChildText(LOGGING_SERVICE_PROPERTY);
			fOparationName = configElement
					.getChildText(LOGGING_OPERATION_PROPERTY);
			if (fName == null || fSimulatedURL == null || wsdlName == null
					|| fServiceName == null || fOparationName == null) {
				throw new ConfigurationException(
						"Configuration of coveragetool in file "
								+ file.getAbsolutePath() + " is not complete");
			}
//			fPathToWSDL = FilenameUtils.concat(directory, wsdlName);

			Partner p = new Partner(fName, directory, wsdlName, fSimulatedURL);
			PartnerTrack pTrack = new PartnerTrack(p);
			String xmlReceive="<xml-fragment service=\"rec:Receive\" port=\"ReceiveSOAP\" operation=\"NewOperation\" fault=\"false\" xmlns:tes=\"http://www.bpelunit.org/schema/testSuite\" xmlns:rec=\"http://www.example.org/Receive/\">" +
					"<tes:condition>" +
					"<tes:expression>//in</tes:expression>" +
					"<tes:value>'test'</tes:value>" +
					"</tes:condition>" +
					"</xml-fragment>";
			ReceiveAsync activity = new ReceiveAsync(pTrack);

		} catch (JDOMException e) {
			throw new ConfigurationException(
					"An XML reading error occurred reading the coverage-logging configuration from file "
							+ file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new ConfigurationException(
					"An I/O error occurred reading the coverage-logging configuration from file "
							+ file.getAbsolutePath(), e);
		}
	}

	public static String getName() {
		return fName;
	}

	public static String getPathToWSDL() {
		return fPathToWSDL;
	}

	public static String getSimulatedURL() {
		return fSimulatedURL;
	}

//	private TestSuite parseSuite(String testDirectory,
//			XMLTestSuiteDocument xmlTestSuiteDocument)
//			throws SpecificationException {
//
//		XMLTestSuite xmlTestSuite = xmlTestSuiteDocument.getTestSuite();
//		if (xmlTestSuite == null)
//			throw new SpecificationException(
//					"Could not find testSuite root element in the test suite XML file.");
//
//		// Name
//		String xmlSuiteName = xmlTestSuite.getName();
//		if (xmlSuiteName == null)
//			throw new SpecificationException(
//					"No name found for the test suite.");
//
//		URL suiteBaseURL;
//		try {
//			String xmlUrl = xmlTestSuite.getBaseURL();
//			if (xmlUrl == null)
//				xmlUrl = BPELUnitConstants.DEFAULT_BASE_URL;
//			suiteBaseURL = new URL(xmlUrl);
//		} catch (MalformedURLException e) {
//			throw new SpecificationException(
//					"Could not create a valid URL from specified base URL.", e);
//		}
//
//		// Load deployment information
//		XMLDeploymentSection xmlDeployment = xmlTestSuite.getDeployment();
//		if (xmlDeployment == null)
//			throw new SpecificationException(
//					"Could not find deployment section inside test suite document.");
//
//		// A map for the partners to re-identify them when reading
//		// PartnerTracks.
//		Map<String, Partner> suitePartners = new HashMap<String, Partner>();
//
//		/*
//		 * The Process Under Test and his Deployer.
//		 */
//
//		XMLPUTDeploymentInformation xmlPut = xmlDeployment.getPut();
//		if (xmlPut == null)
//			throw new SpecificationException(
//					"Expected a Process Under Test (PUT) in the test suite.");
//		String xmlPutName = xmlPut.getName();
//		String xmlPutWSDL = xmlPut.getWsdl();
//		String xmlPutType = xmlPut.getType();
//		List<XMLProperty> xmlPutDeploymentOptions = xmlPut.getPropertyList();
//
//		if ((xmlPutName == null) || (xmlPutWSDL == null)
//				|| (xmlPutType == null))
//			throw new SpecificationException(
//					"Process Under Test must have attributes name, type, wsdl, and a deployment section specified.");
//
//		ProcessUnderTest processUnderTest = new ProcessUnderTest(xmlPutName,
//				testDirectory, xmlPutWSDL, suiteBaseURL.toString());
//
//		for (XMLProperty property : xmlPutDeploymentOptions) {
//			processUnderTest.setXMLDeploymentOption(property.getName(),
//					property.getStringValue());
//		}
//
//		IBPELDeployer suitePutDeployer = fRunner.createNewDeployer(xmlPutType);
//		processUnderTest.setDeployer(suitePutDeployer);
//
//		// Add the put to the partners in case of running in test mode
//		suitePartners.put(processUnderTest.getName(), processUnderTest);
//
//		/*
//		 * The Client. Note that the client uses the PUT's WSDL. This is
//		 * intended as the clients activities will all deal with the partners
//		 * operations.
//		 */
//
//		Partner suiteClient = new Partner(BPELUnitConstants.CLIENT_NAME,
//				testDirectory, xmlPutWSDL, suiteBaseURL.toString());
//
//		/*
//		 * The Partners. Each partner is initialized with the attached WSDL
//		 * information, which allows retrieving operations from this partner
//		 * later on.
//		 */
//
//		List<XMLPartnerDeploymentInformation> xmlPartners = xmlDeployment
//				.getPartnerList();
//		for (XMLPartnerDeploymentInformation xmlPDI : xmlPartners) {
//			String name = xmlPDI.getName();
//			String wsdl = xmlPDI.getWsdl();
//			if ((name == null) || (wsdl == null))
//				throw new SpecificationException(
//						"Name and WSDL attributes of a partner must not be empty.");
//			Partner p = new Partner(xmlPDI.getName(), testDirectory, xmlPDI
//					.getWsdl(), suiteBaseURL.toString());
//			suitePartners.put(p.getName(), p);
//		}
//
//		// Create the suite.
//		TestSuite suite = new TestSuite(xmlSuiteName, suiteBaseURL,
//				processUnderTest);
//
//		/*
//		 * The Test Cases. Each test case consists of a number of Partner
//		 * Tracks, which in turn consist of an ordered collection of Activities.
//		 */
//
//		XMLTestCasesSection xmlTestCases = xmlTestSuite.getTestCases();
//		if (xmlTestCases == null)
//			throw new SpecificationException(
//					"No test case section found in test suite document.");
//
//		List<XMLTestCase> xmlTestCaseList = xmlTestCases.getTestCaseList();
//
//		if (xmlTestCaseList.size() == 0)
//			throw new SpecificationException("No test cases found.");
//
//		int currentNumber = 0;
//		for (XMLTestCase xmlTestCase : xmlTestCaseList) {
//
//			String xmlTestCaseName = xmlTestCase.getName();
//			if (xmlTestCaseName == null)
//				xmlTestCaseName = "Test Case " + currentNumber;
//			currentNumber++;
//
//			boolean isVary = xmlTestCase.getVary();
//			int rounds = 0;
//
//			if (isVary) {
//				/*
//				 * This is a varying test case. Each activity may have an
//				 * arbitrary number of delay times specified. These lists SHOULD
//				 * be of equal length, although no varying also also okay.
//				 * 
//				 * We use XPath to find all delay sequences and find the highest
//				 * size. This will be the size expected from all activities.
//				 * 
//				 */
//				XPath xpath = XPathFactory.newInstance().newXPath();
//				NamespaceContextImpl nsContext = new NamespaceContextImpl();
//				nsContext.setNamespace("ts",
//						BPELUnitConstants.BPELUNIT_TESTSUITE_NAMESPACE);
//				xpath.setNamespaceContext(nsContext);
//				try {
//					NodeList set = (NodeList) xpath.evaluate(
//							"//@delaySequence", xmlTestSuiteDocument
//									.getDomNode(), XPathConstants.NODESET);
//					int currentMax = 0;
//					for (int i = 0; i < set.getLength(); i++) {
//						if (set.item(i) instanceof Attr) {
//							Attr attr = (Attr) set.item(i);
//							List<Integer> ints = getRoundInformation(attr
//									.getValue());
//							if (ints != null)
//								currentMax = ints.size();
//						}
//					}
//					rounds = currentMax;
//				} catch (XPathExpressionException e) {
//					// This should not happen.
//					throw new SpecificationException(
//							"There was a problem finding delay sequences. This most likely indicates a bug in the framework.");
//				}
//			}
//			fLogger.info("Varying: " + isVary + " (Rounds: " + rounds + ")");
//
//			if (isVary && rounds > 0) {
//				for (int i = 0; i < rounds; i++) {
//					// Create a non-computer-science name ;)
//					int roundPlusOne = i + 1;
//					String variedName = xmlTestCaseName + " (Round "
//							+ roundPlusOne + ")";
//					if (!xmlTestCase.getAbstract()) {
//						TestCase test = createTestCase(suitePartners,
//								suiteClient, suite, xmlTestCase, variedName, i);
//						// HIER
//						// Partner loggingPartner = new LoggingService(
//						// LoggingServiceConfiguration.getName(), null,
//						// LoggingServiceConfiguration.getPathToWSDL(),
//						// LoggingServiceConfiguration.getSimulatedURL());
//						// PartnerTrack loggingTrack = new PartnerTrack(test,
//						// loggingPartner);
//						// List<Activity> activities = new
//						// ArrayList<Activity>();
//						// XMLReceiveActivity xmlReceive = (XMLReceiveActivity)
//						// event;
//						// ReceiveAsync activity = new
//						// ReceiveAsync(loggingTrack);
//						// ReceiveDataSpecification spec =
//						// createReceiveSpecificationStandalone(
//						// activity, xmlReceive,
//						// SOAPOperationDirectionIdentifier.INPUT);
//						// activity.initialize(spec);
//						//
//						// activities.add(activity);
//						// loggingTrack.setActivities(activities);
//						// test.addPartnerTrack(loggingTrack);
//						suite.addTestCase(test);
//					}
//				}
//			} else {
//				if (!xmlTestCase.getAbstract()) {
//					TestCase test = createTestCase(suitePartners, suiteClient,
//							suite, xmlTestCase, xmlTestCaseName, 0);
//					suite.addTestCase(test);
//				}
//			}
//		}
//
//		return suite;
//	}

}
