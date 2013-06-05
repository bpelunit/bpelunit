/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.result;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.TestCase;
import net.bpelunit.framework.model.test.TestSuite;
import net.bpelunit.framework.model.test.activity.Activity;
import net.bpelunit.framework.model.test.data.DataCopyOperation;
import net.bpelunit.framework.model.test.data.DataSpecification;
import net.bpelunit.framework.model.test.data.ReceiveCondition;
import net.bpelunit.framework.model.test.data.XMLData;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import net.bpelunit.framework.xml.result.XMLActivity;
import net.bpelunit.framework.xml.result.XMLArtefact;
import net.bpelunit.framework.xml.result.XMLCopyOperation;
import net.bpelunit.framework.xml.result.XMLInfo;
import net.bpelunit.framework.xml.result.XMLPartnerTrack;
import net.bpelunit.framework.xml.result.XMLReceiveCondition;
import net.bpelunit.framework.xml.result.XMLTestCase;
import net.bpelunit.framework.xml.result.XMLTestResult;
import net.bpelunit.framework.xml.result.XMLTestResultDocument;
import net.bpelunit.framework.xml.result.XMLData.XmlData;
import net.bpelunit.framework.xml.result.XMLReceiveCondition.Condition;

/**
 * Producer for the resulting XML of a test run.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public final class XMLResultProducer {

	private XMLResultProducer() {
	}
	
	/**
	 * Writes a complete trace of a test suite to the output stream in form of an XML document encoded in UTF-8.
	 * 
	 * @param out stream to write to
	 * @param suite suite with the data to be written
	 * 
	 * @throws IOException
	 */
	public static void writeXML(OutputStream out, TestSuite suite) throws IOException {
                XMLTestResultDocument xmlTestResultDocument= getXMLResults(suite);

                XmlOptions opts= new XmlOptions();
                opts.setSavePrettyPrint();
                opts.setSavePrettyPrintIndent(4);
                opts.setCharacterEncoding("UTF-8");

                xmlTestResultDocument.save(out, opts);
        }

	private static void handleLowLevel(XMLActivity xmlActivity, ITestArtefact testArtefact) {

		if (testArtefact instanceof Activity) {
			// Recurse activities
			Activity activity= (Activity) testArtefact;

			XMLActivity xmlActivityNew= xmlActivity.addNewActivity();
			transferState(activity, xmlActivityNew);
			xmlActivityNew.setType(activity.getActivityCode());

			List<ITestArtefact> children= activity.getChildren();
			for (ITestArtefact innerArtefact : children) {
				handleLowLevel(xmlActivityNew, innerArtefact);
			}

		} else if (testArtefact instanceof DataSpecification) {
			// Reached the end - data spec
			DataSpecification dataSpec= (DataSpecification) testArtefact;
			net.bpelunit.framework.xml.result.XMLData xmlData= xmlActivity.addNewDataPackage();
			transferState(dataSpec, xmlData);

			// And may have children of type XMLData and ReceiveCondition

			List<ITestArtefact> children= dataSpec.getChildren();
			for (ITestArtefact artefact : children) {
				if (artefact instanceof XMLData) {
					XMLData data= (XMLData) artefact;
					XmlData xmlDataNew= xmlData.addNewXmlData();
					try {
						xmlDataNew.set(XmlObject.Factory.parse(data.getXmlData()));
					} catch (XmlException e) {
						// Should not happen. Usually indicates namespace problems.
						// Just create an empty data tag
					}
					xmlDataNew.setName(data.getName());
				} else if (artefact instanceof ReceiveCondition) {
					ReceiveCondition cond= (ReceiveCondition) artefact;
					XMLReceiveCondition xmlReceiveCondition= xmlData.addNewReceiveCondition();
					handleReceiveCondition(cond, xmlReceiveCondition);
				}
			}

		} else if (testArtefact instanceof DataCopyOperation) {
			// Activity may have child of type DataCopyOperation
			DataCopyOperation dataCopy= (DataCopyOperation) testArtefact;
			XMLCopyOperation xmlCopy= xmlActivity.addNewCopyOperation();
			xmlCopy.setFrom(dataCopy.getFromXPath());
			xmlCopy.setTo(dataCopy.getToXPath());
			xmlCopy.setCopyiedValue(dataCopy.getCopiedValue());
			transferState(dataCopy, xmlCopy);
		}

	}

	private static void handleReceiveCondition(ReceiveCondition cond, XMLReceiveCondition xmlReceiveCondition) {
		Condition condition= xmlReceiveCondition.addNewCondition();
		condition.setExpression(cond.getExpression());
		condition.setExpectedValue(cond.getExpectedValue());
		condition.setActualValue(cond.getActualValue());
		transferState(cond, xmlReceiveCondition);
	}

	private static void handleStateData(XMLInfo info, StateData stateData) {
		info.setName(stateData.getKey());
		info.setStringValue(stateData.getValue());
	}

	private static void transferState(ITestArtefact artefact, XMLArtefact result) {
		result.setName(artefact.getName());
		result.setResult(artefact.getStatus().getCode().toString());
		result.setMessage(artefact.getStatus().getMessage());
		String exceptionMessage= artefact.getStatus().getExceptionMessage();
		if (exceptionMessage != null) {
			result.setException(exceptionMessage);
		}

		List<StateData> stateData= artefact.getStateData();
		for (StateData data : stateData) {
			handleStateData(result.addNewState(), data);
		}
	}

        /**
         * Serializes the trace of a test suite to a tree of XMLBeans classes.
         *
         * @param suite Suite with the data to be written.
         */
        public static XMLTestResultDocument getXMLResults(TestSuite suite) {
                XMLTestResultDocument xmlTestResultDocument = XMLTestResultDocument.Factory.newInstance();
                XMLTestResult xmlTestResult= xmlTestResultDocument.addNewTestResult();
                transferState(suite, xmlTestResult);

                List<ITestArtefact> children= suite.getChildren();
                for (ITestArtefact artefact : children) {
                        TestCase testCase= (TestCase) artefact;
                        XMLTestCase xmlTestCase= xmlTestResult.addNewTestCase();
                        transferState(testCase, xmlTestCase);

                        List<PartnerTrack> partnerTracks= testCase.getPartnerTracks();
                        for (PartnerTrack track : partnerTracks) {
                                XMLPartnerTrack xmlPartnerTrack= xmlTestCase.addNewPartnerTrack();
                                transferState(track, xmlPartnerTrack);

                                List<ITestArtefact> trackChildren= track.getChildren();
                                // Track children may only be activites
                                for (ITestArtefact artefact2 : trackChildren) {
                                        Activity activity= (Activity) artefact2;

                                        XMLActivity xmlActivity= xmlPartnerTrack.addNewActivity();
                                        xmlActivity.setType(activity.getActivityCode());
                                        transferState(activity, xmlActivity);

                                        List<ITestArtefact> activityChildren= activity.getChildren();
                                        // Activity children may be other activites, dataPackages, or arbitrary data
                                        for (ITestArtefact artefact3 : activityChildren) {
                                                handleLowLevel(xmlActivity, artefact3);
                                        }
                                }
                        }
                }
                return xmlTestResultDocument;
        }
}
