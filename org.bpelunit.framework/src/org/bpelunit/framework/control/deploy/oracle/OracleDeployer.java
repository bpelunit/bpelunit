/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.deploy.oracle;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.IBPELDeployer.IBPELDeployerCapabilities;
import org.bpelunit.framework.coverage.ICoverageMeasurementTool;
import org.bpelunit.framework.exception.DeploymentException;
import org.bpelunit.framework.model.ProcessUnderTest;

/**
 * Oracle Deployer - deploys a process to an Oracle BPEL server.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
@IBPELDeployerCapabilities(canDeploy=true)
public class OracleDeployer implements IBPELDeployer {

	private static final String KEYWORD_UNDEPLOY = "undeploy";
	private static final String KEYWORD_DEPLOY = "deploy";

	private static final String BPELDEPLOY_SCRIPT_NAME = "bpeldeploy.bat";
	private static final String ORACLE_BIN_DIRECTORY = "bin/";

	// put config
//	private static final String fsBPELJARPath = "BPELJARFile";
//
//	// general config
//	private static final String fsOracleDirectory = "OracleDirectory";
//	private static final String fsOracleDomain = "OracleDomain";
//	private static final String fsOracleDomainPassword = "OracleDomainPassword";

	private Logger fLogger = Logger.getLogger(this.getClass());

	// private Map<String, String> fDeploymentOptions;
	private String fProcessName;
	private String fBinDir;
	private String fBPELFilePath;
	private String fScriptFilePath;
	private String fDomain;
	private String fPassword;
	private String fOracleDirectory;

	@IBPELDeployerOption(testSuiteSpecific=true)
	public void setBPELJARPath(String value) {
		this.fBPELFilePath = value;
	}

	@IBPELDeployerOption
	public void setOracleDirectory(String value) {
		this.fOracleDirectory = value;
	}
	
	@IBPELDeployerOption
	public void setOracleDomain(String value) {
		this.fDomain = value;
	}
	
	@IBPELDeployerOption
	public void setOracleDomainPassword(String value) {
		this.fPassword = value;
	}
	
	public void deploy(String path, ProcessUnderTest processUnderTest)
			throws DeploymentException {

		if (BPELUnitRunner.measureTestCoverage()) {
			ICoverageMeasurementTool tool = BPELUnitRunner
					.getCoverageMeasurmentTool();
			tool
					.setErrorStatus("Test coverage for Oracle Deployer is not implemented!");
		}

		fLogger.info("Oracle BPEL deployer got deploy request for PUT "
				+ processUnderTest);

		check(fBPELFilePath,
				"BPEL JAR file");
		check(fOracleDirectory, "Oracle Directory");

		if (!new File(fOracleDirectory).exists())
			throw new DeploymentException(
					"The specified Oracle Directory does not exist.");

		fProcessName = processUnderTest.getName();
		fBPELFilePath = FilenameUtils.concat(path, fBPELFilePath);
		fScriptFilePath = FilenameUtils.concat(fOracleDirectory, FilenameUtils.concat(
				ORACLE_BIN_DIRECTORY, BPELDEPLOY_SCRIPT_NAME));

		fBinDir = fOracleDirectory;
		if (fBinDir.endsWith("/") || fBinDir.endsWith("\\"))
			fBinDir = fBinDir.substring(0, fBinDir.length());
		fBinDir = FilenameUtils.separatorsToSystem(fBinDir);

		// XXX necessary? is checked before
		check(fBPELFilePath, "BPEL JAR file");

		if (!new File(fBPELFilePath).exists())
			throw new DeploymentException(
					"The referenced BPEL JAR file for Oracle deployment does not exist: "
							+ fBPELFilePath);

		check(fScriptFilePath, "deployment script file path");

		if (!new File(fScriptFilePath).exists())
			throw new DeploymentException(
					"The referenced deployment script file for Oracle deployment does not exist: "
							+ fScriptFilePath);

		check(fProcessName, "BPEL Process Name");

		check(fDomain, "BPEL server domain name");
		check(fPassword, "BPEL server domain password");

		String[] cmd = generateDeploy(fScriptFilePath, fProcessName,
				fBPELFilePath, fDomain, fPassword);
		runExternal(cmd);

	}

	public void undeploy(String path, ProcessUnderTest processUnderTest)
			throws DeploymentException {

		// May be called even if deploy was not successful
		if (fScriptFilePath != null && fProcessName != null && fDomain != null
				&& fPassword != null) {
			String[] cmd = generateUndeploy(fScriptFilePath, fProcessName,
					fDomain, fPassword);
			runExternal(cmd);
		}
	}

	// ********************* Internals ********************

	private void runExternal(String[] cmd) throws DeploymentException {
		try {
			Runtime rt = Runtime.getRuntime();
			Process proc;

			proc = rt.exec(cmd);
			StreamReader errorReader = new StreamReader(proc.getErrorStream());
			StreamReader outputReader = new StreamReader(proc.getInputStream());

			errorReader.start();
			outputReader.start();

			int exitVal = proc.waitFor();

			if (exitVal != 0) {
				// Error. Get message...
				String lastLine = errorReader.getAsString();
				String errorMessage;
				if (lastLine != null && !lastLine.equals(""))
					errorMessage = lastLine;
				else
					errorMessage = "An unknown error occurred while deploying.";

				throw new DeploymentException(errorMessage);
			}
		} catch (IOException e) {
			throw new DeploymentException("Error during deployment", e);
		} catch (InterruptedException e) {
			throw new DeploymentException("Error during deployment", e);
		}
	}

	private String[] generateDeploy(String script, String processName,
			String processUrl, String domain, String password) {
		String[] cmd = new String[7];
		cmd[0] = script;
		cmd[1] = fBinDir;
		cmd[2] = KEYWORD_DEPLOY;
		cmd[3] = processName;
		cmd[4] = processUrl;
		cmd[5] = domain;
		cmd[6] = password;
		return cmd;
	}

	private String[] generateUndeploy(String script, String processName,
			String domain, String password) {
		String[] cmd = new String[6];
		cmd[0] = script;
		cmd[1] = fBinDir;
		cmd[2] = KEYWORD_UNDEPLOY;
		cmd[3] = processName;
		cmd[4] = domain;
		cmd[5] = password;
		return cmd;
	}

	private void check(String toCheck, String description)
			throws DeploymentException {
		if (toCheck == null)
			throw new DeploymentException(
					"Oracle deployment configuration is missing the "
							+ description + ".");
	}
}
