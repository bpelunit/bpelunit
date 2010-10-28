package net.bpelunit.framework.client.model;

import net.bpelunit.framework.control.ext.IDataSource;
import net.bpelunit.framework.exception.SpecificationException;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * @author Daniel Luebke <bpelunit@daniel-luebke.de>
 */
public class DataSourceExtension extends Extension {

	public DataSourceExtension(String shortName, String name,
			IConfigurationElement e) {
		super(shortName, name, e);
	}

	public IDataSource createNew() throws SpecificationException {
		Object o= getExecutableExtension("class");
		if (o != null) {
			return (IDataSource) o;
		}
		throw new SpecificationException("Can't intantiate class for data source " + getId());
	}

}
