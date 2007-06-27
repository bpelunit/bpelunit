package org.bpelunit.framework.coverage;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.bpelunit.framework.control.deploy.activebpel.ActiveBPELDeployer;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.bpelunit.framework.control.ext.ISOAPEncoder;
import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.annotation.metrics.IMetric;
import org.bpelunit.framework.coverage.annotation.metrics.activitycoverage.ActivityMetricHandler;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.BpelXMLTools;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.deploy.archivetools.IDeploymentArchiveHandler;
import org.bpelunit.framework.coverage.annotation.tools.bpelxmltools.deploy.archivetools.impl.ActiveBPELDeploymentArchiveHandler;
import org.bpelunit.framework.coverage.exceptions.ArchiveFileException;
import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.exceptions.CoverageMeasurmentException;
import org.bpelunit.framework.coverage.receiver.CoverageMessageReceiver;
import org.bpelunit.framework.coverage.receiver.MarkersRegisterForArchive;
import org.bpelunit.framework.coverage.result.statistic.IFileStatistic;
import org.bpelunit.framework.exception.ConfigurationException;
import org.jdom.Document;
import org.jdom.filter.ElementFilter;

/**
 * 
 * Die Klasse bietet die Methode zum Einbinden des Coveragetools an. Außerdem
 * ist die Klasse für das Einlesen der Konfigurationdatei verantwortlich .
 * 
 * @author Alex Salnikow
 * 
 */
public class CoverageMeasurementTool {


	private Logger logger;

	private boolean failure;

	private boolean error;

	private CoverageMessageReceiver messageReceiver = null;

	private MarkersRegisterForArchive markersRegistry = null;

	private MetricsManager metricManager;

	private String pathToWSDL = null;


	public CoverageMeasurementTool() {
		logger = Logger.getLogger(getClass());
		metricManager = new MetricsManager();
		markersRegistry = new MarkersRegisterForArchive(metricManager);
		messageReceiver = new CoverageMessageReceiver(markersRegistry);
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
		if(pathToWSDL==null){
			setErrorStatus("Path to WSDL file for coverage measurment failure");
			throw new CoverageMeasurmentException("Path to WSDL file for coverage measurment failure");
		}
		IDeploymentArchiveHandler archiveHandler = null;
		if (deployer instanceof ActiveBPELDeployer) {
			archiveHandler = new ActiveBPELDeploymentArchiveHandler();
		}
		// else if //point for extention for other BPEL Engines

		if (archiveHandler == null) {
			setErrorStatus(deployer.toString()+" is by coverage tool not supported");
			throw new CoverageMeasurmentException(deployer.toString()
					+ " is by coverage tool not supported");
		}
		 long start=System.currentTimeMillis();
		String newArchiveFile = archiveHandler.createArchivecopy(FilenameUtils
				.concat(pathToArchive, archiveFile));
		prepareLoggingService(archiveHandler);
		executeInstrumentationOfBPEL(archiveHandler);
		archiveHandler.closeArchive();
		long stop=System.currentTimeMillis();
		System.out.println("Instrumentierungszeit="+(stop-start));
		System.out.println("INVOKES "+BpelXMLTools.invoke_count);
		System.out.println("FLOWS "+BpelXMLTools.flow_count);
		System.out.println("Sequence "+BpelXMLTools.sequence_count);
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
			IDeploymentArchiveHandler archiveHandler) throws BpelException,
			ArchiveFileException {

		logger.info("CoverageTool: Instrumentation gestartet.");
		Instrumenter instrumenter = new Instrumenter();
		Document doc;
		BpelXMLTools.count = 0;
		int count=0;
		String bpelFile;
		ActivityMetricHandler.targetscount=0;
		for (Iterator<String> iter = archiveHandler.getAllBPELFileNames()
				.iterator(); iter.hasNext();) {
			bpelFile = iter.next();
			markersRegistry.addRegistryForFile(bpelFile);
			doc = archiveHandler.getDocument(bpelFile);
			Iterator iter2=doc.getDescendants(new ElementFilter("link",doc.getRootElement().getNamespace()));
			while (iter2.hasNext()) {
				iter2.next();
				count++;
				
			}
			doc = instrumenter.insertAnnotations(doc, metricManager);
			archiveHandler.writeDocument(doc, bpelFile);
			logger.info("!!!!!!!!!BPEL-Files gefunden ");

		}
		System.out.println("!!!!!!!LInks gefunden"+count);
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
		archiveHandler.addWSDLFile(new File(
				pathToWSDL));
	}

	public void configureMetrics(Map<String, List<String>> configMap)
			throws ConfigurationException {
		if(configMap==null){
			setErrorStatus("Configuration error.");
			throw new ConfigurationException("Coverage metrics are not configured.");
		}
		Iterator<String> iter = configMap.keySet().iterator();
		String key;
		IMetric metric;
		while (iter.hasNext()) {
			key = iter.next();
			metric = MetricsManager.createMetric(key, configMap.get(key),
					markersRegistry);
			if (metric != null)
				metricManager.addMetric(metric);
		}
	}

	public void setFailureStatus(String message) {
		markersRegistry.addInfo(message);
		failure = true;
	}

	public void setErrorStatus(String message) {
		markersRegistry.addInfo(message);
		error = true;
	}

	/**
	 * 
	 * @param encoder
	 *            sSOAPEncoder für die Dekodierung der Nachrichten mit
	 *            Coverage-Marken
	 */
	public void setSOAPEncoder(ISOAPEncoder encoder) {
		messageReceiver.setSOAPEncoder(encoder);
	}

	/**
	 * 
	 * @param wsdl WSDL-Beschreibung des Coverage Logging Services
	 */
	public void setPathToWSDL(String wsdl) {
		logger.info("PATHTOWSDL="+wsdl);
		pathToWSDL = wsdl;
		messageReceiver.setPathToWSDL(wsdl);
	}

	/**
	 * 
	 * @return Encoding Style der Coverage-Nachrichten
	 */
	public String getEncodingStyle() {
		return messageReceiver.getEncodingStyle();
	}

	/**
	 * Setzt den Testfall, der gerade ausgeführt wird. Dadurch ist es möglich,
	 * die Testabdeckung von jedem Testfalls zu bestimmen.
	 * 
	 * @param testCase
	 *            Testfall, der gerade ausgeführt wird.
	 */
	public void setCurrentTestCase(String testCase) {
		messageReceiver.setCurrentTestcase(testCase);
	}

	/**
	 * Empfängt SOAP-Nachrichten mit Coverage Marken während der Testausführung
	 * 
	 * @param body
	 *            Nachricht mit Coverage-Marken
	 */
	public synchronized void putMessage(String body) {
		messageReceiver.putMessage(body);
		notifyAll();
	}

	/**
	 * Generiert Statistiken (nach dem Testlauf) für alle BPEL-Dateien, die im Archive sind. 
	 * @return Statistiken (nach dem Testlauf) für alle BPEL-Dateien, die im Archive sind. 
	 */
	public List<IFileStatistic> getStatistics() {
		return markersRegistry.getStatistics();
	}

	public List<String> getInfo() {
		return markersRegistry.getInfo();
	}
}
