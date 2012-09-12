package net.bpelunit.model.bpel;

import java.util.List;

public interface IVariableContainer {

	List<?extends IVariable> getVariables();
	
	IVariable addVariable();

	
}
