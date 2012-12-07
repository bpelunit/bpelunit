package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IElseIf;

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
}
