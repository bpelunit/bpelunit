package net.bpelunit.utils.bpelstats;

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

public class StatisticGathererVisitor implements IVisitor {

	private int countAssign;

	private int countCatch;

	private int countCatchAll;

	private int countCompensate;

	private int countCompensateScope;

	private int countCompensationHandler;

	private int countCopy;

	private int countElse;

	private int countElseIf;

	private int countEmpty;

	private int countExit;

	private int countFlow;

	private int countForEach;

	private int countIf;

	private int countInvoke;

	private int countLink;

	private int countOnAlarm;
	
	private int countOnAlarmHandler;

	private int countOnMessage;

	private int countOnMessageHandler;

	private int countPartnerLink;

	private int countPick;

	private int countReceive;

	private int countRepeatUntil;

	private int countReply;

	private int countRethrow;

	private int countScope;

	private int countSequence;

	private int countThrow;

	private int countValidate;

	private int countVariable;

	private int countWait;

	private int countWhile;

	public int getCountAssign() {
		return countAssign;
	}

	public int getCountCatch() {
		return countCatch;
	}

	public int getCountCatchAll() {
		return countCatchAll;
	}

	public int getCountCompensate() {
		return countCompensate;
	}

	public int getCountCompensateScope() {
		return countCompensateScope;
	}

	public int getCountCompensationHandler() {
		return countCompensationHandler;
	}

	public int getCountCopy() {
		return countCopy;
	}

	public int getCountElse() {
		return countElse;
	}

	public int getCountElseIf() {
		return countElseIf;
	}

	public int getCountEmpty() {
		return countEmpty;
	}

	public int getCountExit() {
		return countExit;
	}

	public int getCountFlow() {
		return countFlow;
	}

	public int getCountForEach() {
		return countForEach;
	}

	public int getCountIf() {
		return countIf;
	}

	public int getCountInvoke() {
		return countInvoke;
	}

	public int getCountLink() {
		return countLink;
	}

	public int getCountOnAlarm() {
		return countOnAlarm;
	}

	public int getCountOnAlarmHandler() {
		return countOnAlarmHandler;
	}

	public int getCountOnMessage() {
		return countOnMessage;
	}

	public int getCountOnMessageHandler() {
		return countOnMessageHandler;
	}

	public int getCountPartnerLink() {
		return countPartnerLink;
	}

	public int getCountPick() {
		return countPick;
	}

	public int getCountReceive() {
		return countReceive;
	}

	public int getCountRepeatUntil() {
		return countRepeatUntil;
	}

	public int getCountReply() {
		return countReply;
	}

	public int getCountRethrow() {
		return countRethrow;
	}

	public int getCountScope() {
		return countScope;
	}

	public int getCountSequence() {
		return countSequence;
	}

	public int getCountThrow() {
		return countThrow;
	}

	public int getCountValidate() {
		return countValidate;
	}

	public int getCountVariable() {
		return countVariable;
	}

	public int getCountWait() {
		return countWait;
	}

	public int getCountWhile() {
		return countWhile;
	}

	public int getCountAllActivities() {
		return countAssign + countCatch + countCatchAll + countCompensate
				+ countCompensateScope + countCompensationHandler + countCopy
				+ countElse + countElseIf + countEmpty + countExit + countFlow
				+ countForEach + countIf + countInvoke + countOnAlarm
				+ countOnMessage + countOnMessageHandler + countPick
				+ countReceive + countRepeatUntil + countReply + countRethrow
				+ countScope + countSequence + countThrow + countValidate;
	}

	public int getCountBasicActivities() {
		return countAssign + countCompensate + countCompensateScope
				+ countEmpty + countExit + countInvoke + countReceive
				+ countReply + countRethrow + countThrow + countValidate;
	}

	public int getCountStructuredActivities() {
		return countCatch + countCatchAll + countCompensationHandler
				+ countFlow + countForEach + countIf
				+ countOnAlarmHandler
				+ countOnMessageHandler + countPick + countRepeatUntil
				+ countScope + countSequence;
	}

	public int getCountNonLinearActivities() {
		return countCatch + countCatchAll + countCompensate
				+ countCompensateScope + countCompensationHandler 
				+ countElse + countElseIf + countExit + countFlow
				+ countForEach + countIf + countOnAlarm
				+ countOnMessage + countOnMessageHandler + countPick
				+ countReceive + countRepeatUntil + countRethrow
				+ countThrow;
	}

	
	public void visit(IAssign a) {
		countAssign++;
	}

	public void visit(ICatch ccatch) {
		countCatch++;
	}

	public void visit(ICatchAll catchAll) {
		countCatchAll++;
	}

	public void visit(ICompensate a) {
		countCompensate++;
	}

	public void visit(ICompensateScope a) {
		countCompensateScope++;
	}

	public void visit(ICompensationHandler compensationHandler) {
		countCompensationHandler++;
	}

	public void visit(ICopy c) {
		countCopy++;
	}

	public void visit(IElse else1) {
		countElse++;
	}

	public void visit(IElseIf elseIf) {
		countElseIf++;
	}

	public void visit(IEmpty a) {
		countEmpty++;
	}

	public void visit(IExit a) {
		countExit++;
	}

	public void visit(IFlow a) {
		countFlow++;
	}

	public void visit(IForEach a) {
		countForEach++;
	}

	public void visit(IIf a) {
		countIf++;
	}

	public void visit(IImport i) {
	}

	public void visit(IInvoke a) {
		countInvoke++;
	}

	public void visit(ILink link) {
		countLink++;
	}

	public void visit(IOnAlarm a) {
		countOnAlarm++;
	}

	public void visit(IOnMessage a) {
		countOnMessage++;
	}

	public void visit(IOnMessageHandler onMessageHandler) {
		countOnMessageHandler++;
	}

	public void visit(IPartnerLink pl) {
		countPartnerLink++;
	}

	public void visit(IPick a) {
		countPick++;
	}

	public void visit(IProcess a) {
	}

	public void visit(IReceive a) {
		countReceive++;
	}

	public void visit(IRepeatUntil a) {
		countRepeatUntil++;
	}

	public void visit(IReply a) {
		countReply++;
	}

	public void visit(IRethrow a) {
		countRethrow++;
	}

	public void visit(IScope a) {
		countScope++;
	}

	public void visit(ISequence a) {
		countSequence++;
	}

	public void visit(IThrow a) {
		countThrow++;
	}

	public void visit(IValidate a) {
		countValidate++;
	}

	public void visit(IVariable var) {
		countVariable++;
	}

	public void visit(IWait a) {
		countWait++;
	}

	public void visit(IWhile a) {
		countWhile++;
	}
	
	public void visit(IOnAlarmEventHandler onAlarmEventHandler) {
		countOnAlarmHandler++;
	}

}
