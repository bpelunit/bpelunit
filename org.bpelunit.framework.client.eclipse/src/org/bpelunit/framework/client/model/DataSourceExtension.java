package org.bpelunit.framework.client.model;

import org.eclipse.core.runtime.IConfigurationElement;

public class DataSourceExtension extends Extension {

	public DataSourceExtension(String shortName, String name,
			IConfigurationElement e) {
		super(shortName, name, e);
	}

}
