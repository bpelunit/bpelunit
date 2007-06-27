/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.client.eclipse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.client.eclipse.preferences.PreferenceConstants;
import org.bpelunit.framework.client.model.DeployerExtension;
import org.bpelunit.framework.client.model.ExtensionUtil;
import org.bpelunit.framework.client.model.HeaderProcessorExtension;
import org.bpelunit.framework.client.model.SOAPEncoderExtension;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.IHeaderProcessor;
import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.control.run.TestCaseRunner;
import org.bpelunit.framework.coverage.CoverageMeasurementTool;
import org.bpelunit.framework.coverage.ICoverageMeasurmentTool;
import org.bpelunit.framework.coverage.annotation.metrics.activitycoverage.ActivityMetric;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetric;
import org.bpelunit.framework.coverage.annotation.metrics.chcoverage.CompensationMetric;
import org.bpelunit.framework.coverage.annotation.metrics.fhcoverage.FaultMetric;
import org.bpelunit.framework.coverage.annotation.metrics.linkcoverage.LinkMetric;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BasicActivities;
import org.bpelunit.framework.coverage.receiver.CoverageMessageReceiver;
import org.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.bpelunit.framework.exception.ConfigurationException;
import org.bpelunit.framework.exception.SpecificationException;
import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Implementation of the BPELUnitRunner for use in Eclipse. Uses an Eclipse-based implementation of
 * extension loading and preference storage.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class EclipseBPELUnitRunner extends BPELUnitRunner {

	public EclipseBPELUnitRunner() {
		super();
	}

	@Override
	public void configureDeployers() throws ConfigurationException {
		// done by Eclipse

	}

	@Override
	public void configureExtensions() throws ConfigurationException {
		ExtensionControl.initialize();
	}

	@Override
	public void configureInit() throws ConfigurationException {
		// done by Eclipse

	}

	@Override
	public void configureLogging() {
		Logger rootLogger= Logger.getRootLogger();
		rootLogger.removeAllAppenders();

	}

	public void configureLogging(Level level, Appender appender) {
		Logger rootLogger= Logger.getRootLogger();
		rootLogger.removeAllAppenders();
		rootLogger.addAppender(appender);
		rootLogger.setLevel(level);
	}

	@Override
	public IBPELDeployer createNewDeployer(String type) throws SpecificationException {
		DeployerExtension deployer= ExtensionControl.findDeployerExtension(type);
		if (deployer != null) {
			IBPELDeployer newDeployer= deployer.createNew();
			IPreferenceStore ps= BPELUnitActivator.getDefault().getPreferenceStore();
			newDeployer.setConfiguration(ExtensionUtil.deserializeMap(ps.getString(type)));
			return newDeployer;
		} else
			throw new SpecificationException("Could not find a deployer for type " + type);
	}

	@Override
	public IHeaderProcessor createNewHeaderProcessor(String name) throws SpecificationException {
		HeaderProcessorExtension hproc= ExtensionControl.findHeaderProcessorExtension(name);
		if (hproc != null) {
			return hproc.createNew();
		} else
			throw new SpecificationException("Could not find a header processor for type " + name);
	}

	@Override
	public ISOAPEncoder createNewSOAPEncoder(String styleEncoding) throws SpecificationException {
		SOAPEncoderExtension soapEnc= ExtensionControl.findSOAPEncoderExtension(styleEncoding);
		if (soapEnc != null) {
			return soapEnc.createNew();
		} else
			throw new SpecificationException("Could not find a SOAP Encoder for type " + styleEncoding);
	}

	@Override
	public void configureCoverageTool() throws ConfigurationException {
		// TODO Auto-generated method stub
		
	}

	public void configureCoverageTool(BPELUnitActivator plugin) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<String> list = new ArrayList<String>();
		IPreferenceStore preference = plugin.getPreferenceStore();
		if (preference.getBoolean(BasicActivities.RECEIVE_ACTIVITY)) {
			list.add(BasicActivities.RECEIVE_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.REPLY_ACTIVITY)) {
			list.add(BasicActivities.REPLY_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.INVOKE_ACTIVITY)) {
			list.add(BasicActivities.INVOKE_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.ASSIGN_ACTIVITY)) {
			list.add(BasicActivities.ASSIGN_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.THROW_ACTIVITY)) {
			list.add(BasicActivities.THROW_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.EXIT_ACTIVITY)) {
			list.add(BasicActivities.EXIT_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.WAIT_ACTIVITY)) {
			list.add(BasicActivities.WAIT_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.EMPTY_ACTIVITY)) {
			list.add(BasicActivities.EMPTY_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.COMPENSATE_ACTIVITY)) {
			list.add(BasicActivities.COMPENSATE_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.COMPENSATESCOPE_ACTIVITY)) {
			list.add(BasicActivities.COMPENSATESCOPE_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.RETHROW_ACTIVITY)) {
			list.add(BasicActivities.RETHROW_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.VALIDATE_ACTIVITY)) {
			list.add(BasicActivities.VALIDATE_ACTIVITY);
		}
		if (preference.getBoolean(BasicActivities.TERMINATE_ACTIVITY)) {
			list.add(BasicActivities.TERMINATE_ACTIVITY);
		}
		if (list.size() > 0) {
			map.put(ActivityMetric.METRIC_NAME, list);
		}
		if (preference.getBoolean(BranchMetric.METRIC_NAME)) {
			map.put(BranchMetric.METRIC_NAME, null);
		}
		if (preference.getBoolean(LinkMetric.METRIC_NAME)) {
			map.put(LinkMetric.METRIC_NAME, null);
		}
		if (preference.getBoolean(FaultMetric.METRIC_NAME)) {
			map.put(FaultMetric.METRIC_NAME, null);
		}
		if (preference.getBoolean(CompensationMetric.METRIC_NAME)) {
			map.put(CompensationMetric.METRIC_NAME, null);
		}
		
		TestCaseRunner.wait_time_for_coverage_markers=preference.getInt(PreferenceConstants.P_COVERAGE_WAIT_TIME);

		ICoverageMeasurmentTool coverageTool=new CoverageMeasurementTool();
		BPELUnitRunner.setCoverageMeasurmentTool(coverageTool);
		try {
		coverageTool.configureMetrics(map);
		} catch (ConfigurationException e) {
		}

	}

}
