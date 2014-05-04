package net.bpelunit.framework.model.test.activity;

import net.bpelunit.framework.control.datasource.WrappedContext;
import net.bpelunit.framework.exception.DataSourceException;
import net.bpelunit.framework.model.test.data.extraction.IExtractedDataContainer;
import net.bpelunit.framework.model.test.report.ITestArtefact;

/**
 * Interface for all classes that can create a Velocity context, for use in
 * templates.
 *
 * @author University of Cádiz (Antonio García-Domínguez)
 */
public interface VelocityContextProvider {

	/**
	 * Creates a new Velocity context from the state of the provider.
	 *
	 * Optionally, users may specify a target {@link ITestArtefact} that may be
	 * used to retrieve additional information. For instance, they could retrieve
	 * the latest copy of the extracted data of the target and all its descendants.
	 *
	 * Otherwise, <code>target</code> can be left as <code>null</code>.
	 *
	 * @see IExtractedDataContainer
	 */
	WrappedContext createVelocityContext(ITestArtefact target) throws DataSourceException;
}
