package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IActivityContainer;
import net.bpelunit.model.bpel.IWhile;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TWhile;

public class While extends AbstractSingleContainer<TWhile> implements IWhile {

	public While(TWhile wrappedWhile, IActivityContainer parent) {
		super(wrappedWhile, parent);
	}
}
