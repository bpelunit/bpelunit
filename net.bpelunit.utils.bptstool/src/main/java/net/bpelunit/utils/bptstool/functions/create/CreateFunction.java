package net.bpelunit.utils.bptstool.functions.create;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.wsdl.WSDLException;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;

import net.bpelunit.framework.xml.suite.XMLDeploymentSection;
import net.bpelunit.framework.xml.suite.XMLPUTDeploymentInformation;
import net.bpelunit.framework.xml.suite.XMLPartnerDeploymentInformation;
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
	private Map<String, File> wsdlFilesByNamespace = new HashMap<String, File>();

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
		WSDLReader wsdlReader = null;
		try {
			wsdlReader = WSDLFactory.newInstance().newWSDLReader();
		} catch (WSDLException e) {
			throw new RuntimeException(e);
		}
		File bpelFile = new File(bpelFileName);
		File bptsFile = getBptsFile(bpelFile, testSuiteDir);

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

			List<? extends IPartnerLink> partnerLinks = bpel.getPartnerLinks();
			for (IPartnerLink pl : partnerLinks) {
				if(pl.getPartnerRole() != null) {
					XMLPartnerDeploymentInformation partner = deployment
							.addNewPartner();
					partner.setName(pl.getName());
					
					QName plt = pl.getPartnerLinkType();
					String pltNS = plt.getNamespaceURI();
					File wsdlFile = wsdlFilesByNamespace.get(pltNS);
					Document wsdlXml = XMLUtil.parseXML(new FileInputStream(wsdlFile));
					List<Element> partnerLinkTypeDefinitions = XMLUtil.getChildElementsByName(wsdlXml.getDocumentElement(), "partnerLinkType");
				}
			}

			System.out.println(bptsFile);
			testSuiteDoc.save(bptsFile);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JAXBException e) {
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
		}
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
