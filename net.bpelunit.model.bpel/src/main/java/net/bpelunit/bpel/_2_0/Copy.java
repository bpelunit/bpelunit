package net.bpelunit.bpel._2_0;

import net.bpelunit.bpel.ICopy;
import net.bpelunit.bpel.IFrom;
import net.bpelunit.bpel.ITo;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TCopy;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TFrom;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TTo;

public class Copy extends AbstractBpelObject implements ICopy {

	private TCopy copy;
	private From from;
	private To to;
	
	@Override
	public From getFrom() {
		return from;
	}

	@Override
	public To getTo() {
		return to;
	}

	@Override
	public void setTo(ITo value) {
		if(! (value instanceof To)) {
			throw new IllegalArgumentException("Illegal model element");
		}
		this.to = (To)value;
		copy.setTo(((To)value).getNativeTo());
	}

	@Override
	public void setFrom(IFrom value) {
		if(! (value instanceof From)) {
			throw new IllegalArgumentException("Illegal model element");
		}
		this.from = (From)value;
		this.copy.setFrom(((From)value).getNativeFrom());
	}
	
	Copy(TCopy c) {
		super(c);
		copy = c;
		if(c.getTo() == null) {
			c.setTo(new TTo());
		}
		
		if(c.getFrom() == null) {
			c.setFrom(new TFrom());
		}
		
		to = getFactory().createTo(c.getTo());
		from = getFactory().createFrom(c.getFrom());
	}

}
