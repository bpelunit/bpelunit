package net.bpelunit.utils.bptstool.functions;

public interface IFunction {

	/**
	 * Returns the name of this function that is also the first
	 * parameter for the BPTSTool in order to invoke it.
	 * Names must be all lower-case.
	 * 
	 * @return this function's name
	 */
	String getName();
	
	/**
	 * Returns the localized description of this function.
	 * 
	 * @return localized description
	 */
	String getDescription();

	/**
	 * Returns the localized help text
	 */
	String getHelp();
	
	/**
	 * Executes this function. All parameters except the function name
	 * are passed to this function as is.
	 * 
	 * @param params
	 */
	void execute(String[] params);
	
}
