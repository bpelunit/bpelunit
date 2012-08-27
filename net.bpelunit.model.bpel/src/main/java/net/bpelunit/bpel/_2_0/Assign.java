package net.bpelunit.bpel._2_0;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.bpel.IAssign;
import net.bpelunit.bpel.ICopy;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TAssign;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TCopy;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TExtensibleElements;

class Assign extends AbstractActivity<TAssign> implements IAssign {

	private TAssign assign;
	private List<Copy> copy = new ArrayList<Copy>();
	
	Assign(TAssign a) {
		super(a);
		this.assign = a;
		
		for(TExtensibleElements c : a.getCopyOrExtensionAssignOperation()) {
			if(c instanceof TCopy) {
				copy.add(getFactory().createCopy((TCopy)c));
			}
		}
	}
	
	public void setValidate(boolean value) {
		assign.setValidate(TBooleanHelper.convert(value));
	}

	public boolean getValidate() {
		return TBooleanHelper.convert(assign.getValidate());
	}
	
	@Override
	public boolean isBasicActivity() {
		return true;
	}
	
	@Override
	public void visit(IVisitor v) {
		v.visit(this);
	}

	@Override
	public ICopy addCopy() {
		Copy newCopy = getFactory().createCopy(new TCopy());
		this.copy.add(newCopy);
		return newCopy;
	}
}
