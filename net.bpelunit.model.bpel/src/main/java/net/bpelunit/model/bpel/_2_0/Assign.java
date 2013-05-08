package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IAssign;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;

class Assign extends AbstractBasicActivity<TAssign> implements IAssign {

	private TAssign assign;
	private List<Copy> copy = new ArrayList<Copy>();
	
	public Assign(TAssign a, IContainer parent) {
		super(a, parent);
		setNativeObject(a);
	}

	@Override
	void setNativeObject(Object a) {
		super.setNativeObject(a);
		this.assign = (TAssign)a;
		
		copy.clear();
		for(TCopy c : assign.getCopyArray()) {
			copy.add(new Copy(c));
		}
	}
	
	public void setValidate(boolean value) {
		assign.setValidate(TBooleanHelper.convert(value));
	}

	public boolean getValidate() {
		return TBooleanHelper.convert(assign.getValidate());
	}
	
	public Copy addCopy() {
		TCopy nativeCopy = this.assign.addNewCopy();
		Copy newCopy = new Copy(nativeCopy);
		
		this.copy.add(newCopy);
		
		return newCopy;
	}
	
	@Override
	public List<Copy> getCopies() {
		return Collections.unmodifiableList(copy);
	}
	
	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		for(Copy c : copy) {
			c.visit(v);
		}
	}
}
