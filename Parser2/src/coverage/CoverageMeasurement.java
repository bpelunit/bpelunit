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

import coverage.deployarchivetools.IDeploymentArchiveHandler;
import coverage.deployarchivetools.impl.ActiveBPELDeploymentArchiveHandler;
import coverage.instrumentation.bpelxmltools.BasisActivity;
import coverage.instrumentation.metrics.IMetric;
import coverage.instrumentation.metrics.IMetricHandler;
import coverage.instrumentation.metrics.MetricHandler;
import coverage.instrumentation.metrics.branchcoverage.BranchMetric;
import coverage.instrumentation.metrics.statementcoverage.Statementmetric;
import exception.ArchiveFileException;
import exception.BpelException;
import exception.BpelVersionException;
import exception.CoverageToolException;

public class CoverageMeasurement {

	private static List<IMetric> metrics = new ArrayList<IMetric>();

	private static final String PROPERTY = "property";

	private static final String BASIC_ACTIVITIES = "IncludeBasicActivities";

	private static final String INCLUDE_FLOW_LINKS = "IncludeFlowLinks";

	private static final String INCLUDE_TRUE_FALSE_TRANSITION_CONDITION = "IncludeTrueFalseTransitionCondition";

	private static final String COVERAGE_SERVICE_WSDL = "_LogService_.wsdl";

	private static final Namespace NAMESPACE_CONFIGURATION = Namespace
			.getNamespace("http://www.bpelunit.org/schema/coverageToolConfiguration");

	private Logger logger;

	private String fConfigDirectory;

	public CoverageMeasurement(File file, String fConfigDirectory)
			throws ConfigurationException {
		this.fConfigDirectory = fConfigDirectory;
		logger = Logger.getLogger(getClass());
		loadCoverageMeasurement(file);
	}

	private void loadCoverageMeasurement(File file)
			throws ConfigurationException {
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
					"An XML reading error occurred reading the deployment plug-ins from file "
							+ file.getAbsolutePath(), e);
		} catch (IOException e) {
			throw new ConfigurationException(
					"An I/O error occurred reading the deployment plug-ins from file "
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

	public String prepareForCoverageMeasurement(String pathToArchive,
			String archiveFile, IBPELDeployer deployer)
			throws CoverageToolException {

		IDeploymentArchiveHandler archiveHandler = null;

		if (deployer instanceof ActiveBPELDeployer) {
			archiveHandler = new ActiveBPELDeploymentArchiveHandler();
		}
		if (archiveHandler == null) {
			throw new CoverageToolException(deployer.toString()
					+ " is by coverage tool not supported");
		}
		try {
			archiveHandler.setArchiveFile(FilenameUtils.concat(pathToArchive,
					archiveFile));
			prepareLoggingService(archiveHandler);
			executeInstrumentationOfBPEL(archiveHandler);
		} catch (Exception e) {
			throw new CoverageToolException("", e);
		}

		return archiveHandler.getArchiveFile().getName();
	}

	private void executeInstrumentationOfBPEL(
			IDeploymentArchiveHandler archiveHandler) throws JDOMException,
			IOException, BpelException, BpelVersionException {
		IMetricHandler metricHandler = new MetricHandler();
		for (Iterator<IMetric> iter = metrics.iterator(); iter.hasNext();) {
			metricHandler.addMetric(iter.next());
		}

		de.schlichtherle.io.File bpelFile;
		for (int i = 0; i < archiveHandler.getCountOfBPELFiles(); i++) {
			bpelFile = archiveHandler.getBPELFile(i);
			metricHandler.startInstrumentation(bpelFile);
		}
	}

	private void prepareLoggingService(IDeploymentArchiveHandler archiveHandler)
			throws IOException, ArchiveFileException, JDOMException {
		archiveHandler.addWSDLFile(new File(FilenameUtils.concat(
				fConfigDirectory, COVERAGE_SERVICE_WSDL)));
	}
}
