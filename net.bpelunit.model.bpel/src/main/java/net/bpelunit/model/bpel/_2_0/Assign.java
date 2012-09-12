package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.model.bpel.IAssign;
import net.bpelunit.model.bpel.ICopy;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TAssign;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TCopy;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TExtensibleElements;

class Assign extends AbstractBasicActivity<TAssign> implements IAssign {

	private TAssign assign;
	private List<Copy> copy = new ArrayList<Copy>();
	
	Assign(TAssign a, BpelFactory factory) {
		super(a, factory, IAssign.class);
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
	public ICopy addCopy() {
		TCopy nativeCopy = new TCopy();
		Copy newCopy = getFactory().createCopy(nativeCopy);
		
		this.copy.add(newCopy);
		this.assign.getCopyOrExtensionAssignOperation().add(nativeCopy);
		
		return newCopy;
	}
}
