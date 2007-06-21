package org.bpelunit.framework.coverage.receiver;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.result.statistic.IFileStatistic;

public class MarkersRegisterForArchive {

	private static MarkersRegisterForArchive instance = null;

	private Hashtable<String, MarkerState> allCoverageLabels;

	private List<MarkersRegistryForBPELFile> bpelFiles;

	private Logger logger=Logger.getLogger(getClass());;

	private List<String> infos;

	private MarkersRegistryForBPELFile currentFileRegestry = null;

	private MetricsManager metricManager;

	// public static LabelsRegistry getInstance() {
	// if (instance == null){
	// instance = new LabelsRegistry();
	//
	// logger=Logger.getLogger(instance.getClass());
	// }
	// return instance;
	// }

	public MarkersRegisterForArchive(MetricsManager metricManager) {
		this.metricManager = metricManager;
		allCoverageLabels = new Hashtable<String, MarkerState>();
		bpelFiles = new ArrayList<MarkersRegistryForBPELFile>();
		infos = new ArrayList<String>();
	}

	public void addMarker(String marker) {
		logger.info("MARKER Registrieret=" + marker);
		MarkerState status = new MarkerState();
		allCoverageLabels.put(marker, status);
		currentFileRegestry.addMarker(marker, status);
	}

	public synchronized void setCoverageStatusForAllMarker(String marker,
			String testCase) {
		logger.info("!!!!!!!!!!MARKER empfangen "+marker);
		Scanner scanner = new Scanner(marker);
		scanner.useDelimiter(Instrumenter.SEPARATOR);
		String marke;
		while (scanner.hasNext()) {
			marke = scanner.next().trim();
			if (marke.length() > 0)
				setCoverageStatusForMarker(marke, testCase);
		}
	}

	private void setCoverageStatusForMarker(String coverageLabel,
			String testCase) {
		if (testCase != null) {
			logger.info("!!!!!!!MARKE!!!! " + coverageLabel + " Testcase ="
					+ testCase);
			MarkerState status = allCoverageLabels.get(coverageLabel);
			if (status != null)
				status.setStatus(true, testCase);
			else
				logger.debug("No exist state for marker: "+coverageLabel+"!");
		} else {
			 logger.debug("Test case is not set!");
		}
	}

	public List<IFileStatistic> getStatistics() {
		List<IFileStatistic> statistics = new ArrayList<IFileStatistic>();
		MarkersRegistryForBPELFile registry4File;
		for (Iterator<MarkersRegistryForBPELFile> iter = bpelFiles.iterator(); iter
				.hasNext();) {
			registry4File = iter.next();
			statistics.add(registry4File.getFileStatistic());
		}
		return statistics;
	}

	public void addRegistryForFile(String fileName) {
		MarkersRegistryForBPELFile registry4File = new MarkersRegistryForBPELFile(
				fileName, metricManager);
		bpelFiles.add(registry4File);
		currentFileRegestry = registry4File;
	}

	// public void initialize() {
	// allCoverageLabels = new Hashtable<String, LabelStatus>();
	// bpelFiles = new ArrayList<LabelsRegistryForBPELFile>();
	// currentFileRegestry=null;
	// }

	//
	public void destroy() {
		instance = null;
	}

	public void addInfo(String info) {
		infos.add(info);
	}

	public List<String> getInfo() {
		return infos;
	}

}
