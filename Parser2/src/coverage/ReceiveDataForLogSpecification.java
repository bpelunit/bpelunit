package coverage;

import org.apache.log4j.Logger;
import org.bpelunit.framework.exception.HeaderProcessingException;
import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.test.activity.Activity;
import org.bpelunit.framework.model.test.activity.ActivityContext;
import org.bpelunit.framework.model.test.data.ReceiveDataSpecification;

public class ReceiveDataForLogSpecification extends ReceiveDataSpecification {

	private Logger fLogger;

	public ReceiveDataForLogSpecification(Activity arg0) throws SpecificationException {
		super(arg0);
		// TODO Auto-generated constructor stub

		fLogger = Logger.getLogger(getClass());
	}

	@Override
	public void handle(ActivityContext context, String incomingMessage) {
		fLogger.info("----------------logMessage empfangen-----------");
		// Check content
		setInWireFormat(incomingMessage);

		if (hasProblems())
			return;

		try {
			context.processHeaders(this);
		} catch (HeaderProcessingException e) {
		}

		if (hasProblems())
			return;
		decodeMessage();

		if (hasProblems())
			return;

//		extractMappingData(context);

		if (hasProblems())
			return;

		fLogger.info("----------------"+fLiteralData+"-----------");

	}
	
	

}
