package net.bpelunit.util;

import java.io.PrintWriter;

public class Console {

	public PrintWriter getScreen() {
		if (System.console() != null && System.console().writer() != null) {
			return System.console().writer();
		} else {
			return new PrintWriter(System.out, true);
		}
	}

	public void exit(int exitCode) {
		System.exit(exitCode);
	}

}
