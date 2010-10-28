package net.bpelunit.framework.client.eclipse.preferences;

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class IntegerFieldEditor extends StringFieldEditor {

	
	
	public IntegerFieldEditor(String p_coverage_wait_time, String label, int width, int validate_on_key_stroke, Composite fieldEditorParent) {
		super(p_coverage_wait_time, label, width, validate_on_key_stroke, fieldEditorParent);
	}


	@Override
	protected boolean checkState() {
		if(!super.checkState()){
			return false;
		}
		try{
			Integer.parseInt(getStringValue());
			}catch(NumberFormatException e){
				return false;
			}
		return true;
	}
	

}
