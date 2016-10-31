package net.bpelunit.utils.bpelstats.languagestats;

import java.io.File;

public class FileStats {

	public File absoluteFileName;
	
	public String fileType;
	
	public int xquerySimpleExpressionLOCs;
	public int xquerySimpleQueryLOCs;
	public int xqueryExternalLOCs;
	public int xqueryReuseLOCs;
	public int xsltExternalLOCs;
	public int xsltReuseLOCs;
	public int xpathExpressionsLOCs;
	public int xpathQueriesLOCs;
	public int xqueryComplexExpressionLOCs;
	public int xqueryComplexQueryLOCs;
	public int xquerySimpleExpressionOccurrences = 0;
	public int xqueryComplexExpressionOccurrences = 0;
	public int xquerySimpleQueryOccurrences = 0;
	public int xqueryComplexQueryOccurrences = 0;
	public int xpathQueryOccurrences = 0;
	public int xpathExpressionOccurrences = 0;
	public int xqueryComplexity = 0;
	public int xsltComplexity = 0;

	@Override
	public String toString() {
		return absoluteFileName.getName() + ": XQ=" + (xquerySimpleExpressionLOCs+xquerySimpleQueryLOCs+xqueryExternalLOCs+xqueryReuseLOCs) + ", XSLT=" + (xsltReuseLOCs+xsltExternalLOCs) + ", XPath:" + (xpathExpressionsLOCs+xpathQueriesLOCs);
	}
	
	public void add(FileStats f) {
		xquerySimpleExpressionLOCs += f.xquerySimpleExpressionLOCs;
		xquerySimpleQueryLOCs += f.xquerySimpleQueryLOCs;
		xqueryExternalLOCs += f.xqueryExternalLOCs;
		xqueryReuseLOCs += f.xqueryReuseLOCs;
		xsltExternalLOCs += f.xsltExternalLOCs;
		xsltReuseLOCs += f.xsltReuseLOCs;
		xpathExpressionsLOCs += f.xpathExpressionsLOCs;
		xpathQueriesLOCs += f.xpathQueriesLOCs;
		xqueryComplexExpressionLOCs += f.xqueryComplexExpressionLOCs;
		xqueryComplexQueryLOCs += f.xqueryComplexQueryLOCs;
		xquerySimpleExpressionOccurrences += f.xquerySimpleExpressionOccurrences;
		xqueryComplexExpressionOccurrences += f.xqueryComplexExpressionOccurrences;
		xquerySimpleQueryOccurrences += f.xquerySimpleQueryOccurrences;
		xqueryComplexQueryOccurrences += f.xqueryComplexQueryOccurrences;
		xpathQueryOccurrences += f.xpathQueryOccurrences;
		xpathExpressionOccurrences += f.xpathExpressionOccurrences;
		xqueryComplexity += f.xqueryComplexity;
		xsltComplexity += f.xsltComplexity;
	}
}
