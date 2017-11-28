package net.bpelunit.model.bpel.wsdl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.wsdl.Binding;
import javax.wsdl.Definition;
import javax.wsdl.Message;
import javax.wsdl.PortType;
import javax.wsdl.Service;
import javax.wsdl.WSDLException;
import javax.wsdl.extensions.ElementExtensible;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.soap.SOAPBinding;
import javax.wsdl.factory.WSDLFactory;
import javax.wsdl.xml.WSDLReader;
import javax.xml.namespace.QName;

import com.ibm.wsdl.Constants;

import net.bpelunit.model.bpel.BPELConstants;
import net.bpelunit.util.Predicate;

/**
 * Class which encapsulates all the WSDL information used inside a WS-BPEL
 * document.
 * 
 * @author Antonio García-Domínguez
 */
public class WSDLCatalog {

	private static WSDLReader reader;
	private List<Definition> wsdlDefinitions = new ArrayList<Definition>();

	/**
	 * Returns an unmodifiable list of the WSDL definitions contained in this
	 * catalog.
	 */
	public List<Definition> getDefinitions() {
		return Collections.unmodifiableList(wsdlDefinitions);
	}

	/**
	 * Adds a WSDL file to the type catalog. This will make both its XML Schema
	 * types and WSDL message types available in the type catalog. Dependencies
	 * will be traversed recursively.
	 * 
	 * @param baseDir
	 *            Directory from which relative paths should be resolved. If
	 *            <code>null</code>, relative paths will be resolved from the
	 *            current directory.
	 * @param wsdlFile
	 *            WSDL file to be loaded.
	 */
	public void addWSDL(File baseDir, File wsdlFile) throws IOException, WSDLException {
		String contextURI = null;
		if (baseDir != null) {
			contextURI = baseDir.getCanonicalFile().toURI().toString();
			if (!wsdlFile.isAbsolute()) {
				wsdlFile = new File(baseDir, wsdlFile.getPath());
			}
		}
		addWSDL(getWSDLReader().readWSDL(contextURI, wsdlFile.getPath()));
	}

	/**
	 * Adds a parsed WSDL file to the type catalog. This will make both its XML
	 * Schema types and WSDL message types available in the type catalog.
	 * Dependencies will be traversed recursively.
	 */
	public void addWSDL(Definition wsdlDefinition) {
		wsdlDefinitions.add(wsdlDefinition);
	}

	/**
	 * Finds a binding by QName among all the imported WSDL definitions.
	 */
	public Binding getBinding(QName bindingQName) {
		for (Definition def : wsdlDefinitions) {
			Binding binding = def.getBinding(bindingQName);
			if (binding != null) {
				return binding;
			}
		}
		return null;
	}

	/**
	 * Returns the SOAP style to be used for a binding. If no SOAP style has
	 * been defined, the "document" style will be used, according to the WS-I
	 * 1.1 BP specification.
	 * 
	 * @return Either {@link BPELConstants#SOAP_DOCUMENT_STYLE} or
	 *         {@link BPELConstants#SOAP_RPC_STYLE}.
	 */
	public String getBindingSOAPStyle(final Binding binding) {
		final SOAPBinding soapBinding = findExtensibilityElement(binding,
				SOAPBinding.class, new Predicate<SOAPBinding>() {
					public boolean evaluate(SOAPBinding b) {
						return true;
					}
				});
		if (soapBinding != null) {
			if (soapBinding.getStyle() != null) {
				return soapBinding.getStyle();
			}
		}
		return BPELConstants.SOAP_DOCUMENT_STYLE;
	}

	/**
	 * Finds a message type in the WSDL definitions by QName.
	 * 
	 * @param qName
	 *            QName of the message to be searched.
	 * @return Returns the matching WSDL <code>Message</code>, or
	 *         <code>null</code> if it was not found.
	 */
	public Message getMessageType(QName qName) {
		for (Definition def : wsdlDefinitions) {
			final Message msg = def.getMessage(qName);
			if (msg != null) {
				return msg;
			}
		}
		return null;
	}

	/**
	 * Finds a partner link type by name.
	 * 
	 * @param pltQName
	 *            QName of the partner link to be searched.
	 * @return Returns the matching {@link BPELPartnerLinkType} object if found,
	 *         or <code>null</code> if not found.
	 */
	public BPELPartnerLinkType getPartnerLinkType(final QName pltQName) {
		return findExtensibilityElement(BPELPartnerLinkType.class,
				new Predicate<BPELPartnerLinkType>() {
					public boolean evaluate(BPELPartnerLinkType plt) {
						return pltQName.equals(plt.getName());
					}
				});
	}

	/**
	 * Finds a port type by name. Traverses imports if necessary.
	 * 
	 * @param ptName
	 *            QName of the port type to be searched.
	 * @param Returns
	 *            the first matching {@link BPELPortType} object if found, or
	 *            <code>null</code> if not found.
	 */
	public PortType getPortType(final QName ptName) {
		for (Definition def : wsdlDefinitions) {
			final PortType pt = def.getPortType(ptName);
			if (pt != null) {
				return pt;
			}
		}
		return null;
	}

	/**
	 * Finds bindings using a boolean predicate among all the imported WSDL
	 * definitions.
	 */
	@SuppressWarnings("unchecked")
	public Set<Binding> findAllBindings(Predicate<Binding> predicate) {
		final Set<Binding> bindings = new HashSet<Binding>();
		for (Definition def : wsdlDefinitions) {
			for (Binding binding : ((Map<QName, Binding>) def.getAllBindings())
					.values()) {
				if (predicate.evaluate(binding)) {
					bindings.add(binding);
				}
			}
		}
		return bindings;
	}

