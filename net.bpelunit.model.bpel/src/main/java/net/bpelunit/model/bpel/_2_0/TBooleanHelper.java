package net.bpelunit.model.bpel._2_0;

import org.oasis_open.docs.wsbpel._2_0.process.executable.TBoolean;

final class TBooleanHelper {

	private TBooleanHelper() {
	}
	
	public static boolean convert(TBoolean b) {
		return b.equals(TBoolean.YES);
	}
	
	public static TBoolean convert(boolean b) {
		if(b) {
			return TBoolean.YES;
		} else {
			return TBoolean.NO;
		}
	}
	
}
