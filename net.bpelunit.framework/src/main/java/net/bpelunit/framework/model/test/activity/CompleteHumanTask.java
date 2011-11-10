package net.bpelunit.framework.model.test.activity;

import java.util.Collections;
import java.util.List;

import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.xml.suite.XMLAnyElement;

public class CompleteHumanTask extends Activity {

	private String taskName;
	private XMLAnyElement data;

	public CompleteHumanTask(PartnerTrack pTrack) {
		super(pTrack);
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public void setData(XMLAnyElement data) {
		this.data = data;
	}

	@Override
	public void run(ActivityContext context) {
		System.out.println("RUN HUMAN TASK!");
	}

	@Override
	public int getActivityCount() {
		return 1;
	}

	@Override
	public String getActivityCode() {
		return "CompleteHumanTask";
	}

	@Override
	public String getName() {
		return "Complete Human Task";
	}

	@Override
	public ITestArtefact getParent() {
		return getPartnerTrack();
	}

	@Override
	public List<ITestArtefact> getChildren() {
		return Collections.emptyList();
	}
}
