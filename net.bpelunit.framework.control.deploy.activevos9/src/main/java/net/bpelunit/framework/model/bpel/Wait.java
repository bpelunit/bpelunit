package net.bpelunit.framework.model.bpel;

import org.w3c.dom.Element;

public class Wait extends BasicActivity implements TimingActivity {

	Wait(Element bpelElement, BPELProcess bpelProcess) {
		super(bpelElement, bpelProcess);
	}

	@Override
	public void setDuration(String expression) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setDeadline(String expression) {
		// TODO Auto-generated method stub
		
	}

	
	
}
