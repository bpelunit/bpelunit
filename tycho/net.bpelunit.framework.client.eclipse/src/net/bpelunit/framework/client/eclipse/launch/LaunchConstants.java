/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.launch;

import java.util.Collections;
import java.util.List;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;

/**
 * Some constants for the launch.
 * 
 * @version $Id$
 * @author Philip Mayer, Daniel Luebke
 * 
 */
public class LaunchConstants {

	/**
	 * Launch configuration attribute key. The value is a name of a project associated with a launch
	 * configuration.
	 */
	public static final String ATTR_PROJECT_NAME= BPELUnitActivator.getUniqueIdentifier() + ".PROJECT_ATTR"; //$NON-NLS-1$

	/**
	 * Launch configuration attribute key. The value is a project-relative path of a test suite file
	 * to launch.
	 */
	public static final String ATTR_SUITE_FILE_NAME= BPELUnitActivator.getUniqueIdentifier() + ".SUITE_NAME_ATTR"; //$NON-NLS-1$
	
	/**
	 * Launch configuration attribute key. The value is a project-relative path of a test suite file
	 * to launch.
	 */
	public static final String ATTR_TEST_CASES_NAMES= BPELUnitActivator.getUniqueIdentifier() + ".TEST_CASES_NAMES"; //$NON-NLS-1$

	/**
	 * Launch config type as defined in the plugin.xml file
	 */
	public static final String ID_LAUNCH_CONFIG_TYPE= "net.bpelunit.framework.client.eclipse.testSuiteLauncher";

	/**
	 * The empty string for fetching launch config string attributes
	 */
	public static final String EMPTY_STRING= ""; //$NON-NLS-1$

	/**
	 * An empty list for fetching launch config list attributes
	 */
	public static final List<?> EMPTY_LIST = Collections.EMPTY_LIST;

	/**
	 * Launch configuration attribute key. The value is a boolean indicating whether BPELUnit should halt after an error occurred.
	 */
	public static final String ATTR_HALT_ON_ERROR = BPELUnitActivator.getUniqueIdentifier() + ".HALT_ON_ERROR"; //$NON-NLS-1$
	
	/**
	 * Launch configuration attribute key. The value is a boolean indicating whether BPELUnit should halt after a failure occurred.
	 */
	public static final String ATTR_HALT_ON_FAILURE = BPELUnitActivator.getUniqueIdentifier() + ".HALT_ON_FAILURE"; //$NON-NLS-1$
}
