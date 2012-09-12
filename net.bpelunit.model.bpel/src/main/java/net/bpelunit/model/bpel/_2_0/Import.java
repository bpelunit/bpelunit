package net.bpelunit.model.bpel._2_0;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TImport;

import net.bpelunit.model.bpel.IImport;

public class Import implements IImport {

	private static final String NAMESPACE_WSDL = "http://schemas.xmlsoap.org/wsdl/";
	private TImport im;

	public Import(TImport wrappedImport) {
		this.im = wrappedImport;
	}

	@Override
	public String getNamespace() {
		return im.getNamespace();
	}

	@Override
	public void setNamespace(String newNamespace) {
		im.setNamespace(newNamespace);
	}

	@Override
	public String getLocation() {
		return im.getLocation();
	}

	@Override
	public void setLocation(String newLocation) {
		im.setLocation(newLocation);
	}

	@Override
	public String getImportType() {
		return im.getImportType();
	}

	@Override
	public void setImportType(String newImportType) {
		im.setImportType(newImportType);
	}
	
	@Override
	public boolean isWsdlImport() {
		return NAMESPACE_WSDL.equals(getImportType());
	}
}
