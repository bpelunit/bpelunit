/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.model;

import net.bpelunit.framework.control.ext.ISOAPEncoder;
import net.bpelunit.framework.exception.SpecificationException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * SOAP Encoder Extension
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class SOAPEncoderExtension extends Extension {

	public SOAPEncoderExtension(String id, String name, IConfigurationElement e) {
		super(id, name, e);
	}

	public ISOAPEncoder createNew() throws SpecificationException {
		Object o= getExecutableExtension("encoderClass");
		if (o != null) {
			return (ISOAPEncoder) o;
		}
		throw new SpecificationException("Can't intantiate class for processor " + getId());
	}

}
