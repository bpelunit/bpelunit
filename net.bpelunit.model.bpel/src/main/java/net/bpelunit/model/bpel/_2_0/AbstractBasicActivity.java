package net.bpelunit.model.bpel._2_0;

import java.lang.reflect.Method;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TActivity;

import net.bpelunit.model.bpel.IVisitor;

public class AbstractBasicActivity<T extends TActivity> extends AbstractActivity<T> {

	private Class<?> visitorClass;

	AbstractBasicActivity(T a, BpelFactory factory, Class<?> interfaceForVisit) {
		super(a, factory);
		this.visitorClass = interfaceForVisit;
	}

	@Override
	public final boolean isBasicActivity() {
		return true;
	}

	@Override
	public void visit(IVisitor v) {
		try {
		Method method = v.getClass().getMethod("visit", visitorClass);
		method.invoke(v, this);
		} catch(Exception e) {
			throw new RuntimeException("Cannot visit ", e);
		}
	}
}
