package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;


public class CopyTest {

	private Copy copy;

	@Before
	public void setUp() {
		copy = new Copy(TCopy.Factory.newInstance());
	}
	
	@Test
	public void testNonEmptyFrom() throws Exception {
		assertNotNull(copy.getFrom());
		assertSame(copy.getFrom(), copy.getFrom());
	}
	
	@Test
	public void testNonEmptyTo() throws Exception {
		assertNotNull(copy.getTo());
		assertSame(copy.getTo(), copy.getTo());
	}
	
	@Test
	public void testKeepSrcElementName() throws Exception {
		assertFalse(copy.getKeepSrcElementName());
		
		copy.setKeepSrcElementName(true);
		assertTrue(copy.getKeepSrcElementName());

		copy.setKeepSrcElementName(false);
		assertFalse(copy.getKeepSrcElementName());
	}
	
	@Test
	public void testIgnoreMissingFromDataFromModel() throws Exception {
		assertFalse(copy.getIgnoreMissingFromData());
		
		copy.setIgnoreMissingFromData(true);
		assertTrue(copy.getIgnoreMissingFromData());
		
		copy.setIgnoreMissingFromData(false);
		assertFalse(copy.getIgnoreMissingFromData());
	}
}
