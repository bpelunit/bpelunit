package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IForEach;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.IVisitor;

import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TForEach;

public class ForEach extends AbstractActivity<TForEach> implements IForEach {

	private Scope scope;
	private TForEach forEach;

	public ForEach(TForEach wrappedForEach, BpelFactory f) {
		super(wrappedForEach, f);

		setNativeActivity(wrappedForEach);
	}

	@Override
	protected void setNativeActivity(XmlObject newNativeActivity) {
		super.setNativeActivity(newNativeActivity);

		this.forEach = (TForEach) newNativeActivity;
		this.setScope(getFactory().createActivity(forEach.getScope()));
	}

	public void setScope(IScope s) {
		checkForCorrectModel(s);
		this.scope = (Scope) s;
		this.forEach.setScope(this.scope.getNativeActivity());
	}

	public Scope getScope() {
		return scope;
	}

	public boolean isBasicActivity() {
		return false;
	}

	public void visit(IVisitor v) {
		v.visit(this);
		scope.visit(v);
	}
	
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		IBpelObject o = super.getObjectForNativeObject(nativeObject);
		if(o != null) {
			return o;
		} else {
			if(scope != null) {
				return scope.getObjectForNativeObject(nativeObject);
			}
		}
		return null;
	}
}
