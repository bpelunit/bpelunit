package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.ICopy;
import net.bpelunit.model.bpel.IFrom;
import net.bpelunit.model.bpel.ITo;

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

	public To getTo() {
		return to;
	}
	
	public void setFrom(IFrom value) {
		if(! (value instanceof From)) {
			throw new IllegalArgumentException("Illegal model element");
		}
		this.from = (From)value;
		this.copy.setFrom(((From)value).getNativeFrom());
	}

	public void setTo(ITo value) {
		if(! (value instanceof To)) {
			throw new IllegalArgumentException("Illegal model element");
		}
		this.to = (To)value;
		copy.setTo(((To)value).getNativeTo());
	}
}