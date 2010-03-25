package org.bpelunit.framework.datasource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface DataSource {

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.METHOD)
	public @interface ConfigurationOption { String defaultValue(); String description(); }
	
	public void setSource(String uri) throws InvalidDataSourceException;
	
	public void setData(String data) throws InvalidDataSourceException;
	
	public String[] getFieldNames();
	
	public boolean next();
	
	public String getValueFor(String fieldName);
	
	public void close();
	
	public String[] getSupportedContentTypes();
}
