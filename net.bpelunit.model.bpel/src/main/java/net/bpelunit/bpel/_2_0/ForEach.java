package net.bpelunit.bpel._2_0;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TForEach;

import net.bpelunit.bpel.IForEach;
import net.bpelunit.bpel.IScope;
import net.bpelunit.bpel.IVisitor;

public class ForEach extends AbstractActivity<TForEach> implements IForEach {

	private Scope scope;
	private TForEach forEach;

	public ForEach(TForEach wrappedForEach) {
		super(wrappedForEach);

		this.forEach = wrappedForEach;
		this.setScope(getFactory().createActivity(forEach.getScope()));
	}

	@Override
	public void setScope(IScope s) {
		checkForCorrectModel(s);
		this.scope = (Scope) s;
		this.forEach.setScope(this.scope.getNativeActivity());
	}

	@Override
	public Scope getScope() {
		return scope;
	}

	@Override
	public boolean isBasicActivity() {
		return false;
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		scope.visit(v);
	}
}
