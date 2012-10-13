package net.bpelunit.model.bpel._2_0;

import net.bpelunit.model.bpel.IOnMessage;

import org.oasisOpen.docs.wsbpel.x20.process.executable.TOnMessage;

public class OnMessage extends AbstractSingleContainer<TOnMessage> implements IOnMessage {

	OnMessage(TOnMessage nativeOnMessage) {
		super(nativeOnMessage);
	}
}
