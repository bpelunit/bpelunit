package net.bpelunit.model.bpel;

public interface IWaitingActivity {

	String getDeadline();
	void setDeadline(String deadlineExpression);
	String getDuration();
	void setDuration(String durationExpression);
}
