package org.bpelunit.framework.coverage.exceptions;




public class BpelVersionException extends BpelException {
	
	public static final String WRONG_VERSION="Wrong version"; 
	public BpelVersionException(String version){
		super(version);
	}
}