	/**
	 * Finds the first extensibility element of a certain class that matches a
	 * predicate and is contained inside a specific extensible element, such as
	 * a {@link Service} or a {@link Binding}.
	 * 
	 * @param source
	 *            Extensible element whose extensibility elements are to be
	 *            searched.
	 * @param extensibilityElementClass
	 *            Java class of the extensibility element to search for.
	 * @param predicate
	 *            Predicate which must be met by the extensibility element of
	 *            the specified class.
	 * @return First extensibility element for which the predicate evaluates to
	 *         <code>true</code>, or <code>null</code> if no such element
	 *         exists.
	 */
	public <T extends ExtensibilityElement> T findExtensibilityElement(
			ElementExtensible source, Class<T> extensibilityElementClass,
			Predicate<T> predicate) {
		for (Object elem : source.getExtensibilityElements()) {
			if (!extensibilityElementClass.isInstance(elem)) {
				continue;
			}

			final T t = extensibilityElementClass.cast(elem);
			if (predicate.evaluate(t)) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Finds the first extensibility element of a certain class that matches a
	 * predicate at the root level of every imported WSDL document.
	 * 
	 * @see #findExtensibilityElement(ElementExtensible, Class, Predicate)
	 */
	public <T extends ExtensibilityElement> T findExtensibilityElement(
			Class<T> extensibilityElementClass, Predicate<T> predicate) {
		for (Definition def : wsdlDefinitions) {
			final T t = findExtensibilityElement(def,
					extensibilityElementClass, predicate);
			if (t != null) {
				return t;
			}
		}
		return null;
	}

	/**
	 * Finds all extensibility elements of a certain class that match a certain
	 * predicate from inside the specified <code>source</code> and adds them to
	 * the specified <code>resultSet</code>.
	 * 
	 * @see #findExtensibilityElement(ElementExtensible, Class, Predicate)
	 */
	public <T extends ExtensibilityElement> void findAllExtensibilityElements(
			ElementExtensible source, Class<T> extensibilityElementClass,
			Predicate<T> predicate, final Set<T> resultSet) {
		for (Object elem : source.getExtensibilityElements()) {
			if (!extensibilityElementClass.isInstance(elem)) {
				continue;
			}

			final T t = extensibilityElementClass.cast(elem);
			if (predicate.evaluate(t)) {
				resultSet.add(t);
			}
		}
	}

	/**
	 * Finds all extensibility elements of a certain class from inside the
	 * specified <code>source</code> and adds them to the specified
	 * <code>resultSet</code>.
	 * 
	 * @see #findExtensibilityElement(ElementExtensible, Class, Predicate)
	 */
	public <T extends ExtensibilityElement> void findAllExtensibilityElements(
			ElementExtensible source, Class<T> extensibilityElementClass,
			final Set<T> resultSet) {
		for (Object elem : source.getExtensibilityElements()) {
			if (!extensibilityElementClass.isInstance(elem)) {
				continue;
			}

			final T t = extensibilityElementClass.cast(elem);
			resultSet.add(t);
		}
	}

	/**
	 * Finds all extensibility elements of a certain class that match a certain
	 * predicate at the root level of every imported WSDL document.
	 * 
	 * @see #findAllExtensibilityElements(ElementExtensible, Class, Predicate,
	 *      Set)
	 */
	public <T extends ExtensibilityElement> Set<T> findAllExtensibilityElements(
			Class<T> extensibilityElementClass, Predicate<T> predicate) {
		final Set<T> results = new HashSet<T>();
		for (Definition def : wsdlDefinitions) {
			findAllExtensibilityElements(def, extensibilityElementClass,
					predicate, results);
		}
		return results;
	}

	/**
	 * Finds all extensibility elements of a certain class of every imported
	 * WSDL document.
	 * 
	 * @see #findAllExtensibilityElements(ElementExtensible, Class, Set)
	 */
	public <T extends ExtensibilityElement> Set<T> findAllExtensibilityElements(
			Class<T> extensibilityElementClass) {
		final Set<T> results = new HashSet<T>();
		for (Definition def : wsdlDefinitions) {
			findAllExtensibilityElements(def, extensibilityElementClass,
					results);
		}
		return results;
	}

	private static synchronized WSDLReader getWSDLReader() throws WSDLException {
		if (reader != null) {
			return reader;
		}

		reader = WSDLFactory.newInstance().newWSDLReader();

		// Do not produce the 'Retrieving ...' messages: these confuse MuBPEL
		reader.setFeature(Constants.FEATURE_VERBOSE, false);

		reader.setExtensionRegistry(WSDLFactory.newInstance()
				.newPopulatedExtensionRegistry());

		// We need to handle both the old BPEL4WS (used in MetaSearch) and new
		// WS-BPEL
		// (used in most others) namespace URIs for the plnk:partnerLinkType
		// elements.
		reader.getExtensionRegistry().registerDeserializer(Definition.class,
				BPELPartnerLinkType.TAG,
				new WSBPELPartnerLinkTypeDeserializer());
		reader.getExtensionRegistry().registerDeserializer(Definition.class,
				BPELPartnerLinkType.TAG_BPEL4WS,
				new BPEL4WSPartnerLinkTypeDeserializer());

		return reader;
	}
}
