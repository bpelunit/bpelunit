package net.bpelunit.model.bpel;

import java.io.IOException;
import java.io.InputStream;

import org.apache.xmlbeans.XmlException;
import org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument;

public class BpelFactory {
	private static final String NAMESPACE_BPEL_2_0 = "http://docs.oasis-open.org/wsbpel/2.0/process/executable";

	
	public static IProcess loadProcess(InputStream in) throws IOException {
		try {
			return new net.bpelunit.model.bpel._2_0.BpelFactory(ProcessDocument.Factory.parse(in)).getProcess();
		} catch (XmlException e) {
			throw new IOException("XML Error: " + e.getMessage(), e);
		}
	}

	public static IProcess createProcess() {
		return createProcess(NAMESPACE_BPEL_2_0);
	}

	public static IProcess createProcess(String bpelNamespace) {
		if(NAMESPACE_BPEL_2_0.equals(bpelNamespace)) {
			return new net.bpelunit.model.bpel._2_0.BpelFactory().getProcess();
		} else {
			throw new IllegalArgumentException("No model found for namespace " + bpelNamespace);
		}
	}
	
}
