package net.bpelunit.framework.coverage;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.control.deploy.IBPELProcess;
import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.coverage.instrumentation.AbstractInstrumenter;
import net.bpelunit.framework.coverage.instrumentation.MarkerToActivityConverter;
import net.bpelunit.framework.coverage.instrumentation.activity.ActivityCoverageInstrumenter;
import net.bpelunit.framework.coverage.instrumentation.branch.BranchCoverageInstrumenter;
import net.bpelunit.framework.coverage.output.AbstractCoverageOutputter;
import net.bpelunit.framework.coverage.output.csv.CsvCoverageOutputter;
import net.bpelunit.framework.coverage.service.MarkerService;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.execution.IBPELUnitContext;
import net.bpelunit.framework.execution.ITestLifeCycleElement;

public class CoverageLifeCycle implements ITestLifeCycleElement {

	private List<AbstractInstrumenter> coverageInstrumenters = new ArrayList<AbstractInstrumenter>();
	private List<AbstractCoverageOutputter> coverageOutputters = new ArrayList<AbstractCoverageOutputter>();
	private MarkerService markerService;

	public void doLoad(IBPELUnitContext context) {
		coverageInstrumenters.add(new ActivityCoverageInstrumenter());
		coverageInstrumenters.add(new BranchCoverageInstrumenter());
		
		coverageOutputters.add(new CsvCoverageOutputter());
	}

	public void doMarkProcesses(IBPELUnitContext context) throws DeploymentException {
		IDeployment deployment = context.getDeployment();
		for(AbstractInstrumenter i : coverageInstrumenters) {
			for(IBPELProcess p : deployment.getBPELProcesses()) {
				i.addCoverageMarkers(p.getProcessModel());
			}
		}
	}
	
	public void doPrepareProcesses(IBPELUnitContext context) throws DeploymentException {
		MarkerToActivityConverter markerConverter = new MarkerToActivityConverter();
		markerConverter.convertMarkersToActivities(context);
	}

	public void doRegisterMocks(IBPELUnitContext context) {
		markerService = new MarkerService(coverageInstrumenters);
		context.addService(CoverageConstants.COVERAGE_SERVICE_BPELUNIT_NAME, markerService);
	}
	
	public void doReport(IBPELUnitContext context) {
		/*for(AbstractCoverageOutputter o : coverageOutputters) {
			o.exportCoverageInformation(doc);
		}*/
	}

}
