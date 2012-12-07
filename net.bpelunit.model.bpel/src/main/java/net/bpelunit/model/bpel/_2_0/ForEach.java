package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IExpression;
import net.bpelunit.model.bpel.IForEach;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TBoolean;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TForEach;

public class ForEach extends AbstractActivity<TForEach> implements IContainer, IForEach {

	private Scope scope;
	private Expression startCounterValue;
	private Expression finalCounterValue;
	private CompletionCondition completionCondition;

	public ForEach(TForEach wrappedForEach, IContainer parent) {
		super(wrappedForEach, parent);

		setNativeObject(wrappedForEach);
	}

	void setNativeObject(Object nativeForEach) {
		super.setNativeObject(nativeForEach);
		TForEach wrappedForEach = (TForEach)nativeForEach;
		
		if(wrappedForEach.getScope() == null) {
			wrappedForEach.addNewScope();
		}
		this.scope = new Scope(wrappedForEach.getScope(), this);
		
		if(wrappedForEach.getParallel() == null) {
			wrappedForEach.setParallel(TBoolean.NO);
		}
		
		if(wrappedForEach.getStartCounterValue() == null) {
			wrappedForEach.addNewStartCounterValue();
		}
		startCounterValue = new Expression(wrappedForEach.getStartCounterValue());
		
		if(wrappedForEach.getFinalCounterValue() == null) {
			wrappedForEach.addNewFinalCounterValue();
		}
		startCounterValue = new Expression(wrappedForEach.getFinalCounterValue());
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

	@Override
	public String getCounterName() {
		return getNativeActivity().getCounterName();
	}
	
	@Override
	public void setCounterName(String newCounterName) {
		getNativeActivity().setCounterName(newCounterName);
	}
	
	@Override
	public boolean isParallel() {
		return getNativeActivity().getParallel().equals(TBoolean.YES);
	}
	
	@Override
	public void setParallel(boolean isParallel) {
		getNativeActivity().setParallel(TBooleanHelper.convert(isParallel));	
	}

	@Override
	public Expression getStartCounterValue() {
		return startCounterValue;
	}

	@Override
	public IExpression getFinalCounterValue() {
		return finalCounterValue;
	}

	@Override
	public CompletionCondition getCompletionCondition() {
		return completionCondition;
	}

	@Override
	public CompletionCondition setNewCompletionCondition() {
		if(getNativeActivity().getCompletionCondition() != null) {
			getNativeActivity().unsetCompletionCondition();
		}
		
		completionCondition = new CompletionCondition(getNativeActivity().addNewCompletionCondition());
		
		return completionCondition;
	}

	@Override
	public IScope wrapActivityInNewScope(IActivity childActivity) {
		throw new IllegalArgumentException("Cannot wrap a mandatory scope of a for-each activity");
	}
	
	@Override
	public ISequence wrapActivityInNewSequence(IActivity childActivity) {
		throw new IllegalArgumentException("Cannot wrap a mandatory scope of a for-each activity");
	}

	@Override
	public void unregister(AbstractActivity<?> a) {
		throw new IllegalArgumentException("Cannot unregister a mandatory scope of a for-each activity");
	}
}
