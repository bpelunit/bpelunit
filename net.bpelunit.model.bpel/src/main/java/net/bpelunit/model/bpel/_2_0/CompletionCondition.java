package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.ICompletionCondition;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TBoolean;
import org.oasisOpen.docs.wsbpel.x20.process.executable.TCompletionCondition;

public class CompletionCondition extends AbstractBpelObject implements
		ICompletionCondition {

	private TCompletionCondition completionCondition;

	public CompletionCondition(TCompletionCondition nativeCompletionCondition) {
		super(nativeCompletionCondition);
		this.completionCondition = nativeCompletionCondition;
	}

	@Override
	public boolean getSuccessfulBranchesOnly() {
		try {
			return TBoolean.YES.equals(completionCondition.getBranches()
					.getSuccessfulBranchesOnly());
		} catch (NullPointerException e) {
			return false;
		}
	}

	@Override
	public void setSuccessfulBranchesOnly(boolean newValue) {
		if (completionCondition.getBranches() == null) {
			completionCondition.addNewBranches();
		}

		completionCondition.getBranches().setSuccessfulBranchesOnly(
				TBooleanHelper.convert(newValue));
	}
}
