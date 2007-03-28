package coverage.instrumentation.bpelxmltools;

import coverage.instrumentation.bpelxmltools.exprlang.impl.XpathLanguage;

public abstract class ExpressionLanguage {

	public static final int XPATH1_0 = 0;

	private static ExpressionLanguage xpath = null;

	abstract public String negateExpression(String expression);

	abstract public String valueOf(String string);

	abstract public String concat(String[] strings);

	public static ExpressionLanguage getInstance(int expressionLanguage) {
		ExpressionLanguage language = null;
		switch (expressionLanguage) {
		case 0:
			if (xpath == null)
				xpath = new XpathLanguage();
			language = xpath;
			break;
		}
		return language;
	}
}
