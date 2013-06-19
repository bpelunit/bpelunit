package net.bpelunit.framework.coverage.instrumentation.branch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.coverage.instrumentation.AbstractInstrumenter;
import net.bpelunit.framework.coverage.marker.Marker;
import net.bpelunit.framework.coverage.result.IMetricCoverage;
import net.bpelunit.model.bpel.IActivity;
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
import net.bpelunit.model.bpel.IWait;
import net.bpelunit.model.bpel.IWhile;

public class BranchCoverageInstrumenter extends AbstractInstrumenter {

	
	private Map<String, String> markerMapping = new HashMap<String, String>();
	private Map<String, Integer> markerCounter = new HashMap<String, Integer>();
	private List<String> markers = new ArrayList<String>();
	
	@Override
	public String getMarkerPrefix()	{
		return "BRANCH_MARKER";
	}
	
	@Override
	public void pushMarker(String markerName) {
		Integer markerCount = markerCounter.get(markerName);
		if(markerCount != null) {
			markerCount++;
			markerCounter.put(markerName, markerCount);
		}
	}

	@Override
	public IMetricCoverage getCoverageResult() {
		return new BranchMetricCoverage(markers, markerMapping, markerCounter);
	}
	
	private void instrumentActivity(IActivity a, String xpathToBranch) {
		Marker newMarker = addCoverageMarker(a);
		
		markerMapping.put(newMarker.getName(), xpathToBranch);
		markerCounter.put(newMarker.getName(), 0);
		markers.add(newMarker.getName());
	}
	
	/*---- Visitor Functions ----*/
	
	public void visit(IAssign a) {
	}

	public void visit(ICompensate a) {
	}

	public void visit(ICompensateScope a) {
	}

	public void visit(IEmpty a) {
	}

	public void visit(IExit a) {
	}

	public void visit(IFlow a) {
	}

	public void visit(IForEach a) {
		instrumentActivity(a.getScope(), a.getXPathInDocument());
	}

	public void visit(IIf a) {
		instrumentActivity(a.getMainActivity(), a.getXPathInDocument());
		for(IElseIf i : a.getElseIfs()) {
			instrumentActivity(i.getMainActivity(), i.getXPathInDocument());
		}
		if(a.getElse() == null) {
			IElse e = a.setNewElse();
			e.setNewEmpty();
		}
		instrumentActivity(a.getElse().getMainActivity(), a.getElse().getXPathInDocument());
	}

	public void visit(IInvoke a) {
	}

	public void visit(IPick a) {
		// TODO Structured Activity		
	}

	public void visit(IProcess a) {
	}

	public void visit(IReceive a) {
	}

	public void visit(IRepeatUntil a) {
		// TODO Structured Activity		
	}

	public void visit(IReply a) {
	}

	public void visit(IRethrow a) {
	}

	public void visit(IScope a) {
	}

	public void visit(ISequence a) {
	}

	public void visit(IThrow a) {
	}

	public void visit(IValidate a) {
	}

	public void visit(IWait a) {
	}

	public void visit(IWhile a) {
		// TODO Structured Activity		
	}

	public void visit(IOnAlarm a) {
	}

	public void visit(IOnMessage a) {
	}

	public void visit(ICopy c) {
	}

	public void visit(IImport i) {
	}

	public void visit(IPartnerLink pl) {
	}

	public void visit(IVariable var) {
	}

	public void visit(ICompensationHandler compensationHandler) {
	}

	public void visit(IOnMessageHandler onMessageHandler) {
	}

	public void visit(IElseIf elseIf) {
		// Covered by IIf
	}

	public void visit(IElse else1) {
		// Covered by IIf
	}

	public void visit(ILink link) {
	}

	public void visit(ICatch ccatch) {
	}

	public void visit(ICatchAll catchAll) {
	}
	
	public void visit(IOnAlarmEventHandler onAlarmEventHandler) {
	}
	
}
