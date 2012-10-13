package net.bpelunit.model.bpel;

import java.io.IOException;
import java.io.InputStream;

public class BpelFactory {
	private static final String NAMESPACE_BPEL_2_0 = "http://docs.oasis-open.org/wsbpel/2.0/process/executable";

	
	public static IProcess loadProcess(InputStream in) throws IOException {
		return net.bpelunit.model.bpel._2_0.BpelFactory.INSTANCE.loadProcess(in);
	}

	public static IProcess createProcess() {
		return createProcess(NAMESPACE_BPEL_2_0);
	}

	public static IProcess createProcess(String bpelNamespace) {
		if(NAMESPACE_BPEL_2_0.equals(bpelNamespace)) {
			return net.bpelunit.model.bpel._2_0.BpelFactory.INSTANCE.createProcess();
		} else {
			throw new IllegalArgumentException("No model found for namespace " + bpelNamespace);
		}
	}
	
}
