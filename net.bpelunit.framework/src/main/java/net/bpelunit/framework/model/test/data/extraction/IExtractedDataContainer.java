package net.bpelunit.framework.model.test.data.extraction;

import java.util.Collection;

/**
 * Interface for an object that stores data extracted from the incoming messages.
 *
 * @author University of Cádiz (Antonio García-Domínguez)
 */
public interface IExtractedDataContainer {

	/**
	 * Stores a new piece of extracted data.
	 */
	void putExtractedData(String name, Object value);

	/**
	 * Retrieves a certain piece of extracted data. Returns <code>null</code> if
	 * there is no piece of information stored with that name.
	 * @return 
	 */
	Object getExtractedData(String name);

	/**
	 * Lists all the names of the extracted pieces of data.
	 */
	Collection<String> getAllExtractedDataNames();
}
