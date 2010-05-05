package org.bpelunit.framework.client.model;

import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 */
public class DataSourceExtension extends Extension {

	public DataSourceExtension(String shortName, String name,
			IConfigurationElement e) {
		super(shortName, name, e);
	}

}
