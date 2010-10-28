package net.bpelunit.toolsupport.editors.wizards.fields;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class TreeItemToolTip implements Listener {
	private Shell tip = null;
	private Label label = null;
	private Listener labelListener;
	protected Tree tree;
	private String text = "";

	public TreeItemToolTip(Tree tree) {
		this.tree = tree;
		this.labelListener = new Listener() {
			public void handleEvent(Event event) {
				Label label = (Label) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event();
					e.item = (TreeItem) label.getData("_TABLEITEM");
					// Assuming table is single select, set the selection as if
					// the mouse down event went through to the table
					TreeItemToolTip.this.tree.setSelection((TreeItem) e.item);
					TreeItemToolTip.this.tree.notifyListeners(SWT.Selection, e);
					shell.dispose();
					TreeItemToolTip.this.tree.setFocus();
					break;
				case SWT.MouseExit:
					shell.dispose();
					break;
				}
			}
		};
	}

	@Override
	public void handleEvent(Event event) {
		if ("".equals(this.text) || this.text == null) {
			return;
		}
		switch (event.type) {
		case SWT.Dispose:
		case SWT.KeyDown:
		case SWT.MouseMove: {
			if (this.tip == null) {
				break;
			}
			this.tip.dispose();
			this.tip = null;
			this.label = null;
			break;
		}
		case SWT.MouseHover: {
			TreeItem item = this.tree.getItem(new Point(event.x, event.y));
			if (item != null) {
				if (this.tip != null && !this.tip.isDisposed()) {
					this.tip.dispose();
				}
				this.tip = new Shell(this.tree.getShell(), SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
				this.tip.setBackground(this.tree.getDisplay().getSystemColor(
						SWT.COLOR_INFO_BACKGROUND));
				FillLayout layout = new FillLayout();
				layout.marginWidth = 2;
				this.tip.setLayout(layout);
				this.label = new Label(this.tip, SWT.NONE);
				this.label.setForeground(this.tree.getDisplay().getSystemColor(
						SWT.COLOR_INFO_FOREGROUND));
				this.label.setBackground(this.tree.getDisplay().getSystemColor(
						SWT.COLOR_INFO_BACKGROUND));
				this.label.setData("_TABLEITEM", item);
				this.label.setText(this.getText());
				this.label.addListener(SWT.MouseExit, this.labelListener);
				this.label.addListener(SWT.MouseDown, this.labelListener);
				Point size = this.tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
				Point pt = this.tree.toDisplay(event.x, event.y);
				// TODO: use height of cursor instead of 20. Until now, there exists no method to get the cursor size
				this.tip.setBounds(pt.x, pt.y + 20, size.x, size.y);
				this.tip.setVisible(true);
			}
		}
		}
	}

	private String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
