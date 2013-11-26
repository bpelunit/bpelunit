package net.bpelunit.toolsupport.editors.wizards.fields;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.toolsupport.ToolSupportActivator;
import net.bpelunit.toolsupport.editors.wizards.components.StringValueListener;
import net.bpelunit.toolsupport.util.NamespaceEditor;
import net.bpelunit.toolsupport.util.schema.nodes.Attribute;
import net.bpelunit.toolsupport.util.schema.nodes.ComplexType;
import net.bpelunit.toolsupport.util.schema.nodes.Element;
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
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class MessageEditor extends Composite {

	/**
	 * Displays editor for selected TreeItem if this is EDITABLE.
	 * 
	 * @author cvolhard
	 * 
	 */
	private final class XMLTreeSelectionListener extends SelectionAdapter {
		private final TreeEditor editor;

		XMLTreeSelectionListener(TreeEditor editor) {
			this.editor = editor;
		}

		protected void disposeEditor() {
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

			if (item.getParentItem() != null
					&& MessageEditor.this.isItemDisabled(item.getParentItem())) {
				return;
			}

			Element element = (Element) item.getData(TREE_ITEM_FOR_ELEMENT_CONTENT);
			if (element != null && !MessageEditor.this.isItemDisabled(item)) {
				// item displays the inner text of a tag and is not disabled.
				this.createInnerTextEditor(item);
			} else if ((element = (Element) item.getData(TREE_ITEM_FOR_START_TAG)) != null) {

				// check if the click hit a Button, if so perform the according
				// action and do not display the editor
				int imagePosition = (Integer) item.getData(TREE_ITEM_IMAGE_POSITION);
				if (imagePosition > 0) {
					Point mousePosition = MessageEditor.this.mouseDownPosition;

					// reset necessary, because this method will be called again
					MessageEditor.this.mouseDownPosition = new Point(0, 0);

					if (MessageEditor.this.isInAddButton(imagePosition, mousePosition)) {
						MessageEditor.this.copyItem(item);
						return;
					}

					if (MessageEditor.this.isInDeleteButton(imagePosition, mousePosition)) {
						MessageEditor.this.removeItem(item);
						return;

					}
				}
				this.createStartTagEditor(item, element);
			}
		}

		/**
		 * Displays editor for the a start tag with buttons for adding a new
		 * element of this type, or removing this element. If the element
		 * contains attributes a input field for every attribute is displayed
		 * 
		 * @param item
		 * @param element
		 */
		private void createStartTagEditor(final TreeItem item, final Element element) {
			ComplexType complex = element.getType().getAsComplexType();
			if (complex == null || complex.getAttributes().isEmpty()) {
				// nothing to edit, so do not display the editor
				return;
			}

			boolean itemDisabled = MessageEditor.this.isItemDisabled(item);
			Color color = MessageEditor.this.getColor(itemDisabled);

			final Composite composite = new Composite(MessageEditor.this.getTree(), SWT.BORDER_DOT);
			List<Control> tabList = new ArrayList<Control>();
			composite.setLayout(this.createRowLayout());
			this.addLabel(composite, color, "<" + MessageEditor.this.getTagName(element));

			// display input fields and labels for every attribute
			for (int i = 0; i < complex.getAttributes().size(); i++) {
				Attribute attribute = complex.getAttributes().get(i);
				Text text = this.createAttributeInput(item, composite, attribute, color,
						!itemDisabled);
				if (text != null) {
					tabList.add(text);
				}
			}

			this.addLabel(composite, color, ">");
			if (item.getData(TREE_ITEM_FOR_START_TAG) != null && item.getParentItem() != null) {
				this.addButtons(item, composite);
			}
			if (!tabList.isEmpty()) {
				Control[] tmpTabList = new Control[tabList.size()];
				tabList.toArray(tmpTabList);
				composite.setTabList(tmpTabList);
				tabList.get(0).setFocus();
			}
			this.editor.setEditor(composite, item);
		}

		private Label addLabel(final Composite composite, String text) {
			Label label = new Label(composite, SWT.NULL);
			label.setText(text);
			return label;
		}

		private Label addLabel(final Composite composite, Color color, String text) {
			Label label = this.addLabel(composite, text);
			label.setForeground(color);
			return label;
		}

		private void addButtons(final TreeItem item, final Composite composite) {
			this.addLabel(composite, "   ");
			Label label = new Label(composite, SWT.NULL);
			label.setImage(ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_ADD));
			label.setCursor(new Cursor(MessageEditor.this.getDisplay(), SWT.CURSOR_HAND));
			label.setToolTipText("Add another tag of this kind.");
			label.addListener(SWT.MouseUp, new Listener() {
				@Override
				public void handleEvent(Event arg0) {
					MessageEditor.this.copyItem(item);
				}
			});

			label = this.addLabel(composite, " ");
			Font oldFont = label.getFont();
			FontData fontData = oldFont.getFontData()[0];
			fontData.height *= .75;
			label.setFont(new Font(oldFont.getDevice(), fontData));

			label = new Label(composite, SWT.NULL);
			label.setImage(ToolSupportActivator.getImage(ToolSupportActivator.IMAGE_DELETE));
			label.setCursor(new Cursor(MessageEditor.this.getDisplay(), SWT.CURSOR_HAND));
			label.setToolTipText("Remove this tag.");
			label.addListener(SWT.MouseUp, new Listener() {
				@Override
				public void handleEvent(Event arg0) {
					MessageEditor.this.removeItem(item);
					XMLTreeSelectionListener.this.disposeEditor();
				}
			});
		}

		/**
		 * Adds label with the localPart of the <code>attribute</code> and a
		 * EDITABLE input field with the value of the attribute of the selected
		 * item to <code>composite</code>.
		 * 
		 * @param item
		 *            the selected TreeItem
		 * @param composite
		 * @param attribute
		 * @param color
		 *            color of the text
		 * @param isAttributeEditable
		 * @return
		 */
		private Text createAttributeInput(final TreeItem item, final Composite composite,
				Attribute attribute, Color color, boolean isAttributeEditable) {
			this.addLabel(composite, color, " " + attribute.getLocalPart() + "=\"");
			String attributeValue = (String) item.getData(ATTRIBUTE_VALUE_KEY_PREFIX
					+ attribute.getLocalPart());
			attributeValue = (attributeValue == null) ? "" : attributeValue;
			if (isAttributeEditable) {
				final Text text = new Text(composite, SWT.SINGLE);
				text.setText(attributeValue);
				text.addModifyListener(new ChangeListener(attribute, item));
				text.addFocusListener(new TextFocusListener());
				text.addKeyListener(new KeyAdapter() {

					@Override
					public void keyPressed(KeyEvent event) {
						switch (event.keyCode) {
						case SWT.ARROW_DOWN:
							// select the next item
							if (MessageEditor.this.selectNext(item)) {
								composite.dispose();
							}
							break;
						case SWT.ARROW_UP:
							// select the previous item
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
									int nextIndex = (j + 1 >= tabList.length) ? 0 : j + 1;
									tabList[nextIndex].setFocus();
								}
							}
							event.doit = false;
							break;
						}
					}
				});
				this.addLabel(composite, color, "\"");
				return text;
			}
			this.addLabel(composite, color, attributeValue + "\"");
			return null;
		}

		private RowLayout createRowLayout() {
			RowLayout layout = new RowLayout(SWT.HORIZONTAL);
			layout.fill = false;
			layout.spacing = 0;
			layout.marginLeft = 2;
			layout.marginTop = 2;
			layout.wrap = false;
			return layout;
		}

		/**
		 * Displays a text field for the inner text of the <code>item</code>.
		 * 
		 * @param item
		 */
		private void createInnerTextEditor(final TreeItem item) {
			final Composite composite = new Composite(MessageEditor.this.getTree(), SWT.NULL);
			composite.setLayout(this.createRowLayout());

			final Text text = new Text(composite, SWT.SINGLE);
			text.setText(item.getText());

			text.setVisible(true);

			text.addModifyListener(new ChangeListener(item));
			// text.addFocusListener(new TextFocusListener());
			text.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent event) {
					composite.dispose();
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
			this.editor.setEditor(composite, item);
			text.pack();
			composite.pack();
			text.setSelection(text.getText().length());
			text.setFocus();
		}
	}

	private class ChangeListener implements ModifyListener {

		private Attribute attribute = null;
		private TreeItem item;

		protected ChangeListener(Attribute attribute, TreeItem item) {
			this.attribute = attribute;
			this.item = item;
		}

		protected ChangeListener(TreeItem item) {
			this.item = item;
		}

		@Override
		public void modifyText(ModifyEvent e) {
			Text text = (Text) e.getSource();
			String key = "";
			if (this.attribute == null) {
				key = MessageEditor.ELEMENT_VALUE_KEY;
			} else {
				key = MessageEditor.ATTRIBUTE_VALUE_KEY_PREFIX + this.attribute.getLocalPart();
			}
			this.item.setData(key, text.getText());

			text.pack();
			text.getParent().pack();

			// Workaround to prevent that the first letter in the text field is
			// hidden
			int position = text.getSelection().x;
			text.setSelection(0);
			text.setSelection(Math.min(position, text.getText().length()));

			MessageEditor.this.setItemText(this.item);
			MessageEditor.this.notifyStringValueListeners();
		}
	}

	protected class TextFocusListener implements FocusListener {

		@Override
		public void focusGained(FocusEvent e) {
			Text text = (Text) e.widget;
			text.setSelection(text.getText().length());
		}

		@Override
		public void focusLost(FocusEvent e) {
			// really needed?
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
	private static final String TREE_ITEM_IMAGE_POSITION = "imagePosition";

	private Tree tree;
	private NamespaceEditor namespaceEditor;
	protected boolean isEditable = true;
	private XMLTreeSelectionListener selectionListener;
	private List<StringValueListener> listeners = new ArrayList<StringValueListener>();
	private Color disabledColor;
	private Color normalColor;
	private Element displayedElement;
	protected Point mouseDownPosition;
	protected TreeItemToolTip treeItemToolTip;
	private String xml;

	public MessageEditor(Composite parent, int style, XMLTestSuite suite) {
		super(parent, style);
		this.namespaceEditor = new NamespaceEditor(suite);
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		this.setEnabled(true);
		this.setLayout(new FillLayout());
		this.disabledColor = new Color(this.getForeground().getDevice(), 180, 180, 180);
		this.normalColor = new Color(this.getForeground().getDevice(), 0, 0, 0);

		this.tree = new Tree(this, SWT.SINGLE);
		this.tree.setBackground(this.getBackground());

		final TreeEditor editor = new TreeEditor(this.tree);
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;

		this.selectionListener = new XMLTreeSelectionListener(editor);
		this.tree.addSelectionListener(this.selectionListener);

		this.tree.addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				MessageEditor.this.mouseDownPosition = new Point(arg0.x, arg0.y);
			}
		});

		this.tree.addListener(SWT.MouseMove, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				Point mousePosition = new Point(arg0.x, arg0.y);
				TreeItem item = MessageEditor.this.getTree().getItem(mousePosition);
				if (item != null) {
					Object data = item.getData(TREE_ITEM_IMAGE_POSITION);
					Integer imagePosition = (data == null) ? 0 : (Integer) data;
					if (MessageEditor.this.isInAddButton(imagePosition, mousePosition)) {
						if (MessageEditor.this.isCloneable(item)) {
							MessageEditor.this.setCursor(SWT.CURSOR_HAND);
							MessageEditor.this.treeItemToolTip
									.setText("Add another kind of this tag.");
						} else {
							MessageEditor.this.setCursor(SWT.CURSOR_ARROW);
							MessageEditor.this.treeItemToolTip.setText("");
						}
					} else if (MessageEditor.this.isInDeleteButton(imagePosition, mousePosition)) {
						MessageEditor.this.setCursor(SWT.CURSOR_HAND);
						MessageEditor.this.treeItemToolTip.setText("Remove this tag.");
					} else {
						MessageEditor.this.setCursor(SWT.CURSOR_ARROW);
						MessageEditor.this.treeItemToolTip.setText("");
					}
				} else {
					MessageEditor.this.setCursor(SWT.CURSOR_ARROW);
					MessageEditor.this.treeItemToolTip.setText("");
				}
			}

		});

		// listener for displaying images
		this.tree.addListener(SWT.MeasureItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.index == 0) {
					Image trailingImage = MessageEditor.this.getImage((TreeItem) event.item);
					if (trailingImage != null) {
						event.width += trailingImage.getBounds().width + IMAGE_MARGIN;
					}
					event.height = 18;
				}
			}
		});

		// listener for displaying images
		this.tree.addListener(SWT.PaintItem, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.index == 0) {
					TreeItem item = (TreeItem) event.item;
					Image trailingImage = MessageEditor.this.getImage(item);
					if (trailingImage != null) {
						int x = event.x + event.width + IMAGE_MARGIN;
						int itemHeight = MessageEditor.this.getTree().getItemHeight();
						int imageHeight = trailingImage.getBounds().height;
						int y = event.y + (itemHeight - imageHeight) / 2;
						event.gc.drawImage(trailingImage, x, y);
						item.setData(TREE_ITEM_IMAGE_POSITION, x);
					} else {
						item.setData(TREE_ITEM_IMAGE_POSITION, -1);
					}
				}
			}
		});

		this.treeItemToolTip = new TreeItemToolTip(this.tree);

		this.tree.addListener(SWT.Dispose, this.treeItemToolTip);
		this.tree.addListener(SWT.KeyDown, this.treeItemToolTip);
		this.tree.addListener(SWT.MouseMove, this.treeItemToolTip);
		this.tree.addListener(SWT.MouseHover, this.treeItemToolTip);
	}

	/**
	 * Removes the Element represented by <code>item</code> from the tree.
	 * <code>item</code> has to represent the start tag of element. If this
	 * Element is the last one of this type in the parent Element, it is
	 * disabled and not removed.
	 * 
	 * @param item
	 */
	protected void removeItem(TreeItem item) {
		Element element = (Element) item.getData(TREE_ITEM_FOR_START_TAG);
		TreeItem parent = item.getParentItem();
		TreeItem closingItem = parent.getItem(this.getItemIndex(item) + 1);
		if (this.isLastElementOfType(item, element)) {
			// parent element contains no more of this type, so disable it so
			// that it is possible to add a element of this type again later
			item.setData(TREE_ITEM_IS_DISABLED, "true");
			closingItem.setData(TREE_ITEM_IS_DISABLED, "true");
			item.setData(TREE_ITEM_CHILDREN, item.getItems());

			this.setDisabled(item, true);
			closingItem.setForeground(this.disabledColor);

			// set cursor back to default, otherwise the hand-cursor would be
			// shown although there may be nothing to click
			this.setCursor(SWT.CURSOR_ARROW);
		} else {
			// parent element contains more of this type, so remove this
			item.dispose();
			closingItem.dispose();
		}
		this.notifyStringValueListeners();
	}

	private boolean isLastElementOfType(TreeItem item, Element element) {
		TreeItem parent = item.getParentItem();
		if (parent != null) {
			for (TreeItem child : parent.getItems()) {
				if (item != child) {
					if (element == child.getData(TREE_ITEM_FOR_START_TAG)) {
						return false;
					}
				}
			}
		}
		return true;
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

	protected void copyItem(TreeItem item) {
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
			// this.addElement(element);
			this.expandTreeItem(parent);
		}
		this.selectionListener.disposeEditor();
		this.notifyStringValueListeners();
	}

	protected boolean isItemDisabled(TreeItem item) {
		if (item == null) {
			return false;
		}
		Object data = item.getData(TREE_ITEM_IS_DISABLED);
		return Boolean.parseBoolean((String) data);
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
		this.displayedElement = inputElement;

		if (this.isEditable) {
			this.selectionListener.disposeEditor();
			this.tree.removeAll();
			if (this.displayedElement == null) {
				this.displayError("No element definition available for this message.");
				if (this.isEditable) {
					for (StringValueListener listener : this.listeners) {
						listener.valueChanged("");
					}
				}
			} else {
				this.displayElement(inputElement, null);
				this.expandTreeItem(this.tree.getTopItem());
				this.layout();
				if (notifyListener) {
					this.notifyStringValueListeners();
				}
			}

		} else {
			this.setXML();
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
		if (item != null && !this.isItemDisabled(item)) {
			item.setExpanded(true);
			for (TreeItem child : item.getItems()) {
				this.expandTreeItem(child);
			}
		}
	}

	private void displayElement(Element element, TreeItem parent) {
		int index = (parent == null) ? 0 : parent.getItemCount();
		this.displayElement(element, parent, index);
	}

	private void displayElement(Element element, TreeItem parent, org.w3c.dom.Element domElement) {
		int index = (parent == null) ? 0 : parent.getItemCount();
		this.displayElement(element, parent, index, domElement);

	}

	private TreeItem displayElement(Element element, TreeItem parent, int index) {
		TreeItem startTag = this.createTreeItem(parent, index);
		startTag.setData(TREE_ITEM_FOR_START_TAG, element);

		if (element.getType().isComplexType()) {
			ComplexType complex = element.getType().getAsComplexType();
			for (Attribute attribute : complex.getAttributes()) {
				startTag.setData(ATTRIBUTE_VALUE_KEY_PREFIX + attribute.getLocalPart(), attribute
						.getDefaultOrFixedValue());
			}
			for (Element child : complex.getElements()) {
				this.displayElement(child, startTag, startTag.getItemCount());
			}
		} else {
			TreeItem inner = this.createTreeItem(startTag, startTag.getItemCount());
			inner.setData(TREE_ITEM_FOR_ELEMENT_CONTENT, element);
			this.setItemText(inner);
		}
		this.setItemText(startTag);

		TreeItem endTag = this.createTreeItem(parent, index + 1);
		endTag.setData(TREE_ITEM_FOR_END_TAG, element);
		this.setItemText(endTag);
		return startTag;
	}

	private void displayElement(Element element, TreeItem parent, int index,
			org.w3c.dom.Element domElement) {
		TreeItem startTag = this.createTreeItem(parent, index);
		startTag.setData(TREE_ITEM_FOR_START_TAG, element);

		if (element.getType().isComplexType()) {
			ComplexType complex = element.getType().getAsComplexType();
			for (Attribute attribute : complex.getAttributes()) {
				String name = attribute.getLocalPart();
				startTag.setData(ATTRIBUTE_VALUE_KEY_PREFIX + name, domElement.getAttribute(name));
			}
			List<org.w3c.dom.Element> children = this.getDomChildrenElements(domElement
					.getChildNodes());
			int i = 0;
			org.w3c.dom.Element domChild = null;
			if (!children.isEmpty()) {
				domChild = children.get(0);
			}
			for (Element schemaChild : complex.getElements()) {
				boolean added = false;

				while (this.areNamesEqual(domChild, schemaChild)) {
					this.displayElement(schemaChild, startTag, startTag.getItemCount(), domChild);
					added = true;
					i++;
					if (i >= children.size()) {
						break;
					}
					domChild = children.get(i);
				}

				if (!added) {
					TreeItem item = this.displayElement(schemaChild, startTag, startTag
							.getItemCount());
					this.removeItem(item);
				}
			}
		} else {
			TreeItem inner = this.createTreeItem(startTag, startTag.getItemCount());
			inner.setData(TREE_ITEM_FOR_ELEMENT_CONTENT, element);
			String value = "";
			if (domElement.getFirstChild() != null) {
				value = domElement.getFirstChild().getNodeValue().trim();
			}
			inner.setData(ELEMENT_VALUE_KEY, value);
			this.setItemText(inner);
		}
		this.setItemText(startTag);

		TreeItem endTag = this.createTreeItem(parent, index + 1);
		endTag.setData(TREE_ITEM_FOR_END_TAG, element);
		this.setItemText(endTag);

	}

	private List<org.w3c.dom.Element> getDomChildrenElements(NodeList childNodes) {
		List<org.w3c.dom.Element> elements = new ArrayList<org.w3c.dom.Element>();
		for (int i = 0; i < childNodes.getLength(); i++) {
			Node node = childNodes.item(i);
			if (node instanceof org.w3c.dom.Element) {
				elements.add((org.w3c.dom.Element) node);
			}
		}
		return elements;
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
		String prefix = this.namespaceEditor.getPrefix(element.getNamespace());
		if (prefix.isEmpty()) {
			return element.getLocalPart();
		}
		return prefix + ":" + element.getLocalPart();
	}

	private boolean isEditable(TreeItem item) {
		if (item == null || item.getData(TREE_ITEM_FOR_END_TAG) != null
				|| this.isItemDisabled(item)) {
			return false;
		}
		if (this.tree.getSelection().length > 0 && this.tree.getSelection()[0] == item) {
			return false;
		}

		if (item.getData(TREE_ITEM_FOR_ELEMENT_CONTENT) != null) {
			return true;
		}
		if (item.getData(TREE_ITEM_FOR_START_TAG) != null) {
			Element element = (Element) item.getData(TREE_ITEM_FOR_START_TAG);
			if (element != null && element.getType() != null && element.getType().isComplexType()) {
				return !element.getType().getAsComplexType().getAttributes().isEmpty();
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

		if (this.isItemDisabled(item.getParentItem())) {
			// if parentItem is disabled, all children of parentItem can't be
			// changed. So display no images
			return null;
		}

		boolean itemDisabled = this.isItemDisabled(item);
		boolean cloneable = MessageEditor.this.isCloneable(item);
		boolean editable = MessageEditor.this.isEditable(item);

		if (itemDisabled && cloneable) {
			// item is already disabled, so no need for the delete Image
			imageName = ToolSupportActivator.IMAGE_ADD;
		} else if (editable && cloneable) {
			imageName = ToolSupportActivator.IMAGE_EDITABLE_CLONEABLE;
		} else if (editable) {
			imageName = ToolSupportActivator.IMAGE_EDITABLE;
		} else if (cloneable) {
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
			text = this.setStartTagText(item, (Element) item.getData(TREE_ITEM_FOR_START_TAG));
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
				String attributeValue = (String) item.getData(ATTRIBUTE_VALUE_KEY_PREFIX
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
		if (this.isEditable && !this.isXMLValid()) {
			this.displayElement(this.displayedElement, true);
		}
	}

	public boolean isXMLValid() {
		// if itemCount is < 2, an error is currently being shown
		return this.tree.getItemCount() >= 2;
	}

	public void setXML(String xml) {
		this.xml = xml;
		this.setXML();
	}

	private void setXML() {
		if (this.displayedElement == null) {
			this.displayError("No element definition available in the WSDL.");
			return;
		}
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(this.xml)));
			org.w3c.dom.Element root = document.getDocumentElement();
			if (this.areNamesEqual(root, this.displayedElement)) {
				if (this.compareElements(root, this.displayedElement)) {
					this.selectionListener.disposeEditor();
					this.tree.removeAll();
					this.displayElement(this.displayedElement, null, root);

					this.expandTreeItem(this.tree.getTopItem());
					this.layout();
				}
			} else {
				String errorMsg = "Root element from literal XML does not fit the selected Operation.";
				this.displayError(errorMsg);
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			if ("Premature end of file.".equals(e.getMessage())) {
				this.displayError("Please choose operation.");
			} else {
				this.displayError(e.getMessage());
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void displayError(String errorMsg) {
		this.selectionListener.disposeEditor();
		this.tree.removeAll();
		TreeItem item = this.createTreeItem(null, 0);
		item.setText(errorMsg);
	}

	private boolean compareElements(org.w3c.dom.Element domElement, Element schemaElement) {
		ComplexType type = schemaElement.getType().getAsComplexType();
		if (type != null) {

			if (type.getAttributes().size() != domElement.getAttributes().getLength()) {
				this.displayError("Attributes of " + domElement.getNodeName()
						+ " do not match the Schema.");
				return false;
			}

			for (Attribute attribute : type.getAttributes()) {
				if (domElement.getAttributeNode(attribute.getLocalPart()) == null) {
					this.displayError("Attributes of " + domElement.getNodeName()
							+ " do not match the Schema.");
					return false;
				}
			}

			NodeList domChildren = domElement.getChildNodes();
			List<Element> schemaChildren = type.getElements();
			if (schemaChildren.size() == 0 && domChildren.getLength() > 0) {
				this.displayError("Element " + domElement.getNodeName()
						+ " can not have any children, but has.");
				return false;
			}
			int j = 0;
			for (int i = 0; i < domChildren.getLength(); i++) {
				Node node = domChildren.item(i);
				if (node instanceof org.w3c.dom.Element) {
					org.w3c.dom.Element domChild = (org.w3c.dom.Element) node;
					while (true) {
						Element schemaChild = schemaChildren.get(j);
						if (this.areNamesEqual(domChild, schemaChild)) {
							if (this.compareElements(domChild, schemaChild)) {
								break;
							}
							// no call for displayError, happend already!
							return false;
						}
						j++;
						if (j >= schemaChildren.size()) {
							this.displayError("unexpected " + domChild.getNodeName() + ".");
							return false;
						}
					}
				} else {
					if (node.getNodeValue() != null && !node.getNodeValue().trim().equals("")) {
						this.displayError("Mixed data is not allowed: "
								+ node.getNodeValue().trim());
						return false;
					}
				}
			}
		}

		return true;
	}

	private boolean areNamesEqual(org.w3c.dom.Element domElement, Element schemaElement) {
		if (domElement == null || schemaElement == null) {
			return false;
		}
		String namespace = schemaElement.getNamespace();
		String prefix = this.namespaceEditor.getPrefix(namespace);
		String tagName = schemaElement.getLocalPart();
		if (!prefix.isEmpty()) {
			tagName = prefix + ":" + tagName;
		}
		return tagName.equals(domElement.getNodeName());
	}

	protected boolean isInAddButton(int imagePosition, Point mousePosition) {
		if (this.isInButton(mousePosition)) {
			return imagePosition < mousePosition.x && mousePosition.x < imagePosition + 13;
		}
		return false;
	}

	protected boolean isInDeleteButton(int imagePosition, Point mousePosition) {
		if (this.isInButton(mousePosition)) {
			return imagePosition + 16 <= mousePosition.x
					&& mousePosition.x < imagePosition + 13 + 16;
		}
		return false;
	}

	private boolean isInButton(Point mousePosition) {
		int itemHeight = this.tree.getItemHeight();
		return 2 < mousePosition.y % itemHeight && mousePosition.y % itemHeight < 14;
	}

	protected void setCursor(int cursor) {
		this.setCursor(new Cursor(MessageEditor.this.getDisplay(), cursor));
	}
}
