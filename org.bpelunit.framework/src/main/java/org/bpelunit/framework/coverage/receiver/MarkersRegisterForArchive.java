package org.bpelunit.framework.coverage.receiver;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.bpelunit.framework.coverage.annotation.Instrumenter;
import org.bpelunit.framework.coverage.annotation.MetricsManager;
import org.bpelunit.framework.coverage.result.statistic.IFileStatistic;

/*
 * 
 * Die Klasse verwaltet alle eingefügten Coverage-Marken eines Deployment-Archivs.
 * 
 * @author Alex Salnikow
 *
 */
/**
 * This class manages all coverage markers included in a deployment archive
 * 
 * @author Alex Salnikow, Ronald Becher
 * 
 */
public class MarkersRegisterForArchive {

	private Hashtable<String, MarkerState> allCoverageLabels;

	private List<MarkersRegistryForBPELFile> bpelFiles;

	private List<String> infos;

	private MarkersRegistryForBPELFile currentFileRegestry = null;

	private MetricsManager metricManager;

	public MarkersRegisterForArchive(MetricsManager metricManager) {
		this.metricManager = metricManager;
		allCoverageLabels = new Hashtable<String, MarkerState>();
		bpelFiles = new ArrayList<MarkersRegistryForBPELFile>();
		infos = new ArrayList<String>();
	}

	/*
	 * registriert eingefügte Marken
	 * 
	 * @param marker Coverage-Marke
	 */
	/**
	 * Registers included markers
	 * 
	 * @param marker
	 *            Coverage marker
	 */
	public void registerMarker(String marker) {
		MarkerState status = new MarkerState();
		allCoverageLabels.put(marker, status);
		currentFileRegestry.registerMarker(marker, status);
	}

	/*
	 * Setzt status der empfangenen Marken auf true und vermerkt den Testcase.
	 * 
	 * @param marker Coverage-Marken (einer Nachricht)
	 * 
	 * @param testCase
	 */
	/**
	 * Sets marker status to tested and saves associated test case
	 * 
	 * @param marker
	 *            Coverage marker
	 * @param test
	 *            case
	 */
	public synchronized void setCoverageStatusForAllMarker(String marker,
			String testCase) {
		Scanner scanner = new Scanner(marker);
		scanner.useDelimiter(Instrumenter.SEPARATOR);
		String marke;
		while (scanner.hasNext()) {
			marke = scanner.next().trim();
			if (marke.length() > 0)
				setCoverageStatusForMarker(marke, testCase);
		}
		notifyAll();
	}

	private void setCoverageStatusForMarker(String coverageMarker,
			String testCase) {
		if (testCase != null) {
			MarkerState status = allCoverageLabels.get(coverageMarker);
			if (status != null)
				status.setState(true, testCase);
		}
	}

	/*
	 * Generiert Statistiken (nach dem Testlauf) für alle BPEL-Dateien, die im
	 * Archive sind.
	 * 
	 * @return Statistiken (nach dem Testlauf) für alle BPEL-Dateien, die im
	 * Archive sind.
	 */
	/**
	 * Generates statistics (after test run) for all bpel files included in
	 * archive
	 * 
	 * @return statistics
	 */
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

	public void addInfo(String info) {
		infos.add(info);
	}

	public List<String> getInfo() {
		return infos;
	}

}
