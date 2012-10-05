package net.bpelunit.framework.control.run;

public interface BlackBoardKey {

	/**
	 * Returns <code>true</code> if this blackboard key can still provide the
	 * requested value in the specified blackboard, or <code>false</code> if it
	 * can't. For instance, a {@link PartnerLink} which has completed its
	 * execution will be unable to respond to any blackboard requests.
	 */
	boolean canStillProvideValue(BlackBoard<?, ?> blackboard);

}