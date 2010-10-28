/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.model;

import net.bpelunit.framework.control.ext.IHeaderProcessor;
import net.bpelunit.framework.exception.SpecificationException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Header Processor Extension
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class HeaderProcessorExtension extends Extension {

	public HeaderProcessorExtension(String id, String name, IConfigurationElement e) {
		super(id, name, e);
	}

	public IHeaderProcessor createNew() throws SpecificationException {
		Object o= getExecutableExtension("processorClass");
		if (o != null) {
			return (IHeaderProcessor) o;
		}
		throw new SpecificationException("Can't intantiate class for processor " + getId());
	}

}
