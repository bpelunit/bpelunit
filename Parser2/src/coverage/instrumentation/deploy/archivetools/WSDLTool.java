package coverage.instrumentation.deploy.archivetools;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.wsdl.Definition;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.extensions.soap.SOAPAddress;

public class WSDLTool {

	private Definition wsdlDefinition;

	public WSDLTool(Definition wsdlDefinition) {
		this.wsdlDefinition = wsdlDefinition;
	}

	public Service getService() {
		Service service = null;
		Map services = wsdlDefinition.getServices();
		if (services.size() > 0) {
			 service =(Service) getOneElement(services);
		}
		return service;
	}

	public SOAPAddress getLocation(Port port){
		List liste=port.getExtensibilityElements();
		Iterator iter=liste.iterator();
		Object object;
		SOAPAddress adress=null;
		while(iter.hasNext()){
			object=iter.next();
			if(object instanceof SOAPAddress){
				adress=(SOAPAddress)object;
			}
		}
		return adress;
	}

	public Port getPort(Service service) {
		Port port=null;
		Map ports = service.getPorts();
		if(ports.size()>0){
			port=(Port)getOneElement(ports);
		}
		return port;
	}

	private Object getOneElement(Map map) {
		Object firstElement = null;
		Iterator iter = map.values().iterator();
		if (iter.hasNext()) {
			firstElement = iter.next();
		}
		return firstElement;
	}
}
