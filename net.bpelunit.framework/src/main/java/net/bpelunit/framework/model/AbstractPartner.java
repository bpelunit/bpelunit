package net.bpelunit.framework.model;

public class AbstractPartner {

	public AbstractPartner(String name, String baseURL) {
		this.name = name;
		
		simulatedURL= baseURL;
		if (!simulatedURL.endsWith("/")) {
			simulatedURL+= "/";
		}
		simulatedURL+= name;
	}

	/**
	 * The name of the partner, identifying it in the test suite document and in the URLs of the
	 * partner WSDL.
	 * 
	 */
	private String name;
	
	

	/**
	 * The URL which this partner simulates (base url plus partner name)
	 */
	private String simulatedURL;
	
	
	public String getName() {
		return name;
	}


	public String getSimulatedURL() {
		return simulatedURL;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
