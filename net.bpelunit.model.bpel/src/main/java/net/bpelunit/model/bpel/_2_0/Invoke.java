package net.bpelunit.model.bpel._2_0;

import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.ICompensationHandler;
import net.bpelunit.model.bpel.IInvoke;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.model.bpel.IVisitor;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TActivityContainer;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCatch;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;

public class Invoke extends AbstractBasicActivity<TInvoke> implements IInvoke, IContainer {
	private TInvoke invoke;
	private CompensationHandler compensationHandler;
	private List<Catch> catches = new ArrayList<Catch>();
	private CatchAll catchAll;
	
	public Invoke(TInvoke wrappedInvoke, IContainer parent) {
		super(wrappedInvoke, parent);
		setNativeObjectInternal(wrappedInvoke);
	}

	void setNativeObjectInternal(TInvoke nativeInvoke) {
		super.setNativeObject(nativeInvoke);
		this.invoke = (TInvoke)nativeInvoke;
		if(nativeInvoke.isSetCompensationHandler()) {
			compensationHandler = new CompensationHandler(nativeInvoke.getCompensationHandler(), this);
		}
		for(TCatch c : nativeInvoke.getCatchArray()) {
			catches.add(new Catch(c, this));
		}
		if(nativeInvoke.isSetCatchAll()) {
			catchAll = new CatchAll(nativeInvoke.getCatchAll(), this);
		}
	}

	public String getPartnerLink() {
		return invoke.getPartnerLink();
	}

	public void setPartnerLink(String value) {
		invoke.setPartnerLink(value);
	}

	public QName getPortType() {
		return invoke.getPortType();
	}

	public void setPortType(QName value) {
		invoke.setPortType(value);
	}

	public String getOperation() {
		return invoke.getOperation();
	}

	public void setOperation(String value) {
		invoke.setOperation(value);
	}
	
	public String getInputVariable() {
		return invoke.getInputVariable();
	}

	public void setInputVariable(String value) {
		invoke.setInputVariable(value);
	}
	
	public void setInputVariable(IVariable v) {
		setInputVariable(v.getName());
	}

	public String getOutputVariable() {
		return invoke.getOutputVariable();
	}

	public void setOutputVariable(String value) {
		invoke.setOutputVariable(value);
	}
	
	public void setOutputVariable(IVariable v) {
		setOutputVariable(v.getName());
	}
	
	@Override
	public ICompensationHandler setNewCompensationHandler() {
		if(invoke.getCompensationHandler() != null) {
			invoke.unsetCompensationHandler();
			compensationHandler = null;
		}
		
		compensationHandler = new CompensationHandler(invoke.addNewCompensationHandler(), this);
		
		return compensationHandler;
	}

	@Override
	public Catch addNewCatch() {
		TCatch nativeCatch = getNativeActivity().addNewCatch();
		Catch c = new Catch(nativeCatch, this);
		catches.add(c);
		
		return c;
	}
	
	@Override
	public CatchAll setNewCatchAll() {
		if(invoke.getCatchAll() != null) {
			invoke.unsetCatchAll();
			catchAll = null;
		}
		
		TActivityContainer nativeCatchAll = getNativeActivity().addNewCatchAll();
		catchAll = new CatchAll(nativeCatchAll, this);
		
		return catchAll;
	}
	
	@Override
	public ICompensationHandler getCompensationHandler() {
		return compensationHandler;
	}
	
	@Override
	public void visit(IVisitor v) {
		super.visit(v);
		if(compensationHandler != null) {
			compensationHandler.visit(v);
		}
		if(catchAll != null) {
			catchAll.visit(v);
		}
		for(Catch c : catches) {
			c.visit(v);
		}
	}

	@Override
	public IScope wrapActivityInNewScope(IActivity childActivity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ISequence wrapActivityInNewSequence(IActivity childActivity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void unregister(AbstractActivity<?> a) {
		// TODO Auto-generated method stub
		
	}
}
