package net.bpelunit.model.bpel._2_0;

import java.lang.reflect.Method;

import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity;

public abstract class AbstractBasicActivity<T extends TActivity> extends AbstractActivity<T> {

	private Class<?> visitorClass;

	AbstractBasicActivity(T a, BpelFactory factory, Class<?> interfaceForVisit) {
		super(a, factory);
		this.visitorClass = interfaceForVisit;
	}

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
