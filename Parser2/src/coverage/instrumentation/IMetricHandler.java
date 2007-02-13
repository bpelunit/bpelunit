package coverage.instrumentation;
import java.io.File;
import java.io.IOException;

import org.jdom.JDOMException;


public interface IMetricHandler {

	public void addMetric(IMetric metric);
	
	public void remove(IMetric metric);
	
	public void startInstrumentation(File file) throws JDOMException, IOException;

}
