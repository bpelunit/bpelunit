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


public class LabelsRegistry {

	private static LabelsRegistry instance = null;

	private Hashtable<String, LabelStatus> allCoverageLabels;

	private List<LabelsRegistryForBPELFile> bpelFiles;

	private static Logger logger;
	
	private List<String> infos;

	private LabelsRegistryForBPELFile currentFileRegestry=null;

	private MetricsManager metricManager;

//	public static LabelsRegistry getInstance() {
//		if (instance == null){
//			instance = new LabelsRegistry();
//
//			logger=Logger.getLogger(instance.getClass());
//		}
//		return instance;
//	}


	public LabelsRegistry(MetricsManager metricManager) {
		this.metricManager=metricManager;
		logger = Logger.getLogger(getClass());
		allCoverageLabels = new Hashtable<String, LabelStatus>();
		bpelFiles = new ArrayList<LabelsRegistryForBPELFile>();
		infos=new ArrayList<String>();
	}
	

	public void addMarker(String marker) {
		logger.info("MARKER Registrieret="+marker);
		LabelStatus status=new LabelStatus();
		allCoverageLabels.put(marker, status);
		currentFileRegestry.addMarker(marker,status);
	}

	public synchronized void setCoverageStatusForAllMarker(String marker, String testCase) {
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
		logger.info("!!!!!!!MARKE!!!! "+coverageLabel+" Testcase ="+testCase);
		LabelStatus status = allCoverageLabels.get(coverageLabel);
		status.setStatus(true, testCase);
	}

	public List<IFileStatistic> getStatistics() {
		List<IFileStatistic> statistics = new ArrayList<IFileStatistic>();
		LabelsRegistryForBPELFile registry4File;
		for (Iterator<LabelsRegistryForBPELFile> iter = bpelFiles.iterator(); iter
				.hasNext();) {
			registry4File = iter.next();
			statistics.add(registry4File.getFileStatistic());
		}
		return statistics;
	}

	public void addRegistryForFile(String fileName) {
		LabelsRegistryForBPELFile registry4File = new LabelsRegistryForBPELFile(fileName,metricManager);
		bpelFiles.add(registry4File);
		currentFileRegestry=registry4File;
	}


//	public void initialize() {
//		allCoverageLabels = new Hashtable<String, LabelStatus>();
//		bpelFiles = new ArrayList<LabelsRegistryForBPELFile>();
//		currentFileRegestry=null;
//	}

//
	public void destroy() {
		instance=null;	
	}
	
	public void addInfo(String info){
		infos.add(info);
	}
	
	public List<String> getInfo(){
		return infos;
	}

}
