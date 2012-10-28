package net.bpelunit.model.bpel._2_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import net.bpelunit.model.bpel.ActivityType;

import org.junit.Before;
import org.junit.Test;
import org.oasisOpen.docs.wsbpel.x20.process.executable.FlowDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFlow;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TLinks;


public class FlowTest extends AbstractMultiContainerTest<Flow> {

	private TFlow nativeFlow;
	private Flow flow;

	@Before
	public void setUp() {
		FlowDocument flowDoc = FlowDocument.Factory.newInstance();
		nativeFlow = flowDoc.addNewFlow();
		flow = new Flow(nativeFlow, null);
		
		setContainerActivity(flow);
	}
	
	@Test
	public void testLinks() throws Exception {
		assertEquals(0, flow.getLinks().size());
		
		Link l1 = flow.addLink("L1");
		assertEquals("L1", l1.getName());
		assertEquals(1, flow.getLinks().size());
		assertSame(l1, flow.getLinks().get(0));
		assertEquals(1, flow.activity.getLinks().getLinkArray().length);
		assertSame(l1.link, flow.activity.getLinks().getLinkArray()[0]);
		
		Link l2 = flow.addLink("L2");
		assertEquals("L2", l2.getName());
		assertEquals(2, flow.getLinks().size());
		assertSame(l1, flow.getLinks().get(0));
		assertSame(l2, flow.getLinks().get(1));
		assertEquals(2, flow.activity.getLinks().getLinkArray().length);
		assertSame(l1.link, flow.activity.getLinks().getLinkArray()[0]);
		assertSame(l2.link, flow.activity.getLinks().getLinkArray()[1]);
		
		flow.removeLink(l1);
		assertEquals(1, flow.getLinks().size());
		assertSame(l2, flow.getLinks().get(0));
		assertEquals(1, flow.activity.getLinks().getLinkArray().length);
		assertSame(l2.link, flow.activity.getLinks().getLinkArray()[0]);
	}
	
	@Test
	public void testPrefilledFlow() throws Exception {
		TLinks links = nativeFlow.getLinks();
		links.addNewLink().setName("L1");
		links.addNewLink().setName("L2");
		
		flow = new Flow(nativeFlow, null);
		List<Link> linkList = flow.getLinks();
		assertEquals(2, linkList.size());
		assertEquals("L1", linkList.get(0).getName());
		assertEquals("L2", linkList.get(1).getName());
	}
	

	@Test
	public void testGetActivityType() throws Exception {
		assertEquals(ActivityType.Flow, flow.getActivityType());
	}
}
