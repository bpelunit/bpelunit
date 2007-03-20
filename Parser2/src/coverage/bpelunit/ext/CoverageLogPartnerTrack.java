package coverage.bpelunit.ext;


import org.bpelunit.framework.model.Partner;
import org.bpelunit.framework.model.test.PartnerTrack;
import org.bpelunit.framework.model.test.TestCase;
import org.bpelunit.framework.model.test.activity.Activity;
import org.bpelunit.framework.model.test.activity.ActivityContext;
import org.bpelunit.framework.model.test.report.ArtefactStatus;



public class CoverageLogPartnerTrack extends PartnerTrack {

	private Activity activity;

	public CoverageLogPartnerTrack(TestCase arg0, Partner partner) {
		super(arg0, partner);
		fStatus = ArtefactStatus.createInitialStatus();
	}

	public CoverageLogPartnerTrack(Partner arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArtefactStatus getStatus() {
		return fStatus;
	}

	public void run() {


		fStatus = ArtefactStatus.createPassedStatus();
		for (Activity activity : fActivities) {
			this.activity=activity;
			fLogger.info(getName() + " now starting activity " + activity);

			ActivityContext context = new ActivityContext(fRunner, this);
			activity.run(context);
			fLogger.info(getName() + " returned from activity " + activity);

		}
		fRunner.done(this);

	}

	public void interruptLoggingTrack() {
		fLogger.info("LOGSERVICE wird interrupt");
		((ReceivePermanent)activity).interruptActivity();
	}

}
