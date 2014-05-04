package net.bpelunit.framework.model.test.data.extraction;

import net.bpelunit.framework.model.test.report.ITestArtefact;

import org.apache.velocity.context.Context;

/**
 * Utility methods for operating with {@link IExtractedDataContainer}
 * implementations.
 *
 * @author University of Cádiz (Antonio García-Domínguez)
 */
public final class ExtractedDataContainerUtil {

	private ExtractedDataContainerUtil() {
		// utility class
	}

	/**
	 * Adds all the extracted data available from <code>source</code> and
	 * its ancestors to <code>velocityContext</code> as variables. Extracted
	 * data is added in reverse order of ancestry. If <code>source</code> were
	 * a specific activity, the data would be added in this order:
	 * <ol>
	 * <li>Test suite data,</li>
	 * <li>Test case data,</li>
	 * <li>Partner track data,</li>
	 * <li>and finally the data for the activity.</li>
	 * </ol>
	 */
	public static void addExtractedDataFromAncestors(Context velocityContext, ITestArtefact source) {
		if (source != null) {
			addExtractedDataFromAncestors(velocityContext, source.getParent());
			if (source instanceof IExtractedDataContainer) {
				final IExtractedDataContainer c = (IExtractedDataContainer)source;
				for (String name : c.getAllExtractedDataNames()) {
					velocityContext.put(name, c.getExtractedData(name));
				}
			}
		}
	}
}
