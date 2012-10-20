package net.bpelunit.model.bpel;


public interface ICopy {

	ITo getTo();
	IFrom getFrom();

	boolean getKeepSrcElementName();
	void setKeepSrcElementName(boolean value);

	boolean getIgnoreMissingFromData();
	void setIgnoreMissingFromData(boolean value);
}
