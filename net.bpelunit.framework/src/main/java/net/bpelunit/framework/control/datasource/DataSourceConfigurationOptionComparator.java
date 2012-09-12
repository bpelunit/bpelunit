package net.bpelunit.framework.control.datasource;

import java.util.Comparator;

class DataSourceConfigurationOptionComparator implements
		Comparator<DataSourceConfigurationOption> {

	@Override
	public int compare(DataSourceConfigurationOption o1,
			DataSourceConfigurationOption o2) {
		return o1.getName().compareTo(o2.getName());
	}
	
}
