/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.client.model;

import net.bpelunit.framework.client.eclipse.BPELUnitActivator;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Base class for all extensions
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class Extension {

	private String fId;
	private String fName;
	private IConfigurationElement fConfigElement;

	public Extension(String id, String name, IConfigurationElement e) {
		fId= id;
		fName= name;

		fConfigElement= e;
	}

	protected Object getExecutableExtension(String id) {

		try {
			return fConfigElement.createExecutableExtension(id);
		} catch (CoreException f) {
			BPELUnitActivator.log(f);
		}
		return null;
	}

	public String getId() {
		return fId;
	}

	public String getName() {
		return fName;
	}

}
