package net.bpelunit.bpel._2_0;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import net.bpelunit.bpel.IActivity;
import net.bpelunit.bpel.IImport;
import net.bpelunit.bpel.IPartnerLink;
import net.bpelunit.bpel.IProcess;
import net.bpelunit.bpel.IVariable;
import net.bpelunit.bpel.IVisitor;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TActivity;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TBoolean;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TImport;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TPartnerLink;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TPartnerLinks;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TProcess;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TVariable;
import org.oasis_open.docs.wsbpel._2_0.process.executable.TVariables;

class Process extends AbstractBpelObject implements IProcess {

	private TProcess process;
	private AbstractActivity<?> mainActivity;
	private List<PartnerLink> partnerLinks = new ArrayList<PartnerLink>();
	private List<Import> imports = new ArrayList<Import>();
	private List<Variable> variables = new ArrayList<Variable>();

	Process(TProcess newProcess) {
		super(newProcess);
		TActivity childActivity = TComplexContainerHelper
				.getChildActivity(newProcess);

		this.process = newProcess;
		this.setMainActivity(getFactory().createActivity(childActivity));

		if (newProcess.getPartnerLinks() != null) {
			for (TPartnerLink p : newProcess.getPartnerLinks().getPartnerLink()) {
				partnerLinks.add(getFactory().createPartnerLink(p));
			}
		}

		for (TImport i : newProcess.getImport()) {
			imports.add(getFactory().createImport(i));
		}

		if (newProcess.getVariables() == null) {
			newProcess.setVariables(new TVariables());
		}
		for (TVariable v : newProcess.getVariables().getVariable()) {
			variables.add(getFactory().createVariable(v));
		}
	}

	@Override
	public String getName() {
		return process.getName();
	}

	@Override
	public String getTargetNamespace() {
		return process.getTargetNamespace();
	}

	@Override
	public String getQueryLanguage() {
		return process.getQueryLanguage();
	}

	@Override
	public boolean getSuppressJoinFailure() {
		return process.getSuppressJoinFailure().equals(TBoolean.YES);
	}

	@Override
	public void setName(String value) {
		process.setName(value);
	}

	@Override
	public void setTargetNamespace(String value) {
		process.setTargetNamespace(value);
	}

	@Override
	public void setMainActivity(IActivity a) {
		checkForCorrectModel(a);
		AbstractActivity<?> activity = ((AbstractActivity<?>) a);
		TComplexContainerHelper.removeMainActivity(process);
		if (activity != null) {
			TComplexContainerHelper.setActivity(process,
					activity.getNativeActivity());
		} else {
			TComplexContainerHelper.setActivity(process, null);
		}
		this.mainActivity = activity;
	}

	@Override
	public IActivity getMainActivity() {
		return this.mainActivity;
	}

	@Override
	public List<? extends IPartnerLink> getPartnerLinks() {
		return Collections.unmodifiableList(this.partnerLinks);
	}

	@Override
	public List<Import> getImports() {
		return Collections.unmodifiableList(this.imports);
	}

	@Override
	public void visit(IVisitor v) {
		v.visit(this);
		getMainActivity().visit(v);
	}

	@Override
	public List<? extends IVariable> getVariables() {
		return Collections.unmodifiableList(this.variables);
	}

	@Override
	public IVariable addVariable() {
		TVariable nativeVariable = new TVariable();
		Variable variable = getFactory().createVariable(nativeVariable);

		this.process.getVariables().getVariable().add(nativeVariable);
		this.variables.add(variable);
		return variable;
	}

	@Override
	public IPartnerLink addPartnerLink() {
		TPartnerLink nativePartnerLink = new TPartnerLink();
		PartnerLink partnerLink = getFactory().createPartnerLink(nativePartnerLink);
		
		guaranteePartnerLinks(this.process);
		this.process.getPartnerLinks().getPartnerLink().add(nativePartnerLink);
		this.partnerLinks.add(partnerLink);
		
		return partnerLink;
	}
	
	private void guaranteePartnerLinks(TProcess p) {
		if(p.getPartnerLinks() == null) {
			p.setPartnerLinks(new TPartnerLinks());
		}
	}

	@Override
	public void save(OutputStream out) throws IOException {
		JAXBContext ctx;
		try {
			ctx = JAXBContext.newInstance(TProcess.class);
			JAXBElement<TProcess> rootElement = new JAXBElement<TProcess>(
					new QName(getFactory().getNamespace(), "process"),
					TProcess.class, this.process);
			Marshaller marshaller = ctx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
					Boolean.TRUE);
			marshaller.marshal(rootElement, out);
		} catch (JAXBException e) {
			throw new IOException("Could not marshall BPEL process: "
					+ e.getMessage(), e);
		}
	}
	
	@Override
	public IImport addImport() {
		TImport nativeImport = new TImport();
		Import imp = getFactory().createImport(nativeImport);
		
		this.process.getImport().add(nativeImport);
		this.imports.add(imp);
		
		return imp;
	}
}
