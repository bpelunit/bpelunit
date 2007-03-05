package exception;

public class BpelVersionException extends Exception {
	
	public static final String WRONG_VERSION="Wrong version"; 
	public BpelVersionException(String version){
		super(version);
	}
}
