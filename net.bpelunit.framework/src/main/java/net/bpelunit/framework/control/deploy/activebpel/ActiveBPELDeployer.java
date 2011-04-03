/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.deploy.activebpel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.soap.SOAPException;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import net.bpelunit.framework.control.ext.IBPELDeployer;
import net.bpelunit.framework.control.ext.IDeployment;
import net.bpelunit.framework.control.ext.IBPELDeployer.IBPELDeployerCapabilities;
import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.model.ProcessUnderTest;

/**
 * ActiveBPEL Deployer - deploys a process to an ActiveBPEL server.
 * 
 * By default, the application server running ActiveBPEL is considered to be
 * listening at http://localhost:8080, and deployment archives are stored at
 * $CATALINA_HOME/bpr, where $CATALINA_HOME is an environment variable that
 * needs to be set to the home directory of the application server.
 * 
 * @author Philip Mayer, Antonio Garcia-Dominguez
 */
@IBPELDeployerCapabilities(canDeploy = true, canMeasureTestCoverage = true)
public class ActiveBPELDeployer implements IBPELDeployer {

	/*
	 * Encapsulates the results from an HTTP request: status code and response
	 * body
	 */
	private static class RequestResult {
		public int statusCode;
		public String responseBody;
	}

	// Strings which enclose the number of deployment errors in the summary
	private static final String ERRCOUNT_START = "&lt;deploymentSummary numErrors=&quot;";
	private static final String ERRCOUNT_END = "&quot";
	/* Default URLs for the deployment and administration web services */
	static final String DEFAULT_DEPLOYMENT_URL
		= "http://localhost:8080/active-bpel/services/DeployBPRService";
	static final String DEFAULT_ADMIN_URL
		= "http://localhost:8080/active-bpel/services/ActiveBpelAdmin";

	/*
	 * Name of the environment variable which will be used to build the
	 * deployment directory if no configuration has been specified.
	 */
	static final String DEFAULT_APPSERVER_DIR_ENVVAR = "CATALINA_HOME";

	private Logger fLogger = Logger.getLogger(getClass());

	private String fResultingFile;

	private File fBPRFile;

	private String fDeploymentDirectory;

	private String fDeploymentAdminServiceURL = DEFAULT_DEPLOYMENT_URL;
	private String fAdminServiceURL = DEFAULT_ADMIN_URL;

	private ProcessUnderTest put;

	/* for unit testing */
	static int _terminatedProcessCount = 0;

	@IBPELDeployerOption(testSuiteSpecific = false)
	public void setBPRFile(String bprFile) {
		this.fBPRFile = new File(bprFile);
	}

	@IBPELDeployerOption
	public void setDeploymentDirectory(String deploymentDirectory) {
		if (deploymentDirectory != null) {
			this.fDeploymentDirectory = deploymentDirectory;
		}
	}

	public String getDeploymentAdminServiceURL() {
		return fDeploymentAdminServiceURL;
	}

	@IBPELDeployerOption(defaultValue = DEFAULT_DEPLOYMENT_URL)
	public void setDeploymentAdminServiceURL(String deploymentAdminServiceURL) {
		if (deploymentAdminServiceURL != null) {
			this.fDeploymentAdminServiceURL = deploymentAdminServiceURL;
		}
	}

	public String getAdministrationServiceURL() {
		return fAdminServiceURL;
	}

	@IBPELDeployerOption(defaultValue = DEFAULT_ADMIN_URL)
	public void setAdministrationServiceURL(String adminServiceURL) {
		if (adminServiceURL != null) {
			this.fAdminServiceURL = adminServiceURL;
		}
	}

