package org.bpelunit.toolsupport.editors.wizards.fields;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bpelunit.framework.xml.suite.XMLTestSuite;
import org.bpelunit.toolsupport.ToolSupportActivator;
import org.bpelunit.toolsupport.editors.wizards.components.StringValueListener;
import org.bpelunit.toolsupport.util.NamespaceEditor;
import org.bpelunit.toolsupport.util.schema.nodes.Attribute;
import org.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import org.bpelunit.toolsupport.util.schema.nodes.Element;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TreeEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

// TODO MessageEditor: bei ctrl+enter neue Zeile einfügen und in diese springen.
// TODO MessageEditor: Beim verlassen eines Attribute Editors, diesen disposen, kommt sonst zu seltsamen Effekten wenn beispielsweise eine neue Operation aus gewählt wird
// TODO MessageEditor: überschreiben Button, der Baum ins xml-Literal schreibt
// TODO MessageEditor: Größen anpassung der Textfelder noch nicht ganz perfekt
// TODO MessageEditor: Wizardgröße bei Start anpassen, darf etwas breiter sein und mal testen wies mit ner größen Begrenzung aussieht.
// TODO MessageEditor: Buttongröße anpassen
// TODO MessageEditor: durchsteppen mit den Cursor-Tasten bei den Buttons reparieren
// TODO MessageEditor: Validieren der Eingabe im MessageEditor anhand des Schemas
public class MessageEditor extends Composite {

	private final class XMLTreeSelectionListener extends SelectionAdapter {
		private final TreeEditor editor;

		XMLTreeSelectionListener(TreeEditor editor) {
			this.editor = editor;
		}

		public void disposeEditor() {
			Control oldEditor = this.editor.getEditor();
			if (oldEditor != null) {
				oldEditor.dispose();
			}
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			// Clean up any previous editor control
			this.disposeEditor();
			if (!MessageEditor.this.isEditable) {
				return;
			}

			// Identify the selected row
			final TreeItem item = (TreeItem) e.item;
			if (item == null) {
				return;
			}
			boolean itemDisabled = MessageEditor.this.isItemDisabled(item
					.getParentItem());
			if (item.getParentItem() != null && itemDisabled) {
				return;
			}

			Element element = (Element) item
					.getData(TREE_ITEM_FOR_ELEMENT_CONTENT);
			if (element != null && !MessageEditor.this.isItemDisabled(item)) {
				// item displays the inner text of a tag and is not disabled.
				this.getInnerTextEditor(item, element);
			} else if ((element = (Element) item
					.getData(TREE_ITEM_FOR_START_TAG)) != null) {
				this.getAttributeEditor(item, element);
			}
		}

		private void getAttributeEditor(final TreeItem item,
				final Element element) {
			boolean itemDisabled = MessageEditor.this.isItemDisabled(item);
			Color color = MessageEditor.this.getColor(itemDisabled);

			final Composite composite = new Composite(MessageEditor.this
					.getTree(), SWT.NULL);
			List<Control> tabList = new ArrayList<Control>();
			composite.setLayout(this.createRowLayout());
			Label label = new Label(composite, SWT.NULL);
			label.setText("<" + MessageEditor.this.getTagName(element));
			label.setForeground(color);
			ComplexType complex = element.getType().getAsComplexType();
			if (complex != null) {
				for (int i = 0; i < complex.getAttributes().size(); i++) {
					Attribute attribute = complex.getAttributes().get(i);
					Text text = this.createAttributeInput(item, composite,
							attribute, color, !itemDisabled);
					if (text != null) {
						tabList.add(text);
					}
				}
			}
			label = new Label(composite, SWT.NULL);
			label.setText(label.getText() + ">");
			label.setForeground(color);
			this.addNumberChangeButtons(item, composite, tabList);
			Control[] tmpTabList = new Control[tabList.size()];
			tabList.toArray(tmpTabList);
			composite.setTabList(tmpTabList);
			tabList.get(0).setFocus();
			this.editor.setEditor(composite, item);
		}

