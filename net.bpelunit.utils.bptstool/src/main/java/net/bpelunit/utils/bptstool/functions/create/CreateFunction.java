package net.bpelunit.utils.bptstool.functions.create;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Import;
import javax.wsdl.Port;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.soap.SOAPAddress;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import net.bpelunit.framework.xml.suite.XMLDeploymentSection;
import net.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLTestCase;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.model.bpel.BpelFactory;
import net.bpelunit.model.bpel.IImport;
import net.bpelunit.model.bpel.IPartnerLink;
import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.util.FileUtil;
import net.bpelunit.util.XMLUtil;
import net.bpelunit.utils.bptstool.functions.IFunction;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class CreateFunction implements IFunction {

	private static final String PARAMETER_TESTSUITE_DIRECTORY = "d"; //$NON-NLS-1$
	private static final String PARAMETER_BASEURL = "u";

	private String baseUrl = "http://localhost:7777/ws";
	private String deployerType = "fixed";

	private Options options;
	private String testSuiteDir;
	private String bpelFileName;
	private WSDLFactory wsdlFactory;
	private WSDLWriter wsdlWriter;
	private WSDLReader wsdlReader;

	public CreateFunction() {
		createOptions();
	}

	@SuppressWarnings("static-access")
	private final void createOptions() {
		options = new Options();

		options.addOption(OptionBuilder
				.withDescription(
						"directory into which the test suite and associated files shall be created")
				.hasArg().withArgName("DIRECTORY")
				.create(PARAMETER_TESTSUITE_DIRECTORY));

		options.addOption(OptionBuilder
				.withDescription(
						String.format(
								"Base URL at which the mock services shall be made available. Must start with http://localhost. Defaults to: %s",
								baseUrl)).hasArg().withArgName("URL")
				.create(PARAMETER_BASEURL));
	}

	public String getName() {
		return "create";
	}

	public String getDescription() {
		return "some description";
	}

	public String getHelp() {
		return null;
	}

	@SuppressWarnings("unchecked")
	private final void parseOptionsFromCommandLine(String[] args) {

		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			if (cmd.hasOption(PARAMETER_TESTSUITE_DIRECTORY)) {
				testSuiteDir = cmd
						.getOptionValue(PARAMETER_TESTSUITE_DIRECTORY);
			}

			ArrayList<String> remainingOptions = new ArrayList<String>(
					cmd.getArgList());

			bpelFileName = remainingOptions.get(0);
		} catch (ParseException e) {
			showHelpAndExit();
		}
	}

	private void showHelpAndExit() {
		// TODO Auto-generated method stub

	}

	public void execute(String[] params) {
		parseOptionsFromCommandLine(params);
		File bpelFile = new File(bpelFileName);
		File bptsFile = getBptsFile(bpelFile, testSuiteDir);
		Map<String, File> wsdlFilesByNamespace = new HashMap<String, File>();
		new File(bptsFile.getParentFile(), "wsdl").mkdir();

		// TODO Check parameters
		// checkExistsFile(bpelFile);
		// checkNotExistsFile(bptsFile);

		try {
			IProcess bpel = BpelFactory.loadProcess(new FileInputStream(
					bpelFile));

			List<? extends IImport> imports = bpel.getImports();
			for (IImport i : imports) {
				wsdlFilesByNamespace.put(i.getNamespace(),
						new File(bpelFile.getParentFile(), i.getLocation())
								.getCanonicalFile());
			}

			XMLTestSuiteDocument testSuiteDoc = XMLTestSuiteDocument.Factory
					.newInstance();
			XMLTestSuite testSuite = testSuiteDoc.addNewTestSuite();
			XMLDeploymentSection deployment = testSuite.addNewDeployment();
			testSuite.addNewTestCases();

			testSuite.setName(bptsFile.getName());
			testSuite.setBaseURL(baseUrl);
			XMLPUTDeploymentInformation processUnderTest = deployment
					.addNewPut();
			processUnderTest.setName(bpel.getName());
			processUnderTest.setType(deployerType);

			addPartners(bptsFile, wsdlFilesByNamespace, bpel, deployment);

			// required by schema although it won't be used with the generated structure
			processUnderTest.setWsdl(deployment.getPartnerArray(0).getWsdl());
			
			XMLTestCase dummyTestCase = testSuite.getTestCases().addNewTestCase();
			dummyTestCase.setName("TODO");
			dummyTestCase.addNewClientTrack();
			for(XMLPartnerDeploymentInformation p : deployment.getPartnerList()) {
				dummyTestCase.addNewPartnerTrack().setName(p.getName());
			}
			
			testSuiteDoc.save(bptsFile);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WSDLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addPartners(File bptsFile,
			Map<String, File> wsdlFilesByNamespace, IProcess bpel,
			XMLDeploymentSection deployment) throws SAXException, IOException,
			ParserConfigurationException, FileNotFoundException, WSDLException {

		wsdlFactory = WSDLFactory.newInstance();
		wsdlWriter = wsdlFactory.newWSDLWriter();
		wsdlReader = wsdlFactory.newWSDLReader();
		List<? extends IPartnerLink> partnerLinks = bpel.getPartnerLinks();
		for (IPartnerLink pl : partnerLinks) {
			NamespacePrefixGenerator namespacePrefixGenerator = new NamespacePrefixGenerator();
			XMLPartnerDeploymentInformation partner = deployment
					.addNewPartner();
			partner.setName(pl.getName());

			String partnerWsdlFileName = "wsdl/"
					+ FileUtil.getFileNameWithoutSuffix(bptsFile.getName())
					+ "_" + pl.getName() + ".wsdl";
			File partnerWsdlFile = new File(bptsFile.getParentFile(),
					partnerWsdlFileName);
			partner.setWsdl(partnerWsdlFileName);

			Definition partnerWsdl = wsdlFactory.newDefinition();
			partnerWsdl.setTargetNamespace(ensureTrailingSlash(bpel
					.getTargetNamespace()) + pl.getName());
			partnerWsdl.setExtensionRegistry(wsdlFactory.newPopulatedExtensionRegistry());
			partnerWsdl.addNamespace("soap", "http://schemas.xmlsoap.org/wsdl/soap/");

			QName plt = pl.getPartnerLinkType();
			String pltNS = plt.getNamespaceURI();
			File pltWsdlFile = wsdlFilesByNamespace.get(pltNS);
			Document pltWsdlXml = XMLUtil.parseXML(new FileInputStream(
					pltWsdlFile));
			Definition pltWsdl = wsdlReader.readWSDL(
					pltWsdlFile.getAbsolutePath(), pltWsdlXml);
			Import pltWsdlImport = partnerWsdl.createImport();
			partnerWsdl.addImport(pltWsdlImport);
			pltWsdlImport.setDefinition(pltWsdl);
			pltWsdlImport.setNamespaceURI(pltWsdl.getTargetNamespace());
			pltWsdlImport.setLocationURI(pltWsdlFile.getAbsolutePath()); // TODO
																			// Relative
																			// Path
			List<Element> partnerLinkTypeDefinitions = XMLUtil
					.getChildElementsByName(pltWsdlXml.getDocumentElement(),
							"partnerLinkType");
			Element partnerLinkType = getElementWithNameAttribute(
					partnerLinkTypeDefinitions, plt.getLocalPart());
			List<Element> roleElements = XMLUtil.getChildElementsByName(
					partnerLinkType, "role");

			if (pl.getMyRole() != null) {
				createServiceForRoleInPartnerLink(pl, partnerWsdl,
						roleElements, "Process", pl.getMyRole(),
						namespacePrefixGenerator, "TODO");
			}
			if (pl.getPartnerRole() != null) {
				createServiceForRoleInPartnerLink(pl, partnerWsdl,
						roleElements, "Mock", pl.getPartnerRole(),
						namespacePrefixGenerator, ensureTrailingSlash(baseUrl) + partner.getName());
			}
			wsdlWriter.writeWSDL(partnerWsdl, new FileOutputStream(
					partnerWsdlFile));
		}
	}

	private void createServiceForRoleInPartnerLink(IPartnerLink pl,
			Definition partnerWsdl, List<Element> roleElements,
			String serviceName, String roleName,
			NamespacePrefixGenerator namespacePrefixGenerator,
			String endpointURL) throws WSDLException {
		Element role = getElementWithNameAttribute(roleElements, roleName);
		QName portTypeQName = XMLUtil.resolveQName(
				role.getAttribute("portType"), role);
		Binding binding = findBindingForPortType(portTypeQName, partnerWsdl);

		if (binding == null) {
			binding = partnerWsdl.createBinding();
			partnerWsdl.addBinding(binding);
			binding.setPortType(partnerWsdl.getPortType(portTypeQName));
		} else {
			String bindingNS = binding.getQName().getNamespaceURI();
			partnerWsdl.addNamespace(
					namespacePrefixGenerator.getNamespacePrefix(bindingNS),
					bindingNS);
		}

		Service service = partnerWsdl.createService();
		service.setQName(new QName(partnerWsdl.getTargetNamespace(),
				serviceName));
		Port port = partnerWsdl.createPort();
		port.setName(roleName);
		port.setBinding(binding);
		SOAPAddress soapAddress = (SOAPAddress)partnerWsdl.getExtensionRegistry().createExtension(Port.class, new QName("http://schemas.xmlsoap.org/wsdl/soap/", "address"));
		soapAddress.setLocationURI(endpointURL);
		port.addExtensibilityElement(soapAddress);
		service.addPort(port);
		partnerWsdl.addService(service);
	}

	@SuppressWarnings("unchecked")
	private Binding findBindingForPortType(QName resolveQName, Definition d) {
		for (Binding b : (Collection<Binding>) (d.getAllBindings().values())) {
			if (b.getPortType().getQName().equals(resolveQName)) {
				return b;
			}
		}
		return null;
	}

	private String ensureTrailingSlash(String ns) {
		return ns.endsWith("/") ? ns : ns + "/";
	}

	private Element getElementWithNameAttribute(List<Element> elements,
			String name) {
		for (Element e : elements) {
			if (name.equals(e.getAttribute("name"))) {
				return e;
			}
		}
		return null;
	}

	File getBptsFile(File bpelFile, String testSuiteDir) {
		File bptsFile;

		if (testSuiteDir == null) {
			bptsFile = new File(FileUtil.getFileNameWithoutSuffix(bpelFile
					.getAbsolutePath()) + ".bpts");
		} else {
			bptsFile = new File(testSuiteDir);
			if (bptsFile.isAbsolute()) {
				bptsFile = new File(bptsFile,
						FileUtil.getFileNameWithoutSuffix(bpelFile.getName())
								+ ".bpts");
			} else {
				bptsFile = new File(new File(bpelFile.getParentFile(),
						testSuiteDir),
						FileUtil.getFileNameWithoutSuffix(bpelFile.getName())
								+ ".bpts");
			}
			bptsFile.getParentFile().mkdirs();
		}
		try {
			return bptsFile.getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
			return bptsFile;
		}
	}

}
