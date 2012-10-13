package net.bpelunit.model.bpel;

import java.io.InputStream;

public interface IBpelFactory {
	String getNamespace();
	IProcess createProcess();
	IProcess loadProcess(InputStream in);
}
