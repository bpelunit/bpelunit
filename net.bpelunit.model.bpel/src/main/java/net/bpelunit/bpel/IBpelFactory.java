package net.bpelunit.bpel;




public interface IBpelFactory {

	IAssign createAssign();
	ICompensate createCompensate();
	ICompensateScope createCompensateScope();
	IEmpty createEmpty();
	IExit createExit();
	IFlow createFlow();
	IForEach createForEach();
	IIf createIf();
	IInvoke createInvoke();
	IPick createPick();
	IReceive createReceive();
	IRepeatUntil createRepeatUntil();
	IReply createReply();
	IRethrow createRethrow();
	IScope createScope();
	ISequence createSequence();
	IThrow createThrow();
	IValidate createValidate();
	IWait createWait();
	IWhile createWhile();
	
	String getNamespace();
}
