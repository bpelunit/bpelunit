/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.model.test.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.framework.model.AbstractPartner;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.SendDataSpecification;
import net.bpelunit.framework.model.test.data.extraction.IExtractedDataContainer;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import net.bpelunit.framework.model.test.wire.OutgoingMessage;

/**
 * An activity is one logical event in the chain of events of a partner track. It is executed as
 * part of the partner track, "acting on behalf of the partner".
 * 
 * An activity uses other activities and, in the end, the data send/receive packages to achieve its
 * goal. When one of these child activities fail, error, or abort, the parent takes on its status.
 * 
 * If none of the children fail, error, or abort, the activity reports a passed status.
 * 
 * Each activity has very unique requirements. For example, a send/receive synchronous activity will
 * send and receive in a single, indivisable HTTP connection. A receive/send asynchronous activity
 * has in fact two consecutive single HTTP connections, each with send and receive data.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public abstract class Activity implements ITestArtefact, IExtractedDataContainer {

	/**
	 * The partner track this activity belongs to.
	 */
	private PartnerTrack fPartnerTrack;

	/**
	 * The status of this object
	 */
	private ArtefactStatus fStatus;

	private String fAssumption;

	private String id;

	private List<String> dependsOn = new ArrayList<String>();

	private final Map<String, Object> extractedData = new HashMap<String, Object>();

	// ******************* Initialization *********************

	public Activity(PartnerTrack partnerTrack) {
		fPartnerTrack= partnerTrack;
		fStatus= ArtefactStatus.createInitialStatus();
	}


	// ************************** Run *************************

	public final void run(ActivityContext context) {
		try {
			preRun(context);
			if(getStatus().isAborted()) {
				return;
			}
		
			runInternal(context);
		} finally {
			postRun(context);
		}
	}
	
	public abstract void runInternal(ActivityContext context);

	protected void preRun(ActivityContext context) {
		while(!context.getExecutedActivities().containsAll(dependsOn)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				setStatus(ArtefactStatus.createAbortedStatus("Thread was interrupted while waiting for all dependent activities to complete."));
				return;
			}
		}
	}
	
	protected void postRun(ActivityContext context) {
		context.markActivityAsExecuted(this.getId());
	}


	// ******************* Getters and Setters ***************

	public PartnerTrack getPartnerTrack() {
		return fPartnerTrack;
	}


	public AbstractPartner getPartner() {
		return fPartnerTrack.getPartner();
	}

	public abstract int getActivityCount();

	/**
	 * Returns the code (not human-readable) of the activity, for example, "sendReceiveSync"
	 * 
	 * @return
	 */
	public abstract String getActivityCode();

	public boolean hasProblems() {
		return getStatus().hasProblems();
	}

	public String getAssumption() {
		return fAssumption;
	}

	public void setAssumption(String assumption) {
		fAssumption = assumption;
	}

	// ********************** ITestArtefact ******************

	public abstract String getName();

	public ArtefactStatus getStatus() {
		return fStatus;
	}

	protected void setStatus(ArtefactStatus status) {
		this.fStatus = status;
	}
	
	public abstract ITestArtefact getParent();

	public abstract List<ITestArtefact> getChildren();

	public List<StateData> getStateData() {
		return fStatus.getAsStateData();
	}

	public void reportProgress(ITestArtefact artefact) {
		fPartnerTrack.reportProgress(artefact);
	}

	// ************** IExtractedDataContainer ******************

	@Override
	public void putExtractedData(String name, Object value) {
		extractedData.put(name,  value);
	}

	@Override
	public Object getExtractedData(String name) {
		return extractedData.get(name);
	}

	@Override
	public Collection<String> getAllExtractedDataNames() {
		return extractedData.keySet();
	}

	// *********************** Other ***************************

	@Override
	public String toString() {
		return getName();
	}

	protected static void copyProtocolOptions(SendDataSpecification source, OutgoingMessage target) {
		for(String option : source.getProtocolOptionNames()) {
			target.addProtocolOption(option, source.getProtocolOption(option));
		}
	}

	public void setId(String newId) {
		this.id = newId;
	}

	private String getId() {
		return this.id;
	}
	
	public void setDependsOn(List<String> newDependsOn) {
		if(newDependsOn != null) {
			this.dependsOn = new ArrayList<String>(newDependsOn);
		} else {
			this.dependsOn = new ArrayList<String>();
		}
	}
}
