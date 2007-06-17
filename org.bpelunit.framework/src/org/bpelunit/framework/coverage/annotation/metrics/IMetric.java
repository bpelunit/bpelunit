package org.bpelunit.framework.coverage.annotation.metrics;

import java.util.Hashtable;
import java.util.List;

import org.bpelunit.framework.coverage.exceptions.BpelException;
import org.bpelunit.framework.coverage.receiver.LabelStatus;
import org.bpelunit.framework.coverage.result.statistic.IStatistic;
import org.jdom.Element;




public interface IMetric {

	public String getName();

	public List<String> getMetriclabelsIds();

	public IStatistic createStatistic(
			Hashtable<String, Hashtable<String, LabelStatus>> allLabels);
	
	public void  setOriginalBPELDocument(Element element);

	public void insertMarkers() throws BpelException;
}
