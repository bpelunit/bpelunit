package net.bpelunit.framework.coverage.annotation.tools.bpelxmltools.exprlang;


// TODO DL Make ENUM
public abstract class ExpressionLanguage {

	public static final int XPATH1_0 = 0;
	public static final int XQUERY1_0 = 1;

	private static ExpressionLanguage instance = null;

	abstract public String negateExpression(String expression);

	abstract public String valueOf(String string);

	abstract public String concat(String[] strings);
	
	abstract public String getLanguageSpecification();

	public static ExpressionLanguage getInstance(int expressionLanguage) {
		ExpressionLanguage language = null;
		switch (expressionLanguage) {
		case 0:
			if (instance == null)
				instance = new XpathLanguage();
			language = instance;
			break;
			
		case 1:
			if (instance == null)
				instance = new XpathLanguage();
			language = instance;
			break;
		}
		return language;
	}
}
