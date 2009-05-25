/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */

package org.bpelunit.framework.control.deploy.ode;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import javax.xml.namespace.QName;
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
import org.bpelunit.framework.control.deploy.helpers.IDeployment;
import org.bpelunit.framework.control.deploy.helpers.ODEDeployment;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.IBPELDeployer.IBPELDeployerCapabilities;
import org.bpelunit.framework.coverage.ICoverageMeasurementTool;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.model.Partner;
import org.bpelunit.framework.model.ProcessUnderTest;
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

	private String fPackageId;

	private String fArchive;

	private String fDeploymentAdminServiceURL;

	private ODERequestEntityFactory fFactory;

	private ODEDeployment fDeployment;

	public ODEDeployer() {
		fFactory = ODERequestEntityFactory.newInstance();
	}

	@IBPELDeployerOption(testSuiteSpecific = true)
	public void setDeploymentArchive(String archive) {
		this.fArchive = archive;
	}

	@IBPELDeployerOption(defaultValue = "http://localhost:8080/ode/processes/DeploymentService")
	public void setODEDeploymentServiceURL(String deploymentAdminServiceURL) {
		this.fDeploymentAdminServiceURL = deploymentAdminServiceURL;
	}

	public void deploy(String pathToTest, ProcessUnderTest put)
			throws DeploymentException {
		fLogger.info("ODE deployer got request to deploy " + put);

		check(fArchive, "Archive Location");
		check(fDeploymentAdminServiceURL, "deployment admin server URL");

		boolean archiveCreated = false;

		String pathToArchive = FilenameUtils.concat(pathToTest, FilenameUtils
				.getFullPath(fArchive));

		if (!FilenameUtils.getName(fArchive).contains(".zip")) {
			// if the deployment is a directory not a zip file

			File dir = new File(FilenameUtils.concat(pathToArchive, fArchive));
			if (dir.isDirectory()) {
				// creates a zip file in the same location as directory

				File zipFile = new File(dir.getAbsolutePath() + ".zip");
				dir.copyAllTo(zipFile);
				fArchive = zipFile.getAbsolutePath();

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

		fArchive = FilenameUtils.getName(fArchive);
		boolean fileReplaced = false;

		// process the bundle for replacing wsdl eprs here. requires base url
		// string from specification loader.
		// should be done via the ODEDeploymentArchiveHandler. hard coded ode
		// process deployment string will be used
		// to construct the epr of the process wsdl. this requires the location
		// of put wsdl in order to obtain the
		// service name of the process in process wsdl. alternatively can use
		// inputs from deploymentsoptions.

		// test coverage logic. for now just raise an error

		if (BPELUnitRunner.measureTestCoverage()) {
			ICoverageMeasurementTool tool = BPELUnitRunner
					.getCoverageMeasurmentTool();
			tool
					.setErrorStatus("Test coverage for ODE Deployer is not implemented!");
		}

		File uploadingFile = new File(FilenameUtils.concat(pathToArchive,
				fArchive));

		if (!uploadingFile.exists())
			throw new DeploymentException(
					"ODE deployer could not find zip file " + fArchive);

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(fDeploymentAdminServiceURL);

		RequestEntity re;
		try {
			re = fFactory.getDeployRequestEntity(uploadingFile);
		} catch (IOException e) {
			throw new DeploymentException(
					"Problem while creating SOAP request: " + e.getMessage(), e);
		} catch (SOAPException e) {
			throw new DeploymentException(
					"Problem while creating SOAP request: " + e.getMessage(), e);
		}

		method.setRequestEntity(re);

		fLogger
				.info("ODE deployer about to send SOAP request to deploy "
						+ put);

		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(1, false));
		method.addRequestHeader("SOAPAction", "");

		String responseBody;
		try {
			int statusCode = client.executeMethod(method);
			responseBody = method.getResponseBodyAsString();

			if (statusCode < 200 || statusCode > 299) {
				throw new DeploymentException(
						"ODE Server reported a Deployment Error: "
								+ responseBody);
			}
		} catch (HttpException e) {
			throw new DeploymentException("Problem contacting the ODE Server: "
					+ e.getMessage(), e);
		} catch (IOException e) {
			throw new DeploymentException("Problem contacting the ODE Server: "
					+ e.getMessage(), e);
		} finally {
			method.releaseConnection();
			if (fileReplaced && uploadingFile.exists() || archiveCreated) {
				uploadingFile.deleteAll();
			}
		}

		try {
			fProcessId = extractProcessId(responseBody);
			fPackageId = extractPackageId(fProcessId);
		} catch (IOException e) {
			throw new DeploymentException(
					"Problem extracting deployment information: "
							+ e.getMessage(), e);
		}
	}

	public void undeploy(String testPath, ProcessUnderTest put)
			throws DeploymentException {

		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(fDeploymentAdminServiceURL);

		RequestEntity re;
		try {
			re = fFactory.getUndeployRequestEntity(fPackageId);
		} catch (IOException e) {
			throw new DeploymentException(
					"Problem while creating SOAP request: " + e.getMessage(), e);
		} catch (SOAPException e) {
			throw new DeploymentException(
					"Problem while creating SOAP request: " + e.getMessage(), e);
		}
		method.setRequestEntity(re);

		fLogger.info("ODE deployer about to send SOAP request to undeploy "
				+ put);

		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(1, false));
		method.addRequestHeader("SOAPAction", "");

		try {
			int statusCode = client.executeMethod(method);
			String responseBody = method.getResponseBodyAsString();

			if (statusCode < 200 || statusCode > 299) {

				throw new DeploymentException(
						"ODE Server reported a undeployment Error: "
								+ responseBody);
			}

		} catch (HttpException e) {
			throw new DeploymentException("Problem contacting the ODE Server: "
					+ e.getMessage(), e);
		} catch (IOException e) {
			throw new DeploymentException("Problem contacting the ODE Server: "
					+ e.getMessage(), e);
		} finally {
			method.releaseConnection();
		}
	}

	public IDeployment getDeployment(ProcessUnderTest put)
			throws DeploymentException {
		if (fDeployment == null) {
			Map<String, Partner> partnerList = put.getPartners();
			Partner[] partners = new Partner[partnerList.values().size()];
			partners = (Partner[]) (new ArrayList<Partner>(partnerList.values())
					.toArray(partners));

			if (partners != null && fArchive != null) {
				fDeployment = new ODEDeployment(partners, fArchive);
			} else {
				throw new DeploymentException(
						"Problem creating ODEDeployment: ", null);
			}
		}

		return this.fDeployment;
	}

	// *****Private helper methods*****

	private String extractProcessId(String responseBody) throws IOException {
		SAXBuilder builder = new SAXBuilder();
		String processId = null;

		try {
			Document doc = builder.build(new StringReader(responseBody));
			Element envelope = doc.getRootElement();

			Iterator<Element> it = envelope.getDescendants(new ElementFilter(
					"id"));
			Element idElement = it.next();

			String qnameStr = idElement.getText();
			QName qname = extractQName(qnameStr, idElement);
			processId = qname.getLocalPart();
		} catch (JDOMException e) {
			throw new IOException(e);
		}

		return processId;
	}

	private QName extractQName(String serviceName, Element idElement) {
		final int NS_URI = 0;
		final int LOCALNAME = 1;

		String tokens[];

		if (serviceName.contains(":")) {
			tokens = serviceName.split(":");

			if (isUri(tokens[NS_URI]) && !isPrefix(tokens[NS_URI], idElement)) {
				return new QName(tokens[NS_URI], tokens[LOCALNAME]);
			} else {
				String namespace = getPrefixValue(tokens[NS_URI], idElement);
				return new QName(namespace, tokens[LOCALNAME]);
			}
		}

		return new QName(null, serviceName);
	}

	private boolean isUri(String uriStr) {
		try {
			new URI(uriStr);
		} catch (URISyntaxException e) {
			return false;
		}

		return true;
	}
	
	private boolean isPrefix(String uriStr,Element service){
		
		if(service !=null && service.getNamespace(uriStr) != null){
			return true;
		}
		return false;
	}
	
	private String getPrefixValue(String prefix,Element service){

		if(service !=null && service.getNamespace(prefix) != null){
			return service.getNamespace(prefix).getURI();
		}
		return null;
	}

	private String extractPackageId(String processId) {
		String version = null;
		String packageName = null;

		if (processId.contains("-")) {
			version = processId.split("-")[1];
			// processId is in the form HelloBPELProcess-1. Extract out version.
		}

		if (fArchive.contains(".")) {
			packageName = fArchive.split("\\.")[0];
			// fBundle is in the form HelloBPEL.zip. Take out .zip part.
		}

		return packageName + "-" + version; // this is the deployed package id.
	}

	private void check(String toCheck, String description)
			throws DeploymentException {
		if (toCheck == null)
			throw new DeploymentException(
					"ODE deployment configuration is missing the "
							+ description + ".");
	}

}