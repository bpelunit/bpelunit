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
import java.util.concurrent.TimeoutException;

import net.bpelunit.framework.model.AbstractPartner;
import net.bpelunit.framework.model.test.PartnerTrack;
import net.bpelunit.framework.model.test.data.SendDataSpecification;
import net.bpelunit.framework.model.test.data.extraction.IExtractedDataContainer;
import net.bpelunit.framework.model.test.report.ArtefactStatus;
import net.bpelunit.framework.model.test.report.ITestArtefact;
import net.bpelunit.framework.model.test.report.StateData;
import net.bpelunit.framework.model.test.wire.IncomingMessage;
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
	private PartnerTrack partnerTrack;

	/**
	 * The status of this object
	 */
	private ArtefactStatus status;

	private String fAssumption;

	private String id;

	private List<String> dependsOn = new ArrayList<String>();

	private final Map<String, Object> extractedData = new HashMap<String, Object>();

	private ITestArtefact parent;

	// ******************* Initialization *********************

	public Activity(PartnerTrack partnerTrack, ITestArtefact parent) {
		this.partnerTrack= partnerTrack;
		this.parent = parent;
		
		status = ArtefactStatus.createInitialStatus();
	}
	
	public Activity(Activity parent) {
		this(parent.getPartnerTrack(), parent);
	}
	
	public Activity(PartnerTrack pt) {
		this(pt, pt);
	}

	public Activity(ITestArtefact parent) {
		ITestArtefact partnerTrack = parent;
		
		while(!(partnerTrack instanceof PartnerTrack)) {
			partnerTrack = partnerTrack.getParent();
		}
		this.partnerTrack = (PartnerTrack)partnerTrack;
		this.parent = parent;
		
		status = ArtefactStatus.createInitialStatus();
	}

	// ************************** Run *************************


	public final void run(ActivityContext context) {
		run(context, null);
	}
	
	public final void run(ActivityContext context, IncomingMessage message) {
		try {
			preRun(context);
			if(getStatus().isAborted()) {
				return;
			}
		
			if(isStartingWithMessageReceive()) {
				if(message == null) {
					try {
						message = context.receiveMessage(this.getPartnerTrack());
					} catch (TimeoutException e) {
						setStatus(ArtefactStatus.createErrorStatus("Timeout while waiting for incoming asynchronous message", e));
						return;
					} catch (InterruptedException e) {
						setStatus(ArtefactStatus.createAbortedStatus("Aborted while waiting for incoming asynchronous messsage", e));
						return;
					}
				}
				runInternal(context, message);
			} else {
				runInternal(context);
			}
			
			
		} finally {
			postRun(context);
		}
	}
	
	/**
	 * Called for activities that do NOT require a message to be received first
	 */
	public void runInternal(ActivityContext context) {
		
	}
	
	/**
	 * Called for activities that require a message to be received first
	 */
	public void runInternal(ActivityContext context, IncomingMessage message) {
		
	}

	protected void preRun(ActivityContext context) {
		while(!context.getExecutedActivities().containsAll(dependsOn)) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				setStatus(ArtefactStatus.createAbortedStatus("Thread was interrupted while waiting for all dependent activities to complete."));
			}
		}
	}
	
	protected void postRun(ActivityContext context) {
		context.markActivityAsExecuted(this.getId());
	}


	// ******************* Getters and Setters ***************

	public PartnerTrack getPartnerTrack() {
		return partnerTrack;
	}


	public AbstractPartner getPartner() {
		return partnerTrack.getPartner();
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
		return status;
	}

	protected void setStatus(ArtefactStatus status) {
		this.status = status;
	}
	
	public final ITestArtefact getParent() {
		return parent;
	}

	public abstract List<? extends ITestArtefact> getChildren();

	public List<StateData> getStateData() {
		return status.getAsStateData();
	}

	public void reportProgress(ITestArtefact artefact) {
		partnerTrack.reportProgress(artefact);
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
	
	/** 
	 * A flag whether an activity's first operation is to wait for a message or not
	 */
	public abstract boolean isStartingWithMessageReceive();
	
	public boolean canExecute(ActivityContext context, IncomingMessage message) {
		return false;
	}
}
