package coverage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;
import org.bpelunit.framework.control.ext.IBPELDeployer;
import org.jdom.JDOMException;

import coverage.deployarchivetools.IDeploymentArchiveHandler;
import coverage.deployarchivetools.impl.ActiveBPELDeploymentArchiveHandler;
import coverage.instrumentation.metrics.IMetricHandler;
import coverage.instrumentation.metrics.MetricHandler;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;
import exception.ArchiveFileException;
import exception.BpelException;
import exception.BpelVersionException;

public class CoverageMeasurement {
	
	public void initializeCoverageMeasurement(){
		
	}

	public static String prepareForCoverageMeasurement(String pathToArchive,String bprFile,
			IBPELDeployer deployer) throws FileNotFoundException,
			BpelException, BpelVersionException, ArchiveFileException {

		IDeploymentArchiveHandler archiveHandler = null;
		try {

			// if (deployer instanceof ActiveBPELDeployer) {
			archiveHandler = new ActiveBPELDeploymentArchiveHandler();
			// }
			archiveHandler.setArchiveFile(FilenameUtils.concat(pathToArchive, bprFile));
			prepareLoggingService(archiveHandler);
			executeInstrumentationOfBPEL(archiveHandler);
		} catch (JDOMException e) {
			throw new ArchiveFileException("", e);
		} catch (IOException e) {
			throw new ArchiveFileException("", e);
		}

		return archiveHandler.getArchiveFile().getName();
	}

	private static void executeInstrumentationOfBPEL(
			IDeploymentArchiveHandler archiveHandler) throws JDOMException,
			IOException, BpelException, BpelVersionException {
		IMetricHandler metricHandler = new MetricHandler();
		Statementmetric metric = (Statementmetric) metricHandler
				.addMetric(MetricHandler.STATEMENT_METRIC);
		metric.addAllBasicActivities();
		metricHandler.addMetric(MetricHandler.BRANCH_METRIC);
		de.schlichtherle.io.File bpelFile;
		for (int i = 0; i < archiveHandler.getCountOfBPELFiles(); i++) {
			bpelFile = archiveHandler.getBPELFile(i);
			metricHandler.startInstrumentation(bpelFile);
		}
	}

	private static void prepareLoggingService(
			IDeploymentArchiveHandler archiveHandler) throws IOException,
			ArchiveFileException, JDOMException {
		// TODO configuration einlesen
		archiveHandler.addWSDLFile(new File(
				"C:/bpelunit/conf/_LogService_.wsdl"));
	}
}
