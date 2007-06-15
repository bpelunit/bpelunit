package org.bpelunit.framework.coverage;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bpelunit.framework.control.deploy.activebpel.ActiveBPELDeployer;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.annotation.metrics.activitycoverage.ActivityMetric;
import org.bpelunit.framework.coverage.annotation.metrics.branchcoverage.BranchMetric;
import org.bpelunit.framework.coverage.annotation.metrics.chcoverage.CompensationMetric;
import org.bpelunit.framework.coverage.annotation.metrics.fhcoverage.FaultMetric;
import org.bpelunit.framework.coverage.annotation.metrics.linkcoverage.LinkMetric;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.deploy.archivetools.IDeploymentArchiveHandler;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.deploy.archivetools.impl.ActiveBPELDeploymentArchiveHandler;
import org.bpelunit.framework.coverage.exceptions.ArchiveFileException;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.exceptions.CoverageMeasurmentException;
import org.bpelunit.framework.coverage.receiver.CoverageMessageReceiver;
import org.bpelunit.framework.coverage.receiver.LabelsRegistry;
import org.bpelunit.framework.exception.ConfigurationException;
import org.jdom.Document;

/**
 * 
 * Die Klasse bietet die Methode zum Einbinden des Coveragetools an. Außerdem
 * ist die Klasse für das Einlesen der Konfigurationdatei verantwortlich .
 * 
 * @author Alex Salnikow
 * 
 */
public class CoverageMeasurementTool {

	// private static List<IMetric> metrics = new ArrayList<IMetric>();

	private static final String PROPERTY = "property";

	private static final String BASIC_ACTIVITIES = "IncludeBasicActivities";

	private static final String NAME_ATTRIBUTE = "name";

	private Logger logger;

	private String fBpelunitConfigDirectory;

	/**
	 * 
	 */
	public CoverageMeasurementTool() {

		MetricsManager.getInstance().initialize();
		logger = Logger.getLogger(getClass());
		logger.info("CoverageMeasurmentTool erzeugt");
		// this.fBpelunitConfigDirectory=FilenameUtils.concat(System.getenv(BPELUnitBaseRunner.BPELUNIT_HOME_ENV),BPELUnitBaseRunner.CONFIG_DIR);
	}

	private void configTool(Map<String, List<String>> configMap) {
		createStatementmetric(configMap);
		createOtherMetrics(configMap);

	}

	private void createOtherMetrics(Map<String, List<String>> configMap) {
		if (configMap.containsKey(BranchMetric.METRIC_NAME))
			MetricsManager.createMetric(BranchMetric.METRIC_NAME, null);
		if (configMap.containsKey(FaultMetric.METRIC_NAME))
			MetricsManager.createMetric(FaultMetric.METRIC_NAME, null);
		if (configMap.containsKey(CompensationMetric.METRIC_NAME))
			MetricsManager.createMetric(CompensationMetric.METRIC_NAME, null);
		if (configMap.containsKey(LinkMetric.METRIC_NAME))
			MetricsManager.createMetric(LinkMetric.METRIC_NAME, null);
	}

	// ********** prepare archive file for coverage measurment **********
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
		//PFAD==NULL oder falsch behandeln
		IDeploymentArchiveHandler archiveHandler = null;
		if (deployer instanceof ActiveBPELDeployer) {
			archiveHandler = new ActiveBPELDeploymentArchiveHandler();
		}
		// else if //point for extention for other BPEL Engines

		if (archiveHandler == null){
			throw new CoverageMeasurmentException(deployer.toString()
					+ " is by coverage tool not supported");
		}
		String newArchiveFile=archiveHandler.createArchivecopy(FilenameUtils.concat(pathToArchive,
				archiveFile));
		prepareLoggingService(archiveHandler);
		executeInstrumentationOfBPEL(archiveHandler);
		archiveHandler.closeArchive();
		return newArchiveFile;
	}

	/**
	 * Startet die Instrumentierung aller BPEL-Dateien, die im Archive sind.
	 * 
	 * @param archiveHandler
	 * @throws BpelException 
	 * @throws BpelException
	 * @throws ArchiveFileException 
	 */
	private void executeInstrumentationOfBPEL(
			IDeploymentArchiveHandler archiveHandler) throws BpelException, ArchiveFileException {
		logger.info("CoverageTool: Instrumentation gestartet.");
		Instrumenter annotator = new Instrumenter();
		Document doc;
		BpelXMLTools.count = 0;
		String bpelFile;
		for (Iterator<String> iter = archiveHandler.getAllBPELFileNames()
				.iterator(); iter.hasNext();) {
			bpelFile = iter.next();
			LabelsRegistry.getInstance().addRegistryForFile(bpelFile);
				doc = archiveHandler.getDocument(bpelFile);
				doc = annotator.insertAnnotations(doc);
				archiveHandler.writeDocument(doc, bpelFile);

		}
		logger.info("CoverageTool: Instrumentation beendet.");
	}

	/**
	 * Fügt die WSDL-Datei für Service, der die Log-Einträge empfängt. Diese
	 * Log-Einträge dokumentieren die Ausführung bestimmter Codeteile.
	 * 
	 * @param archiveHandler
	 * @param simulatedUrl 
	 * @throws ArchiveFileException
	 */
	private void prepareLoggingService(IDeploymentArchiveHandler archiveHandler)
			throws ArchiveFileException {
		// archiveHandler.addWSDLFile(new
		// File(FilenameUtils.concat(fBpelunitConfigDirectory,CoverageConstants.COVERAGE_SERVICE_WSDL)));
		archiveHandler.addWSDLFile(new File(CoverageMessageReceiver.ABSOLUT_CONFIG_PATH));
	}

	private void createStatementmetric(Map<String, List<String>> configMap) {
		if (configMap.containsKey(ActivityMetric.METRIC_NAME)) {
			MetricsManager.createMetric(ActivityMetric.METRIC_NAME, configMap
					.get(ActivityMetric.METRIC_NAME));

		}

	}

	public void setConfig(Map<String, List<String>> configMap)
			throws ConfigurationException {
		configTool(configMap);
	}
}
