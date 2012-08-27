package net.bpelunit.bpel;

public interface IVisitor {
	void visit(IAssign a);
	void visit(ICompensate a);
	void visit(ICompensateScope a);
	void visit(IEmpty a);
	void visit(IExit a);
	void visit(IFlow a);
	void visit(IForEach a);
	void visit(IIf a);
	void visit(IInvoke a);
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
	void visit(IWait a);
	void visit(IWhile a);
}
