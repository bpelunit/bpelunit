package net.bpelunit.model.bpel;

public interface IImport extends IBpelObject {

	public static final String IMPORTTYPE_WSDL = "http://schemas.xmlsoap.org/wsdl/";
	public static final String IMPORTTYPE_XSD = "http://www.w3.org/2001/XMLSchema";
	
	String getNamespace();

	void setNamespace(String newNamespace);

	String getLocation();

	void setLocation(String newLocation);

	String getImportType();

	void setImportType(String newImportType);

	boolean isWsdlImport();

}
