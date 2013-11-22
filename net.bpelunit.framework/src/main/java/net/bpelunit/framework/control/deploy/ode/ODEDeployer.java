/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */

package net.bpelunit.framework.control.deploy.ode;

import java.io.File;
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
import net.bpelunit.util.ZipUtil;

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

/**
 * Deploys and undeploys processes to an Apache ODE server.
 *
 * @author Buddhika Chamith, Antonio Garcia-Dominguez
 */
@IBPELDeployerCapabilities(canDeploy = true, canMeasureTestCoverage = false)
public class ODEDeployer implements IBPELDeployer {

	private static final Logger LOGGER = Logger.getLogger(ODEDeployer.class);

	private String fProcessId;

	private String fArchive;

	private String fDeploymentAdminServiceURL;

	private ODERequestEntityFactory fFactory;

	public ODEDeployer() {
		fFactory = ODERequestEntityFactory.newInstance();
	}

	public String getDeploymentArchive() {
		return fArchive;
	}

	@IBPELDeployerOption(
			testSuiteSpecific=true,
			description="The (relative) path to the deployment archive that is to be deployed."
	)
	public void setDeploymentArchive(String archive) {
		this.fArchive = archive;
	}

	public String getODEDeploymentServiceURL() {
		return fDeploymentAdminServiceURL;
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
		LOGGER.info("ODE deployer got request to deploy " + put);

		check(fArchive, "Archive Location");
		check(fDeploymentAdminServiceURL, "deployment admin server URL");

		String archivePath = getArchiveLocation(pathToTest);
		if (!new File(archivePath).exists()) {
			throw new DeploymentException(String.format("The archive location '%s' does not exist", archivePath));
		}

		boolean archiveIsTemporary = false;
		if (!FilenameUtils.getName(archivePath).endsWith(".zip")) {
			// if the deployment is a directory and not a zip file
			if (new File(archivePath).isDirectory()) {
				archivePath = zipDirectory(new File(archivePath));

				// Separate zip file was created and should be later cleaned up
				archiveIsTemporary = true;
			} else {
				throw new DeploymentException(
						"Unknown archive format for the archive " + fArchive);
			}
		}
		java.io.File uploadingFile = new java.io.File(archivePath);

		// process the bundle for replacing wsdl eprs here. requires base url
		// string from specification loader.
		// should be done via the ODEDeploymentArchiveHandler. hard coded ode
		// process deployment string will be used
		// to construct the epr of the process wsdl. this requires the location
		// of put wsdl in order to obtain the
		// service name of the process in process wsdl. alternatively can use
		// inputs from deploymentsoptions.

		// test coverage logic. Moved to ProcessUnderTest deploy() method.

		HttpClient client = new HttpClient(new NoPersistenceConnectionManager());
		PostMethod method = new PostMethod(fDeploymentAdminServiceURL);
		RequestEntity re = fFactory.getDeployRequestEntity(uploadingFile);
		method.setRequestEntity(re);

		LOGGER
				.info("ODE deployer about to send SOAP request to deploy "
						+ put);

		// Provide custom retry handler if necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(1, false));
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

			if (uploadingFile.exists() && archiveIsTemporary) {
				uploadingFile.delete();
			}
		}

		if (isHttpErrorCode(statusCode)) {
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

	public void undeploy(String testPath, ProcessUnderTest put)
			throws DeploymentException {

		HttpClient client = new HttpClient(new NoPersistenceConnectionManager());
		PostMethod method = new PostMethod(fDeploymentAdminServiceURL);

		RequestEntity re = fFactory.getUndeployRequestEntity(fProcessId);
		method.setRequestEntity(re);

		LOGGER.info("ODE deployer about to send SOAP request to undeploy "
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

		if(isHttpErrorCode(statusCode)) {
			throw new DeploymentException(
					"ODE Server reported a undeployment Error: "
							+ responseBody);
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

	private String zipDirectory(File dir) throws DeploymentException {
		// Creates a zip file in the same location as the directory
		File zipFile = new File(dir.getAbsolutePath() + ".zip");
		
		try {
			ZipUtil.zipDirectory(dir, zipFile);
		} catch (IOException e) {
			throw new DeploymentException(
					"Could not pack directory " + dir + " into zip", e);
		}
		
		fArchive = zipFile.getAbsolutePath();
		
		return fArchive;
	}

	private boolean isHttpErrorCode(int statusCode) {
		return statusCode < 200 || statusCode > 299;
	}

	private String getArchiveLocation(String pathToTest) {
		String pathToArchive = FilenameUtils.concat(pathToTest, FilenameUtils.getFullPath(fArchive));
		String archiveName = FilenameUtils.getName(fArchive);
		return FilenameUtils.concat(pathToArchive, archiveName);
	}

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

}