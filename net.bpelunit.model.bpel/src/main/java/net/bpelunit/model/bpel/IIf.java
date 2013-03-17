package net.bpelunit.model.bpel;

import java.util.List;

public interface IIf extends ISingleContainer, IActivity {

	IExpression getCondition();
	
	List<?extends IElseIf> getElseIfs();
	IElseIf addNewElseIf();
	
	IElse setNewElse();
	IElse getElse();
}
