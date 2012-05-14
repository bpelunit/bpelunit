package net.bpelunit.framework.control.datasource;

public class DataSourceConfigurationOption {

	private final String name;
	private final String defaultValue;
	private final String description;

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

	public String getDefaultValue() {
		return defaultValue;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

}