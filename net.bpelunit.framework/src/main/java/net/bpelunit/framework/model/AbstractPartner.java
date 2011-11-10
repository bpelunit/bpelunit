package net.bpelunit.framework.model;

public class AbstractPartner {

	public AbstractPartner(String name, String testBasePath, String baseURL) {
		this.name = name;
		this.basePath = testBasePath;
		
		simulatedURL= baseURL;
		if (!simulatedURL.endsWith("/"))
			simulatedURL+= "/";
		simulatedURL+= getName();
	}

	/**
	 * The name of the partner, identifying it in the test suite document and in the URLs of the
	 * partner WSDL.
	 * 
	 */
	private String name;
	
	/**
	 * Base path of the test suite (location of .bpts file)
	 */
	private String basePath;

	/**
	 * The URL which this partner simulates (base url plus partner name)
	 */
	private String simulatedURL;
	
	
	public String getName() {
		return name;
	}
	
	public String getBasePath() {
		return basePath;
	}

	public String getSimulatedURL() {
		return simulatedURL;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
