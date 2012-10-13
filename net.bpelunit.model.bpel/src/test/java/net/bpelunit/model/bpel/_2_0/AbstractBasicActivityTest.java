package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.*;

import org.junit.Test;

public abstract class AbstractBasicActivityTest<T extends AbstractActivity<?>> {

	private AbstractActivity<?> activity;

	protected void setActivity(AbstractActivity<?> a) {
		this.activity = a;
	}

	@Test
	public void testSetGetName() throws Exception {
		activity.setName("Name");
		assertEquals("Name", activity.getName());
	}
	
}
