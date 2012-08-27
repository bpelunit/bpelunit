package usagetests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.namespace.QName;

import net.bpelunit.bpel.BpelFactory;
import net.bpelunit.bpel.IAssign;
import net.bpelunit.bpel.IBpelFactory;
import net.bpelunit.bpel.ICopy;
import net.bpelunit.bpel.IImport;
import net.bpelunit.bpel.IPartnerLink;
import net.bpelunit.bpel.IProcess;
import net.bpelunit.bpel.IReceive;
import net.bpelunit.bpel.IReply;
import net.bpelunit.bpel.ISequence;
import net.bpelunit.bpel.IVariable;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;


public class UsageTests {

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
		
		IBpelFactory factory = p.getFactory();
		ISequence mainSequence = factory.createSequence();
		mainSequence.setName("mainSequence");
		p.setMainActivity(mainSequence);
		
		IReceive receive = factory.createReceive();
		receive.setName("receive");
		receive.setCreateInstance(true);
		receive.setVariable(request);
		receive.setOperation("operation");
		receive.setPartnerLink("processPL");
		receive.setPortType(new QName(wsdlNamespace, "Porttype"));
		mainSequence.addActivity(receive);
		
		IAssign assign = factory.createAssign();
		assign.setName("assign");
		ICopy copy = assign.addCopy();
		copy.getFrom().setVariable(request);
		copy.getFrom().setPart("parameters");
		copy.getFrom().setExpression("in");
		mainSequence.addActivity(assign);
		copy.getTo().setVariable(response);
		copy.getTo().setPart("parameters");
		copy.getTo().setExpression("in");
		
		IReply reply = factory.createReply();
		reply.setName("reply");
		reply.setVariable(response);
		reply.setOperation("operation");
		reply.setPartnerLink("processPL");
		reply.setPortType(new QName(wsdlNamespace, "Porttype"));
		mainSequence.addActivity(reply);

		FileOutputStream fo = null;
		try {
			File bpelFile = new File("target/tests/actual/testSimpleProcessSequence.bpel");
			bpelFile.getParentFile().mkdirs();
			fo = new FileOutputStream(bpelFile);
			p.save(fo);
			
			String actual = FileUtils.readFileToString(bpelFile);
			String expected = FileUtils.readFileToString(new File("src/test/resources/testSimpleProcessSequence.bpel"));
			assertEquals(expected.trim().replaceAll("\\r", ""), actual.trim().replaceAll("\\r", ""));
		}
		finally {
			IOUtils.closeQuietly(fo);
		}
	}
}
