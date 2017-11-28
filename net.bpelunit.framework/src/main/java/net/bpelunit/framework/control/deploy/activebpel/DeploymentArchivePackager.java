package net.bpelunit.framework.control.deploy.activebpel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Port;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.extensions.soap12.SOAP12Address;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import net.bpelunit.model.bpel.IImport;
import net.bpelunit.model.bpel.IPartnerLink;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel._2_0.BpelFactory;
import net.bpelunit.model.bpel.wsdl.BPELPartnerLinkType;
import net.bpelunit.model.bpel.wsdl.WSDLCatalog;
import net.bpelunit.util.Predicate;

/**
 * <p>
 * Class which implements all the logic required for generating the
 * engine-specific files required for ActiveBPEL 4.1 and packing everything into
 * a proper BPR deployment archive. In particular, it can create the ActiveBPEL
 * 4.1 <code>catalog.xml</code> and <code>process.pdd</code> files.
 * </p>
 *
 * <p>
 * This class was adapted from the MuBPEL source code, with one simplification:
 * XSLT dependencies are not automatically detected, since that would require
 * parsing all XPath queries and much more code would be necessary.
 * </p>
 * 
 * @author Antonio García Domínguez
 * @version 1.3
 */
public class DeploymentArchivePackager {

	public static class Pair<T, U> {
		private T t;
		private U u;

		public Pair(T t, U u) {
			this.t = t;
			this.u = u;
		}

		public T getLeft() {
			return t;
		}

		public U getRight() {
			return u;
		}
	}

	public static final String SOAP_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/soap/";
	public static final String WSA200303_NAMESPACE = "http://schemas.xmlsoap.org/ws/2003/03/addressing";
	public static final String PLINK_NAMESPACE = "http://docs.oasis-open.org/wsbpel/2.0/plnktype";
	public static final String SOAP_RPC_STYLE = "rpc";

	/** Target namespace of the ActiveBPEL 4.1 deployment catalog schema. */
	public static final String CATALOGXML_NAMESPACE = "http://schemas.active-endpoints.com/catalog/2006/07/catalog.xsd";

	/** Target namespace of the ActiveBPEL 4.1 deployment descriptor schema. */
	public static final String PDD_NAMESPACE        = "http://schemas.active-endpoints.com/pdd/2006/08/pdd.xsd";

	public static final String CATALOGXML_FILENAME  = "catalog.xml";
	public static final String ORIGINALPDD_FILENAME = "process.pdd";

	private static final Logger LOGGER = Logger.getLogger(DeploymentArchivePackager.class);
	private static final String PROCESS_NS_PREFIX = "bpelns";
	private static final int ZIP_BUFSIZE = 8096;
	private static final int DEFAULT_BPELUNIT_PORT = 7777;

	private IProcess bpelProcessDefinition = null;

	private final WSDLCatalog wsdlCatalog = new WSDLCatalog();
	private Set<File> wsdlDependencies = new HashSet<File>();
	private Set<File> xsdDependencies = new HashSet<File>();

	/** Port where BPELUnit is supposed to be listening on */
	private int fBPELUnitPort = DEFAULT_BPELUNIT_PORT;
	
	/** If <code>true</code>, we should preserve the original URLs to the partner services */
	private boolean fPreserveServiceURLs = false;
	private File bpelFile;

	public DeploymentArchivePackager(File fBPEL) throws BPELPackagingException, WSDLException {
		try {
			this.bpelFile = fBPEL.getCanonicalFile();
			this.bpelProcessDefinition = BpelFactory.INSTANCE.loadProcess(new FileInputStream(bpelFile));

			for (IImport imp : bpelProcessDefinition.getImports()) {
				if (imp.isWsdlImport()) {
					final File fWSDL = new File(bpelFile.getParentFile(), imp.getLocation()).getCanonicalFile();
					wsdlCatalog.addWSDL(fWSDL.getParentFile(), fWSDL);
				}
			}

			final BPELDependencyAnalyzer analyzer = new BPELDependencyAnalyzer(bpelFile);
			wsdlDependencies = analyzer.getWSDLDependencies();
			xsdDependencies = analyzer.getXSDDependencies();
		} catch (WSDLException ex) {
			throw ex;
		} catch (Exception e) {
			throw new BPELPackagingException(e);
		}
	}

