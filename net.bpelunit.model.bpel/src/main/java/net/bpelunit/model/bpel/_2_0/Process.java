package net.bpelunit.model.bpel._2_0;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelObject;
import net.bpelunit.model.bpel.IImport;
import net.bpelunit.model.bpel.IPartnerLink;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel.IVariable;
import net.bpelunit.model.bpel.IVisitor;

import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.oasisOpen.docs.wsbpel.x20.process.executable.ProcessDocument;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TAssign;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TBoolean;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TImport;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TPartnerLink;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TProcess;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TVariable;

class Process extends AbstractBpelObject implements IProcess {

	private ProcessDocument processDoc;
	private TProcess process;
	private AbstractActivity<?> mainActivity;
	private List<PartnerLink> partnerLinks = new ArrayList<PartnerLink>();
	private List<Import> imports = new ArrayList<Import>();
	private List<Variable> variables = new ArrayList<Variable>();
	private BpelFactory factory;

	Process(ProcessDocument newProcess, BpelFactory f) {
		super(newProcess.getProcess(), null);
		this.factory = f;
		
		this.processDoc = newProcess;
		this.process = processDoc.getProcess();
		
		org.oasisOpen.docs.wsbpel.x20.process.executable.TActivity childActivity = TComplexContainerHelper
				.getChildActivity(process);
		
		this.setMainActivity(f.createActivity(childActivity));

		if (process.getPartnerLinks() != null) {
			for (TPartnerLink p : process.getPartnerLinks().getPartnerLinkArray()) {
				partnerLinks.add(getFactory().createPartnerLink(p));
			}
		}

		for (TImport i : process.getImportArray()) {
			imports.add(getFactory().createImport(i));
		}

		if(process.getVariables() == null) {
			process.addNewVariables();
		}
		if (process.getVariables().getVariableArray() == null) {
			process.addNewVariables();
		}
		for (TVariable v : process.getVariables().getVariableArray()) {
			variables.add(getFactory().createVariable(v));
		}
	}

	@Override
	public BpelFactory getFactory() {
		return factory;
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

	public boolean getSuppressJoinFailure() {
		return process.getSuppressJoinFailure().equals(TBoolean.YES);
	}

	public void setName(String value) {
		process.setName(value);
	}

	public void setTargetNamespace(String value) {
		process.setTargetNamespace(value);
	}

	public void setMainActivity(IActivity a) {
		checkForCorrectModel(a);
		AbstractActivity<?> activity = ((AbstractActivity<?>) a);
		TComplexContainerHelper.removeMainActivity(process);
		if (activity != null) {
			TAssign tempActivity = process.addNewAssign();
			XmlObject newNativeActivity = tempActivity.set(activity.getNativeActivity());
			activity.setNativeActivity(newNativeActivity);
		}
		this.mainActivity = activity;
	}

	public AbstractActivity<?> getMainActivity() {
		return this.mainActivity;
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
		getMainActivity().visit(v);
	}

	public List<? extends IVariable> getVariables() {
		return Collections.unmodifiableList(this.variables);
	}

	public IVariable addVariable() {
		TVariable nativeVariable = process.getVariables().addNewVariable();
		
		Variable variable = getFactory().createVariable(nativeVariable);
		this.variables.add(variable);
		
		return variable;
	}

	public IPartnerLink addPartnerLink() {
		guaranteePartnerLinks(this.process);
		TPartnerLink nativePartnerLink = process.getPartnerLinks().addNewPartnerLink();
		
		PartnerLink partnerLink = getFactory().createPartnerLink(nativePartnerLink);
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
			suggestedPrefixes.put(factory.getNamespace(), "bpel");
			options.setSaveSuggestedPrefixes(suggestedPrefixes);
			
			processDoc.save(out, options);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}
	
	public IImport addImport() {
		TImport nativeImport = process.addNewImport();;

		Import imp = getFactory().createImport(nativeImport);
		this.imports.add(imp);
		
		return imp;
	}

	public List<IBpelObject> getElementsByXPath(String xpathToBpelElement) {
		
		String xpath = "declare namespace bpel='"+ factory.getNamespace() + "' " + xpathToBpelElement;
				
		XmlObject[] results = process.selectPath(xpath);
		
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
		if(mainActivity != null) {
			return mainActivity.getObjectForNativeObject(nativeObject);
		} else {
			return null;
		}
	}
}
