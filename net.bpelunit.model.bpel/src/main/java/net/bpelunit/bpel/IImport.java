package net.bpelunit.bpel;

public interface IImport {

	public static final String IMPORTTYPE_WSDL = "http://schemas.xmlsoap.org/wsdl/";
	
	String getNamespace();

	void setNamespace(String newNamespace);

	String getLocation();

	void setLocation(String newLocation);

	String getImportType();

	void setImportType(String newImportType);

	boolean isWsdlImport();

}
