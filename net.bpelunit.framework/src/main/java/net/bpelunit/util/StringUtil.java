package net.bpelunit.util;

public final class StringUtil {

	private StringUtil() {
	}
	
	/**
	 * Convenience method. Should be in some StringHelper
	 * 
	 */
	public static String toFirstUpper(String optionName) {
		if (optionName == null || optionName.length() == 0) {
			return "";
		}

		return optionName.substring(0, 1).toUpperCase()
				+ optionName.substring(1);
	}
	
}
