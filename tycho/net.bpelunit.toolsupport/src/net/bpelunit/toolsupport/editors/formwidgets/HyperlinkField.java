/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.editors.formwidgets;

import org.eclipse.jface.preference.JFacePreferences;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.Hyperlink;

/**
 * The hyperlink field is a label which behaves like a hyperlink in a HTML page being displayed in a
 * browser, i.e. it carries the hyperlink color, is underlined, and selectable.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class HyperlinkField {

	public interface IHyperLinkFieldListener {

		public void hyperLinkActivated();
	}

	private String fText;
	private IHyperLinkFieldListener fListener;

	public HyperlinkField(String text) {
		fText= text;
	}

	public void setHyperLinkFieldListener(IHyperLinkFieldListener listener) {
		fListener= listener;
	}

	public void removeHyperLinkFieldListener() {
		fListener= null;
	}

	public Control createControl(Composite parent, int nColumns, int horizontalAlign) {
		Hyperlink link= new Hyperlink(parent, SWT.NONE);
		link.setText(fText);
		link.setUnderlined(true);
		link.setForeground(getHyperlinkText(link.getDisplay()));
		link.addMouseListener(new MouseAdapter() {

			private boolean down= false;

			@Override
			public void mouseUp(MouseEvent e) {
				if (down) {
					if (fListener != null)
						fListener.hyperLinkActivated();
					down= false;
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
				down= true;
			}
		});

		GridData d= new GridData(horizontalAlign, GridData.BEGINNING, true, false);
		d.horizontalSpan= nColumns;
		link.setLayoutData(d);

		return link;
	}

	private static Color getHyperlinkText(Display display) {
		return JFaceResources.getColorRegistry().get(JFacePreferences.HYPERLINK_COLOR);
	}

}
