package coverage;

import org.jdom.Namespace;

import coverage.instrumentation.bpelxmltools.exprlang.ExpressionLanguage;



public class CoverageConstants {
	public static final String PARTNERLINK_NAMESPACE = "http://schemas.xmlsoap.org/ws/2003/03/addressing";

	public static final Namespace COVERAGETOOL_NAMESPACE = Namespace
			.getNamespace("log", "http://www.bpelunit.org/coverage/logService");

	public static final char PREFIX_COPY_OF_ARCHIVEFILE = '_';

	public static final int EXPRESSION_LANGUAGE = ExpressionLanguage.XPATH1_0;
}
