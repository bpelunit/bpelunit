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
 * @author Philip Mayer, Antonio Garcia-Dominguez
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

	/* Encapsulates the results from an HTTP request: status code and
	 * response body */
	private static class RequestResult {
		public int    statusCode;
		public String responseBody;
	}

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

		RequestEntity re;
		try {
			re = new BPRDeployRequestEntity(uploadingFile);
		} catch (IOException e) {
			throw new DeploymentException(
					"An input/output error occured in ActivBPEL deployer when deploying: "
							+ e.getMessage());
		} catch (SOAPException e) {
			throw new DeploymentException(
					"An error occurred while creating SOAP message for ActiveBPEL deployment: "
							+ e.getMessage());
		}

		fLogger
				.info("ActiveBPEL deployer about to send SOAP request to deploy "
						+ put);

		try {
			RequestResult result = sendRequestToActiveBPEL(fDeploymentAdminServiceURL, re);

			if (result.statusCode < 200 || result.statusCode > 299) {
				throw new DeploymentException(
						"ActiveBPEL Server reported a Deployment Error: "
								+ result.responseBody);
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

	/**
	 * @param re SOAP request entity to be sent to ActiveBPEL.
	 * @return Response from the ActiveBPEL administration service.
	 * @throws IOException
	 * @throws HttpException
	 */
	private static RequestResult sendRequestToActiveBPEL(
			final String url, RequestEntity re)
			throws IOException, HttpException {
		PostMethod method = null;
		try {
			HttpClient client = new HttpClient();
			method = new PostMethod(url);
			method.setRequestEntity(re);

			// Provide custom retry handler is necessary
			method.getParams().setParameter(
				HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(1, false));
			method.addRequestHeader("SOAPAction", "");
			client.executeMethod(method);

			// We need to read the response body right now: if it is called
			// after the connection is released, it will only return null
			RequestResult result = new RequestResult();
			result.statusCode    = method.getStatusCode();
			result.responseBody  = method.getResponseBodyAsString();

			return result;
		}  finally {
			if (method != null) {
			    method.releaseConnection();
			}
		}
	}

}
