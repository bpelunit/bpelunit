package net.bpelunit.model.bpel._2_0;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.NamespaceContext;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IVisitor;
import net.bpelunit.util.XMLUtil;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TDocumentation;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExtensibleElements;

abstract class AbstractBpelObject implements IBpelObject {

	protected List<Documentation> documentations = new ArrayList<Documentation>();
	private TExtensibleElements nativeElement;
	
	public AbstractBpelObject(TExtensibleElements t) {
		this.nativeElement = t;

		setNativeObjectInternal(t);
	}

	void setNativeObject(Object newNativeObject) {
		setNativeObjectInternal(newNativeObject);
	}
	
	private final void setNativeObjectInternal(Object newNativeObject) {
		nativeElement = (TExtensibleElements) newNativeObject;
		
		documentations.clear();
		for (TDocumentation d : nativeElement.getDocumentationArray()) {
			documentations.add(new Documentation(d));
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
		NamespaceContext ctx = BpelFactory.INSTANCE.createNamespaceContext();
		return XMLUtil.getXPathForElement(nativeElement.getDomNode(), ctx);
	}
	
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(nativeElement == nativeObject) {
			return this;
		} else {
			return null;
		}
	}
	
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
