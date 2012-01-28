package net.bpelunit.framework.model.test.activity;

import org.apache.velocity.VelocityContext;

public interface VelocityContextProvider {

	public VelocityContext createVelocityContext() throws Exception;
}
