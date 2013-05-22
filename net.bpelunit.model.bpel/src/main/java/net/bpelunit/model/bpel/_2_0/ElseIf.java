package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IElseIf;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TElseif;

public class ElseIf extends AbstractSingleContainer<TElseif> implements IElseIf {

	private Condition condition; 
	
	public ElseIf(TElseif nativeElseIf, IContainer parent) {
		super(nativeElseIf, parent);
		
		condition = new Condition(nativeElseIf.getCondition());
	}

	@Override
	public Condition getCondition() {
		return condition;
	}
	
	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		if(this.getMainActivity() != null) {
			this.getMainActivity().visit(v);
		}
	}
	
	@Override
	public String toString() {
		return "ElseIf";
	}
}
