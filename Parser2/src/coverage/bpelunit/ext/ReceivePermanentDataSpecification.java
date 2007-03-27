package coverage.bpelunit.ext;

import org.apache.log4j.Logger;
import org.bpelunit.framework.exception.HeaderProcessingException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.test.TestCase;
import org.bpelunit.framework.model.test.activity.Activity;
import org.bpelunit.framework.model.test.activity.ActivityContext;
import org.bpelunit.framework.model.test.data.ReceiveDataSpecification;

import coverage.instrumentation.metrics.IMetric;
import coverage.instrumentation.metrics.MetricHandler;
import coverage.wstools.CoverageRegistry;

public class ReceivePermanentDataSpecification extends ReceiveDataSpecification {

	private Logger fLogger;

	private boolean lastMessageReceived = false;

	private TestCase testCase;

	public ReceivePermanentDataSpecification(Activity arg0)
			throws SpecificationException {
		super(arg0);

		fLogger = Logger.getLogger(getClass());
	}

	@Override
	public void handle(ActivityContext context, String incomingMessage) {
		fLogger.info("----------------logMessage empfangen-----------");
		fLogger.info("----------------incommingMessage" + incomingMessage
				+ "-----------");
		// Check content
		setInWireFormat(incomingMessage);

		try {
			context.processHeaders(this);
		} catch (HeaderProcessingException e) {
		}

		decodeMessage();

		String content = fLiteralData.getTextContent();
		fLogger.info("----------------" + fLiteralData.getTextContent()
				+ "-----------");
		if (content.equals(MetricHandler.STOP_FLAG)) {
			lastMessageReceived=true;
		} else {
			CoverageRegistry covRegistry=CoverageRegistry.getInstance();
			if(content.startsWith(IMetric.DYNAMIC_COVERAGE_LABEL_IDENTIFIER)){
				covRegistry.addMarkerForEach(content);
			}else{
			covRegistry.setCoverageStatusForAllMarker(
					content, testCase.getName());
			}
		}


	}

	public boolean isLastMessageReceived() {
		return lastMessageReceived;
	}

	public void setTestCase(TestCase testCase) {
		this.testCase=testCase;
		
	}

}
