package net.bpelunit.framework.util;

import java.io.PrintWriter;

public class Console {

	public PrintWriter getScreen() {
		return System.console().writer();
	}
	
	public void exit(int exitCode) {
		System.exit(exitCode);
	}
	
}
