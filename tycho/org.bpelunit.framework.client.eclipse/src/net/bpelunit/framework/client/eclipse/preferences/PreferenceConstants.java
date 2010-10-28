/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.eclipse.preferences;

/**
 * Constants for identifying the settings of the BPELUnit Eclipse plugin.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class PreferenceConstants {

	/**
	 * Whether to enable logging to the console
	 */
	public static final String P_LOGTOCONSOLE= "logToConsole";

	/**
	 * Log level to be passed on to log4j
	 */
	public static final String P_LOGLEVEL= "logLevel";

	/**
	 * The global timeout value for send/receive operations
	 */
	public static final String P_TIMEOUT= "globalTimeout";


	/**
	 * The currently selected deployer in the preferences (UI feature)
	 */
	public static final String P_CURRENT_DEPLOYER= "CurrentDeployer";
	

	public static final String P_COVERAGE_MEASURMENT= "CoverageMeasurment";
	
	public static final String P_COVERAGE_WAIT_TIME= "WaitTime";
	
	public static final String P_ENDPOINT_MODIFICATION = "EndpointModification";

}
