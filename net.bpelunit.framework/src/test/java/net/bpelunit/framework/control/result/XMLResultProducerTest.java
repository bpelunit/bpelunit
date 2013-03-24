package net.bpelunit.framework.control.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.xmlbeans.XmlOptions;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.xml.result.XMLArtefact;
import net.bpelunit.framework.xml.result.XMLData;
import net.bpelunit.framework.xml.result.XMLInfo;
import net.bpelunit.framework.xml.result.XMLPartnerTrack;
import net.bpelunit.framework.xml.result.XMLReceiveCondition;
import net.bpelunit.framework.xml.result.XMLTestCase;
import net.bpelunit.framework.xml.result.XMLTestResult;
import net.bpelunit.framework.xml.result.XMLTestResultDocument;
import net.bpelunit.framework.xml.result.XMLData.XmlData;
import net.bpelunit.framework.xml.result.XMLPartnerTrack.Activity;
import net.bpelunit.framework.xml.result.XMLReceiveCondition.Condition;
import net.bpelunit.test.end2end.End2EndTester;
import net.bpelunit.test.util.StringOutputStream;
import net.bpelunit.test.util.TestTestRunner;

import org.junit.AfterClass;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Tests for ensuring that the results produced by XMLResultProducer are valid
 * and complete.
 * 
 * @author Antonio García-Domínguez
 */
public class XMLResultProducerTest {

	@AfterClass
	public static void shutdown() {
		MultiThreadedHttpConnectionManager.shutdownAll();
	}

	@Test
	public void testSendOnlyReceiveOnlyResultsAreValid() throws Exception {
		assertValid(getResults(End2EndTester.getSendOnlyReceiveOnlyRunner()));
	}

	@Test
	public void testSendReceiveSyncResultsAreValid() throws Exception {
		assertValid(getResults(End2EndTester.getSendReceiveSyncRunner()));
	}

	@Test
	public void testSendReceiveAsyncResultsAreValid() throws Exception {
		assertValid(getResults(End2EndTester.getSendReceiveAsyncRunner()));
	}

	@Test
	public void testInvalidAssumptionResultsAreValid() throws Exception {
		TestTestRunner runner
			= new TestTestRunner("src/test/resources/result/InvalidAssumption/",
					"WastePaperBasketTestSuite.bpts");
		assertValid(getResults(runner));
	}

	/**
	 * Test which performs several checks on the contents of an XML result file.
	 * It is not intended to be complete, but rather just ensure that the
	 * refactoring goes smoothly.
	 */
	@Test
	public void testSendOnlyReceiveOnlyResultsAreComplete() throws Exception {
		final String sPassed = ArtefactStatus.StatusCode.PASSED.toString();
		final Map<String, String> passedState = new HashMap<String, String>();
		passedState.put("Status Code", sPassed);
		passedState.put("Status Message", "Passed");

		// <testResult>
		XMLTestResultDocument resDoc
			= getResults(End2EndTester.getSendOnlyReceiveOnlyRunner());
		XMLTestResult res;
		assertNotNull(res = resDoc.getTestResult());
		assertState(res, sPassed, passedState);

		// <testCase>
		List<XMLTestCase> testCases = res.getTestCaseList();
		assertEquals("There should be results for 1 test case", 1, testCases.size());
		XMLTestCase testCase = res.getTestCaseArray(0);
		assertEquals("Test Case Throw something in!", testCase.getName());
		assertState(testCase, sPassed, passedState);

		// <partnerTrack>
		List<XMLPartnerTrack> partnerTracks = testCase.getPartnerTrackList();
		assertEquals("There should be 2 partner tracks", 2, partnerTracks.size());
		final String partnerTrackNames[] = new String[] {
				"Partner Track client", "Partner Track WastePaperBasket" };
		for (int iPartnerTrack = 0; iPartnerTrack < partnerTracks.size(); ++iPartnerTrack) {
			final XMLPartnerTrack partnerTrack = partnerTracks.get(iPartnerTrack);
			final String sPartnerTrackName = partnerTrackNames[iPartnerTrack];
			assertEquals(sPartnerTrackName, partnerTrack.getName());
			assertState(partnerTrack, sPassed, passedState);

			List<Activity> activities = partnerTrack.getActivityList();
			assertEquals("Partner track " + sPartnerTrackName
					+ " should have 1 activity", 1, activities.size());
			Activity activity = activities.get(0);
			assertState(activity, sPassed, passedState);

			List<XMLData> dataPackages = activity.getDataPackageList();
			assertEquals("Activity "
					+ activity.getName() + " should have 1 data package",
					1, dataPackages.size());
			XMLData dataPackage = dataPackages.get(0);

			if (iPartnerTrack == 0) {
				checkDataPackage(dataPackage, true,
						new String[]{"Literal XML data",
						"SOAP Message data",
						"Plain outgoing message"});

				assertEquals(
						"Send only activity should have no receive conditions",
						0,
						dataPackage.getReceiveConditionList().size());
			} else {
				checkDataPackage(dataPackage, false,
						new String[]{"Plain incoming message",
							"SOAP Message data",
							"Literal XML data"});

				// <receiveCondition>
				List<XMLReceiveCondition> receiveConds
					= dataPackage.getReceiveConditionList();
				assertEquals(
						"Receive only activity should only have 1 condition",
						1,
						receiveConds.size());

				XMLReceiveCondition receiveCond = receiveConds.get(0);
				final Map<String, String> condState
					= new HashMap<String, String>();
				final String expectedExpression
					= "wpb:WastePaperBasketUsage/wpb:WasteThrower/text()";
				final String expectedValue
					= "'Phil'";

				condState.put("Expression", expectedExpression);
				condState.put("Value", expectedValue);
				assertState(receiveCond, sPassed, passedState);
				assertState(receiveCond, null, condState);

				Condition cond = receiveCond.getCondition();
				assertNotNull(cond);
				assertEquals(expectedExpression, cond.getExpression());
				assertEquals(expectedValue, cond.getExpectedValue());
				assertEquals(null, cond.getActualValue());
			}
		}


	}

