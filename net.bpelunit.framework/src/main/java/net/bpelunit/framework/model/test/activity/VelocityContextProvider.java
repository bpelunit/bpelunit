package net.bpelunit.framework.model.test.activity;

import org.apache.velocity.context.Context;

public interface VelocityContextProvider {

	public Context createVelocityContext() throws Exception;
}
