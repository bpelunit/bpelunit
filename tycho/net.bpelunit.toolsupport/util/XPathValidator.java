/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.toolsupport.util;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import net.bpelunit.framework.client.eclipse.dialog.DialogFieldValidator;
import net.bpelunit.framework.control.util.BPELUnitUtil;

/**
 * A validator for XPath expressions.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class XPathValidator extends DialogFieldValidator {

	private String fFieldName;

	public XPathValidator(String fieldName) {
		fFieldName= fieldName;
	}

	@Override
	public String validate(String value) {

		if ("".equals(value))
			return "XPath expression for " + fFieldName + " must be specified.";

		XPath xpath= XPathFactory.newInstance().newXPath();
		// xpath.setNamespaceContext(context);

		try {
			xpath.compile(value);
		} catch (Exception e) {
			Throwable root= BPELUnitUtil.findRootThrowable(e);
			return "Problem with " + fFieldName + " XPath: " + root.getMessage();
		}

		return null;
	}

}
