package coverage.loggingservice;


import org.bpelunit.framework.exception.SpecificationException;
import org.bpelunit.framework.model.Partner;

public class LoggingService extends Partner {

	public LoggingService(String name, String testBasePath, String wsdlName, String baseURL) throws SpecificationException {
		super( name, testBasePath, wsdlName, baseURL);
	}


}
