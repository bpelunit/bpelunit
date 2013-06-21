package usagetests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.bpelunit.model.bpel.IAssign;
import net.bpelunit.model.bpel.ICatch;
import net.bpelunit.model.bpel.ICatchAll;
import net.bpelunit.model.bpel.ICompensate;
import net.bpelunit.model.bpel.ICompensateScope;
import net.bpelunit.model.bpel.ICompensationHandler;
import net.bpelunit.model.bpel.ICopy;
import net.bpelunit.model.bpel.IElse;
import net.bpelunit.model.bpel.IElseIf;
import net.bpelunit.model.bpel.IEmpty;
import net.bpelunit.model.bpel.IExit;
import net.bpelunit.model.bpel.IFlow;
import net.bpelunit.model.bpel.IForEach;
import net.bpelunit.model.bpel.IIf;
import net.bpelunit.model.bpel.IImport;
import net.bpelunit.model.bpel.IInvoke;
import net.bpelunit.model.bpel.ILink;
import net.bpelunit.model.bpel.IOnAlarm;
import net.bpelunit.model.bpel.IOnAlarmEventHandler;
import net.bpelunit.model.bpel.IOnMessage;
import net.bpelunit.model.bpel.IOnMessageHandler;
import net.bpelunit.model.bpel.IPartnerLink;
import net.bpelunit.model.bpel.IPick;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IReceive;
import net.bpelunit.model.bpel.IRepeatUntil;
import net.bpelunit.model.bpel.IReply;
import net.bpelunit.model.bpel.IRethrow;
import net.bpelunit.model.bpel.IScope;
import net.bpelunit.model.bpel.ISequence;
import net.bpelunit.model.bpel.IThrow;
import net.bpelunit.model.bpel.IValidate;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.model.bpel.IVisitor;
import net.bpelunit.model.bpel.IWait;
import net.bpelunit.model.bpel.IWhile;

public class GatheringVisitor implements IVisitor {

	private List<Object> objectsToVisit = new ArrayList<Object>();
	private List<Object> additionalObjects = new ArrayList<Object>();
	
	public GatheringVisitor(IProcess p, Object... activities) {
		objectsToVisit.add(p);
		objectsToVisit.addAll(Arrays.asList(activities));
	}

	
	public List<Object> getRemainingObjects() {
		return objectsToVisit;
	}
	
	public List<Object> getAdditionalObjects() {
		return additionalObjects;
	}

	private void processVisitedObject(Object o) {
		if(objectsToVisit.contains(o)) {
			objectsToVisit.remove(o);
		} else {
			additionalObjects.add(o);
		}
	}
	
	@Override
	public void visit(IAssign a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(ICompensate a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(ICompensateScope a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IEmpty a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IExit a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IFlow a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IForEach a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IIf a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IInvoke a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IOnAlarm a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IOnMessage a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IPick a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IProcess a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IReceive a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IRepeatUntil a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IReply a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IRethrow a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IScope a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(ISequence a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IThrow a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IValidate a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IWait a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IWhile a) {
		processVisitedObject(a);
	}

	@Override
	public void visit(IPartnerLink pl) {
		processVisitedObject(pl);
	}
	
	@Override
	public void visit(IVariable var) {
		processVisitedObject(var);
	}
	
	@Override
	public void visit(IImport i) {
		processVisitedObject(i);	
	}
	
	@Override
	public void visit(ICopy c) {
		processVisitedObject(c);
	}


	@Override
	public void visit(ICompensationHandler compensationHandler) {
		processVisitedObject(compensationHandler);
	}
	
	@Override
	public void visit(IOnMessageHandler onMessageHandler) {
		processVisitedObject(onMessageHandler);
	}
	
	@Override
	public void visit(IElseIf elseIf) {
		processVisitedObject(elseIf);
	}

	@Override
	public void visit(IElse eelse) {
		processVisitedObject(eelse);
	}
	
	@Override
	public void visit(ILink link) {
		processVisitedObject(link);
	}


	@Override
	public void visit(ICatch ccatch) {
		processVisitedObject(ccatch);
	}


	@Override
	public void visit(ICatchAll catchAll) {
		processVisitedObject(catchAll);
	}


	@Override
	public void visit(IOnAlarmEventHandler onAlarmEventHandler) {
		processVisitedObject(onAlarmEventHandler);
	}
}