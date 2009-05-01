package org.bpelunit.framework.control.deploy.ode;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.coverage.ICoverageMeasurementTool;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.model.ProcessUnderTest;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.filter.ElementFilter;
import org.jdom.input.SAXBuilder;

import de.schlichtherle.io.ArchiveException;
import de.schlichtherle.io.File;

public class ODEDeployer implements IBPELDeployer {

	// put config
	private static final String fsBundle = "BundleLocation";
	
	// general config
	private static final String fsDeploymentServiceURL = "ODEDeploymentServiceURL";

	private Logger fLogger = Logger.getLogger(getClass());

	private Map<String, String> fDeploymentOptions;
	
	private String fProcessId;
	
	private String fPackageId;

	private String fBundle;

	private String fDeploymentAdminServiceURL;
	
	private ODERequestEntityFactory fFactory;
	
	public ODEDeployer(){
		fFactory=ODERequestEntityFactory.newInstance();
	}

	public void deploy(String pathToTest, ProcessUnderTest put)
			throws DeploymentException {
		fLogger.info("ODE deployer got request to deploy " + put);

		fBundle = put.getDeploymentOption(fsBundle);
		fDeploymentAdminServiceURL = fDeploymentOptions.get(fsDeploymentServiceURL);

		check(fBundle, "Bundle Location");
		check(fDeploymentAdminServiceURL, "deployment admin server URL");
		
		boolean archiveCreated = false;
		
		if(!FilenameUtils.getName(fBundle).contains(".zip")){// if the bundle is a directory not a zip file
			File dir=new File(fBundle);
			if(dir.isDirectory()){
				File zipFile=new File(dir.getAbsolutePath() + ".zip"); 
				dir.copyAllTo(zipFile); // creates a zip file in the same location as directory
				fBundle=zipFile.getAbsolutePath();
				
				try {
					File.umount(true, true, true, true);
				} catch (ArchiveException e) {
					throw new DeploymentException("Could not convert to zip deployment format", e);
				}
				
				archiveCreated = true; // Separate zip file was created
			}else{
				throw new DeploymentException("Unknown bundle format for the bundle "+ fBundle);
			}
		}
		
		String pathToArchive = FilenameUtils.concat(pathToTest, FilenameUtils
				.getFullPath(fBundle));
		fBundle = FilenameUtils.getName(fBundle);
		boolean fileReplaced = false;
		
		//process the bundle for replacing wsdl eprs here. requires base url string from specification loader.
		//should be done via the ODEDeploymentArchiveHandler. hard coded ode process deployment string will be used
		//to construct the epr of the process wsdl. this requires the location of put wsdl in order to obtain the 
		//service name of the process in process wsdl. alternatively can use inputs from deploymentsoptions.
		
		//test coverage logic. for now just raise an error
		
		if (BPELUnitRunner.measureTestCoverage()) {
			ICoverageMeasurementTool tool = BPELUnitRunner
					.getCoverageMeasurmentTool();
			tool
					.setErrorStatus("Test coverage for ODE Deployer is not implemented!");
		}
		
		File uploadingFile = new File(FilenameUtils.concat(pathToArchive,fBundle));

		if (!uploadingFile.exists())
			throw new DeploymentException(
					"ODE deployer could not find zip file " + fBundle);
		
		HttpClient client = new HttpClient();
		PostMethod method = new PostMethod(fDeploymentAdminServiceURL);

		RequestEntity re;
		try {
			re = fFactory.getDeployRequestEntity(uploadingFile);
		} catch (IOException e) {
			throw new DeploymentException(
					"Problem while creating SOAP request: "
							+ e.getMessage(), e);
		} catch (SOAPException e) {
			throw new DeploymentException(
					"Problem while creating SOAP request: "
							+ e.getMessage(), e);
		}
		
		method.setRequestEntity(re);

		fLogger.info("ODE deployer about to send SOAP request to deploy "+ put);
		
		// Provide custom retry handler is necessary
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(1, false));
		method.addRequestHeader("SOAPAction", "");

