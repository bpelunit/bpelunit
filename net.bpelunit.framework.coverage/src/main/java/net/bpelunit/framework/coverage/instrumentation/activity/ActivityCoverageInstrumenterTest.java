package net.bpelunit.framework.coverage.instrumentation.activity;

import static org.junit.Assert.*;

import net.bpelunit.framework.coverage.marker.Marker;
import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IBpelFactory;
import net.bpelunit.model.bpel.IEmpty;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.ISequence;

import org.junit.Test;


public class ActivityCoverageInstrumenterTest {

	private ActivityCoverageInstrumenter instrumenter = new ActivityCoverageInstrumenter();
	
	@Test
	public void testInstrumentation() throws Exception {
		IProcess p = BpelFactory.createProcess();
		IBpelFactory factory = p.getFactory();
		
		ISequence s1 = factory.createSequence();
		p.setMainActivity(s1);
		
		ISequence s2 = factory.createSequence();
		s1.addActivity(s2);
		
		IEmpty empty1 = factory.createEmpty();
		empty1.setName("empty1");
		s1.addActivity(empty1);

		IEmpty empty2 = factory.createEmpty();
		empty2.setName("empty2");
		s1.addActivity(empty2);
		
		IEmpty empty3 = factory.createEmpty();
		empty3.setName("empty3");
		s2.addActivity(empty3);
		
		IEmpty empty4 = factory.createEmpty();
		empty4.setName("empty4");
		s2.addActivity(empty4);
		
		instrumenter.addCoverageMarkers(p);
		
		// complex activities do not carry markers
		assertEquals(0, p.getDocumentation().size());
		assertEquals(0, s1.getDocumentation().size());
		assertEquals(0, s2.getDocumentation().size());
		
		for(IEmpty e : new IEmpty[]{ empty1, empty2, empty3, empty4} ) {
			assertEquals(e.getName() + " must carry coverage marker", 1, e.getDocumentation().size());
			assertTrue(e.getName(), e.getDocumentation().get(0).getDocumentationElements().get(0) instanceof Marker);
		}
	}
	
}
