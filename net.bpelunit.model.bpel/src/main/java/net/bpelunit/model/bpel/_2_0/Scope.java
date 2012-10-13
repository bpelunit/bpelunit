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
	
	public Scope(TScope wrappedScope) {
		super(wrappedScope);
		this.scope = wrappedScope;
		
		if (!wrappedScope.isSetVariables()) {
			wrappedScope.addNewVariables();
		}
		for (TVariable v : wrappedScope.getVariables().getVariableArray()) {
			variables.add(new Variable(v));
		}
	}

	@Override
	TScope getNativeActivity() {
		return this.scope;
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		AbstractActivity<?> mainActivity = getMainActivity();
		if(mainActivity != null) {
			mainActivity.visit(v);
		}
	}
	
	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(nativeObject == scope) {
			return this;
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

		Variable variable = new Variable(nativeVariable);
		this.variables.add(variable);
		return variable;
	}

}
