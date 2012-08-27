package net.bpelunit.bpel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface IProcess extends IBpelObject, ISingleContainer, IVisitable {

	String getQueryLanguage();

	boolean getSuppressJoinFailure();

	String getName();

	void setName(String value);

	String getTargetNamespace();

	void setTargetNamespace(String value);

	void setMainActivity(IActivity a);

	IActivity getMainActivity();

	List<?extends IPartnerLink> getPartnerLinks();

	List<?extends IImport> getImports();

	List<?extends IVariable> getVariables();
	
	IVariable addVariable();

	void save(OutputStream out) throws IOException;

	IPartnerLink addPartnerLink();

	IImport addImport();
}