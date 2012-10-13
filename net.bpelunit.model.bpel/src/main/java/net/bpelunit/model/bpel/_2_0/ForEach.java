package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IForEach;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TForEach;

public class ForEach extends AbstractActivity<TForEach> implements IForEach {

	private Scope scope;

	public ForEach(TForEach wrappedForEach) {
		super(wrappedForEach);

		if(wrappedForEach.getScope() == null) {
			wrappedForEach.addNewScope();
		}
		
		this.scope = new Scope(wrappedForEach.getScope());
		
		setNativeActivity(wrappedForEach);
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
