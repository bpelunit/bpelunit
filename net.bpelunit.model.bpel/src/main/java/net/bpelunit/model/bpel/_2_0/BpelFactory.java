package net.bpelunit.model.bpel._2_0;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import net.bpelunit.model.bpel.IBpelFactory;
import net.bpelunit.model.bpel.ICompensate;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TAssign;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TCompensate;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TCompensateScope;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TCopy;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TDocumentation;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TEmpty;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TExit;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TFlow;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TForEach;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TFrom;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TIf;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TImport;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TInvoke;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TLink;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TPartnerLink;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TPick;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TProcess;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TReceive;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TRepeatUntil;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TReply;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TRethrow;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TScope;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TSequence;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TThrow;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TTo;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TValidate;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TVariable;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TWait;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TWhile;

public class BpelFactory implements IBpelFactory {

	private static final String NAMESPACE_BPEL_2_0 = "http://docs.oasis-open.org/wsbpel/2.0/process/executable";
	
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	private Process process;

	public BpelFactory() {
		this(new TProcess());
	}
	
	public BpelFactory(TProcess nativeProcess) {
		this.process = new Process(nativeProcess, this);
		registerClass(TProcess.class);
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

	@Override
	public Assign createAssign() {
		return createActivity(new TAssign());
	}

	@Override
	public ICompensate createCompensate() {
		return createActivity(new TCompensate());
	}

	@Override
	public CompensateScope createCompensateScope() {
		return createActivity(new TCompensateScope());
	}

	@Override
	public Empty createEmpty() {
		return createActivity(new TEmpty());
	}

	@Override
	public Exit createExit() {
		return createActivity(new TExit());
	}

	@Override
	public Flow createFlow() {
		return createActivity(new TFlow());
	}

	@Override
	public ForEach createForEach() {
		TForEach forEach = new TForEach();
		forEach.setScope(new TScope());
		return createActivity(forEach);
	}

	@Override
	public If createIf() {
		return createActivity(new TIf());
	}

	@Override
	public Invoke createInvoke() {
		return createActivity(new TInvoke());
	}

	@Override
	public Pick createPick() {
		return createActivity(new TPick());
	}

	@Override
	public Receive createReceive() {
		return createActivity(new TReceive());
	}
	
	@Override
	public RepeatUntil createRepeatUntil() {
		return createActivity(new TRepeatUntil());
	}

	@Override
	public Reply createReply() {
		return createActivity(new TReply());
	}

	@Override
	public Rethrow createRethrow() {
		return createActivity(new TRethrow());
	}
	
	@Override
	public Scope createScope() {
		return createActivity(new TScope());
	}
	
	@Override
	public Sequence createSequence() {
		return createActivity(new TSequence());
	}
	
	@Override
	public Throw createThrow() {
		return createActivity(new TThrow());
	}
	
	@Override
	public Validate createValidate() {
		return createActivity(new TValidate());
	}

	@Override
	public Wait createWait() {
		return createActivity(new TWait());
	}
	
	@Override
	public While createWhile() {
		return createActivity(new TWhile());
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

	@Override
	public String getNamespace() {
		return NAMESPACE_BPEL_2_0; 
	}

	public JAXBContext getJAXBContext() throws JAXBException {
		return JAXBContext.newInstance(classes.toArray(new Class<?>[0]));
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
