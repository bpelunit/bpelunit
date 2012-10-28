package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IImport;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TImport;

public class Import extends AbstractBpelObject implements IImport {

	private static final String NAMESPACE_WSDL = "http://schemas.xmlsoap.org/wsdl/";
	private TImport im;

	public Import(TImport wrappedImport) {
		super(wrappedImport);
		this.im = wrappedImport;
	}

	public String getNamespace() {
		return im.getNamespace();
	}

	public void setNamespace(String newNamespace) {
		im.setNamespace(newNamespace);
	}

	public String getLocation() {
		return im.getLocation();
	}

	public void setLocation(String newLocation) {
		im.setLocation(newLocation);
	}

	public String getImportType() {
		return im.getImportType();
	}

	public void setImportType(String newImportType) {
		im.setImportType(newImportType);
	}
	
	public boolean isWsdlImport() {
		return NAMESPACE_WSDL.equals(getImportType());
	}
}
