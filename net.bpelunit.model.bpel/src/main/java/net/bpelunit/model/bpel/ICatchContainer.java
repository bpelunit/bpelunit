package net.bpelunit.model.bpel;

import net.bpelunit.model.bpel._2_0.Catch;
import net.bpelunit.model.bpel._2_0.CatchAll;

public interface ICatchContainer {

	CatchAll setNewCatchAll();

	Catch addNewCatch();

}
