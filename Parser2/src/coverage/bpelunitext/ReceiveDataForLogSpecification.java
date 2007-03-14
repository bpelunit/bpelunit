package coverage.bpelunitext;

import org.apache.log4j.Logger;
import org.bpelunit.framework.exception.HeaderProcessingException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.test.activity.Activity;
import org.bpelunit.framework.model.test.activity.ActivityContext;
import org.bpelunit.framework.model.test.data.ReceiveDataSpecification;

import coverage.instrumentation.metrics.MetricHandler;
import coverage.loggingservice.CoverageRegestry;

public class ReceiveDataForLogSpecification extends ReceiveDataSpecification {

	private Logger fLogger;

//	private boolean lastMessageReceived = false;

	public ReceiveDataForLogSpecification(Activity arg0)
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
		CoverageRegestry.getInstance().setCoverageStatusForAllMarker(fLiteralData.getTextContent(), "Testcase1");
		fLogger.info("----------------"+fLiteralData.getTextContent()+"-----------");

	}

//	public boolean isLastMessageReceived() {
//		return lastMessageReceived;
//	}

}