		String responseBody;
		try {
			int statusCode = client.executeMethod(method);
			responseBody=method.getResponseBodyAsString();
			
			if (statusCode < 200 || statusCode > 299) {
				throw new DeploymentException(
						"ODE Server reported a Deployment Error: "
								+ responseBody);
			}
		} catch (HttpException e) {
			throw new DeploymentException(
					"Problem contacting the ODE Server: "
							+ e.getMessage(), e);
		} catch (IOException e) {
			throw new DeploymentException(
					"Problem contacting the ODE Server: "
							+ e.getMessage(), e);
		} finally {
			method.releaseConnection();
			if (fileReplaced && uploadingFile.exists() || archiveCreated) {
				uploadingFile.deleteAll();
			}
		}

		try{
			fProcessId=extractProcessId(responseBody);
			fPackageId=extractPackageId(fProcessId);
		}catch(IOException e){
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
					"Problem while creating SOAP request: "
							+ e.getMessage(), e);
		} catch (SOAPException e) {
			throw new DeploymentException(
					"Problem while creating SOAP request: "
							+ e.getMessage(), e);
		}
		method.setRequestEntity(re);

		fLogger
				.info("ODE deployer about to send SOAP request to undeploy "
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
			throw new DeploymentException(
					"Problem contacting the ODE Server: "
							+ e.getMessage(), e);
		} catch (IOException e) {
			throw new DeploymentException(
					"Problem contacting the ODE Server: "
							+ e.getMessage(), e);
		} finally {
			method.releaseConnection();
		}
	}
	
	public void setConfiguration(Map<String, String> options) {
		fDeploymentOptions = options;	
	}
	
	//*****Private helper methods*****
	
	private String extractProcessId(String responseBody) throws IOException{
		SAXBuilder builder=new SAXBuilder();
		String processId=null;
		
		try{
			Document doc = builder.build(new StringReader(responseBody));
			Element envelope=doc.getRootElement();
			
			Iterator<Element> it=envelope.getDescendants(new ElementFilter("id"));
			Element idElement=it.next();
			
			String qnameStr=idElement.getText();
			QName qname=extractQName(qnameStr,idElement);
			processId=qname.getLocalPart();
		}catch(JDOMException e){
			throw new IOException(e);
		}
		
		return processId;
	}

	private QName extractQName(String qnameStr,Element idElement){
		final int NAMESPACE=0;
		final int LOCAL_PART=1;
		
		if(qnameStr.contains(":")){
			String[] tokens=qnameStr.split(":");
			
			if(isUri(tokens[NAMESPACE])){ //if the namespace uri is directly used in text node qname value
				return new QName(tokens[NAMESPACE],tokens[LOCAL_PART]);
			}else{ // if a namespace prefix is used in text node qname value
				//prefix may be defined in id element
				String uriStr=idElement.getNamespace(tokens[NAMESPACE]).getURI();
				
				if(isUri(uriStr)){
					return new QName(uriStr,tokens[LOCAL_PART]);
				}
			}
		}
		
		return new QName(null,qnameStr); //just the local part is present
	}
	
	private boolean isUri(String uriStr){
		try {
			URI uri=new URI(uriStr);
		} catch (URISyntaxException e) {
			return false;
		} 
		
		return true;
	}
	
	private String extractPackageId(String processId){
		String version=null;
		String packageName=null;
		
		if(processId.contains("-")){
			version=processId.split("-")[1];//processId is in the form HelloBPELProcess-1. Extract out version id.	
		}
		
		if(fBundle.contains(".")){
			packageName=fBundle.split("\\.")[0]; //fBundle is in the form HelloBPEL.zip. Take out .zip part.
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

	public String[] getConfigurationParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getDefaultValueForParameter(String parameter) {
		// TODO Auto-generated method stub
		return null;
	}

}