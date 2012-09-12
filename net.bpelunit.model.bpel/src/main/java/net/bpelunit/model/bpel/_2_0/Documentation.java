package net.bpelunit.model.bpel._2_0;

import java.util.Collections;
import java.util.List;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TDocumentation;

import net.bpelunit.model.bpel.IDocumentation;

public class Documentation implements IDocumentation {

	private TDocumentation documentation;
	private BpelFactory factory;
	
	public Documentation(TDocumentation wrappedDocumentation, BpelFactory f) {
		this.documentation = wrappedDocumentation;
		this.factory = f;
	}
	
	@Override
	public List<Object> getDocumentationElements() {
		return Collections.unmodifiableList(documentation.getContent());
	}

	@Override
	public void setDocumentationElement(Object doc) {
		documentation.getContent().clear();
		documentation.getContent().add(doc);
		factory.registerClass(doc.getClass());
	}

	@Override
	public void setDocumentationElements(List<Object> e) {
		documentation.getContent().clear();
		if(e != null) {
			documentation.getContent().addAll(e);
			for(Object o : e) {
				factory.registerClass(o.getClass());
			}
		}
	}
}
