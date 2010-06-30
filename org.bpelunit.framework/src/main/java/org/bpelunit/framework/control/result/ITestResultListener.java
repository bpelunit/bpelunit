/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.result;

import org.bpelunit.framework.model.test.TestCase;
import org.bpelunit.framework.model.test.report.ITestArtefact;

/**
 * The test result listener interface is intended to be implemented by BPELUnit clients who wish to
 * be informed about the test progress. The result listener must be registered on the test suite and
 * will be informed of starting/stopping test cases as well as finer-grained progress in any of the
 * test artefacts (partner tracks and activities).
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public interface ITestResultListener {

	/**
	 * A test case started.
	 * 
	 * @param testCase the test case
	 */
	public void testCaseStarted(TestCase testCase);

	/**
	 * A test case ended.
	 * 
	 * @param testCase the test case
	 */
	public void testCaseEnded(TestCase testCase);

	/**
	 * There was progress in a test artefact.
	 * 
	 * @param testArtefact the test artefact
	 */
	public void progress(ITestArtefact testArtefact);

}
