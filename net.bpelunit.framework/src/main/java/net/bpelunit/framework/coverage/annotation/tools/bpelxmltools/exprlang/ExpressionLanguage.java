package net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.exprlang;


// TODO DL Make ENUM
public abstract class ExpressionLanguage {

	public static final int XPATH1_0 = 0;
	public static final int XQUERY1_0 = 1;

	private static ExpressionLanguage instance = null;

	public abstract String negateExpression(String expression);

	public abstract String valueOf(String string);

	public abstract String concat(String[] strings);
	
	public abstract String getLanguageSpecification();

	public static ExpressionLanguage getInstance(int expressionLanguage) {
		ExpressionLanguage language = null;
		switch (expressionLanguage) {
		case 0:
			if (instance == null) {
				instance = new XpathLanguage();
			}
			language = instance;
			break;
			
		case 1:
			if (instance == null) {
				instance = new XpathLanguage();
			}
			language = instance;
			break;
		}
		return language;
	}
}
