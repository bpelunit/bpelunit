package coverage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bpelunit.framework.control.deploy.activebpel.ActiveBPELDeployer;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.exception.ConfigurationException;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import coverage.deploy.archivetools.IDeploymentArchiveHandler;
import coverage.deploy.archivetools.activebpel.ActiveBPELDeploymentArchiveHandler;
import coverage.exception.ArchiveFileException;
import coverage.exception.BpelException;
import coverage.exception.CoverageMeasurmentException;
import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.metrics.IMetric;
import coverage.instrumentation.metrics.MetricHandler;
import coverage.instrumentation.metrics.branchcoverage.BranchMetric;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;
import coverage.wstools.CoverageRegistry;

/**
 * 
 * Die Klasse bietet die Methode zum Einbinden des Coveragetools an. Außerdem
 * ist die Klasse für das Einlesen der Konfigurationdatei verantwortlich .
 * 
 * @author Alex Salnikow
 * 
 */
public class CoverageMeasurementTool {

	private static List<IMetric> metrics = new ArrayList<IMetric>();

	private static final String PROPERTY = "property";

	private static final String BASIC_ACTIVITIES = "IncludeBasicActivities";

	private static final String INCLUDE_FLOW_LINKS = "IncludeFlowLinks";

	private static final String INCLUDE_TRUE_FALSE_TRANSITION_CONDITION = "IncludeTrueFalseTransitionCondition";

	private static final String COVERAGE_SERVICE_WSDL = "_LogService_.wsdl";

	private static final Namespace NAMESPACE_CONFIGURATION = Namespace
			.getNamespace("http://www.bpelunit.org/schema/coverageToolConfiguration");

	private Logger logger;

	private String fBpelunitConfigDirectory;

	/**
	 * 
	 * @param file
	 *            Konfiguration für Coveragemessung
	 * @param fBpelunitConfigDirectory
	 * @throws ConfigurationException
	 */
	public CoverageMeasurementTool(File file, String fBpelunitConfigDirectory)
			throws ConfigurationException {
		this.fBpelunitConfigDirectory = fBpelunitConfigDirectory;
		logger = Logger.getLogger(getClass());
		loadConfiguration(file);
	}

