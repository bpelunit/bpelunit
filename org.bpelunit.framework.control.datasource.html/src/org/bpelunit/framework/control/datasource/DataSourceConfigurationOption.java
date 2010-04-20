package org.bpelunit.framework.control.datasource;

public class DataSourceConfigurationOption {

	public final String name;
	public final String defaultValue;
	public final String description;

	public DataSourceConfigurationOption(String name, String defaultValue,
			String description) {

		if (name == null || defaultValue == null || description == null) {
			throw new IllegalArgumentException(
					"All values for a data source configuration option must not be null: "
							+ name + ", " + defaultValue + ", " + description);
		}

		this.name = name;
		this.defaultValue = defaultValue;
		this.description = description;
	}

}
