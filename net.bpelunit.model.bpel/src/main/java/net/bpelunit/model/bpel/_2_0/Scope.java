package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.model.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TScope;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TVariable;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TVariables;

class Scope extends AbstractSingleContainer<TScope> implements IScope {

	private TScope scope;
	private List<Variable> variables = new ArrayList<Variable>();

	
	Scope(TScope wrappedScope, BpelFactory f) {
		super(wrappedScope, f.createActivity(
				TComplexContainerHelper.getChildActivity(wrappedScope)), f);
		this.scope = wrappedScope;
		
		if (wrappedScope.getVariables() == null) {
			wrappedScope.setVariables(new TVariables());
		}
		for (TVariable v : wrappedScope.getVariables().getVariable()) {
			variables.add(getFactory().createVariable(v));
		}
	}

	@Override
	TScope getNativeActivity() {
		return this.scope;
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		getMainActivity().visit(v);
	}
	
	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		IBpelObject o = super.getObjectForNativeObject(nativeObject);
		if(o != null) {
			return o;
		} else {
			if(scope != null) {
				return getMainActivity().getObjectForNativeObject(nativeObject);
			}
		}
		return null;
	}
	
	@Override
	public List<? extends IVariable> getVariables() {
		return Collections.unmodifiableList(this.variables);
	}

	@Override
	public IVariable addVariable() {
		TVariable nativeVariable = new TVariable();
		Variable variable = getFactory().createVariable(nativeVariable);

		this.scope.getVariables().getVariable().add(nativeVariable);
		this.variables.add(variable);
		return variable;
	}


	@Override
	protected void setMainActivityBpel(AbstractActivity<?> activity) {
		TComplexContainerHelper.removeMainActivity(scope);
		if (activity != null) {
			TComplexContainerHelper.setActivity(scope,
					activity.getNativeActivity());
		} else {
			TComplexContainerHelper.setActivity(scope, null);
		}
	}
}