	private void loadConfiguration(File file) throws ConfigurationException {
		logger.info("Konfiguration für Coverage wird geladen");
		SAXBuilder builder = new SAXBuilder();
		String attribute;
		try {
			Document doc = builder.build(file);
			Element config = doc.getRootElement();
			List children = config.getChildren("metric",
					NAMESPACE_CONFIGURATION);

			for (Iterator iter = children.iterator(); iter.hasNext();) {
				Element element = (Element) iter.next();
				attribute = element.getAttributeValue("name");

				if (attribute.equalsIgnoreCase(Statementmetric.METRIC_NAME)) {
					metrics.add(createStatementmetric(element));
				} else if (attribute.equalsIgnoreCase(BranchMetric.METRIC_NAME)) {
					metrics.add(createBranchmetric(element));
				}
			}

			logger
					.info("Konfiguration für Coverage wurde erfolgreich geladen:Metriken="
							+ metrics.size());
		} catch (JDOMException e) {
			throw new ConfigurationException(
					"An XML reading error occurred reading the configuration of coverage tool from file "
							+ file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new ConfigurationException(
					"An I/O error occurred reading the configuration of coverage tool from file "
							+ file.getAbsolutePath(), e);
		}

	}

	private IMetric createStatementmetric(Element element) {
		Statementmetric metric = new Statementmetric();
		List children = element.getChildren(PROPERTY, NAMESPACE_CONFIGURATION);
		Element child;
		String name = null;
		for (Iterator iter = children.iterator(); iter.hasNext();) {

			child = (Element) iter.next();
			name = child.getAttributeValue("name");

			if (name != null && name.equals(BASIC_ACTIVITIES)) {
				analyzeString(child.getText(), metric);
			}
		}
		return metric;
	}

	private void analyzeString(String activities, Statementmetric metric) {
		if (!activities.endsWith(","))
			activities = activities + ",";
		Scanner scanner = new Scanner(activities);
		scanner.useDelimiter(",");
		String activity;
		while (scanner.hasNext()) {
			activity = scanner.next().trim();
			if (BasisActivity.isBasisActivity(activity)) {
				metric.addBasicActivity(activity);
			}
		}
	}

	private IMetric createBranchmetric(Element element) {
		IMetric metric = new BranchMetric();
		List children = element.getChildren(PROPERTY, NAMESPACE_CONFIGURATION);
		Element child;
		boolean flowLinks = true;
		boolean trueAndFalse = true;
		String attribute;
		for (Iterator iter = children.iterator(); iter.hasNext();) {
			child = (Element) iter.next();
			attribute = child.getAttributeValue("name");
			if (attribute != null) {
				if (attribute.equalsIgnoreCase(INCLUDE_FLOW_LINKS)) {
					if (child.getTextTrim().equalsIgnoreCase("no")) {
						flowLinks = false;
					}
				} else if (attribute
						.equalsIgnoreCase(INCLUDE_TRUE_FALSE_TRANSITION_CONDITION)) {
					if (child.getTextTrim().equalsIgnoreCase("no")) {
						trueAndFalse = false;
					}
				}
			}
		}
		return metric;
	}

	/**
	 * Prepariert das Deploymentarchive für die Messung der Abdeckung beim
	 * Testen des BPEL-Prozesses. Die Instrumentierung wird auf einer Kopie
	 * durchgeführt.
	 * 
	 * @param pathToArchive
	 * @param archiveFile
	 * @param deployer
	 * @return Name der preparierten Archivedatei
	 * @throws CoverageMeasurmentException
	 */
	public String prepareArchiveForCoverageMeasurement(String pathToArchive,
			String archiveFile, IBPELDeployer deployer)
			throws CoverageMeasurmentException {

		IDeploymentArchiveHandler archiveHandler = null;

		if (deployer instanceof ActiveBPELDeployer) {
			archiveHandler = new ActiveBPELDeploymentArchiveHandler();
		}
		if (archiveHandler == null) {
			throw new CoverageMeasurmentException(deployer.toString()
					+ " is by coverage tool not supported");
		}
		archiveHandler.setArchiveFile(FilenameUtils.concat(pathToArchive,
				archiveFile));
		prepareLoggingService(archiveHandler);
		executeInstrumentationOfBPEL(archiveHandler);
		
		return archiveHandler.getArchiveFile().getName();
	}

	/**
	 * Startet die Instrumentierung aller BPEL-Dateien, die im Archive sind.
	 * 
	 * @param archiveHandler
	 * @throws BpelException
	 */
	private void executeInstrumentationOfBPEL(
			IDeploymentArchiveHandler archiveHandler) throws BpelException {
		MetricHandler metricHandler = new MetricHandler();
		for (Iterator<IMetric> iter = metrics.iterator(); iter.hasNext();) {
			metricHandler.addMetric(iter.next());
		}
		

		de.schlichtherle.io.File bpelFile;
		for (int i = 0; i < archiveHandler.getCountOfBPELFiles(); i++) {
			bpelFile = archiveHandler.getBPELFile(i);
			metricHandler.executeInstrumentation(bpelFile);
		}
	}

	/**
	 * Fügt die WSDL-Datei für Service, der die Log-Einträge empfängt. Diese
	 * Log-Einträge dokumentieren die Ausführung bestimmter Codeteile.
	 * 
	 * @param archiveHandler
	 * @throws ArchiveFileException
	 */
	private void prepareLoggingService(IDeploymentArchiveHandler archiveHandler)
			throws ArchiveFileException {
		archiveHandler.addWSDLFile(new File(FilenameUtils.concat(
				fBpelunitConfigDirectory, COVERAGE_SERVICE_WSDL)));
	}
}
