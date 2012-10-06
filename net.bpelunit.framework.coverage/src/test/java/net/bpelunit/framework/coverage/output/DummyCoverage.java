package net.bpelunit.framework.coverage.output;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import net.bpelunit.framework.coverage.result.IBPELCoverage;
import net.bpelunit.framework.coverage.result.IMetricCoverage;

public class DummyCoverage implements IBPELCoverage {

	public static final DummyCoverage createDummyCoverage(String bptsName) {
		DummyCoverage dc = new DummyCoverage(bptsName);
		
		for(int i = 1; i <= 2; i++) {
			DummyMetricCoverage dmc = new DummyMetricCoverage("Metric" + i);
			dc.getMetricCoverages().add(dmc);
			
			for(int j = 1; j <= 10; j++) {
				DummyCoverageResult dcr = new DummyCoverageResult("//A[" + j + "]", j, 0.0, (double)j, (double)j / 2.0, 1.0);
				dmc.getCoverageResult().add(dcr);
			}
		}
		
		return dc;
	}
	
	private List<IMetricCoverage> metricCoverages = new ArrayList<IMetricCoverage>();
	private QName processName;

	public DummyCoverage(String processLocalName) {
		this.processName = new QName("http://www.example.org", processLocalName);
	}
	
	public QName getProcessName() {
		return this.processName;
	}

	public List<IMetricCoverage> getMetricCoverages() {
		return this.metricCoverages;
	}

}