	/**
	 * Sets the port on which BPELUnit is expected to listen.
	 * @param port The port to be used when generating the PDD.
	 */
	public void setBPELUnitPort(int port) {
		this.fBPELUnitPort = port;
	}

	/**
	 * Gets the port on which BPELUnit is expected to listen.
	 * @return The port to be used when generating the PDD.
	 */
	public int getBPELUnitPort() {
		return fBPELUnitPort;
	}

	/**
	 * Generates the catalog required by ActiveBPEL listing all the
	 * WSDL and XML Schema files used, and which should be later placed through
	 * other means at the META-INF/catalog.xml path of the BPR. You will need to
	 * dump the document to a catalog.xml file later on.
	 * 
	 * @throws BPELPackagingException 
	 */
	public Document generateCatalogXML() throws ParserConfigurationException {
		DocumentBuilderFactory bldF = DocumentBuilderFactory.newInstance();
		bldF.setNamespaceAware(true);

		Document doc = bldF.newDocumentBuilder().newDocument();
		Element eCatalog = doc.createElementNS(CATALOGXML_NAMESPACE, "catalog");

		for (File wsdl : wsdlDependencies) {
			Element eWSDLEntry = doc.createElementNS(CATALOGXML_NAMESPACE, "wsdlEntry");

			eWSDLEntry.setAttribute("location", String.format("project:/wsdl/%s", wsdl.getName()));
			eWSDLEntry.setAttribute("classpath", String.format("wsdl/%s", wsdl.getName()));
			eCatalog.appendChild(eWSDLEntry);
		}
		for (File xsd : xsdDependencies) {
			Element eSchemaEntry = doc.createElementNS(CATALOGXML_NAMESPACE, "schemaEntry");

			eSchemaEntry.setAttribute("location", String.format("project:/wsdl/%s", xsd.getName()));
			eSchemaEntry.setAttribute("classpath", String.format("wsdl/%s", xsd.getName()));
			eCatalog.appendChild(eSchemaEntry);
		}

		doc.appendChild(eCatalog);
		return doc;
	}

