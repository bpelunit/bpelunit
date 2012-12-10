/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */

package net.bpelunit.framework.control.deploy.ode;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import net.bpelunit.framework.control.deploy.IBPELDeployer;
import net.bpelunit.framework.control.deploy.IBPELDeployer.IBPELDeployerCapabilities;
import net.bpelunit.framework.control.deploy.IDeployment;
import net.bpelunit.framework.control.util.NoPersistenceConnectionManager;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.model.ProcessUnderTest;
import net.bpelunit.util.JDomUtil;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;

import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.File;

/**
 * ODE Deployer-deploys/undeploys a process to an ODE server.
 * 
 * @author Buddhika Chamith
 */

@IBPELDeployerCapabilities(canDeploy = true, canMeasureTestCoverage = false)
public class ODEDeployer implements IBPELDeployer {

	private Logger fLogger = Logger.getLogger(getClass());

	private String fProcessId;

	private String fArchive;

	private String fDeploymentAdminServiceURL;

	private ODERequestEntityFactory fFactory;

	public ODEDeployer() {
		fFactory = ODERequestEntityFactory.newInstance();
	}

	@IBPELDeployerOption(
			testSuiteSpecific=true,
			description="The (relative) path to the deployment archive that is to be deployed."
	)
	public void setDeploymentArchive(String archive) {
		this.fArchive = archive;
	}

	@IBPELDeployerOption(
			defaultValue="http://localhost:8080/ode/processes/DeploymentService",
			description="The endpoint of the Apache ODE Deployment Service."
	)
	public void setODEDeploymentServiceURL(String deploymentAdminServiceURL) {
		this.fDeploymentAdminServiceURL = deploymentAdminServiceURL;
	}

	public void deploy(String pathToTest, ProcessUnderTest put)
			throws DeploymentException {
		fLogger.info("ODE deployer got request to deploy " + put);

		check(fArchive, "Archive Location");
		check(fDeploymentAdminServiceURL, "deployment admin server URL");

		boolean archiveCreated = false;

		String archivePath = getArchiveLocation(pathToTest);

		if (!FilenameUtils.getName(archivePath).endsWith(".zip")) {
			// if the deployment is a directory not a zip file

			File dir = new File(archivePath);
			if (dir.isDirectory()) {
				// creates a zip file in the same location as directory

				File zipFile = new File(dir.getAbsolutePath() + ".zip");
				dir.copyAllTo(zipFile);
				archivePath = zipFile.getAbsolutePath();

				fArchive += ".zip";// Newly created zip archive.

				try {
					File.umount(true, true, true, true);
				} catch (ArchiveException e) {
					throw new DeploymentException(
							"Could not convert to zip deployment format", e);
				}

				archiveCreated = true; // Separate zip file was created
			} else {
				throw new DeploymentException(
						"Unknown archive format for the archive " + fArchive);
			}
		}

		// process the bundle for replacing wsdl eprs here. requires base url
		// string from specification loader.
		// should be done via the ODEDeploymentArchiveHandler. hard coded ode
		// process deployment string will be used
		// to construct the epr of the process wsdl. this requires the location
		// of put wsdl in order to obtain the
		// service name of the process in process wsdl. alternatively can use
		// inputs from deploymentsoptions.

		// test coverage logic. Moved to ProcessUnderTest deploy() method.

		java.io.File uploadingFile = new java.io.File(archivePath);

		if (!uploadingFile.exists()) {
			throw new DeploymentException(
					"ODE deployer could not find zip file " + fArchive);
		}

		HttpClient client = new HttpClient(new NoPersistenceConnectionManager());
		PostMethod method = new PostMethod(fDeploymentAdminServiceURL);

		RequestEntity re = fFactory.getDeployRequestEntity(uploadingFile);

		method.setRequestEntity(re);

		fLogger
				.info("ODE deployer about to send SOAP request to deploy "
						+ put);

		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(1, false));
		method.addRequestHeader("SOAPAction", "");

		String responseBody;
		int statusCode = 0;
		try {
			statusCode = client.executeMethod(method);
			responseBody = method.getResponseBodyAsString();
		} catch (Exception e) {
			throw new DeploymentException("Problem contacting the ODE Server: "
					+ e.getMessage(), e);
		} finally {
			method.releaseConnection();

			if (uploadingFile.exists() && archiveCreated) {
				uploadingFile.delete();
			}
		}
		
		if (isHttpOkayCode(statusCode)) {
			throw new DeploymentException(
					"ODE Server reported a Deployment Error: "
							+ responseBody);
		}

		try {
			fProcessId = extractProcessId(responseBody);
		} catch (IOException e) {
			throw new DeploymentException(
					"Problem extracting deployment information: "
							+ e.getMessage(), e);
		}
	}

	private boolean isHttpOkayCode(int statusCode) {
		return statusCode < 200 || statusCode > 299;
	}

	public void undeploy(String testPath, ProcessUnderTest put)
			throws DeploymentException {

		HttpClient client = new HttpClient(new NoPersistenceConnectionManager());
		PostMethod method = new PostMethod(fDeploymentAdminServiceURL);

		RequestEntity re = fFactory.getUndeployRequestEntity(fProcessId);
		method.setRequestEntity(re);

		fLogger.info("ODE deployer about to send SOAP request to undeploy "
				+ put);

		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(1, false));
		method.addRequestHeader("SOAPAction", "");

		int statusCode = 0;
		String responseBody = null;
		try {
			statusCode = client.executeMethod(method);
			responseBody = method.getResponseBodyAsString();
		} catch (Exception e) {
			throw new DeploymentException("Problem contacting the ODE Server: "
					+ e.getMessage(), e);
		} finally {
			method.releaseConnection();
		}
		
		if(isHttpOkayCode(statusCode)) {
			throw new DeploymentException(
					"ODE Server reported a undeployment Error: "
							+ responseBody);
		}
	}

	private String getArchiveLocation(String pathToTest) {
		String pathToArchive = FilenameUtils.concat(pathToTest, FilenameUtils
				.getFullPath(fArchive));
		String archiveName = FilenameUtils.getName(fArchive);
		return FilenameUtils.concat(pathToArchive, archiveName);
	}

	// *****Private helper methods*****

	private String extractProcessId(String responseBody) throws IOException {
		SAXBuilder builder = new SAXBuilder();
		String processId = null;

		try {
			Document doc = builder.build(new StringReader(responseBody));
			Element envelope = doc.getRootElement();

			Iterator<Element> it = JDomUtil.getDescendants(envelope,
					new ElementFilter("name"));
			Element idElement = it.next();

			processId = idElement.getTextNormalize();
		} catch (JDOMException e) {
			throw new IOException(e);
		}

		return processId;
	}

	private void check(String toCheck, String description)
			throws DeploymentException {
		if (toCheck == null) {
			throw new DeploymentException(
					"ODE deployment configuration is missing the "
							+ description + ".");
		}
	}

	@Override
	public void cleanUpAfterTestCase() throws DeploymentException {
		// do nothing.
	}

	@Override
	public IDeployment getDeployment(ProcessUnderTest processUnderTest)
			throws DeploymentException {
		// TODO Auto-generated method stub
		return null;
	}

}