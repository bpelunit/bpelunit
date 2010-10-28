package net.bpelunit.toolsupport.util.schema.nodes.impl;

import static org.junit.Assert.assertEquals;

import javax.xml.namespace.QName;

import net.bpelunit.toolsupport.util.schema.nodes.SchemaNode;
import org.junit.Test;

public abstract class SchemaNodeTestAbstract {
	private String targetNs = "http://own.namespace.org/test";
	private String localPart = "PersonenType";
	private QName qName = new QName(this.targetNs, this.localPart);

	protected abstract SchemaNode constructSchemaNode(QName name);

	protected abstract SchemaNode constructSchemaNode(String namespace, String name);

	@Test
	public void testSchemaNodeImplQName() {
		SchemaNode node = this.constructSchemaNode(this.qName);
		assertEquals(this.qName, node.getQName());
		assertEquals(this.targetNs, node.getNamespace());
		assertEquals(this.localPart, node.getLocalPart());
	}

	@Test
	public void testSchemaNodeImplStringString() {
		SchemaNode node = this.constructSchemaNode(this.targetNs, this.localPart);
		assertEquals(this.qName, node.getQName());
		assertEquals(this.targetNs, node.getNamespace());
		assertEquals(this.localPart, node.getLocalPart());
	}

	@Test
	public void testNullTargetNamespace() throws Exception {
		SchemaNode node = this.constructSchemaNode(null, this.localPart);
		assertEquals(null, node.getNamespace());
		assertEquals(this.localPart, node.getLocalPart());
		assertEquals(new QName("", this.localPart), node.getQName());
	}

}