	/**
	 * Generates the ActiveBPEL Process Deployment Descriptor document in memory.
	 * It should be dumped to the appropriate process.pdd file later on.
	 */
	public Document generatePDD(File fBPEL) throws Exception {
		DocumentBuilderFactory bldF = DocumentBuilderFactory.newInstance();
		bldF.setNamespaceAware(true);
		Document doc = bldF.newDocumentBuilder().newDocument();

		final String baseURI = "http://localhost:" + getBPELUnitPort()  + "/ws/";

		// We need to avoid using the same service+port more than once - there may be multiple services with the same binding and port type
		final Set<Pair<QName, String>> usedPorts = new HashSet<Pair<QName, String>>(); 

		Element eProcess = doc.createElementNS(PDD_NAMESPACE, "process");
		eProcess.setAttributeNS(
			XMLConstants.XMLNS_ATTRIBUTE_NS_URI,
			XMLConstants.XMLNS_ATTRIBUTE + ":" + PROCESS_NS_PREFIX,
			bpelProcessDefinition.getTargetNamespace());
		eProcess.setAttribute("persistenceType", "full");
		eProcess.setAttribute("location", getPathToBPELInPDD(fBPEL));
		eProcess.setAttribute("name", PROCESS_NS_PREFIX + ":" + bpelProcessDefinition.getName());

		Element ePartnerLinks = doc.createElementNS(PDD_NAMESPACE, "partnerLinks");
		for (IPartnerLink pl : bpelProcessDefinition.getPartnerLinks()) {
			Element ePartnerLink = doc.createElementNS(PDD_NAMESPACE, "partnerLink");
			ePartnerLink.setAttribute("name", pl.getName());

			if (pl.getPartnerRole() != null && pl.getPartnerRole().length() > 0) {
				Pair<Service, Port> searchResult = findService(bpelProcessDefinition, pl.getPartnerLinkType(), pl.getPartnerRole(), usedPorts);
				if (searchResult == null) {
					throw new BPELPackagingException(String.format(
						"Could not find a matching service for role '%s' in partner link '%s'",
						pl.getPartnerRole(), pl.getName()));
				}
				final Service service = searchResult.getLeft();
				final Port port = searchResult.getRight();

				Element ePartnerRole = doc.createElementNS(PDD_NAMESPACE, "partnerRole");
				ePartnerRole.setAttribute("endpointReference", "static");
				ePartnerRole.setAttribute("invokeHandler", "default:Address");

				Element eEndpointReference = doc.createElementNS(WSA200303_NAMESPACE, "wsa:EndpointReference");
				eEndpointReference.setAttributeNS(
					XMLConstants.XMLNS_ATTRIBUTE_NS_URI, "xmlns:s",
					service.getQName().getNamespaceURI());

				Element eAddress = doc.createElementNS(WSA200303_NAMESPACE, "wsa:Address");
				String url = null;
				if (isPreserveServiceURLs()) {
					for (Object o : port.getExtensibilityElements()) {
						if (o instanceof SOAP12Address) {
							url = ((SOAP12Address)o).getLocationURI();
						}
						else if (o instanceof SOAPAddress) {
							url = ((SOAPAddress)o).getLocationURI();
						}
					}
				}
				if (url == null) {
					url = baseURI + pl.getName();
				}
				eAddress.setTextContent(url);

				Element eServiceName = doc.createElementNS(WSA200303_NAMESPACE, "wsa:ServiceName");
				eServiceName.setAttribute("PortName", port.getName());
				eServiceName.setTextContent(String.format("s:%s", service.getQName().getLocalPart()));

				eEndpointReference.appendChild(eAddress);
				eEndpointReference.appendChild(eServiceName);
				ePartnerRole.appendChild(eEndpointReference);
				ePartnerLink.appendChild(ePartnerRole);
			}

			if (pl.getMyRole() != null && pl.getMyRole().length() > 0) {
				Pair<Service, Port> resBusqueda = findService(bpelProcessDefinition, pl.getPartnerLinkType(), pl.getMyRole(), usedPorts);
				if (resBusqueda == null) {
					throw new BPELPackagingException(String.format(
						"Could not find a matching service for role '%s' in partner link '%s'",
						pl.getMyRole(), pl.getName()));
				}

				final Service service = resBusqueda.getLeft();
				final Binding binding = resBusqueda.getRight().getBinding();
				String soapStyle = "document";
				for (ExtensibilityElement eElem : (List<ExtensibilityElement>) binding.getExtensibilityElements()) {
					if (eElem instanceof SOAPBinding) {
						soapStyle = ((SOAPBinding)eElem).getStyle();
					}
				}
				final String activeBPELSOAPStyle = SOAP_RPC_STYLE.equals(soapStyle) ? "RPC-LIT" : "MSG";

				Element eMyRole = doc.createElementNS(PDD_NAMESPACE, "myRole");
				eMyRole.setAttribute("allowedRoles", "");
				eMyRole.setAttribute("binding", activeBPELSOAPStyle);
				eMyRole.setAttribute("service", service.getQName().getLocalPart());

				ePartnerLink.appendChild(eMyRole);
			}

			ePartnerLinks.appendChild(ePartnerLink);
		}
		eProcess.appendChild(ePartnerLinks);

		// Add references to the WSDL documents
		Element eReferences = doc.createElementNS(PDD_NAMESPACE, "references");
		for (Map.Entry<String, String> locNSPair : computeWSDLName2NamespaceMap().entrySet()) {
			Element eWSDL = doc.createElementNS(PDD_NAMESPACE, "wsdl");
			eWSDL.setAttribute("location", "project:/wsdl/" + locNSPair.getKey());
			eWSDL.setAttribute("namespace", locNSPair.getValue());
			eReferences.appendChild(eWSDL);
		}
		eProcess.appendChild(eReferences);

		doc.appendChild(eProcess);
		return doc;
	}

