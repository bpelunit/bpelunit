package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TScope;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;

class Scope extends AbstractSingleContainer<TScope> implements IScope {

	private TScope scope;
	private List<Variable> variables = new ArrayList<Variable>();

	
	Scope(TScope wrappedScope, BpelFactory f) {
		super(wrappedScope, f.createActivity(
				TComplexContainerHelper.getChildActivity(wrappedScope)), f);
		this.scope = wrappedScope;
		
		if (!wrappedScope.isSetVariables()) {
			wrappedScope.addNewVariables();
		}
		for (TVariable v : wrappedScope.getVariables().getVariableArray()) {
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
	
	public List<? extends IVariable> getVariables() {
		return Collections.unmodifiableList(this.variables);
	}

	public IVariable addVariable() {
		TVariable nativeVariable = scope.getVariables().addNewVariable();

		Variable variable = getFactory().createVariable(nativeVariable);
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
