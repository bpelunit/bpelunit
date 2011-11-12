package net.bpelunit.framework.model.test.activity;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import net.bpelunit.framework.model.HumanPartner;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.wsht.WSHTClient;
import net.bpelunit.framework.xml.suite.XMLAnyElement;

import org.example.wsHT.api.XMLTTask;

public class CompleteHumanTask extends Activity {

	private String taskName;
	private XMLAnyElement data;
	private int waitTime = 500;
	private int maxTimeOut = 10000;

	/**
	 * This lock is used to serialize requests to WS-HT services. This allows
	 * two different tracks to complete tasks with the same name without
	 * interfering with each other.
	 */
	private static final ReentrantLock WSHT_LOCK = new ReentrantLock();

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
						// XXX Not nice, acquiring lock so that the finally unlock() does work
						WSHT_LOCK.lock();
						// TODO Indicate error
						return;
					}
				} while (taskList.size() == 0);
				XMLTTask taskToFinish = taskList.get(taskList.size()-1);
				
				client.completeTaskWithOutput(taskToFinish.getId(), data);
			} finally {
				WSHT_LOCK.unlock();
			}

		} catch (Exception e) {
			e.printStackTrace();
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
		return Collections.emptyList();
	}
}