	private Pair<Service, Port> findService(IProcess bpelProcessDefinition, QName partnerLinkType, String partnerRole,
			Set<Pair<QName, String>> usedPorts) {

		final BPELPartnerLinkType plt = wsdlCatalog.getPartnerLinkType(partnerLinkType);
		if (plt == null) {
			LOGGER.warn("Could not find the partner link type " + partnerLinkType);
			// Could not find the PLT
			return null;
		}

		final QName portTypeName = plt.getPortTypesByRole().get(partnerRole);

		final PortType portType = wsdlCatalog.getPortType(portTypeName);
		if (portType == null) {
			LOGGER.warn(String.format("Could not find the role %s within partner link type %s", partnerRole, plt.getName()));
			// Could not find the specified role in the PLT
			return null;
		}
		LOGGER.debug("Found port type " + portType.getQName());

		// Filter bindings to search for those with the right PLTs
		final Set<QName> mapSelectedBindings = findBindingQNamesByPLT(portType.getQName());
		if (mapSelectedBindings.isEmpty()) {
			LOGGER.warn("Could not find a binding for the port type " + portType.getQName());
			// No matching bindings
			return null;
		}

		// Pick the first service which has a port with one of the bindings selected above
		return findServiceWithBindingIn(mapSelectedBindings, usedPorts);
	}

