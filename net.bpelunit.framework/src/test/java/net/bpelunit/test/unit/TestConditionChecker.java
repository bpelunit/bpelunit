/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.unit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import net.bpelunit.framework.control.soap.NamespaceContextImpl;
import net.bpelunit.framework.model.test.data.ReceiveCondition;
import net.bpelunit.test.util.TestUtil;
import org.junit.Test;
import org.w3c.dom.Element;

/**
 * 
 * Tests the XPath condition checker.
 * 
 * @version $Id: TestConditionChecker.java,v 1.5 2006/07/11 14:27:43 phil Exp $
 * @author Philip Mayer
 * 
 */
public class TestConditionChecker extends SimpleTest {

	private static final String PATH_TO_FILES= "/condition/";

	private ReceiveCondition eval(String eval, String result) throws Exception {

		Element parent= TestUtil.readLiteralData(PATH_TO_FILES + "simple.xmlfrag");

		ReceiveCondition c= new ReceiveCondition(null, eval, null, result);

		NamespaceContextImpl ns= new NamespaceContextImpl();
		ns.setNamespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		ns.setNamespace("b", "http://xmlns.oracle.com/AirlineReservationSync");

		c.evaluate(null, parent, ns, null);
		return c;
	}

	@Test
	public void testBooleanOK() throws Exception {
		ReceiveCondition c= eval("b:AirlineReservationSyncProcessResponse/b:result/text()", "'false'");
		assertFalse(c.isError() || c.isFailure());
	}

	@Test
	public void testTextNode() throws Exception {
		ReceiveCondition c= eval("b:AirlineReservationSyncProcessResponse/b:bookingNumber/text()", "'589588'");
		assertFalse(c.isError() || c.isFailure());
	}

	@Test
	public void testAttribute() throws Exception {
		ReceiveCondition c= eval("b:AirlineReservationSyncProcessResponse/b:result/@some", "'Hallo'");
		assertFalse(c.isError() || c.isFailure());
	}

	@Test
	public void testWrongXPath() throws Exception {
		ReceiveCondition c= eval("b:AirlineReservationSyncProcessResponse/b:result@some", "'Hallo'");
		assertTrue(c.isError());
	}

	@Test
	public void testFail() throws Exception {
		ReceiveCondition c= eval("b:AirlineReservationSyncProcessResponse/b:result/@some", "'Lulu'");
		assertTrue(c.isFailure());
	}
}
