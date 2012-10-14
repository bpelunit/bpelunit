package net.bpelunit.model.bpel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;

public class BpelFactory {
	private static final String NAMESPACE_BPEL_2_0 = "http://docs.oasis-open.org/wsbpel/2.0/process/executable";

	private static Map<String, IBpelFactory> factories = new HashMap<String, IBpelFactory>();
	
	static {
		BufferedReader properties = new BufferedReader(new InputStreamReader(BpelFactory.class.getResourceAsStream("BpelFactory.properties")));
		try {
			String line;
			while((line = properties.readLine()) != null) {
				line = line.trim();
				if(! "".equals(line)) {
					String[] components = line.split("=");
					factories.put(components[0], (IBpelFactory) Class.forName(components[1]).newInstance());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error while reading configuration for BpelFactories", e);
		} finally {
			IOUtils.closeQuietly(properties);
		}
	}
	
	public static IProcess loadProcess(InputStream in) throws IOException {
		return factories.get(NAMESPACE_BPEL_2_0).loadProcess(in);
	}

	public static IProcess createProcess() {
		return createProcess(NAMESPACE_BPEL_2_0);
	}

	public static IProcess createProcess(String bpelNamespace) {
		return factories.get(bpelNamespace).createProcess();
	}
	
}
