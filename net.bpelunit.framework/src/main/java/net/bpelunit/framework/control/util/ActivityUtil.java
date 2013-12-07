/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.util;

import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.xml.suite.XMLActivity;
import net.bpelunit.framework.xml.suite.XMLCompleteHumanTaskActivity;
import net.bpelunit.framework.xml.suite.XMLCondition;
import net.bpelunit.framework.xml.suite.XMLHeaderProcessor;
import net.bpelunit.framework.xml.suite.XMLHumanPartnerTrack;
import net.bpelunit.framework.xml.suite.XMLMapping;
import net.bpelunit.framework.xml.suite.XMLReceiveActivity;
import net.bpelunit.framework.xml.suite.XMLSendActivity;
import net.bpelunit.framework.xml.suite.XMLSoapActivity;
import net.bpelunit.framework.xml.suite.XMLTrack;
import net.bpelunit.framework.xml.suite.XMLTwoWayActivity;

import org.apache.commons.lang.StringUtils;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.w3c.dom.Node;

/**
 * This class implements various utility methods for dealing with XML
 * Activities. The specific structure of the XMLBeans-generated activity classes
 * necessitate some XML navigation and wrapper methods for correctly handling
 * activities in the specification loader, and in higher-level components
 * (specifically, the tool support).
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public final class ActivityUtil {

	/**
	 * The ActivityConstant enum is a type-safe collection of all possible
	 * activities, along with their XML names and a pretty-print, human-readable
	 * name.
	 * 
	 * @version $Id$
	 * @author Philip Mayer
	 * 
	 */
	public enum ActivityConstant {

		SEND_ONLY("sendOnly", "Send Asynchronous"), RECEIVE_ONLY("receiveOnly",
				"Receive Asynchronous"), SEND_RECEIVE_SYNC("sendReceive",
				"Send/Receive Synchronous"), RECEIVE_SEND_SYNC("receiveSend",
				"Receive/Send Synchronous"), SEND_RECEIVE_ASYNC(
				"sendReceiveAsynchronous", "Send/Receive Asynchronous"), RECEIVE_SEND_ASYNC(
				"receiveSendAsynchronous", "Receive/Send Asynchronous"), SEND(
				"send", "Send"), RECEIVE("receive", "Receive"), WAIT("wait",
				"Wait"), COMPLETEHUMANTASK("completeHumanTask",
				"Complete Human Task");

		private String fXmlName;
		private String fNiceName;

		ActivityConstant(String name, String niceName) {
			fXmlName = name;
			fNiceName = niceName;
		}

		public String getNiceName() {
			return fNiceName;
		}

		public String getXmlName() {
			return fXmlName;
		}

		public static ActivityConstant getForXmlName(String xmlName) {
			ActivityConstant[] values = values();
			for (int i = 0; i < values.length; i++) {
				if (values[i].getXmlName().equals(xmlName)) {
					return values[i];
				}
			}
			return null;
		}

		public static ActivityConstant getForNiceName(String niceName) {
			ActivityConstant[] values = values();
			for (int i = 0; i < values.length; i++) {
				if (values[i].getNiceName().equals(niceName)) {
					return values[i];
				}
			}
			return null;
		}
	}

	private ActivityUtil() {
		// Utility class
	}

	// ****************************** Activity names & constants
	// **************************

	/**
	 * Returns the activity constant for an activity, given as an object. The
	 * object is tested for the correct type. If no activity was found or unkown
	 * type, null is returned.
	 * 
	 * @param presumedActivity
	 * @return
	 */
	public static ActivityConstant getActivityConstant(Object presumedActivity) {

		if (!(presumedActivity instanceof XmlObject)) {
			return null;
		}
		XmlObject activity = (XmlObject) presumedActivity;

		Node node = activity.getDomNode();

		String localName = null;
		if (node != null) {
			localName = node.getLocalName();
		} else {
			return null;
		}

		return ActivityConstant.getForXmlName(localName);
	}

	/**
	 * Returns all possible activities as constants.
	 * 
	 * @return
	 */
	public static ActivityConstant[] getActivities() {
		return ActivityConstant.values();
	}

	/**
	 * Returns all top-level activities as constants.
	 * 
	 * @return
	 */
	public static List<ActivityConstant> getTopLevelSoapActivities() {
		ActivityConstant[] constants = ActivityConstant.values();
		List<ActivityConstant> list = new ArrayList<ActivityConstant>();
		for (int i = 0; i < constants.length; i++) {
			if (!constants[i].equals(ActivityConstant.RECEIVE)
					&& (!constants[i].equals(ActivityConstant.SEND))
					&& (!constants[i]
							.equals(ActivityConstant.COMPLETEHUMANTASK))) {
				list.add(constants[i]);
			}
		}
		return list;
	}

	// ***************************** Activity identity checking
	// ******************************

	/**
	 * Returns whether the given object is an activity.
	 * 
	 * @param presumedActivity
	 * @return
	 */
	public static boolean isActivity(Object presumedActivity) {
		return presumedActivity instanceof XMLActivity;
	}

	/**
	 * Returns whether the given object is an activity, and whether it is
	 * exactly of the type given as an activity constant.
	 * 
	 * @param presumedActivity
	 * @param constant
	 * @return
	 */
	public static boolean isActivity(Object presumedActivity,
			ActivityConstant constant) {
		String localName = getName(presumedActivity);
		if (localName == null) {
			return false;
		}

		return constant.getXmlName().equals(localName);
	}

	/**
	 * Returns whether the given activitiy is an activity, and whether it is a
	 * two-way activity.
	 * 
	 * @param presumedActivity
	 * @return
	 */
	public static boolean isTwoWayActivity(Object presumedActivity) {
		return isActivity(presumedActivity, ActivityConstant.RECEIVE_SEND_SYNC)
				|| isActivity(presumedActivity,
						ActivityConstant.SEND_RECEIVE_SYNC)
				|| isActivity(presumedActivity,
						ActivityConstant.RECEIVE_SEND_ASYNC)
				|| isActivity(presumedActivity,
						ActivityConstant.SEND_RECEIVE_ASYNC);
	}

	/**
	 * Returns whether this activity is a child activity, i.e. a receive or send
	 * below an asynchronous top-level activity.
	 * 
	 * @param op
	 * @return
	 */
	public static boolean isChildActivity(Object op) {
		return isActivity(op, ActivityConstant.RECEIVE)
				|| isActivity(op, ActivityConstant.SEND);
	}

	/**
	 * Returns whether this activity is an asynchronous activity. These are
	 * SEND_RECEIVE_ASYNC and RECEIVE_SEND_ASYNC.
	 * 
	 * @param object
	 * @return
	 */
	public static boolean isAsynchronous(Object object) {
		return (isActivity(object, ActivityConstant.SEND_RECEIVE_ASYNC) || isActivity(
				object, ActivityConstant.RECEIVE_SEND_ASYNC));
	}

	/**
	 * Returns whether this activity is an activity with a receive-block
	 * upfront. These are RECEIVE, RECEIVE_ONLY, RECEIVE_SEND_ASYNC and
	 * RECEIVE_SEND_SYNC.
	 * 
	 * @param activity
	 * @return
	 */
	public static boolean isReceiveFirstActivity(XMLActivity activity) {
		return isActivity(activity, ActivityConstant.RECEIVE)
				|| isActivity(activity, ActivityConstant.RECEIVE_ONLY)
				|| isActivity(activity, ActivityConstant.RECEIVE_SEND_ASYNC)
				|| isActivity(activity, ActivityConstant.RECEIVE_SEND_SYNC);
	}

	// ************************* PROPERTIES OF ACTIVITIES
	// *************************************

	/**
	 * Returns the XML name of an object, if it is an activity.
	 */
	public static String getName(Object presumedActivity) {
		if (!(presumedActivity instanceof XMLActivity)) {
			return null;
		}

		XMLActivity activity = (XMLActivity) presumedActivity;
		Node node = activity.getDomNode();

		if (node == null) {
			return null;
		}

		return node.getLocalName();
	}

	/**
	 * Returns the nice name of an object, if it is an activity.
	 * 
	 * @param presumedActivity
	 * @return
	 */
	public static String getNiceName(Object presumedActivity) {
		ActivityConstant constant = ActivityUtil
				.getActivityConstant(presumedActivity);
		if (constant == null) {
			return "Unknown activity";
		}

		return constant.getNiceName();
	}

	/**
	 * Returns a name suitable for displaying to the user. This includes
	 * additional info like operation names etc. so that a user can easily see
	 * which activity is meant
	 * 
	 * @param presumedActivity
	 * @return
	 */
	public static String getUIName(Object presumedActivity) {
		if (presumedActivity instanceof XMLSoapActivity
				&& ActivityUtil.getParentActivityFor(presumedActivity) == null) {
			XMLSoapActivity xml = (XMLSoapActivity) presumedActivity;
			String operation = xml.getOperation();
			operation = operation != null ? operation : "n/a";
			return operation + " ("
					+ ActivityUtil.getNiceName(presumedActivity) + ")";
		}
		if (presumedActivity instanceof XMLCompleteHumanTaskActivity) {
			XMLCompleteHumanTaskActivity xml = (XMLCompleteHumanTaskActivity) presumedActivity;
			return xml.getTaskName() + " ("
					+ ActivityUtil.getNiceName(presumedActivity) + ")";
		}

		if (ActivityUtil.isActivity(presumedActivity)) {
			return ActivityUtil.getNiceName(presumedActivity);
		} else if (presumedActivity instanceof XMLMapping) {
			return "Data Copy";
		} else if (presumedActivity instanceof XMLHeaderProcessor) {
			return "Header Processor ("
					+ ((XMLHeaderProcessor) presumedActivity).getName() + ")";
		} else if (presumedActivity instanceof XMLCondition) {
			return "Condition ("
					+ StringUtils
							.abbreviate(
									BPELUnitUtil
											.removeSpaceLineBreaks(((XMLCondition) presumedActivity)
													.getExpression()), 100)
					+ ")";
		}
		if (presumedActivity != null) {
			return presumedActivity.toString();
		} else {
			return "";
		}
	}

	// ************************ ACTIVITY NAVIGATION *************************

	/**
	 * Returns all the activities of a partner track.
	 * 
	 * @param xmlClientTrack
	 * @return
	 */
	public static List<XMLActivity> getActivities(XMLTrack xmlClientTrack) {
		List<XMLActivity> activities = new ArrayList<XMLActivity>();
		XmlCursor newCursor = xmlClientTrack.newCursor();

		try {
			if (newCursor.toFirstChild()) {
				addActivity(activities, newCursor.getObject());
				while (newCursor.toNextSibling()) {
					addActivity(activities, newCursor.getObject());
				}
			}
		} finally {
			newCursor.dispose();
		}
		return activities;
	}
	
	/**
	 * Returns all the activities of a human partner track.
	 * 
	 * @param xmlhumanPartnerTrack
	 * @return
	 */
	public static List<XMLActivity> getActivities(XMLHumanPartnerTrack xmlhumanPartnerTrack) {
		List<XMLActivity> activities = new ArrayList<XMLActivity>();
		XmlCursor newCursor = xmlhumanPartnerTrack.newCursor();
		
		try {
			if (newCursor.toFirstChild()) {
				addActivity(activities, newCursor.getObject());
				while (newCursor.toNextSibling()) {
					addActivity(activities, newCursor.getObject());
				}
			}
		} finally {
			newCursor.dispose();
		}
		return activities;
	}

	/**
	 * Returns the parent activity of an XMLObject. This method starts its
	 * search on the parent of the given object, i.e. if the object is already
	 * an activity, its parent activity (if available) is returned nevertheless.
	 * 
	 * @param viewerSelection
	 * @return
	 */
	public static XMLActivity getParentActivityFor(Object viewerSelection) {
		if (!(viewerSelection instanceof XmlObject)) {
			return null;
		}
		return getParentActivityForActivity((XmlObject) viewerSelection);
	}

	private static XMLActivity getParentActivityForActivity(XmlObject parent) {

		if (parent == null) {
			return null;
		}

		XmlCursor cursor = parent.newCursor();
		if (cursor.toParent()) {
			XmlObject newParent = cursor.getObject();
			if (newParent instanceof XMLActivity) {
				return (XMLActivity) newParent;
			} else {
				return getParentActivityForActivity(newParent);
			}
		} else {
			return null;
		}
	}

	/**
	 * Returns the enclosing partner track of this activity.
	 * 
	 * @param activity
	 * @return
	 */
	public static XMLTrack getEnclosingTrack(XMLActivity activity) {
		XmlCursor c = activity.newCursor();
		try {
			while (c.toParent()) {
				XmlObject object = c.getObject();
				if (object == null) {
					return null;
				} else if (object instanceof XMLTrack) {
					return ((XMLTrack) object);
				}
			}
			return null;
		} finally {
			c.dispose();
		}
	}

	// ******************************* FAULTS
	// ********************************************

	/**
	 * Returns the receive fault for this activity. The fault always resides at
	 * the receive element, no matter how deep it is nested inside the activity.
	 */
	public static boolean getReceiveFault(XMLActivity activity) {
		if (isTwoWayActivity(activity)) {
			return ((XMLTwoWayActivity) activity).getReceive().getFault();
		} else {
			return getSimpleFault(activity);
		}
	}

	/**
	 * Returns the send fault for this activity. The fault always resides at the
	 * send element, no matter how deep it is nested inside the activity.
	 */
	public static boolean getSendFault(XMLActivity activity) {
		if (isTwoWayActivity(activity)) {
			return ((XMLTwoWayActivity) activity).getSend().getFault();
		} else {
			return getSimpleFault(activity);
		}
	}

	/**
	 * Returns the receive fault code for this activity. The fault always
	 * resides at the receive element, no matter how deep it is nested inside
	 * the activity.
	 */
	public static String getReceiveFaultString(XMLActivity activity) {
		if (isTwoWayActivity(activity)) {
			return ((XMLTwoWayActivity) activity).getReceive().getFaultstring();
		} else {
			return getSimpleFaultString(activity);
		}
	}

	/**
	 * Returns the send fault code for this activity. The fault always resides
	 * at the send element, no matter how deep it is nested inside the activity.
	 */
	public static String getSendFaultString(XMLActivity activity) {
		if (isTwoWayActivity(activity)) {
			return ((XMLTwoWayActivity) activity).getSend().getFaultstring();
		} else {
			return getSimpleFaultString(activity);
		}
	}

	/**
	 * Returns a "fault" for this activity, which might be a send or receive
	 * fault, depending on the type of the activity.
	 * 
	 * The activity must not be a two way activity.
	 * 
	 * @param activity
	 * @return
	 */
	private static boolean getSimpleFault(XMLActivity activity) {
		if (ActivityUtil.isActivity(activity, ActivityConstant.RECEIVE_ONLY)
				|| ActivityUtil.isActivity(activity, ActivityConstant.RECEIVE)) {
			return ((XMLReceiveActivity) activity).getFault();
		} else if (ActivityUtil
				.isActivity(activity, ActivityConstant.SEND_ONLY)
				|| ActivityUtil.isActivity(activity, ActivityConstant.SEND)) {
			return ((XMLSendActivity) activity).getFault();
		}

		return false;
	}

	/**
	 * Returns a "fault string" for this activity, which might be a send or
	 * receive fault string, depending on the type of the activity.
	 * 
	 * The activity must not be a two way activity.
	 * 
	 * @param activity
	 * @return
	 */
	private static String getSimpleFaultString(XMLActivity activity) {
		if (ActivityUtil.isActivity(activity, ActivityConstant.RECEIVE_ONLY)
				|| ActivityUtil.isActivity(activity, ActivityConstant.RECEIVE)) {
			return ((XMLReceiveActivity) activity).getFaultstring();
		} else if (ActivityUtil
				.isActivity(activity, ActivityConstant.SEND_ONLY)
				|| ActivityUtil.isActivity(activity, ActivityConstant.SEND)) {
			return ((XMLSendActivity) activity).getFaultstring();
		}
		return null;
	}

	// ************************** ACTIVITY MANIPULATION
	// *********************************

	/**
	 * Adds the given presumed activity to a list of activities, if it is an
	 * activity
	 */
	public static void addActivity(List<XMLActivity> activities,
			XmlObject presumedActivity) {
		if (presumedActivity instanceof XMLActivity) {
			activities.add((XMLActivity) presumedActivity);
		}
	}

	/**
	 * Adds a new top level activity of the given type to the track and returns
	 * it.
	 * 
	 * @param toTrack
	 * @param type
	 * @return
	 */
	public static XMLActivity createNewTopLevelActivity(XMLTrack toTrack,
			ActivityConstant type) {
		switch (type) {
		case RECEIVE_ONLY:
			return toTrack.addNewReceiveOnly();
		case RECEIVE_SEND_ASYNC:
			return toTrack.addNewReceiveSendAsynchronous();
		case RECEIVE_SEND_SYNC:
			return toTrack.addNewReceiveSend();
		case SEND_ONLY:
			return toTrack.addNewSendOnly();
		case SEND_RECEIVE_ASYNC:
			return toTrack.addNewSendReceiveAsynchronous();
		case SEND_RECEIVE_SYNC:
			return toTrack.addNewSendReceive();
		case WAIT:
			return toTrack.addNewWait();
		}
		return null;
	}

	// ********************************* OTHER
	// ****************************************

	/**
	 * Searches for object in an array of objects. Returns the index where the
	 * object was found, or -1 if not found.
	 * 
	 * @param objects
	 * @param object
	 * @return
	 */
	public static int getIndexFor(Object[] objects, Object object) {
		for (int i = 0; i < objects.length; i++) {
			if (objects[i].equals(object)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Checks whether the given XML object has a previous sibling in its parent
	 * (i.e., it is not the first child).
	 * 
	 * @param someObject
	 * @return
	 */
	public static boolean hasPrevious(XmlObject someObject) {
		return someObject.newCursor().toPrevSibling();
	}

	/**
	 * Checks whether the given XML object has a next sibling in its parent
	 * (i.e., it is not the last child)
	 * 
	 * @param someObject
	 * @return
	 */
	public static boolean hasNext(XmlObject someObject) {
		return someObject.newCursor().toNextSibling();
	}
}
