package net.bpelunit.model.bpel;


public interface IVisitor {
	void visit(IAssign a);
	void visit(ICompensate a);
	void visit(ICompensateScope a);
	void visit(ICopy c);
	void visit(IEmpty a);
	void visit(IExit a);
	void visit(IFlow a);
	void visit(IForEach a);
	void visit(IIf a);
	void visit(IImport i);
	void visit(IInvoke a);
	void visit(IOnAlarm a);
	void visit(IOnMessage a);
	void visit(IPartnerLink pl);
	void visit(IPick a);
	void visit(IProcess a);
	void visit(IReceive a);
	void visit(IRepeatUntil a);
	void visit(IReply a);
	void visit(IRethrow a);
	void visit(IScope a);
	void visit(ISequence a);
	void visit(IThrow a);
	void visit(IValidate a);
	void visit(IVariable var);
	void visit(IWait a);
	void visit(IWhile a);
	void visit(ICompensationHandler compensationHandler);
	void visit(IOnMessageHandler onMessageHandler);
	void visit(IElseIf elseIf);
	void visit(IElse else1);
	void visit(ILink link);
	void visit(ICatch ccatch);
	void visit(ICatchAll catchAll);
	void visit(IOnAlarmEventHandler onAlarmEventHandler);
}
