package net.bpelunit.model.bpel;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public interface IProcess extends IBpelObject, ISingleContainer, IVisitable, IVariableContainer,IEventHandlerHolder {

	String URN_XPATH_1_0_IN_BPEL_2_0 = "urn:oasis:names:tc:wsbpel:2.0:sublang:xpath1.0";

	String getQueryLanguage();
	void setQueryLanguage(String queryLanguageUrn);

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
	
	String getExpressionLanguage();
	void setExpressionLanguage(String expressionLanguageUrn);

	boolean getExitOnStandardFault();
	void setExitOnStandardFault(boolean value);
}