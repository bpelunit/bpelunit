package net.bpelunit.model.bpel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface IProcess extends IBpelObject, ISingleContainer, IVisitable, IVariableContainer {

	String getQueryLanguage();

	boolean getSuppressJoinFailure();

	String getName();

	void setName(String value);

	String getTargetNamespace();

	void setTargetNamespace(String value);

	List<?extends IPartnerLink> getPartnerLinks();

	List<?extends IImport> getImports();

	void save(OutputStream out, Class<?>... additionalClasses) throws IOException;

	IPartnerLink addPartnerLink();

	IImport addImport();

	List<IBpelObject> getElementsByXPath(String xpathToBpelElement);
}