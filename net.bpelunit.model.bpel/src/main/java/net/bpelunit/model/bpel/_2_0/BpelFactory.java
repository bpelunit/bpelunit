package net.bpelunit.model.bpel._2_0;

import java.io.InputStream;
import java.lang.reflect.Constructor;

import javax.xml.namespace.NamespaceContext;

import net.bpelunit.model.bpel.IActivityContainer;
import net.bpelunit.model.bpel.IBpelFactory;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.util.SimpleNamespaceContext;

import org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

public class BpelFactory implements IBpelFactory {

	private static final String NAMESPACE_BPEL_2_0 = "http://docs.oasis-open.org/wsbpel/2.0/process/executable";
	
	public static final BpelFactory INSTANCE = new BpelFactory(); 
	
	public String getNamespace() {
		return NAMESPACE_BPEL_2_0; 
	}

	@Override
	public IProcess createProcess() {
		ProcessDocument processDoc = ProcessDocument.Factory.newInstance();
		processDoc.addNewProcess();
		return new Process(processDoc);
	}

	public AbstractActivity<?> createWrapper(TActivity child, IActivityContainer parent) {
		if(child == null) {
			return null;
		}
		
		String activityName = calculateBpelActivityName(child);
		String wrapperClassName = getClass().getPackage().getName() + "." + activityName;
		
		try {
			Class<?> clazz = Class.forName(wrapperClassName);
			Constructor<?> c = null;
			for(Class<?> i : child.getClass().getInterfaces()) {
				try {
					c = clazz.getConstructor(i, IContainer.class);
					break;
				} catch (Exception e) {
					// ignore
				}
			}
			return (AbstractActivity<?>) c.newInstance(child, parent);
		} catch (Exception e) {
			throw new RuntimeException("Cannot find wrapper " + wrapperClassName + " for " + child.getClass().getCanonicalName(), e);
		}
	}

	private String calculateBpelActivityName(TActivity child) {
		String name = child.getClass().getSimpleName();
		
		if(name.startsWith("T")) {
			name = name.substring(1);
		}
		
		if(name.endsWith("Impl")) {
			name = name.substring(0, name.length() - "Impl".length());
		}
		
		return name;
	}

	@Override
	public IProcess loadProcess(InputStream in) {
		ProcessDocument processDoc;
		try {
			processDoc = ProcessDocument.Factory.parse(in);
			return new Process(processDoc);
		} catch (Exception e) {
			throw new RuntimeException("Cannot load process", e);
		}
		
	}

	public NamespaceContext createNamespaceContext() {
		SimpleNamespaceContext ctx = new SimpleNamespaceContext();
		ctx.addNamespace("bpel", NAMESPACE_BPEL_2_0);
		
		return ctx;
	}
}
