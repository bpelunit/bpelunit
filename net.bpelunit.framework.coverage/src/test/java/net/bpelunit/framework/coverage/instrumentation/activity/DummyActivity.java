package net.bpelunit.framework.coverage.instrumentation.activity;

import java.util.List;

import net.bpelunit.model.bpel.IActivity;
import net.bpelunit.model.bpel.IBpelFactory;
import net.bpelunit.model.bpel.IDocumentation;
import net.bpelunit.model.bpel.IVisitor;

class DummyActivity implements IActivity {

	private String activityName;
	private String activityType;

	public DummyActivity(String activityName, String activityType) {
		super();
		this.activityName = activityName;
		this.activityType = activityType;
	}
	
	@Override
	public IBpelFactory getFactory() {
		return null;
	}

	@Override
	public List<? extends IDocumentation> getDocumentation() {
		return null;
	}

	@Override
	public IDocumentation addDocumentation() {
		return null;
	}

	@Override
	public void visit(IVisitor v) {
	}

	@Override
	public boolean isBasicActivity() {
		return true;
	}

	@Override
	public String getActivityName() {
		return activityType;
	}

	@Override
	public String getName() {
		return activityName;
	}

	@Override
	public String getXPathInDocument() {
		return "//" + activityType + "['" + activityName + "']";
	}

	@Override
	public void setName(String newName) {
		throw new UnsupportedOperationException("Test Mock");
	}
}