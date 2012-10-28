package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ICopy;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TBoolean;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;

public class Copy extends AbstractBpelObject implements ICopy {

	private TCopy copy;
	private From from;
	private To to;
	
	Copy(TCopy c) {
		super(c);
		copy = c;
		if(c.getTo() == null) {
			c.addNewTo();
		}
		
		if(c.getFrom() == null) {
			c.addNewFrom();
		}
		
		to = new To(c.getTo());
		from = new From(c.getFrom());
	}

	@Override
	public From getFrom() {
		return from;
	}

	@Override
	public To getTo() {
		return to;
	}

	@Override
	public boolean getKeepSrcElementName() {
		return copy.getKeepSrcElementName().equals(TBoolean.YES);
	}

	@Override
	public void setKeepSrcElementName(boolean value) {
		copy.setKeepSrcElementName(TBooleanHelper.convert(value));
	}

	@Override
	public boolean getIgnoreMissingFromData() {
		return copy.getIgnoreMissingFromData().equals(TBoolean.YES);
	}

	@Override
	public void setIgnoreMissingFromData(boolean value) {
		copy.setIgnoreMissingFromData(TBooleanHelper.convert(value));
	}
	
}