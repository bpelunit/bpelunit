package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.bpelunit.model.bpel.IIf;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TElseif;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;

public class If extends AbstractSingleContainer<TIf> implements IIf {

	private List<ElseIf> elseIfs = new ArrayList<ElseIf>();
	private Else eelse;
	private Expression condition;

	public If(TIf i, IContainer parent) {
		super(i, parent);
		
		setNativeObject(i);
		
		if(i.isSetElse()) {
			eelse = new Else(i.getElse(), this);
		}
		
		for(TElseif elseIf : i.getElseifArray()) {
			ElseIf e = new ElseIf(elseIf, this);
			elseIfs.add(e);
		}
	}

	void setNativeObjectInternal(Object nativeIf) {
		super.setNativeObject(nativeIf);
		TIf i = (TIf)nativeIf;
		elseIfs.clear();
		for(TElseif nativeElseIf : i.getElseifArray()) {
			elseIfs.add(new ElseIf(nativeElseIf, this));
		}
		if(i.getElse() != null) {
			eelse = new Else(i.getElse(), this);
		}
		if(i.getCondition() == null) {
			i.addNewCondition();
		}
		condition = new Expression(i.getCondition());
	}

	public boolean isBasicActivity() {
		return false;
	}
	
	@Override
	public ElseIf addNewElseIf() {
		ElseIf elseIf = new ElseIf(getNativeActivity().addNewElseif(), this);
		elseIfs.add(elseIf);
		return elseIf;
	}
	
	@Override
	public List<ElseIf> getElseIfs() {
		return Collections.unmodifiableList(elseIfs);
	}
	
	@Override
	public Expression getCondition() {
		return condition;
	}
	
	
	@Override
	public Else setNewElse() {
		if(getNativeActivity().getElse() != null) {
			getNativeActivity().unsetElse();
		}
		
		eelse = new Else(getNativeActivity().addNewElse(), this);
		return eelse;
	}
	
	@Override
	public Else getElse() {
		return eelse;
	}	
	
	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		if(getMainActivity() != null) {
			getMainActivity().visit(v);
		}
		for(ElseIf elseIf : elseIfs) {
			elseIf.visit(v);
		}
		if(eelse != null) {
			eelse.visit(v);
		}
	}
}
