package net.bpelunit.bpel._2_0;

import java.util.Collections;
import java.util.List;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TDocumentation;

import net.bpelunit.bpel.IDocumentation;

public class Documentation implements IDocumentation {

	private TDocumentation documentation;
	
	public Documentation(TDocumentation wrappedDocumentation) {
		this.documentation = wrappedDocumentation;
	}
	
	@Override
	public List<Object> getDocumentationElements() {
		return Collections.unmodifiableList(documentation.getContent());
	}

	@Override
	public void setDocumentationElement(String doc) {
		documentation.getContent().clear();
		documentation.getContent().add(doc);
	}

	@Override
	public void setDocumentationElements(List<Object> e) {
		documentation.getContent().clear();
		documentation.getContent().addAll(e);
	}
}
