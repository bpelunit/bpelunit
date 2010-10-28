/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.unit;

import javax.xml.parsers.ParserConfigurationException;

import net.bpelunit.framework.control.util.BPELUnitUtil;
import net.bpelunit.framework.exception.ConfigurationException;
import org.junit.BeforeClass;

/**
 * 
 * Tests availability of the XML parser.
 * 
 * @version $Id: SimpleTest.java,v 1.3 2006/07/11 14:27:43 phil Exp $
 * @author Philip Mayer
 * 
 */
public abstract class SimpleTest {

	@BeforeClass
	public static void setUp() throws ConfigurationException {
		try {
			BPELUnitUtil.initializeParsing();
		} catch (ParserConfigurationException e) {
			throw new ConfigurationException("Could not initialize XML Parser Component.", e);
		}
	}

}
