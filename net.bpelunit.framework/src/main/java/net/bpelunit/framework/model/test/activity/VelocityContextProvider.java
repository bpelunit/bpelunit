package net.bpelunit.framework.model.test.activity;

import net.bpelunit.framework.control.datasource.WrappedContext;
import net.bpelunit.framework.exception.DataSourceException;

public interface VelocityContextProvider {
	WrappedContext createVelocityContext() throws DataSourceException;
}
