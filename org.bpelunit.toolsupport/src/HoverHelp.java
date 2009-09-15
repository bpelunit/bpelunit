/*
 * Tool Tips example snippet: create fake tool tips for items in a table
 *
 * For a list of all SWT example snippets see
 * http://www.eclipse.org/swt/snippets/
 */
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class HoverHelp {

	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		final Table table = new Table(shell, SWT.BORDER);
		for (int i = 0; i < 20; i++) {
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText("item " + i);
		}
		// Disable native tooltip
		table.setToolTipText("");

		// Implement a "fake" tooltip
		final Listener labelListener = new Listener() {
			public void handleEvent(Event event) {
				Label label = (Label) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event();
					e.item = (TableItem) label.getData("_TABLEITEM");
					// Assuming table is single select, set the selection as if
					// the mouse down event went through to the table
					table.setSelection(new TableItem[] { (TableItem) e.item });
					table.notifyListeners(SWT.Selection, e);
					shell.dispose();
					table.setFocus();
					break;
				case SWT.MouseExit:
					shell.dispose();
					break;
				}
			}
		};

		Listener tableListener = new Listener() {
			Shell tip = null;
			Label label = null;

			public void handleEvent(Event event) {
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
					TableItem item = table.getItem(new Point(event.x, event.y));
					if (item != null) {
						if (this.tip != null && !this.tip.isDisposed()) {
							this.tip.dispose();
						}
						this.tip = new Shell(shell, SWT.ON_TOP | SWT.NO_FOCUS | SWT.TOOL);
						this.tip.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						FillLayout layout = new FillLayout();
						layout.marginWidth = 2;
						this.tip.setLayout(layout);
						this.label = new Label(this.tip, SWT.NONE);
						this.label.setForeground(display.getSystemColor(SWT.COLOR_INFO_FOREGROUND));
						this.label.setBackground(display.getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						this.label.setData("_TABLEITEM", item);
						this.label.setText(item.getText());
						this.label.addListener(SWT.MouseExit, labelListener);
						this.label.addListener(SWT.MouseDown, labelListener);
						Point size = this.tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
						Rectangle rect = item.getBounds(0);
						Point pt = table.toDisplay(rect.x, rect.y);
						this.tip.setBounds(pt.x, pt.y, size.x, size.y);
						this.tip.setVisible(true);
					}
				}
				}
			}
		};
		table.addListener(SWT.Dispose, tableListener);
		table.addListener(SWT.KeyDown, tableListener);
		table.addListener(SWT.MouseMove, tableListener);
		table.addListener(SWT.MouseHover, tableListener);
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}
}