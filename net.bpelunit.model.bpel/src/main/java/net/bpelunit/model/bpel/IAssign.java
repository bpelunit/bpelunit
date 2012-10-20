package net.bpelunit.model.bpel;

import java.util.List;

public interface IAssign extends IActivity {

	ICopy addCopy();
	List<? extends ICopy> getCopies();
	 
}
