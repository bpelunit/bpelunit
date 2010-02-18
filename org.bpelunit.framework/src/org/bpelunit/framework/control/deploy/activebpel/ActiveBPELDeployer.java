/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.deploy.activebpel;

import java.io.File;
import java.io.IOException;

import javax.xml.soap.SOAPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.IDeployment;
import org.bpelunit.framework.control.ext.IBPELDeployer.IBPELDeployerCapabilities;
import org.bpelunit.framework.coverage.ICoverageMeasurementTool;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.model.ProcessUnderTest;

/**
 * ActiveBPEL Deployer - deploys a process to an ActiveBPEL server.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
@IBPELDeployerCapabilities(canDeploy = true, canMeasureTestCoverage = true)
public class ActiveBPELDeployer implements IBPELDeployer {

	// // put config
	// private static final String fsBPRFile = "BPRFile";
	//
	// // general config
	// private static final String fsDeploymentDirectory =
	// "ActiveBPELDeploymentDirectory";
	//
	// private static final String fsDeploymentServiceURL =
	// "ActiveBPELDeploymentServiceURL";

	private Logger fLogger = Logger.getLogger(getClass());

	private String fResultingFile;

	private String fBPRFile;

	private String fDeploymentDirectory;

	private String fDeploymentAdminServiceURL;

	@IBPELDeployerOption(testSuiteSpecific = false)
	public void setBPRFile(String bprFile) {
		this.fBPRFile = bprFile;
	}

	@IBPELDeployerOption
	public void setDeploymentDirectory(String deploymentDirectory) {
		this.fDeploymentDirectory = deploymentDirectory;
	}

	@IBPELDeployerOption(defaultValue = "http://localhost:8080/active-bpel/services/DeployBPRService")
	public void setDeploymentAdminServiceURL(String deploymentAdminServiceURL) {
		this.fDeploymentAdminServiceURL = deploymentAdminServiceURL;
	}

	public void deploy(String pathToTest, ProcessUnderTest put)
			throws DeploymentException {
		fLogger.info("ActiveBPEL deployer got request to deploy " + put);

		check(fBPRFile, "BPR File");
		check(fDeploymentDirectory, "deployment directory path");
		check(fDeploymentAdminServiceURL, "deployment admin server URL");

		// changed the way the archive location is obtained.
		/*
		 * String pathToArchive = FilenameUtils.concat(archivePath,
		 * FilenameUtils .getFullPath(fBPRFile)); fBPRFile =
		 * FilenameUtils.getName(fBPRFile);
		 */
		// fBPRFile = pathToTest;
		boolean fileReplaced = false;
		String archivePath = getArchiveLocation(pathToTest);

		// this has been moved to ProcessUnderTest deploy() method.
		/*
		 * if (BPELUnitRunner.measureTestCoverage()) { ICoverageMeasurementTool
		 * coverageTool = BPELUnitRunner .getCoverageMeasurmentTool(); try {
		 * 
		 * String newFile;
		 * 
		 * newFile = coverageTool.prepareArchiveForCoverageMeasurement(
		 * pathToArchive, FilenameUtils.getName(fBPRFile), this);
		 * 
		 * archivePath = coverageTool
		 * .prepareArchiveForCoverageMeasurement(pathToTest,
		 * getDeployment(put)); fBPRFile = archivePath; fileReplaced = true; }
		 * catch (Exception e) { coverageTool.setErrorStatus(
		 * "Coverage measurmetn is failed. An error occurred when annotation for coverage: "
		 * + e.getMessage()); // e.printStackTrace(); } }
		 */

		File uploadingFile = new File(archivePath);

		if (!uploadingFile.exists())
			throw new DeploymentException(
					"ActiveBPEL deployer could not find BPR file " + fBPRFile);

		File resultingFile = new File(fDeploymentDirectory, FilenameUtils
				.getName(fBPRFile));

		// Upload it.

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(fDeploymentAdminServiceURL);

		RequestEntity re;
		try {
			re = new ActiveBPELRequestEntity(uploadingFile);
		} catch (IOException e) {
			throw new DeploymentException(
					"An input/output error occured in ActivBPEL deployer when deploying: "
							+ e.getMessage());
		} catch (SOAPException e) {
			throw new DeploymentException(
					"An error occurred while creating SOAP message for ActiveBPEL deployment: "
							+ e.getMessage());
		}
		method.setRequestEntity(re);

		fLogger
				.info("ActiveBPEL deployer about to send SOAP request to deploy "
						+ put);

		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(1, false));
		method.addRequestHeader("SOAPAction", "");

		try {
			int statusCode = client.executeMethod(method);

			if (statusCode < 200 || statusCode > 299) {
				String responseBody = method.getResponseBodyAsString();
				throw new DeploymentException(
						"ActiveBPEL Server reported a Deployment Error: "
								+ responseBody);
			}

			// done.
			fResultingFile = resultingFile.toString();

		} catch (HttpException e) {
			throw new DeploymentException(
					"Problem contacting the ActiveBPEL Server: "
							+ e.getMessage(), e);
		} catch (IOException e) {
			throw new DeploymentException(
					"Problem contacting the ActiveBPEL Server: "
							+ e.getMessage(), e);
		} finally {
			method.releaseConnection();
			if (fileReplaced && uploadingFile.exists()) {
				uploadingFile.delete();
			}
		}

	}

	public void undeploy(String path, ProcessUnderTest deployable)
			throws DeploymentException {

		// undeploy may be called even if deploy was not successful
		if (fResultingFile == null)
			return;

		File bprFile = new File(fResultingFile);
		if (fResultingFile == null)
			throw new DeploymentException("Cannot undeploy BPR for Deployable "
					+ deployable + ": Metadata about file name not found.");
		if (!bprFile.exists())
			throw new DeploymentException("Cannot undeploy BPR for Deployable "
					+ deployable + ": File " + bprFile + " not found.");

		if (!bprFile.delete())
			throw new DeploymentException("Cannot undeploy BPR for Deployable "
					+ deployable + ": File " + bprFile
					+ " could not be deleted.");

	}

	// new method to get Archive Location.
	public String getArchiveLocation(String pathToTest) {
		String pathToArchive = FilenameUtils.concat(pathToTest, FilenameUtils
				.getFullPath(fBPRFile));
		String archiveName = FilenameUtils.getName(fBPRFile);
		return FilenameUtils.concat(pathToArchive, archiveName);
	}

	public void setArchiveLocation(String archive) {
		this.fBPRFile = archive;
	}

	private void check(String toCheck, String description)
			throws DeploymentException {
		if (toCheck == null)
			throw new DeploymentException(
					"ActiveBPEL deployment configuration is missing the "
							+ description + ".");
	}

	public IDeployment getDeployment(ProcessUnderTest processUnderTest)
			throws DeploymentException {
		// TODO Auto-generated method stub
		return null;
	}
}
