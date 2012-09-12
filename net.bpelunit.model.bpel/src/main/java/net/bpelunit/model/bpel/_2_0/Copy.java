package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.ICopy;
import net.bpelunit.model.bpel.IFrom;
import net.bpelunit.model.bpel.ITo;
import net.bpelunit.model.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TCopy;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TFrom;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TTo;

public class Copy extends AbstractBpelObject implements ICopy {

	private TCopy copy;
	private From from;
	private To to;
	
	Copy(TCopy c, BpelFactory f) {
		super(c, f);
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

	@Override
	public From getFrom() {
		return from;
	}

	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(nativeObject == copy) {
			return this;
		} else {
			return null;
		}
	}

	@Override
	public To getTo() {
		return to;
	}
	
	@Override
	public void setFrom(IFrom value) {
		if(! (value instanceof From)) {
			throw new IllegalArgumentException("Illegal model element");
		}
		this.from = (From)value;
		this.copy.setFrom(((From)value).getNativeFrom());
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
	void visit(IVisitor v) {
	}
}
