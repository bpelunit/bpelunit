package net.bpelunit.toolsupport.util.schema;

/**
 * Exception which indicates that no element definition exists for some
 * message. It differs from the InvalidInputException in that it's not that
 * the activity doesn't match the WSDL, or that there's something wrong in
 * the WSDL. It means that there is genuinely no schema which can be used
 * to generate a sample XML fragment.
 * 
 * For instance, the message could have no parts, or the user might be defining
 * a custom fault.
 */
public class NoElementDefinitionExistsException extends Exception {
	private static final long serialVersionUID = 1L;

	public NoElementDefinitionExistsException(String s) {
		super(s);
	}
}