	private Set<QName> findBindingQNamesByPLT(final QName tipoPuertoBuscado) {
		Set<Binding> bindings = wsdlCatalog.findAllBindings(new Predicate<Binding>() {
			public boolean evaluate(Binding b) {
				return tipoPuertoBuscado.equals(b.getPortType().getQName());
			}
		});

		Set<QName> results = new HashSet<QName>();
		for (Binding b : bindings) {
			results.add(b.getQName());
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private Pair<Service, Port> findServiceWithBindingIn(Set<QName> mapSelectedBindings, Set<Pair<QName, String>> usedPorts)
	{
		for (Definition def : wsdlCatalog.getDefinitions()) {
			for (Service s : (Collection<Service>)def.getAllServices().values()) {
				final QName serviceQName = s.getQName();
				for (Port p : (Collection<Port>)s.getPorts().values()) {
					final QName bindingQName = p.getBinding().getQName();
					final Pair<QName, String> servicePortKey =
						new Pair<QName, String>(serviceQName, p.getName());
					if (mapSelectedBindings.contains(bindingQName) && usedPorts.add(servicePortKey)) {
						return new Pair<Service, Port>(s, p);
					}
				}
			}
		}
		return null;
	}
	
	public File generateBPR(final File bprFile) throws BPELPackagingException {
		try {
			// All extra files will be generated in the same directory as the .bpr file
			final String parentDirectoryPath = bprFile.getCanonicalFile().getParent();

			// ActiveBPEL 4.1 catalog.xml
			final String pathToCatalogXML = parentDirectoryPath
					+ File.separator
					+ DeploymentArchivePackager.CATALOGXML_FILENAME;
			LOGGER.debug("Generating ActiveBPEL 4.1 catalog.xml...");
			dumpToFile(generateCatalogXML(), new File(pathToCatalogXML));

			// Process Deployment Descriptor
			final String pathToPDD = parentDirectoryPath + File.separator
					+ DeploymentArchivePackager.ORIGINALPDD_FILENAME;
			LOGGER.debug("Generating ActiveBPEL 4.1 process deployment descriptor...");
			dumpToFile(generatePDD(bpelFile), new File(pathToPDD));

			// Compressed BPR archive ready to be deployed
			LOGGER.debug("Packing BPR file...");
			packBPR(bpelFile.getPath(), pathToPDD, pathToCatalogXML, bprFile.getPath());

			LOGGER.info("Generated BPR '" + bprFile + "' for '" + bpelFile + "'.");
			return bprFile;
		} catch (Exception e) {
			throw new BPELPackagingException(e);
		}
	}

	/**
	 * Packs into an ActiveBPEL BPR all files currently known and the previously
	 * generated PDD and catalog.xml files, along with the WS-BPEL process
	 * definition.
	 * 
	 * @param pathToBPEL
	 *            Path to the BPEL process definition file to be used.
	 * @param pathToPDD
	 *            Path to the Process Deployment Descriptor to be used.
	 * @param pathToCatalogXML
	 *            Path to the catalog.xml file to be used.
	 * @param pathToBPR
	 *            Path of the resulting BPR file.
	 * @throws BPELPackagingException
	 *             There was an error while building the .bpr file.
	 */
	public void packBPR(final String pathToBPEL, final String pathToPDD,
			final String pathToCatalogXML, final String pathToBPR) throws BPELPackagingException
	{
		try {
			ZipOutputStream zipOS = new ZipOutputStream(new FileOutputStream(pathToBPR));

			for (File fichWSDL : wsdlDependencies) {
				addFileToZip(zipOS, fichWSDL, "wsdl/" + fichWSDL.getName());
			}
			for (File fichXSD : xsdDependencies) {
				addFileToZip(zipOS, fichXSD, "wsdl/" + fichXSD.getName());
			}
			addFileToZip(zipOS, new File(pathToPDD), "process.pdd");
			addFileToZip(zipOS, new File(pathToCatalogXML), "META-INF/catalog.xml");
			String pathBPELInPDD = getPathToBPELInPDD(new File(pathToBPEL));
			addFileToZip(zipOS, new File(pathToBPEL), pathBPELInPDD);

			zipOS.flush();
			zipOS.close();
		} catch (Exception ex) {
			throw new BPELPackagingException(ex);
		}
	}

	private Map<String, String> computeWSDLName2NamespaceMap() throws Exception
	{
		final HashMap<String, String> map = new HashMap<String, String>();
		for (Definition def : wsdlCatalog.getDefinitions()) {
			accumulateWSDLName2NamespaceMap(def, map);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	private void accumulateWSDLName2NamespaceMap(Definition def, HashMap<String, String> map) throws MalformedURLException {
		// Definition#getDocumentBaseURI() is actually an URL (WSDLReaderImpl#readWSDL decodes escaped RFC2396 sequences)
		final String wsdlURL = def.getDocumentBaseURI();
		final String wsdlPath = new URL(wsdlURL).getPath();
		final String name = new File(wsdlPath).getName();

		map.put(name, def.getTargetNamespace());
		for (List<Import> imports : ((Map<String, List<Import>>)def.getImports()).values()) {
			for (Import imp : imports) {
				if (imp.getDefinition() != null) {
					accumulateWSDLName2NamespaceMap(imp.getDefinition(), map);
				}
			}
		}
	}

	/**
	 * Adds the file passed as an argument to the zip file
	 *
	 * @param zipOS
	 *            The destination ZIP file
	 * @param file
	 *            The source file
	 * @param entryName
	 *            Name for the entry in the ZIP file
	 * @throws IOException
	 *             It is thrown if the source file can not be opened
	 */
	private void addFileToZip(ZipOutputStream zipOS, File file, String entryName) throws IOException
	{
		zipOS.putNextEntry(new ZipEntry(entryName));

		FileInputStream is = null;
		try {
			is = new FileInputStream(file);

			byte[] buf = new byte[ZIP_BUFSIZE];
			int readBytes = -1;
			while ((readBytes = is.read(buf)) != -1) {
				zipOS.write(buf, 0, readBytes);
			}

			zipOS.closeEntry();
		} finally {
			if (is != null) {
				is.close();
			}
		}
	}

	private String getPathToBPELInPDD(File fBPEL) {
		return "bpel/" + fBPEL.getName();
	}

	public boolean isPreserveServiceURLs() {
		return fPreserveServiceURLs;
	}

	public void setPreserveServiceURLs(boolean preserveURLs) {
		this.fPreserveServiceURLs = preserveURLs;
	}

	public static void dumpToFile(Document d, File f) throws TransformerException, IOException {
		FileOutputStream fOS = new FileOutputStream(f);
		try {
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer t = tf.newTransformer();
			t.transform(new DOMSource(d), new StreamResult(fOS));
		} finally {
			fOS.flush();
			fOS.close();
		}
	}
	
}
