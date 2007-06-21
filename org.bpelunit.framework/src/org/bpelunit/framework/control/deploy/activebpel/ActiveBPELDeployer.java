/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.deploy.activebpel;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.xml.soap.SOAPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.coverage.CoverageMeasurementTool;
import org.bpelunit.framework.coverage.receiver.CoverageMessageReceiver;
import org.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.model.ProcessUnderTest;

/**
 * ActiveBPEL Deployer - deploys a process to an ActiveBPEL server.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class ActiveBPELDeployer implements IBPELDeployer {

	// put config
	private static final String fsBPRFile = "BPRFile";

	// general config
	private static final String fsDeploymentDirectory = "ActiveBPELDeploymentDirectory";

	private static final String fsDeploymentServiceURL = "ActiveBPELDeploymentServiceURL";

	private Logger fLogger = Logger.getLogger(getClass());

	private Map<String, String> fDeploymentOptions;

	private String fResultingFile;

	private String fBPRFile;

	private String fDeploymentDirectory;

	private String fDeploymentAdminServiceURL;

	public void deploy(String pathToTest, ProcessUnderTest put) throws DeploymentException {
		fLogger.info("ActiveBPEL deployer got request to deploy " + put);

		fBPRFile = put.getDeploymentOption(fsBPRFile);
		fDeploymentDirectory = fDeploymentOptions.get(fsDeploymentDirectory);
		fDeploymentAdminServiceURL = fDeploymentOptions
				.get(fsDeploymentServiceURL);

		check(fBPRFile, "BPR File");
		check(fDeploymentDirectory, "deployment directory path");
		check(fDeploymentAdminServiceURL, "deployment admin server URL");

		String pathToArchive = FilenameUtils.concat(pathToTest, FilenameUtils
				.getFullPath(fBPRFile));
		fBPRFile = FilenameUtils.getName(fBPRFile);
		boolean fileReplaced = false;

		if (BPELUnitRunner.measureTestCoverage()) {
			CoverageMeasurementTool coverageTool = BPELUnitRunner
			.getCoverageMeasurmentTool();
			try {
				
				String newFile;
				newFile = coverageTool.prepareArchiveForCoverageMeasurement(
						pathToArchive, FilenameUtils.getName(fBPRFile), this);
				fBPRFile = newFile;
				fileReplaced = true;
			} catch (Exception e) {
				coverageTool.setErrorStatus("Coverage can not ... An error occurred when annotation for coverage: "
								+ e.getMessage());
				e.printStackTrace();
				// HIER
			}
		}
		File uploadingFile = new File(FilenameUtils.concat(pathToArchive,
				fBPRFile));

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
				//HIER
//				uploadingFile.delete();
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

	private void check(String toCheck, String description)
			throws DeploymentException {
		if (toCheck == null)
			throw new DeploymentException(
					"ActiveBPEL deployment configuration is missing the "
							+ description + ".");
	}

	public void setConfiguration(Map<String, String> options) {
		fDeploymentOptions = options;
	}

}
