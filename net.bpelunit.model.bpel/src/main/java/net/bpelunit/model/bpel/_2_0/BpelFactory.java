package net.bpelunit.model.bpel._2_0;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import net.bpelunit.model.bpel.IBpelFactory;
import net.bpelunit.model.bpel.ICompensate;

import org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCompensate;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCompensateScope;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCopy;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TDocumentation;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TEmpty;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TExit;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFlow;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TForEach;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TFrom;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TIf;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TImport;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TInvoke;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TLink;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLink;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPick;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReceive;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TRepeatUntil;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TReply;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TRethrow;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TScope;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TSequence;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TThrow;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TTo;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TValidate;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWait;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TWhile;

public class BpelFactory implements IBpelFactory {

	private static final String NAMESPACE_BPEL_2_0 = "http://docs.oasis-open.org/wsbpel/2.0/process/executable";
	
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	private Process process;

	public BpelFactory() {
		this(ProcessDocument.Factory.newInstance());
	}
	
	public BpelFactory(ProcessDocument nativeProcess) {
		if(nativeProcess.getProcess() == null) {
			nativeProcess.addNewProcess();
		}
		this.process = new Process(nativeProcess, this);
	}

	AbstractActivity<?> createActivity(Object a) {
		if(a == null) {
			return null;
		}
		
		try {
			Method createActitiy = getClass().getMethod("createActivity", a.getClass());
			return (AbstractActivity<?>) createActitiy.invoke(this, a);
		} catch (Exception e) {
			for(Method m : getClass().getMethods()) {
				System.out.print(m.getName() + "(");
				for(Class<?> c :m.getParameterTypes()) {
					System.out.print(c.getCanonicalName() + " ");
				}
				System.out.println(");");
			}
			throw new IllegalArgumentException(
					"Cannot create wrapper for class " + a.getClass(), e);
		}
	}

	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Assign createActivity(TAssign assign) {
		return new Assign(assign, this);
	}

	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Empty createActivity(TEmpty empty) {
		return new Empty(empty, this);
	}

	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Compensate createActivity(TCompensate compensate) {
		return new Compensate(compensate, this);
	}

	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public CompensateScope createActivity(TCompensateScope compensate) {
		return new CompensateScope(compensate, this);
	}

	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Exit createActivity(TExit exit) {
		return new Exit(exit, this);
	}

	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Flow createActivity(TFlow flow) {
		return new Flow(flow, this);
	}

	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public ForEach createActivity(TForEach fe) {
		return new ForEach(fe, this);
	}

	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public If createActivity(TIf i) {
		return new If(i, this);
	}

	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Invoke createActivity(TInvoke i) {
		return new Invoke(i, this);
	}
	
	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Receive createActivity(TReceive r) {
		return new Receive(r, this);
	}
	
	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Pick createActivity(TPick p) {
		return new Pick(p, this);
	}
	
	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public RepeatUntil createActivity(TRepeatUntil r) {
		return new RepeatUntil(r, this);
	}
	
	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Reply createActivity(TReply r) {
		return new Reply(r, this);
	}
	
	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Rethrow createActivity(TRethrow r) {
		return new Rethrow(r, this);
	}

	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Scope createActivity(TScope s) {
		return new Scope(s, this);
	}
	
	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Sequence createActivity(TSequence s) {
		return new Sequence(s, this);
	}
	
	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Throw createActivity(TThrow t) {
		return new Throw(t, this);
	}
	
	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Validate createActivity(TValidate v) {
		return new Validate(v, this);
	}
	
	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public Wait createActivity(TWait w) {
		return new Wait(w, this);
	}
	
	/**
	 * Should not be called directly. Needs to be public for reflection call. 
	 */
	public While createActivity(TWhile w) {
		return new While(w, this);
	}

	Link createLink(TLink l) {
		return new Link(l, this);
	}
	
	Documentation createDocumentation(TDocumentation bpelDoc) {
		return new Documentation(bpelDoc, this);
	}

	public Assign createAssign() {
		return createActivity(TAssign.Factory.newInstance());
	}

	public ICompensate createCompensate() {
		return createActivity(TCompensate.Factory.newInstance());
	}

	public CompensateScope createCompensateScope() {
		return createActivity(TCompensateScope.Factory.newInstance());
	}

	public Empty createEmpty() {
		return createActivity(TEmpty.Factory.newInstance());
	}

	public Exit createExit() {
		return createActivity(TExit.Factory.newInstance());
	}

	public Flow createFlow() {
		return createActivity(TFlow.Factory.newInstance());
	}

	public ForEach createForEach() {
		TForEach forEach = TForEach.Factory.newInstance();
		return createActivity(forEach);
	}

	public If createIf() {
		return createActivity(TIf.Factory.newInstance());
	}

	public Invoke createInvoke() {
		return createActivity(TInvoke.Factory.newInstance());
	}

	public Pick createPick() {
		return createActivity(TPick.Factory.newInstance());
	}

	public Receive createReceive() {
		return createActivity(TReceive.Factory.newInstance());
	}
	
	public RepeatUntil createRepeatUntil() {
		return createActivity(TRepeatUntil.Factory.newInstance());
	}

	public Reply createReply() {
		return createActivity(TReply.Factory.newInstance());
	}

	public Rethrow createRethrow() {
		return createActivity(TRethrow.Factory.newInstance());
	}
	
	public Scope createScope() {
		return createActivity(TScope.Factory.newInstance());
	}
	
	public Sequence createSequence() {
		return createActivity(TSequence.Factory.newInstance());
	}
	
	public Throw createThrow() {
		return createActivity(TThrow.Factory.newInstance());
	}
	
	public Validate createValidate() {
		return createActivity(TValidate.Factory.newInstance());
	}

	public Wait createWait() {
		return createActivity(TWait.Factory.newInstance());
	}
	
	public While createWhile() {
		return createActivity(TWhile.Factory.newInstance());
	}

	public PartnerLink createPartnerLink(TPartnerLink p) {
		return new PartnerLink(p);
	}

	public Import createImport(TImport i) {
		return new Import(i);
	}

	public Process getProcess() {
		return process;
	}

	Variable createVariable(TVariable v) {
		return new Variable(v, this);
	}

	Copy createCopy(TCopy c) {
		return new Copy(c, this);
	}

	To createTo(TTo to) {
		return new To(to);
	}

	From createFrom(TFrom from) {
		return new From(from, this);
	}

	public String getNamespace() {
		return NAMESPACE_BPEL_2_0; 
	}

	void registerClass(Class<?> clazz) {
		try {
			if(clazz.getConstructor() != null) {
				classes.add(clazz);
			}
		} catch (SecurityException e) {
			// cannot add
		} catch (NoSuchMethodException e) {
			// cannot add
		}
	}
}