		private void addNumberChangeButtons(final TreeItem item,
				final Composite composite, List<Control> tabList) {
			if (MessageEditor.this.isCloneable(item)) {
				Button button = new Button(composite, SWT.PUSH);
				button.setImage(ToolSupportActivator
						.getImage(ToolSupportActivator.IMAGE_ADD));
				button.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						MessageEditor.this.cloneItem(item);

					}
				});
				tabList.add(button);
				// add delete-button only if item is not disabled.
				if (!MessageEditor.this.isItemDisabled(item)) {
					button = new Button(composite, SWT.PUSH);
					button.setImage(ToolSupportActivator
							.getImage(ToolSupportActivator.IMAGE_DELETE));
					button.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							MessageEditor.this.removeItem(item);

						}
					});
					tabList.add(button);
				}
			}
		}

		private Text createAttributeInput(final TreeItem item,
				final Composite composite, Attribute attribute, Color color,
				boolean isAttributeEditable) {
			Label label = new Label(composite, SWT.NULL);
			label.setText(" " + attribute.getLocalPart() + "=\"");
			label.setForeground(color);
			String attributeValue = (String) item
					.getData(ATTRIBUTE_VALUE_KEY_PREFIX
							+ attribute.getLocalPart());
			attributeValue = (attributeValue == null) ? "" : attributeValue;
			if (isAttributeEditable) {
				Text text = new Text(composite, SWT.MULTI | SWT.BORDER);
				text.setText(attributeValue);
				text.addModifyListener(new ChangeListener(attribute, item));
				text.addFocusListener(new TextFocusListener());
				text.addKeyListener(new KeyAdapter() {

					@Override
					public void keyPressed(KeyEvent event) {
						switch (event.keyCode) {
						case SWT.ARROW_DOWN:
							if (MessageEditor.this.selectNext(item)) {
								composite.dispose();
							}
							break;
						case SWT.ARROW_UP:
							if (MessageEditor.this.selectPrevious(item)) {
								composite.dispose();
							}
							break;
						case SWT.KEYPAD_CR:
						case SWT.CR:
							composite.dispose();
							break;
						case SWT.TAB:
							Control[] tabList = composite.getTabList();
							for (int j = 0; j < tabList.length; j++) {
								if (tabList[j] == event.getSource()) {
									int nextIndex = (j + 1 >= tabList.length) ? 0
											: j + 1;
									tabList[nextIndex].setFocus();
								}
							}
							event.doit = false;
							break;
						}
					}
				});
				label = new Label(composite, SWT.NULL);
				label.setText("\"");
				label.setForeground(color);
				return text;
			}
			label = new Label(composite, SWT.NULL);
			label.setText(attributeValue + "\"");
			label.setForeground(color);
			return null;
		}

		private RowLayout createRowLayout() {
			RowLayout layout = new RowLayout(SWT.HORIZONTAL);
			layout.fill = false;
			layout.spacing = 0;
			layout.marginTop = 0;
			layout.wrap = false;
			return layout;
		}

		private void getInnerTextEditor(final TreeItem item, Element element) {
			final Text text = new Text(MessageEditor.this.getTree(), SWT.MULTI
					| SWT.BORDER);
			text.setText(item.getText());
			text.setVisible(true);
			text.setFocus();

			text.addModifyListener(new ChangeListener(item));
			text.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent event) {
					text.dispose();
				}

				@Override
				public void focusGained(FocusEvent e) {
					text.setSelection(0);
				}
			});

			text.addKeyListener(new KeyAdapter() {

				@SuppressWarnings("fallthrough")
				@Override
				public void keyPressed(KeyEvent event) {
					switch (event.keyCode) {
					case SWT.ARROW_DOWN:
						text.dispose();
						MessageEditor.this.selectNext(item);
						break;
					case SWT.ARROW_UP:
						text.dispose();
						MessageEditor.this.selectPrevious(item);
						break;
					case SWT.TAB:
						event.doit = false;
					case SWT.KEYPAD_CR:
					case SWT.CR:
						text.dispose();
						break;
					}
				}
			});
			this.editor.setEditor(text, item);
		}
	}

	private class ChangeListener implements ModifyListener {

		private Attribute attribute = null;
		private TreeItem item;

		public ChangeListener(Attribute attribute, TreeItem item) {
			this.attribute = attribute;
			this.item = item;
		}

		public ChangeListener(TreeItem item) {
			this.item = item;
		}

		@Override
		public void modifyText(ModifyEvent e) {
			Text text = (Text) e.getSource();
			String key = "";
			if (this.attribute == null) {
				key = MessageEditor.ELEMENT_VALUE_KEY;
			} else {
				key = MessageEditor.ATTRIBUTE_VALUE_KEY_PREFIX
						+ this.attribute.getLocalPart();
			}
			this.item.setData(key, text.getText());
			MessageEditor.this.setItemText(this.item);
			text.pack();
			if (this.attribute != null) {
				text.getParent().pack();
			}
			MessageEditor.this.notifyStringValueListeners();
		}
	}

	protected class TextFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			((Text) e.widget).selectAll();
		}

		@Override
		public void focusLost(FocusEvent e) {
			((Text) e.widget).setSelection(0);
		}
	}

	protected static final int IMAGE_MARGIN = 4;
	private static final String ATTRIBUTE_VALUE_KEY_PREFIX = "@";
	private static final String ELEMENT_VALUE_KEY = "elementContent";
	private static final String TREE_ITEM_FOR_START_TAG = "startTag";
	private static final String TREE_ITEM_FOR_END_TAG = "endTag";
	private static final String TREE_ITEM_FOR_ELEMENT_CONTENT = "inner";
	private static final String TREE_ITEM_IS_DISABLED = "null";
	private static final String TREE_ITEM_CHILDREN = "children";

	private Tree tree;
	private NamespaceEditor namespaceEditor;
	protected boolean isEditable;
	private Map<Element, Integer> elementCount;
	private XMLTreeSelectionListener selectionListener;
	private List<StringValueListener> listeners = new ArrayList<StringValueListener>();
	private Color disabledColor;
	private Color normalColor;

	public MessageEditor(Composite parent, int style, XMLTestSuite suite) {
		super(parent, style);
		this.namespaceEditor = new NamespaceEditor(suite);
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.setEnabled(true);
		this.setLayout(new FillLayout());
		this.disabledColor = new Color(this.getForeground().getDevice(), 180,
				180, 180);
		this.normalColor = new Color(this.getForeground().getDevice(), 0, 0, 0);

		this.tree = new Tree(this, SWT.SINGLE);
		this.tree.setBackground(this.getBackground());
		// this.tree.setHeaderVisible(true);

		// TreeColumn treeColumn = new TreeColumn(this.tree, SWT.LEFT);
		// treeColumn.setWidth(400);
		// treeColumn = new TreeColumn(this.tree, SWT.RIGHT);
		// treeColumn.setWidth(20);

		final TreeEditor editor = new TreeEditor(this.tree);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		this.selectionListener = new XMLTreeSelectionListener(editor);
		this.tree.addSelectionListener(this.selectionListener);
		this.tree.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				if (event.index == 0) {
					Image trailingImage = MessageEditor.this
							.getImage((TreeItem) event.item);
					if (trailingImage != null) {
						event.width += trailingImage.getBounds().width
								+ IMAGE_MARGIN;
					}
				}
			}
		});
		this.tree.addListener(SWT.PaintItem, new Listener() {
			public void handleEvent(Event event) {
				if (event.index == 0) {
					Image trailingImage = MessageEditor.this
							.getImage((TreeItem) event.item);
					if (trailingImage != null) {
						int x = event.x + event.width + IMAGE_MARGIN;
						int itemHeight = MessageEditor.this.getTree()
								.getItemHeight();
						int imageHeight = trailingImage.getBounds().height;
						int y = event.y + (itemHeight - imageHeight) / 2;
						event.gc.drawImage(trailingImage, x, y);
					}
				}
			}
		});
	}

	protected void removeItem(TreeItem item) {
		Element element = (Element) item.getData(TREE_ITEM_FOR_START_TAG);
		Integer count = this.getElementCount(element);
		TreeItem parent = item.getParentItem();
		TreeItem closingItem = parent.getItem(this.getItemIndex(item) + 1);
		if (count > 0) {
			item.dispose();
			closingItem.dispose();
			this.removeElement(element);
		} else {
			item.setData(TREE_ITEM_IS_DISABLED, "true");
			closingItem.setData(TREE_ITEM_IS_DISABLED, "true");
			item.setData(TREE_ITEM_CHILDREN, item.getItems());

			this.setDisabled(item, true);
			closingItem.setForeground(this.disabledColor);
			this.selectionListener.disposeEditor();
		}
		this.notifyStringValueListeners();
	}

	private void setDisabled(TreeItem item, boolean disabled) {
		Color color = this.getColor(disabled);
		item.setForeground(color);
		for (TreeItem i : item.getItems()) {
			this.setDisabled(i, disabled);
		}
		item.setExpanded(!disabled);
		item.setData(TREE_ITEM_IS_DISABLED, "" + disabled);

	}

	protected Color getColor(boolean disabled) {
		Color color;
		if (disabled) {
			color = this.disabledColor;
		} else {
			color = this.normalColor;
		}
		return color;
	}

	protected void cloneItem(TreeItem item) {
		TreeItem parent = item.getParentItem();
		int index = this.getItemIndex(item);
		if (this.isItemDisabled(item)) {
			TreeItem closingItem = parent.getItem(index + 1);
			item.setData(TREE_ITEM_IS_DISABLED, "false");
			closingItem.setData(TREE_ITEM_IS_DISABLED, "false");

			Color color = new Color(this.getForeground().getDevice(), 0, 0, 0);
			this.setDisabled(item, false);
			closingItem.setForeground(color);
		} else {
			Element element = (Element) item.getData(TREE_ITEM_FOR_START_TAG);
			// + 2 because the new element has to be placed after the closing
			// tag of item
			this.displayElement(element, parent, index + 2);
			this.addElement(element);
			this.expandTreeItem(parent);
		}
		this.selectionListener.disposeEditor();
		this.notifyStringValueListeners();
	}

	protected boolean isItemDisabled(TreeItem item) {
		return Boolean.parseBoolean((String) item
				.getData(TREE_ITEM_IS_DISABLED));
	}

	private Integer getElementCount(Element element) {
		Integer count = this.elementCount.get(element);
		if (count == null) {
			count = 0;
		}
		return count;
	}

	private int addElement(Element element) {
		int count = this.getElementCount(element) + 1;
		this.elementCount.put(element, count);
		return count;
	}

	private int removeElement(Element element) {
		int count = Math.max(0, this.getElementCount(element) - 1);
		this.elementCount.put(element, count);
		return count;
	}

	private int getItemIndex(TreeItem item) {
		int index = 0;
		TreeItem parent = item.getParentItem();
		for (TreeItem i : parent.getItems()) {
			if (i == item) {
				break;
			}
			index++;
		}
		return index;
	}

	protected boolean selectNext(TreeItem selected) {
		return this.select(this.getNext(selected, true));
	}

	protected boolean selectPrevious(TreeItem selected) {
		return this.select(this.getPrevious(selected));
	}

	private boolean select(TreeItem newSelection) {
		if (newSelection != null) {
			this.tree.setSelection(newSelection);
			Event event = new Event();
			event.item = newSelection;
			event.widget = this.tree;
			this.selectionListener.widgetSelected(new SelectionEvent(event));
			return true;
		}
		return false;
	}

	private TreeItem getNext(TreeItem selected, boolean selectChildren) {
		if (selected.getItemCount() > 0 && selectChildren) {
			return selected.getItem(0);
		}
		TreeItem parent = selected.getParentItem();
		TreeItem next = null;
		if (parent != null) {
			if (parent.getItemCount() == 1) {
				this.getNext(parent, false);
			}
			next = this.findNext(selected, parent.getItems());
			if (next == null) {
				next = this.getNext(parent, false);
			}
		} else {
			next = this.findNext(selected, this.tree.getItems());
		}
		return next;
	}

	private TreeItem findNext(TreeItem selected, TreeItem[] siblings) {

		for (int i = 0; i < siblings.length - 1; i++) {
			if (siblings[i] == selected) {
				return siblings[i + 1];
			}
		}
		return null;
	}

	private TreeItem getPrevious(TreeItem selected) {
		TreeItem parent = selected.getParentItem();
		TreeItem previous = null;
		if (parent != null) {
			previous = this.findPrevious(selected, parent.getItems());
			if (previous == null) {
				previous = parent;
			}
		} else {
			previous = this.findPrevious(selected, this.tree.getItems());
		}
		return previous;
	}

	private TreeItem findPrevious(TreeItem selected, TreeItem[] siblings) {
		if (siblings.length == 1) {
			return selected.getParentItem();
		}
		for (int i = 1; i < siblings.length; i++) {
			if (siblings[i] == selected) {
				return siblings[i - 1];
			}
		}
		return null;
	}

	protected Tree getTree() {
		return this.tree;
	}

	public void displayElement(Element inputElement, boolean notifyListener) {
		this.selectionListener.disposeEditor();
		this.tree.removeAll();
		this.elementCount = new HashMap<Element, Integer>();
		this.displayElement(inputElement, null);

		this.expandTreeItem(this.tree.getTopItem());
		this.layout();
		if (notifyListener) {
			this.notifyStringValueListeners();
		}
	}

	public void updateItems() {
		if (this.tree.getItemCount() > 0) {
			this.updateItems(this.tree.getItems());
			this.notifyStringValueListeners();
		}
	}

	private void updateItems(TreeItem[] items) {
		for (TreeItem item : items) {
			this.setItemText(item);
			if (item.getItemCount() > 0) {
				this.updateItems(item.getItems());
			}
		}

	}

	private void expandTreeItem(TreeItem item) {
		item.setExpanded(true);
		for (TreeItem child : item.getItems()) {
			this.expandTreeItem(child);
		}
	}

	private void displayElement(Element element, TreeItem parent) {
		int index = (parent == null) ? 0 : parent.getItemCount();
		this.displayElement(element, parent, index);
	}

	private void displayElement(Element element, TreeItem parent, int index) {
		TreeItem startTag = this.createTreeItem(parent, index++);
		startTag.setData(TREE_ITEM_FOR_START_TAG, element);

		if (element.getType().isComplexType()) {
			ComplexType complex = element.getType().getAsComplexType();
			for (Attribute attribute : complex.getAttributes()) {
				startTag.setData(ATTRIBUTE_VALUE_KEY_PREFIX
						+ attribute.getLocalPart(), attribute.getValue());
			}
			for (Element child : complex.getElements()) {
				this.displayElement(child, startTag, startTag.getItemCount());
			}
		} else {
			TreeItem inner = this.createTreeItem(startTag, startTag
					.getItemCount());
			inner.setData(TREE_ITEM_FOR_ELEMENT_CONTENT, element);
			this.setItemText(inner);
		}
		this.setItemText(startTag);

		TreeItem endTag = this.createTreeItem(parent, index);
		endTag.setData(TREE_ITEM_FOR_END_TAG, element);
		this.setItemText(endTag);
	}

	private TreeItem createTreeItem(TreeItem parent, int index) {
		TreeItem treeItem;
		if (parent == null) {
			treeItem = new TreeItem(this.tree, SWT.NULL, index);
		} else {
			treeItem = new TreeItem(parent, SWT.NULL, index);
		}
		treeItem.setData(TREE_ITEM_IS_DISABLED, "false");
		treeItem.setExpanded(true);
		return treeItem;
	}

	protected String getTagName(Element element) {
		return this.namespaceEditor.getPreffix(element.getTargetNamespace())
				+ ":" + element.getLocalPart();
	}

	private boolean isEditable(TreeItem item) {
		if (item == null || item.getData(TREE_ITEM_FOR_END_TAG) != null
				|| this.isItemDisabled(item)) {
			return false;
		}
		if (item.getData(TREE_ITEM_FOR_ELEMENT_CONTENT) != null) {
			return true;
		}
		if (item.getData(TREE_ITEM_FOR_START_TAG) != null) {
			Element element = (Element) item.getData(TREE_ITEM_FOR_START_TAG);
			if (element.getType().isComplexType()) {
				return !element.getType().getAsComplexType().getAttributes()
						.isEmpty();
			}
		}
		return false;
	}

	protected boolean isCloneable(TreeItem item) {
		if (item == null || item.getData(TREE_ITEM_FOR_END_TAG) != null
				|| item.getData(TREE_ITEM_FOR_ELEMENT_CONTENT) != null) {
			return false;
		}
		// Every element except the first one can be cloned
		return this.tree.getItem(0) != item;
	}

	protected Image getImage(TreeItem item) {
		String imageName = null;
		if (item.getParentItem() == null
				|| this.isItemDisabled(item.getParentItem())) {
			// if parentItem is disabled, all children of parentItem can't be
			// changed. So display no images
			return null;
		}
		if (this.isItemDisabled(item) && MessageEditor.this.isCloneable(item)) {
			// item is already disabled, so no need for the delete Image
			imageName = ToolSupportActivator.IMAGE_ADD;
		} else if (MessageEditor.this.isEditable(item)
				&& MessageEditor.this.isCloneable(item)) {
			imageName = ToolSupportActivator.IMAGE_EDITABLE_CLONEABLE;
		} else if (MessageEditor.this.isEditable(item)) {
			imageName = ToolSupportActivator.IMAGE_EDITABLE;
		} else if (MessageEditor.this.isCloneable(item)) {
			imageName = ToolSupportActivator.IMAGE_CLONEABLE;
		}
		if (imageName != null) {
			return ToolSupportActivator.getImage(imageName);
		}
		return null;
	}

	protected void setItemText(TreeItem item) {
		String text = "";

		if (item.getData(TREE_ITEM_FOR_START_TAG) != null) {
			text = this.setStartTagText(item, (Element) item
					.getData(TREE_ITEM_FOR_START_TAG));
		} else if (item.getData(TREE_ITEM_FOR_END_TAG) != null) {
			Element element = (Element) item.getData(TREE_ITEM_FOR_END_TAG);
			text = "</" + this.getTagName(element) + ">";
		} else if (item.getData(TREE_ITEM_FOR_ELEMENT_CONTENT) != null) {
			text = (String) item.getData(ELEMENT_VALUE_KEY);
		}

		item.setText(0, (text == null) ? "" : text);

	}

	private String setStartTagText(TreeItem item, Element element) {
		String text = "<" + this.getTagName(element);
		if (element.getType().isComplexType()) {
			ComplexType complex = element.getType().getAsComplexType();
			for (Attribute attribute : complex.getAttributes()) {
				text += " " + attribute.getLocalPart() + "=\"";
				String attributeValue = (String) item
						.getData(ATTRIBUTE_VALUE_KEY_PREFIX
								+ attribute.getLocalPart());
				text += (attributeValue == null) ? "" : attributeValue;
				text += "\"";
			}
		}
		text += ">";
		return text;
	}

	public void addStringValueListener(StringValueListener listener) {
		for (StringValueListener l : this.listeners) {
			if (l == listener) {
				return;
			}
		}
		this.listeners.add(listener);
	}

	public void removeStringValueListener(StringValueListener listener) {
		this.listeners.remove(listener);
	}

	protected void notifyStringValueListeners() {
		if (this.isEditable) {
			String newValue = this.treeToXml(this.tree.getItems(), 0);
			for (StringValueListener listener : this.listeners) {
				listener.valueChanged(newValue);
			}
		}
	}

	public String getMessageAsXML() {
		return this.treeToXml(this.tree.getItems(), 0);
	}

	private String treeToXml(TreeItem[] treeItems, int indent) {
		String retval = "";
		String indentString = "";
		for (int i = 0; i < indent; i++) {
			indentString += "\t";
		}
		for (TreeItem item : treeItems) {
			if (!this.isItemDisabled(item)) {
				String text = item.getText();
				if (!text.isEmpty()) {
					retval += indentString + text + "\n";
				}
				if (item.getItemCount() > 0) {
					retval += this.treeToXml(item.getItems(), indent + 1);
				}
			}
		}
		return retval;
	}

	public void setEditable(boolean b) {
		this.isEditable = b;

	}
}
