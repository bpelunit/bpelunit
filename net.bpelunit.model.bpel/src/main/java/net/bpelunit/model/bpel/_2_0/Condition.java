package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ICondition;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TExpression;

public class Condition implements ICondition {

	private TExpression condition;

	public Condition(TExpression nativeExpression) {
		condition = nativeExpression;
	}

}
