package coverage.instrumentation.statementcoverage;

import java.util.Hashtable;

import org.jdom.Element;
import org.jdom.Namespace;
import org.jdom.filter.ElementFilter;

import coverage.instrumentation.BasisActivity;

public class BasicActivitiesFilter extends ElementFilter {

	private Hashtable<String, String> activities_to_respect; 
	
	public BasicActivitiesFilter(Namespace namespace,Hashtable<String,String> activities_to_respect){
		super(namespace);
		this.activities_to_respect=activities_to_respect;
	}
	
	@Override
	public boolean matches(Object obj) {
		boolean result=false;
		Element element;
		if(super.matches(obj)){
			element=(Element)obj;
			if(BasisActivity.isBasisActivity(element)){
				result=true;
			}
		}
		return result;
	}
	
	

}
