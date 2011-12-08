package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import net.bpelunit.framework.model.HumanPartner;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.CompleteHumanTaskSpecification;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.wsht.WSHTClient;

import org.apache.xmlbeans.XmlObject;
import org.example.wsHT.api.XMLTTask;

public class CompleteHumanTask extends Activity {

	private String taskName;
	private int waitTime = 50;
	private int maxTimeOut = 10000;
	private CompleteHumanTaskSpecification dataSpec;

	/**
	 * This lock is used to serialize requests to WS-HT services. This allows
	 * two different tracks to complete tasks with the same name without
	 * interfering with each other.
	 */
	private static final ReentrantLock WSHT_LOCK = new ReentrantLock();

	public CompleteHumanTask(PartnerTrack pTrack) {
		super(pTrack);
	}

	public void initialize(CompleteHumanTaskSpecification spec) {
		dataSpec = spec;
		fStatus = ArtefactStatus.createInitialStatus();
	}
	
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public void run(ActivityContext context) {
		HumanPartner partner = (HumanPartner) getPartner();
		WSHTClient client = partner.getWSHTClient();

		try {
			try {
				int timeout = 0;
				List<XMLTTask> taskList;
				do {
					WSHT_LOCK.lock();
					taskList = client.getReadyTaskList(taskName).getTaskAbstractList();
					if(taskList.size() == 0) {
						WSHT_LOCK.unlock();
						timeout += waitTime ;
						Thread.sleep(waitTime);
					}
					
					if(timeout >= maxTimeOut ) {
						WSHT_LOCK.lock();
						// XXX Not nice, acquiring lock so that the finally unlock() does work
						fStatus = ArtefactStatus.createErrorStatus("Timeout while waiting for task " + taskName);
						return;
					}
				} while (taskList.size() == 0);
				XMLTTask taskToFinish = taskList.get(taskList.size()-1);
				
				XmlObject data = client.getInput(taskToFinish.getId()).getTaskData();
				XmlObject output = dataSpec.handle(data);
				
				client.completeTaskWithOutput(taskToFinish.getId(), output);
			} finally {
				WSHT_LOCK.unlock();
			}

			if (dataSpec.hasProblems())
				fStatus= dataSpec.getStatus();
			else
				fStatus= ArtefactStatus.createPassedStatus();
		} catch (Exception e) {
			fStatus= ArtefactStatus.createErrorStatus("Error while completing human task", e);
		}

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
		List<ITestArtefact> children = new ArrayList<ITestArtefact>();
		children.add(dataSpec);
		return children;
	}
}
