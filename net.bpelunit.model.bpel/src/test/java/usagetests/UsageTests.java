package usagetests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IAssign;
import net.bpelunit.model.bpel.ICompensate;
import net.bpelunit.model.bpel.ICompensateScope;
import net.bpelunit.model.bpel.ICopy;
import net.bpelunit.model.bpel.IEmpty;
import net.bpelunit.model.bpel.IExit;
import net.bpelunit.model.bpel.IFlow;
import net.bpelunit.model.bpel.IForEach;
import net.bpelunit.model.bpel.IIf;
import net.bpelunit.model.bpel.IImport;
import net.bpelunit.model.bpel.IInvoke;
import net.bpelunit.model.bpel.IPartnerLink;
import net.bpelunit.model.bpel.IPick;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IReceive;
import net.bpelunit.model.bpel.IRepeatUntil;
import net.bpelunit.model.bpel.IReply;
import net.bpelunit.model.bpel.IRethrow;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IThrow;
import net.bpelunit.model.bpel.IValidate;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.model.bpel.IWait;
import net.bpelunit.model.bpel.IWhile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class UsageTests {

	@Test
	public void testAllBasicActivitiesInSequence() throws Exception {
		IProcess p = BpelFactory.createProcess();
		p.setName("MyProcess");
		p.setTargetNamespace("ProcessNamespace");

		ISequence mainSequence = p.setNewSequence();
		assertNotNull(mainSequence);
		IAssign assign = mainSequence.addAssign();
		assertNotNull(assign);
		ICompensate compensate = mainSequence.addCompensate();
		assertNotNull(compensate);
		ICompensateScope compensateScope = mainSequence.addCompensateScope();
		assertNotNull(compensateScope);
		IEmpty empty = mainSequence.addEmpty();
		assertNotNull(empty);
		IExit exit = mainSequence.addExit();
		assertNotNull(exit);
		IFlow flow = mainSequence.addFlow();
		assertNotNull(flow);
		IForEach forEach = mainSequence.addForEach();
		assertNotNull(forEach);
		IIf iif = mainSequence.addIf();
		assertNotNull(iif);
		IInvoke invoke = mainSequence.addInvoke();
		assertNotNull(invoke);
		IPick pick = mainSequence.addPick();
		assertNotNull(pick);
		IReceive receive = mainSequence.addReceive();
		assertNotNull(receive);
		IRepeatUntil repeatUntil = mainSequence.addRepeatUntil();
		assertNotNull(repeatUntil);
		IReply reply = mainSequence.addReply();
		assertNotNull(reply);
		IRethrow rethrow = mainSequence.addRethrow();
		assertNotNull(rethrow);
		IScope scope = mainSequence.addScope();
		assertNotNull(scope);
		ISequence sequence = mainSequence.addSequence();
		assertNotNull(sequence);
		IThrow tthrow = mainSequence.addThrow();
		assertNotNull(tthrow);
		IValidate validate = mainSequence.addValidate();
		assertNotNull(validate);
		IWait wait = mainSequence.addWait();
		assertNotNull(wait);
		IWhile wwhile = mainSequence.addWhile();
		assertNotNull(wwhile);

		testVisit(p, mainSequence, assign, compensate, compensateScope, empty,
				exit, flow, forEach, forEach.getScope(), iif, invoke, pick,
				receive, repeatUntil, reply, rethrow, scope, sequence, tthrow,
				validate, wait, wwhile);
		
		List<? extends IActivity> activities = mainSequence.getActivities();
		assertSame(assign, activities.get(0));
		assertSame(compensate, activities.get(1));
		assertSame(compensateScope, activities.get(2));
	}

	@Test
	public void testSimpleProcessSequence() throws Exception {
		IProcess p = BpelFactory.createProcess();
		p.setName("MyProcess");
		p.setTargetNamespace("ProcessNamespace");
		String wsdlNamespace = "http://www.example.org/service";

		IImport imp = p.addImport();
		imp.setImportType(IImport.IMPORTTYPE_WSDL);
		imp.setLocation("testSimpleProcessSequence.wsdl");
		imp.setNamespace(wsdlNamespace);

		IPartnerLink pl = p.addPartnerLink();
		pl.setName("processPL");
		pl.setPartnerLinkType(new QName(wsdlNamespace, "processPLT"));
		pl.setMyRole("process");

		IVariable request = p.addVariable();
		request.setName("request");
		request.setMessageType(new QName(wsdlNamespace, "Request"));

		IVariable response = p.addVariable();
		response.setName("response");
		response.setMessageType(new QName(wsdlNamespace, "Response"));

		ISequence mainSequence = p.setNewSequence();
		mainSequence.setName("mainSequence");

		IReceive receive = mainSequence.addReceive();
		receive.setName("receive");
		receive.setCreateInstance(true);
		receive.setVariable(request);
		receive.setOperation("operation");
		receive.setPartnerLink("processPL");
		receive.setPortType(new QName(wsdlNamespace, "Porttype"));

		IAssign assign = mainSequence.addAssign();
		assign.setName("assign");
		ICopy copy = assign.addCopy();
		copy.getFrom().setVariable(request);
		copy.getFrom().setPart("parameters");
		copy.getFrom().setNewQuery().setQueryContents("in");
		copy.getTo().setVariable(response);
		copy.getTo().setPart("parameters");
		copy.getTo().setExpression("in");

		IReply reply = mainSequence.addReply();
		reply.setName("reply");
		reply.setVariable(response);
		reply.setOperation("operation");
		reply.setPartnerLink("processPL");
		reply.setPortType(new QName(wsdlNamespace, "Porttype"));

		FileOutputStream fo = null;
		try {
			File bpelFile = new File(
					"target/tests/actual/testSimpleProcessSequence.bpel");
			bpelFile.getParentFile().mkdirs();
			fo = new FileOutputStream(bpelFile);
			p.save(fo);

			String actual = FileUtils.readFileToString(bpelFile);
			String expected = FileUtils.readFileToString(new File(
					"src/test/resources/testSimpleProcessSequence.bpel"));
			assertEquals(expected.trim().replaceAll("\\r", ""), actual.trim()
					.replaceAll("\\r", ""));
		} finally {
			IOUtils.closeQuietly(fo);
		}
		testVisit(p, imp, pl, request, response, mainSequence, receive, reply,
				assign, copy);
	}

	private void testVisit(IProcess p, Object... activities) {
		GatheringVisitor v = new GatheringVisitor(p, activities);
		p.visit(v);
		assertEquals(v.getRemainingObjects().toString(), 0, v
				.getRemainingObjects().size());
		assertEquals(v.getAdditionalObjects().toString(), 0, v
				.getAdditionalObjects().size());
	}
}
