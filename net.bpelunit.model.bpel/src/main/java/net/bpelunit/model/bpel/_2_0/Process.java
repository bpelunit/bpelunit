package net.bpelunit.model.bpel._2_0;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IImport;
import net.bpelunit.model.bpel.IOnAlarmEventHandler;
import net.bpelunit.model.bpel.IPartnerLink;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.model.bpel.IVisitor;

import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TBoolean;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TDocumentation;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TImport;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnAlarmEvent;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnEvent;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLink;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;

class Process extends AbstractSingleContainer<TProcess> implements IProcess {

	private ProcessDocument processDoc;
	private TProcess process;
	private List<PartnerLink> partnerLinks = new ArrayList<PartnerLink>();
	private List<Import> imports = new ArrayList<Import>();
	private List<Variable> variables = new ArrayList<Variable>();
	private List<OnAlarmEventHandler> onAlarms = new ArrayList<OnAlarmEventHandler>();
	private List<OnMessageHandler> onMessages = new ArrayList<OnMessageHandler>();
	private List<Documentation> documentations = new ArrayList<Documentation>();
	
	Process(ProcessDocument newProcess) {
		super(newProcess.getProcess(), null);
		
		this.processDoc = newProcess;
		this.process = processDoc.getProcess();
		
		if (process.getPartnerLinks() != null) {
			for (TPartnerLink p : process.getPartnerLinks().getPartnerLinkArray()) {
				partnerLinks.add(new PartnerLink(p));
			}
		}

		for (TImport i : process.getImportArray()) {
			imports.add(new Import(i));
		}

		if(process.getVariables() == null) {
			process.addNewVariables();
		}
		if (process.getVariables().getVariableArray() == null) {
			process.addNewVariables();
		}
		for (TVariable v : process.getVariables().getVariableArray()) {
			variables.add(new Variable(v));
		}
		
		if(process.getEventHandlers() != null) {
			for(TOnAlarmEvent a : process.getEventHandlers().getOnAlarmArray()) {
				this.onAlarms.add(new OnAlarmEventHandler(a, this));
			}
			for(TOnEvent e : process.getEventHandlers().getOnEventArray()) {
				this.onMessages.add(new OnMessageHandler(e, this));
			}
		}
		
		for(TDocumentation d : process.getDocumentationArray()) {
			documentations.add(new Documentation(d));
		}
	}

	public String getName() {
		return process.getName();
	}

	public String getTargetNamespace() {
		return process.getTargetNamespace();
	}

	public String getQueryLanguage() {
		return process.getQueryLanguage();
	}
	
	@Override
	public void setQueryLanguage(String queryLanguageUrn) {
		this.process.setQueryLanguage(queryLanguageUrn);	
	}

	public boolean getSuppressJoinFailure() {
		return process.getSuppressJoinFailure().equals(TBoolean.YES);
	}
	
	@Override
	public void setSuppressJoinFailure(boolean value) {
		process.setSuppressJoinFailure(TBooleanHelper.convert(value));
	}

	public void setName(String value) {
		process.setName(value);
	}
	
	@Override
	public String getExpressionLanguage() {
		return process.getExpressionLanguage();
	}
	
	@Override
	public void setExpressionLanguage(String expressionLanguageUrn) {
		process.setExpressionLanguage(expressionLanguageUrn);
	}

	public void setTargetNamespace(String value) {
		process.setTargetNamespace(value);
	}

	public List<? extends IPartnerLink> getPartnerLinks() {
		return Collections.unmodifiableList(this.partnerLinks);
	}

	public List<Import> getImports() {
		return Collections.unmodifiableList(this.imports);
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		for(PartnerLink pl : partnerLinks) {
			pl.visit(v);
		}
		for(Variable var : variables) {
			var.visit(v);
		}
		for(Import imp : imports) {
			imp.visit(v);
		}
		for(OnAlarmEventHandler a : onAlarms) {
			a.visit(v);
		}
		for(OnMessageHandler a : onMessages) {
			a.visit(v);
		}
		
		getMainActivity().visit(v);
	}

