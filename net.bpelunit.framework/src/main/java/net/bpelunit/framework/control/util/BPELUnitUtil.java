/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.util;

import java.io.ByteArrayOutputStream;
import java.util.HashSet;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Detail;
import javax.xml.soap.DetailEntry;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlOptions;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utilities for use in the BPELUnit framework.
 * 
 * @version $Id$
 * @author Philip Mayer, Antonio Garcia-Dominguez (changed serialization method)
 * 
 */
public class BPELUnitUtil {

	public static final String DUMMY_ELEMENT_NAME = "literalData";

	/**
	 * The document builder to use.
	 */
	private static DocumentBuilder fgDocumentBuilder;

	/**
	 * Initializes the parser component. This is necessary to be able to deal with severe CLASSPATH
	 * problems (i.e. no parser in CLASSPATH) in a predictable manner.
	 * 
	 * @throws ParserConfigurationException
	 */
	public static void initializeParsing() throws ParserConfigurationException {
		DocumentBuilderFactory factory= DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		BPELUnitUtil.fgDocumentBuilder= factory.newDocumentBuilder();
	}

	/**
	 * Creates a new document with a dummy root node, intended to store literal data for a receive
	 * or send.
	 * 
	 * CAUTION: This method depends on initialization which is done by calling
	 * {@link initializeParsing}. Not initializing this class will cause uncaught NPEs.
	 * 
	 * @return
	 */
	public static Element generateDummyElementNode() {
		Document document= fgDocumentBuilder.newDocument();
		Element root= document.createElement(DUMMY_ELEMENT_NAME);
		document.appendChild(root);
		return root;
	}

	/**
	 * Creates a new, generic SOAP fault to be used when something goes wrong in a partner track and
	 * other tracks must be notified.
	 * 
	 * @return
	 */
	public static String generateGenericSOAPFault() {

		try {
			MessageFactory mFactory= MessageFactory.newInstance();
			SOAPMessage message= mFactory.createMessage();
			SOAPBody body= message.getSOAPBody();
			SOAPFault fault= body.addFault(
			        BPELUnitConstants.SOAP_FAULT_CODE_CLIENT,
			        BPELUnitConstants.SOAP_FAULT_DESCRIPTION);

			Detail detail= fault.addDetail();
			DetailEntry entry= detail.addDetailEntry(new QName("http://www.bpelunit.org/framework/error", "BPELUnitFault"));
			entry.addTextNode("The BPELUnit test framework has detected a test failure or error. This test case is aborted.");

			ByteArrayOutputStream b= new ByteArrayOutputStream();
			message.writeTo(b);
			return b.toString();

		} catch (Exception e) {
			return "(internal fault)";
		}
	}

	/**
	 * Removes line breaks from a string
	 * 
	 * @param string
	 * @return
	 */
	public static String removeSpaceLineBreaks(String string) {
		if (string == null)
			return "";
		String v= string.trim();
		v= StringUtils.remove(v, '\n');
		v= StringUtils.remove(v, '\r');
		v= StringUtils.remove(v, '\t');
		return v;
	}

	/**
	 * XPath exception causes tend to be deeply nested. The first meaningful exception (with an
	 * error message) is some way down. Find it.
	 * 
	 * @param e
	 * @return
	 */
	public static Throwable findRootThrowable(Exception e) {
		Throwable f= e;
		while (f.getMessage() == null && f.getCause() != null) {
			f= f.getCause();
		}
		return f;
	}

	/**
	 * Outputs a DOM Document node to a string with identation.
	 * 
	 * @param element
	 * @return
	 */
	public static String toFormattedString(Document element) {
		try {
			return serializeXML(element);
		} catch (TransformerException e) {
			return "(no data)";
		}
	}

	private static String serializeXML(Node node) throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer t = tf.newTransformer();
		t.setOutputProperty(OutputKeys.INDENT, "yes");
		t.setOutputProperty(OutputKeys.METHOD, "xml");
		ByteArrayOutputStream bOS = new ByteArrayOutputStream();
		t.transform(new DOMSource(node), new StreamResult(bOS));
		return bOS.toString();
	}

	/**
	 * Returns default output options for outputting XmlObjects from XBeans.
	 * 
	 * @return
	 */
	public static XmlOptions getDefaultXMLOptions() {
		XmlOptions opts= new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(4);
		return opts;
	}

	private static class SOAPClassLoader extends ClassLoader {
		private HashSet<String> exceptionalClasses= new HashSet<String>();

		public SOAPClassLoader(ClassLoader parentClassLoader) {
			super(parentClassLoader);
			exceptionalClasses.add("com.sun.xml.messaging.saaj.soap.SAAJMetaFactoryImpl");
		}

		@Override
		public Class<?> loadClass(String name) throws ClassNotFoundException {
			if (exceptionalClasses.contains(name)) {
				return Class.forName(name);
			} else {
				return super.loadClass(name);
			}
		}
	}

	/**
	 * <p>
	 * Returns a new message factory instance. The problem with a new factory instance is the code
	 * inside {@link javax.xml.soap.MessageFactory#newInstance()}, or rather the method called by
	 * newInstance(), namingly {@link javax.xml.soap.FactoryFinder#newInstance(String)}. The code
	 * fragment
	 * </p>
	 * 
	 * <pre>
	 * private static Object newInstance(String factoryClassName) throws SOAPException {
	 * 	ClassLoader classloader= null;
	 * 	try {
	 * 		classloader= Thread.currentThread().getContextClassLoader();
	 * 	} catch (Exception exception) {
	 * 		throw new SOAPException(exception.toString(), exception);
	 * 	}
	 * 
	 * 	try {
	 * 		Class factory= null;
	 * 		if (classloader == null) {
	 * 			factory= Class.forName(factoryClassName);
	 * 		} else {
	 * 			try {
	 * 				factory= classloader.loadClass(factoryClassName);
	 * 			} catch (ClassNotFoundException cnfe) {
	 * 			}
	 * 		}
	 * 		if (factory == null) {
	 * 			classloader= FactoryFinder.class.getClassLoader();
	 * 			factory= classloader.loadClass(factoryClassName);
	 * 		}
	 * 		return factory.newInstance();
	 * 	} catch (ClassNotFoundException classnotfoundexception) {
	 * 		throw new SOAPException(&quot;Provider &quot; + factoryClassName + &quot; not found&quot;, classnotfoundexception);
	 * 	} catch (Exception exception) {
	 * 		throw new SOAPException(&quot;Provider &quot; + factoryClassName + &quot; could not be instantiated: &quot; + exception, exception);
	 * 	}
	 * }
	 * </pre>
	 * 
	 * <p>
	 * always uses the context class loader of the current thread if one is available. Sadly, in the
	 * case of running BPELUnit from inside Eclipse or using ant, the context class loader does not
	 * know about the classes required for BPELUnit, including the classes from the SAAJ jar.
	 * Therefore, the context class loader must be replaced by a proxy classloader which loads the
	 * needed classes using the actual current classloader instead of the context classloader.
	 * </p>
	 * 
	 * @return factory instance.
	 * @throws SOAPException
	 */
	public static MessageFactory getMessageFactoryInstance() throws SOAPException {

		ClassLoader classLoader= Thread.currentThread().getContextClassLoader();
		classLoader= new SOAPClassLoader(classLoader);
		Thread.currentThread().setContextClassLoader(classLoader);

		return MessageFactory.newInstance();
	}

}
