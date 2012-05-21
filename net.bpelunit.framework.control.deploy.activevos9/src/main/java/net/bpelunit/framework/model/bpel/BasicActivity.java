package net.bpelunit.framework.model.bpel;

import org.w3c.dom.Element;

public abstract class BasicActivity extends BPELActivity {

	BasicActivity(Element bpelElement, BPELProcess bpelProcess) {
		super(bpelElement, bpelProcess);
	}

}