	private void checkDataPackage(XMLData dataPackage, boolean bIsSend,
			String[] expectedXmlDataNames) {
		final Map<String, String> directionState = new HashMap<String, String>();
		directionState.put("Style/Encoding", "document/literal");
		directionState.put("Direction", "INPUT");
		final Map<String, String> sendState = new HashMap<String, String>();
		sendState.put("Target URL", "http://localhost:7777/ws/WastePaperBasket");
		sendState.put("HTTP Action", "initiate");

		assertState(dataPackage, null, directionState);
		if (bIsSend)
			assertState(dataPackage, null, sendState);

		List<XmlData> xmlDataList = dataPackage.getXmlDataList();
		assertEquals("Data packages should have 3 <xmlData> elements",
				3, xmlDataList.size());
		for (int iData = 0; iData < xmlDataList.size(); ++iData) {
			final XmlData xmlData = xmlDataList.get(iData);
			assertEquals(expectedXmlDataNames[iData], xmlData.getName());
			Element firstElement = getFirstChildElement(xmlData.getDomNode());

			final String expectedName =
				((bIsSend && iData == 0) || (!bIsSend && iData == 2))
				? "literalData" : "SOAP-ENV:Envelope";
			assertEquals(expectedName, firstElement.getNodeName());
		}
	}

	private Element getFirstChildElement(Node domNode) {
		for (Node nChild = domNode.getFirstChild();
			nChild != null;
			nChild = nChild.getNextSibling())
		{
			if (nChild instanceof Element) {
				return (Element)nChild;
			}
		}
		return null;
	}

	private void assertState(XMLArtefact artefact,
			String expectedResult,
			Map<String, String> expectedStateData) {
		if (expectedResult != null) {
			assertEquals(artefact.toString(), expectedResult, artefact.getResult());
		}
		for (XMLInfo info : artefact.getStateList()) {
			final String key = info.getName();
			if (expectedStateData.get(key) != null) {
				assertEquals(expectedStateData.get(key), info.getStringValue());
				expectedStateData.remove(key);
			}
		}
		if (expectedStateData.size() != 0) {
			fail("Not all expected state values were found");
		}
	}

	private XMLTestResultDocument getResults(TestTestRunner runner) throws Exception {
		runner.testRun();
		StringOutputStream os = new StringOutputStream();
		XMLResultProducer.writeXML(os, runner.getTestSuite());
		return XMLTestResultDocument.Factory.parse(os.getString());
	}

	private void assertValid(XMLTestResultDocument res) {
		ArrayList<Object> errors = new ArrayList<Object>();
		XmlOptions options = new XmlOptions();
		options.setErrorListener(errors);

		if (!res.validate(options)) {
			StringWriter sW = new StringWriter();
			for (Object o : errors) {
				sW.append(o + "\n");
			}
			fail("Results should pass XML Schema validation: " + sW.toString()
					+ "\nXML source was:\n\n" + res.toString());
		}
	}
}
