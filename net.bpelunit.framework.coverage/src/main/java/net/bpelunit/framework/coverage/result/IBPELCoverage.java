package net.bpelunit.framework.coverage.result;

import java.util.List;

import javax.xml.namespace.QName;

public interface IBPELCoverage {

	QName getProcessName();
	
	List<IMetricCoverage> getMetricCoverages();
	
	
}
