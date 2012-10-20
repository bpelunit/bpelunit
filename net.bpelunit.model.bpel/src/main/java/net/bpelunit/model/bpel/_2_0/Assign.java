package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IAssign;
import net.bpelunit.model.bpel.IVisitor;

import org.apache.xmlbeans.XmlObject;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;

class Assign extends AbstractBasicActivity<TAssign> implements IAssign {

	private TAssign assign;
	private List<Copy> copy = new ArrayList<Copy>();
	
	public Assign(TAssign a) {
		super(a);
		this.assign = a;
		
		setNativeActivity(a);
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
	protected void setNativeActivity(XmlObject newNativeActivity) {
		super.setNativeActivity(newNativeActivity);
		TAssign a = (TAssign)newNativeActivity;
		this.assign = a;
		
		copy.clear();
		for(TCopy c : a.getCopyArray()) {
			copy.add(new Copy(c));
		}
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
