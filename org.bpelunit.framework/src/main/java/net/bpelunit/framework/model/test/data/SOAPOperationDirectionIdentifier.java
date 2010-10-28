/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.data;

/**
 * <p>
 * Specification of the direction of a SOAP Operation Call. There are three possibilities:
 * </p>
 * <p>
 * <ul>
 * <li>INPUT: Messages are sent TO the service represented by the WSDL.</li>
 * <li>OUTPUT: Messages are received FROM the service represented by the WSDL.</li>
 * <li>FAULT: Messages are received FROM or sent TO the service represented by the WSDL, and they
 * correspond to SOAP Faults.</li>
 * </ul>
 * </p>
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public enum SOAPOperationDirectionIdentifier {
	INPUT, OUTPUT, FAULT
}