	public List<? extends IVariable> getVariables() {
		return Collections.unmodifiableList(this.variables);
	}

	public IVariable addVariable() {
		TVariable nativeVariable = process.getVariables().addNewVariable();
		
		Variable variable = new Variable(nativeVariable);
		this.variables.add(variable);
		
		return variable;
	}

	public IPartnerLink addPartnerLink() {
		guaranteePartnerLinks(this.process);
		TPartnerLink nativePartnerLink = process.getPartnerLinks().addNewPartnerLink();
		
		PartnerLink partnerLink = new PartnerLink(nativePartnerLink);
		this.partnerLinks.add(partnerLink);
		
		return partnerLink;
	}
	
	private void guaranteePartnerLinks(TProcess p) {
		if(!p.isSetPartnerLinks()) {
			p.addNewPartnerLinks();
		}
	}

	public void save(OutputStream out, Class<?>... additionalClasses) throws IOException {
		try {
			XmlOptions options = new XmlOptions();
			options.setSavePrettyPrint();
			
			Map<String, String> suggestedPrefixes = new HashMap<String, String>();
			suggestedPrefixes.put(BpelFactory.INSTANCE.getNamespace(), "bpel");
			options.setSaveSuggestedPrefixes(suggestedPrefixes);
			
			processDoc.save(out, options);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	public IImport addImport() {
		TImport nativeImport = process.addNewImport();;

		Import imp = new Import(nativeImport);
		this.imports.add(imp);
		
		return imp;
	}

	public List<IBpelObject> getElementsByXPath(String xpathToBpelElement) {
		
		XmlObject[] results = process.selectPath(xpathToBpelElement);
		
		List<IBpelObject> retval = new ArrayList<IBpelObject>();
		for(XmlObject o : results){
			IBpelObject bo = getObjectForNativeObject(o);
			if(bo != null) {
				retval.add(bo);
			} else {
				throw new RuntimeException("No object mapping for " + o + " evaluated from " + xpathToBpelElement);
			}
		}
		
		return retval;
	}

	@Override
	IBpelObject getObjectForNativeObject(Object nativeObject) {
		if(getMainActivity() != null) {
			return getMainActivity().getObjectForNativeObject(nativeObject);
		} else {
			return null;
		}
	}

	@Override
	public List<OnAlarmEventHandler> getOnAlarms() {
		return new ArrayList<OnAlarmEventHandler>(onAlarms);
	}

	@Override
	public List<OnMessageHandler> getOnMessages() {
		return new ArrayList<OnMessageHandler>(onMessages);
	}

	@Override
	public IOnAlarmEventHandler addNewOnAlarm() {
		createEventHandlersIfNecessary();
		
		TOnAlarmEvent nativeOnAlarm = process.getEventHandlers().addNewOnAlarm();
		OnAlarmEventHandler onAlarm = new OnAlarmEventHandler(nativeOnAlarm, this);
		onAlarms.add(onAlarm);
		
		return onAlarm;
	}

	private void createEventHandlersIfNecessary() {
		if(process.getEventHandlers() == null) {
			process.addNewEventHandlers();
		}
	}

	@Override
	public OnMessageHandler addNewOnMessage() {
		createEventHandlersIfNecessary();
		
		TOnEvent nativeOnMessage = process.getEventHandlers().addNewOnEvent();
		OnMessageHandler onMessage = new OnMessageHandler(nativeOnMessage, this);
		onMessages.add(onMessage);
		
		return onMessage;
	}
	
	@Override
	public boolean getExitOnStandardFault() {
		return process.getExitOnStandardFault().equals(TBoolean.YES);
	}
	
	@Override
	public void setExitOnStandardFault(boolean value) {
		process.setExitOnStandardFault(TBooleanHelper.convert(value));
	}
}

