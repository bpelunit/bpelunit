package net.bpelunit.model.bpel._2_0;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TBoolean;

final class TBooleanHelper {

	private TBooleanHelper() {
	}
	
	public static boolean convert(TBoolean b) {
		return b.getStringValue().equals(TBoolean.YES.toString());
	}
	
	public static boolean convert(TBoolean.Enum b) {
		return b.equals(TBoolean.YES);
	}
	
	public static TBoolean.Enum convert(boolean b) {
		if(b) {
			return TBoolean.YES;
		} else {
			return TBoolean.NO;
		}
	}
	
}
