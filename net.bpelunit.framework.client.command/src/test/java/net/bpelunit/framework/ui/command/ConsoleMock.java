package net.bpelunit.framework.ui.command;

import java.io.PrintWriter;
import java.io.StringWriter;

import net.bpelunit.util.Console;

public class ConsoleMock extends Console {

	private StringWriter consoleBuffer = new StringWriter();
	private PrintWriter consoleWriter = new PrintWriter(consoleBuffer);

	@SuppressWarnings("serial")
	public static class ProgramExitException extends RuntimeException {
		private int exitCode;
		
		public ProgramExitException(int exitCode) {
			super("Exit Code: " + exitCode);
		}

		public int getExitCode() {
			return exitCode;
		}
	}
	
	@Override
	public PrintWriter getScreen() {
		return consoleWriter;
	}
	
	@Override
	public void exit(int exitCode) {
		throw new ProgramExitException(exitCode);
	}

	public String getConsoleBuffer() {
		return consoleBuffer.toString();
	}
}
