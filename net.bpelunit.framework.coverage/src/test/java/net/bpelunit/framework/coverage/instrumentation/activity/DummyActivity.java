package net.bpelunit.framework.coverage.instrumentation.activity;

import java.util.List;

import net.bpelunit.model.bpel.ActivityType;
import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IActivityContainer;
import net.bpelunit.model.bpel.IBpelFactory;
import net.bpelunit.model.bpel.IDocumentation;
import net.bpelunit.model.bpel.IVisitor;
import net.bpelunit.util.StringUtil;

class DummyActivity implements IActivity {

	private String activityName;
	private String activityType;

	public DummyActivity(String activityName, String activityType) {
		super();
		this.activityName = activityName;
		this.activityType = activityType;
	}
	
	public IBpelFactory getFactory() {
		return null;
	}

	public List<? extends IDocumentation> getDocumentation() {
		return null;
	}

	public IDocumentation addDocumentation() {
		return null;
	}

	public void visit(IVisitor v) {
	}

	public boolean isBasicActivity() {
		return true;
	}

	public String getActivityName() {
		return activityType;
	}

	public String getName() {
		return activityName;
	}

	public String getXPathInDocument() {
		return "//" + activityType + "['" + activityName + "']";
	}

	public void setName(String newName) {
		throw new UnsupportedOperationException("Test Mock");
	}

	public ActivityType getActivityType() {
		return ActivityType.valueOf(StringUtil.toFirstUpper(activityType));
	}

	public boolean getSuppressJoinFailure() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setSuppressJoinFailure(boolean value) {
		// TODO Auto-generated method stub
		
	}

	public IActivityContainer getParent() {
		// TODO Auto-generated method stub
		return null;
	}
}