	public void deploy(String pathToTest, ProcessUnderTest put)
			throws DeploymentException {
		this.put = put;

		fLogger.info("ActiveBPEL deployer got request to deploy " + put);

		if (fDeploymentDirectory == null
				&& System.getenv(DEFAULT_APPSERVER_DIR_ENVVAR) != null) {
			fDeploymentDirectory = System.getenv(DEFAULT_APPSERVER_DIR_ENVVAR)
					+ File.separator + "bpr";
		}
		check(fBPRFile, "BPR File");
		check(fDeploymentDirectory, "deployment directory path");
		check(fDeploymentAdminServiceURL, "deployment admin server URL");
		check(fAdminServiceURL, "engine admin server URL");

		// changed the way the archive location is obtained.
		boolean fileReplaced = false;
		String archivePath = getArchiveLocation(pathToTest);

		File uploadingFile = new File(archivePath);

		if (!uploadingFile.exists())
			throw new DeploymentException(
					"ActiveBPEL deployer could not find BPR file " + fBPRFile);

		File resultingFile = new File(fDeploymentDirectory, fBPRFile.getName());

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

			if (result.statusCode < 200 || result.statusCode > 299 || errorsInSummary(result.responseBody)) {
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

		try {
			terminateAllRunningProcesses(deployable.getName());
		} catch (Exception e) {
			e.printStackTrace();
			throw new DeploymentException(e.getLocalizedMessage());
		}
	}

	public String getArchiveLocation(String pathToTest) {
		try {
			if (fBPRFile.isAbsolute()) {
				// absolute paths are left as is
				return fBPRFile.getCanonicalPath();
			} else {
				// relative paths are resolved from the directory of the .bpts
				return new File(pathToTest, fBPRFile.getName()).getCanonicalPath();
			}
		} catch (IOException e) {
			// if the path cannot be cleaned up, just turn it into an absolute path
			return fBPRFile.getAbsolutePath();
		}
	}

	public void setArchiveLocation(String archive) {
		setBPRFile(archive);
	}

	private void check(Object toCheck, String description)
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

	private void terminateAllRunningProcesses(String processName) throws Exception {
	    for (int pid : listRunningProcesses(processName)) {
	        terminateProcess(pid);
	    }
	}

	  private List<Integer> listRunningProcesses(String processName) throws Exception {
		try {
			ArrayList<Integer> vProcesses = new ArrayList<Integer>();
			RequestResult listResponse = sendRequestToActiveBPEL(
				fAdminServiceURL,
				new ProcessListRequestEntity(processName));

			if (listResponse.statusCode != 200) {
				throw new Exception(
					String.format(
						"Could not obtain the running process list: "
						+ "got status code %d\nResponse:\n%s",
						listResponse.statusCode,
						listResponse.responseBody));
			}

			// No need to perform XML parsing: we're only interested
			// in some simple elements
			Pattern patPID = Pattern.compile(
				"<[^>]*processId>\\s*([0-9]+)\\s*</[^>]+>");
			Matcher matcher = patPID.matcher(listResponse.responseBody);
			while (matcher.find()) {
				vProcesses.add(Integer.parseInt(matcher.group(1)));
			}

			return vProcesses;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
				"Could not obtain the running process list: "
				+ e.toString(), e);
		}
	}

	private void terminateProcess(int pid) throws Exception {
		try {
			++_terminatedProcessCount;
			RequestResult response = sendRequestToActiveBPEL(
				fAdminServiceURL,
				new TerminateProcessRequestEntity(pid));
			if (response.statusCode != 200) {
				throw new Exception(
					String.format(
						"Could not kill process #%d: "
						+ "non-OK status code %d\nResponse:\n%s",
						pid,
						response.statusCode,
						response.responseBody));
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(
				String.format(
					"Could not kill process #%d: %s",
					pid, e.toString()), e);
		}
	}

	private boolean errorsInSummary(String responseBody) {
		int startErrorCount = responseBody.indexOf(ERRCOUNT_START);
		if (startErrorCount == -1) return false;
		startErrorCount += ERRCOUNT_START.length();

		final int endErrorCount = responseBody.indexOf(ERRCOUNT_END, startErrorCount);
		if (endErrorCount == -1) return false;

		final String sErrorCount
			= responseBody.substring(startErrorCount, endErrorCount);
		final int errorCount = Integer.parseInt(sErrorCount);

		return errorCount > 0;
	}

    public void cleanUpAfterTestCase() throws Exception {
        terminateAllRunningProcesses(put.getName());
    }
}


