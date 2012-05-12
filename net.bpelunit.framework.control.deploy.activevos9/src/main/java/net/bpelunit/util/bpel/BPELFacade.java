package net.bpelunit.util.bpel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.bpelunit.util.XMLUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public final class BPELFacade {

	private static final String ACTIVITY_OPTION_UNTIL = "until";
	private static final String EVENT_ONALARM = "onAlarm";
	private static final String ACTIVITY_WAIT = "wait";
	private static final String ACTIVITY_OPTION_FOR = "for";
	public static final String NAMESPACE_BPEL_1_1 = "http://schemas.xmlsoap.org/ws/2003/03/business-process/";
	public static final String NAMESPACE_BPEL_2_0 = "http://docs.oasis-open.org/wsbpel/2.0/process/executable";

	public enum BPELVersion {
		BPEL_1_1 {
			@Override
			String getNamespace() {
				return NAMESPACE_BPEL_1_1;
			}
		}, BPEL_2 {
			@Override
			String getNamespace() {
				return NAMESPACE_BPEL_2_0;
			}
		};
		
		abstract String getNamespace();
		
		static BPELVersion getVersionWithNamespace(String namespace) {
			for(BPELVersion v : values()) {
				if(v.getNamespace().equals(namespace)) {
					return v;
				}
			}
			
			return null;
		}
	}
	
	@SuppressWarnings("serial")
	private static class WrongBPELVersionException extends RuntimeException {
		public WrongBPELVersionException() {
			super("Wrong BPEL Version");
		}
	}
	
	@SuppressWarnings("serial")
	private static class WrongBPELActivity extends RuntimeException {
		public WrongBPELActivity() {
			super("Tried to use a BPEL activity where it is not allowed");
		}
	}
	
	private static final Map<BPELVersion, BPELFacade> INSTANCES = new HashMap<BPELVersion, BPELFacade>();
	private BPELVersion bpel; 
	
	public BPELFacade(BPELVersion v) {
		this.bpel = v;
	}

	public static synchronized BPELFacade getInstance(String namespace) {
		BPELVersion v = BPELVersion.getVersionWithNamespace(namespace);
		
		if(!INSTANCES.containsKey(v)) {
			INSTANCES.put(v, new BPELFacade(v));
		}
		
		return INSTANCES.get(v);
	}
	
	/**
	 * Sets the new time expression to a duration (for) and removes all
	 * other timing expressions if presents
	 * 
	 * @param activity wait, onAlarm (pick), or onAlarm (event handler)
	 * @param durationExpression
	 */
	public void setFor(Element activity, String durationExpression) {
		checkForCorrectVersion(activity);
		checkForElementName(activity, ACTIVITY_WAIT, EVENT_ONALARM);
		
		List<Element> timeExpressions = XMLUtil.getChildElementsByName(activity, ACTIVITY_OPTION_FOR);
		timeExpressions.addAll(XMLUtil.getChildElementsByName(activity, ACTIVITY_OPTION_UNTIL));
		
		Document document = activity.getOwnerDocument();
		Element newFor = document.createElementNS(bpel.getNamespace(), ACTIVITY_OPTION_FOR);
		Text newDuration = document.createTextNode(durationExpression);
		newFor.appendChild(newDuration);
		
		if(timeExpressions.size() > 0) {
			activity.replaceChild(newFor, timeExpressions.get(0));
		} else {
			XMLUtil.addAsFirstChild(activity, newFor);
		}
	}

	private void checkForElementName(Element activity, String... allowedElementNames) {
		if(!Arrays.asList(allowedElementNames).contains(activity.getLocalName())) {
			throw new WrongBPELActivity();
		}
	}

	private void checkForCorrectVersion(Element activity) {
		if(!bpel.getNamespace().equals(activity.getNamespaceURI())) {
			throw new WrongBPELVersionException();
		}
	}
}
