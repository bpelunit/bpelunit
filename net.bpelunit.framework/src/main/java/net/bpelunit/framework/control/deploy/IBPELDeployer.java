/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.deploy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.bpelunit.framework.exception.DeploymentException;
import net.bpelunit.framework.model.ProcessUnderTest;

/**
 * The IBPELDeployer interface represents a BPEL Deployer, i.e. an entity which
 * is responsible for deploying a given BPEL process into an engine, and also
 * for undeploying it.
 * 
 * This interface is intended to be implemented by deployers for concrete engine
 * implementations. Please note that to be recognized by BPELUnit, new deployers
 * must be registered in the concrete BPELUnit runner instance (for example, in
 * the command line runner, the deployer must be added to the configuration.xml
 * file, which resides in the BPELUnit configuration directory).
 * 
 * For each deployed PUT, a new instance will be created. It is thus safe to
 * store undeployment data in the deployer instance.
 * 
 * Additionally, after each test case has been run, cleanUpAfterTestCase() will be called, in case
 * some cleanup needs to be done after every test.
 *
 * @version $Id$
 * @author Philip Mayer, Antonio García Domínguez (added cleanUpAfterTestCase)
 * 
 */
public interface IBPELDeployer {

	/**
	 * This annotation can be added to any deployer class to indicate the
	 * capabilities of the deployer that are not exposed via the interface
	 * 
	 * @author dluebke
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.TYPE)
	public @interface IBPELDeployerCapabilities {
		boolean canDeploy() default false;

		boolean canMeasureTestCoverage() default false;

		boolean canIntroduceMocks() default false;
	}

	/**
	 * This annotation must be used to identify configuration parameters by
	 * annotating the setters. The setters must be of the form
	 * <code>public void set${propertyName}(String value)</code>
	 * 
	 * @author dluebke
	 */
	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface IBPELDeployerOption {
		String defaultValue() default "";

		boolean testSuiteSpecific() default false;
		
		String description() default "";
	}

	/**
	 * Deploy the PUT. This method must block until the PUT is fully deployed
	 * and ready to accept incoming calls. In case of an error, a
	 * <code>DeploymentException</code> must be thrown.
	 * 
	 * @param archivePath
	 *            absolute path in the file system to the test 
	 * @param processUnderTest
	 *            the PUT
	 * @throws DeploymentException
	 */
	void deploy(String pathToTest, ProcessUnderTest processUnderTest)
			throws DeploymentException;

	/**
	 * Undeploy the PUT. This method may return when undeployment is triggered,
	 * however it is good practise to only return after the PUT has been fully
	 * undeployed. This method may be called by the framework even if deployment
	 * did not succeed.
	 * 
	 * In case of an error, a <code>DeploymentException must</code> be thrown.
	 * 
	 * @param testPath
	 *            path in the file system to the test files
	 * @param processUnderTest
	 *            the PUT
	 * @throws DeploymentException
	 */
	void undeploy(String testPath, ProcessUnderTest processUnderTest)
			throws DeploymentException;

	/**
	 * Performs engine-specific test cleanup after each test. This may or
	 * may not include killing any stale processes which may have end up in
	 * an infinite loop, among other things. Most implementations will
	 * probably do nothing here.
	 *
	 * Cleaning up after the test suite has ended doesn't require any new
	 * methods: adding the required logic to {@see #undeploy(String,
	 * ProcessUnderTest)} is enough.
	 *
	 * @throws Exception There was a problem while cleaning up after the
	 * test case.
	 */
	void cleanUpAfterTestCase() throws DeploymentException;

	/**
	 * Returns the deployment this deployer is configured for. Must only
	 * be called after all options are set.
	 * 
	 * @param processUnderTest
	 * @return
	 * @throws DeploymentException
	 */
	IDeployment getDeployment(ProcessUnderTest processUnderTest)
			throws DeploymentException;
}
