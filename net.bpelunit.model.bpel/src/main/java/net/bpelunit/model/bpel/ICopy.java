package net.bpelunit.model.bpel;


public interface ICopy {

	void setFrom(IFrom value);

	void setTo(ITo value);

	ITo getTo();

	IFrom getFrom();

}
