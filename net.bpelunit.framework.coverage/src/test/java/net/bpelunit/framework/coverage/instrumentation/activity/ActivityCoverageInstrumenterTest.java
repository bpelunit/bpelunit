package net.bpelunit.framework.coverage.instrumentation.activity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IEmpty;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.ISequence;

import org.junit.Test;
import org.w3c.dom.Node;


public class ActivityCoverageInstrumenterTest {

	private ActivityCoverageInstrumenter instrumenter = new ActivityCoverageInstrumenter();
	
	@Test
	public void testInstrumentation() throws Exception {
		IProcess p = BpelFactory.createProcess();
		
		ISequence s1 = p.setNewSequence();
		
		ISequence s2 = s1.addSequence();
		
		IEmpty empty1 = s1.addEmpty();
		empty1.setName("empty1");

		IEmpty empty2 = s1.addEmpty();
		empty2.setName("empty2");
		
		IEmpty empty3 = s2.addEmpty();
		empty3.setName("empty3");
		
		IEmpty empty4 = s2.addEmpty();
		empty4.setName("empty4");
		
		instrumenter.addCoverageMarkers(p);
		
		// complex activities do not carry markers
		assertEquals(0, p.getDocumentation().size());
		assertEquals(0, s1.getDocumentation().size());
		assertEquals(0, s2.getDocumentation().size());
		
		for(IEmpty e : new IEmpty[]{ empty1, empty2, empty3, empty4} ) {
			assertEquals(e.getName() + " must carry coverage marker", 1, e.getDocumentation().size());
			List<Node> docElements = e.getDocumentation().get(0).getDocumentationElements();
			assertTrue(e.getName(), docElements.size() > 0);
		}
	}
	
}
