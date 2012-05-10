package net.bpelunit.framework.model.test.activity;

import net.bpelunit.framework.exception.DataSourceException;

import org.apache.velocity.context.Context;

public interface VelocityContextProvider {

	public Context createVelocityContext() throws DataSourceException;
}
