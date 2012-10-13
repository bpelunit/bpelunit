package net.bpelunit.model.bpel._2_0;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TDocumentation;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExtensibleElements;

abstract class AbstractBpelObject implements IBpelObject {

	protected List<Documentation> documentations = new ArrayList<Documentation>();
	private TExtensibleElements nativeElement;
	
	public AbstractBpelObject(TExtensibleElements t) {
		if (t == null) {
			throw new NullPointerException("Wrapped activity must not be null!");
		}

		this.nativeElement = t;

		for (TDocumentation d : nativeElement.getDocumentationArray()) {
			documentations.add(new Documentation(d));
		}
	}

	void checkForCorrectModel(IBpelObject o) {
		if (!(o instanceof AbstractBpelObject)) {
			throw new IllegalArgumentException(o.getClass()
					+ " is not supported by the BPEL 2.0 facade.");
		}
	}

	AbstractActivity<?> checkForCorrectModel(IActivity a) {
		try {
			return (AbstractActivity<?>) a;
		} catch (Exception e) {
			throw new IllegalArgumentException(a.getClass()
					+ " is not supported by the BPEL 2.0 facade.");
		}
	}

	public List<Documentation> getDocumentation() {
		return Collections.unmodifiableList(documentations);
	}

	public Documentation addDocumentation() {
		
		TDocumentation bpelDoc = nativeElement.addNewDocumentation();

		Documentation bpelDocumentation = new Documentation(bpelDoc);

		this.documentations.add(bpelDocumentation);

		return bpelDocumentation;
	}
	
	public String getXPathInDocument() {
		return null;
	}
	
	abstract IBpelObject getObjectForNativeObject(Object nativeObject);
	
	public void visit(IVisitor v) {
		Class<? extends IVisitor> visitorClass = v.getClass();
		for(Class<?> c : this.getClass().getInterfaces()) {
			try {
				Method m = visitorClass.getMethod("visit", c);
				m.invoke(v, this);
				return;
			} catch (Exception e) {
				// ignore
			}
		}
		throw new RuntimeException("Cannot call visit for " + this.getClass());
	}
}
