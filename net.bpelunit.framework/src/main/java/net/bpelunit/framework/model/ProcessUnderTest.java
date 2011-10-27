/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.control.ext.DeploymentOption;
import net.bpelunit.framework.control.ext.IBPELDeployer;
import net.bpelunit.framework.control.ext.IDeployment;
import net.bpelunit.framework.control.ext.PartnerLink;
import net.bpelunit.framework.control.util.ExtensionRegistry;
import net.bpelunit.framework.coverage.ArchiveUtil;
import net.bpelunit.framework.coverage.ICoverageMeasurementTool;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.exception.EndPointException;
import net.bpelunit.framework.exception.SpecificationException;

/**
 * The ProcessUnderTest is the internal representation of the BPEL process which
 * is tested in a particular test suite. The process is characterized by name, a
 * deployer, and deployment options.
 * 
 * The PUT is a subclass of Partner, as it is an extended version of a partner,
 * which is capable of being deployed outside of BPELUnit, whereas a partner is
 * always simulated by the framework.
 * 
 * Note that in test mode, the PUT is not deployed, but simulated by the
 * framework.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ProcessUnderTest extends Partner {

	/**
	 * The deployer which will be called to deploy this PUT.
	 */
	private IBPELDeployer fDeployer;

	/**
	 * The deployment options taken from the test suite which will be passed on
	 * to the deployer. May be null.
	 */
	private List<DeploymentOption> fXMLDeploymentOptions;

	/**
	 * True if the PUT is currently deployed.
	 */
	private boolean isDeployed;

	/**
	 * The global configuration options for the deployer that shall be
	 * considered when doing the deployment. inv: fGlobalConfiguration != null
	 */
	private Map<String, String> fGlobalConfiguration = new HashMap<String, String>();

	/**
	 * Map of partners of this process including the process itself.
	 */
	private Map<String, Partner> fPartners;

	/**
	 * Will be different from the simulated URL. In case of actual deployment to
	 * a process engine, this will be a process engine specific URL.
	 */
	private String fDeploymentURL;

	public ProcessUnderTest(String name, String testBasePath, String wsdlFile, String partnerWSDLFile, 
			String baseURL) throws SpecificationException {
		super(name, testBasePath, wsdlFile, partnerWSDLFile, baseURL);
		fXMLDeploymentOptions = new ArrayList<DeploymentOption>();
	}

	public void setDeployer(IBPELDeployer deployer) {
		fDeployer = deployer;
	}

	public void deploy() throws DeploymentException {
		// changing end point logic goes here.
		if (BPELUnitRunner.changeEndpoints()) {
			IDeployment deployment = fDeployer.getDeployment(this);
			PartnerLink[] partnerLinks = deployment.getPartnerLinks();
			Partner[] partners = fPartners.values().toArray(new Partner[] {});
			Map<Partner, PartnerLink> linkMap = getLinkMapping(partners,
					partnerLinks);

			try {
				for (Partner p : linkMap.keySet()) {
					PartnerLink pl = linkMap.get(p);
					deployment.replaceEndpoints(pl, p);
				}
			} catch (EndPointException e) {
				throw new DeploymentException("Error in changing endpoints.", e);
			}
		}

		boolean archiveCopied = false;
		String newFile = null;

		if (BPELUnitRunner.measureTestCoverage()) {
			ICoverageMeasurementTool coverageTool = BPELUnitRunner
					.getCoverageMeasurmentTool();
			try {

				/*
				 * newFile = coverageTool.prepareArchiveForCoverageMeasurement(
				 * pathToArchive, FilenameUtils.getName(fBPRFile), this);
				 */
				newFile = coverageTool.prepareArchiveForCoverageMeasurement(
						fDeployer.getArchiveLocation(getBasePath()), this,
						fDeployer);
				fDeployer.setArchiveLocation(newFile);
				archiveCopied = true;

			} catch (Exception e) {
				coverageTool
						.setErrorStatus("Coverage measurmetn is failed. An error occurred when annotation for coverage: "
								+ e.getMessage());
				// e.printStackTrace();
			}
		}

		fDeployer.deploy(getBasePath(), this);
		// if no exception was thrown, the service is deployed.

		if (archiveCopied) {
			ArchiveUtil.deleteArchive(newFile);
		}

		isDeployed = true;
	}

	private void configureDeployer() {
		// merge global options and test suite options
		// test suite options have precedence over global ones
		Map<String, String> options = new HashMap<String, String>();
		List<String> keys = ExtensionRegistry.getPossibleConfigurationOptions(
				fDeployer.getClass(), true);

		for (String key : keys) {
			String value = getDeploymentOption(key);
			if (value == null) {
				value = fGlobalConfiguration.get(key);
			}
			options.put(key, value);
		}

		ExtensionRegistry.configureDeployer(fDeployer, options);
	}

	public void undeploy() throws DeploymentException {
		fDeployer.undeploy(getBasePath(), this);
		// no exception -> undeploy was successful.
		isDeployed = false;
	}

	public boolean isDeployed() {
		return isDeployed;
	}

	public void setXMLDeploymentOption(String name, String stringValue) {
		fXMLDeploymentOptions.add(new DeploymentOption(name, stringValue));
	}

	public String getDeploymentOption(String key) {
		if (key == null) {
			return null;
		}

		for (DeploymentOption option : fXMLDeploymentOptions) {
			if (key.equals(option.getKey())) {
				return option.getValue();
			}
		}
		return null;
	}

	public IBPELDeployer getDeployer() {
		return fDeployer;
	}

	public void setGlobalConfiguration(Map<String, String> globalConfiguration) {
		if (globalConfiguration != null) {
			this.fGlobalConfiguration = globalConfiguration;
		} else {
			this.fGlobalConfiguration = new HashMap<String, String>();
		}
		configureDeployer();
	}

	public void setPartners(Map<String, Partner> partners) {
		this.fPartners = partners;
	}

	public Map<String, Partner> getPartners() {
		return this.fPartners;
	}

	public void setDeploymentURL(String url) {
		this.fDeploymentURL = url;
	}

	public String getDeploymentURL() {
		return this.fDeploymentURL;
	}

	/**
	 * This operation maps the Partner objects the corresponding PartnerLink
	 * object. Any algorithm can be used to achieve this according to the
	 * requirements. Currently Partner and PartnerLink are matched using their
	 * names.
	 * 
	 * @param partners
	 *            Array of Partner objects to be matched
	 * @param partnerLinks
	 *            Array of PartnerLink objects to be matched
	 * @return
	 */
	public Map<Partner, PartnerLink> getLinkMapping(Partner[] partners,
			PartnerLink[] partnerLinks) {
		Map<Partner, PartnerLink> linkMap = new HashMap<Partner, PartnerLink>();
		for (Partner p : partners) {
			for (PartnerLink pl : partnerLinks) {
				if (p.getName().equals(pl.getName())) {
					linkMap.put(p, pl);
					break;
				}
			}
		}

		return linkMap;
	}

	public void cleanUpAfterTestCase() throws Exception {
		fDeployer.cleanUpAfterTestCase();
	}
}
