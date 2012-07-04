package net.bpelunit.framework.model;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.ParserConfigurationException;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.wsht.WSHTClient;

import org.apache.xmlbeans.XmlException;
import org.example.wsHT.api.XMLTStatus.Enum;
import org.example.wsHT.api.XMLTTask;
import org.example.wsHT.api.xsd.XMLGetMyTasksResponseDocument.GetMyTasksResponse;
import org.xml.sax.SAXException;

public class HumanPartner extends AbstractPartner {
	
	/**
	 * This lock is used to serialize requests to WS-HT services. This allows
	 * two different tracks to complete tasks with the same name without
	 * interfering with each other.
	 */
	public static final ReentrantLock WSHT_LOCK = new ReentrantLock();
	
	private URL endPoint;
	private String username;
	private String password;
	private Calendar startTime = Calendar.getInstance(TimeZone.getTimeZone("GMT")); 
	
	private WSHTClient wshtClient;
	
	public URL getEndPoint() {
		return endPoint;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public WSHTClient getWSHTClient() {
		return this.wshtClient;
	}
	
	public HumanPartner(String name, URL baseURL, String endPoint, String username,
			String password) throws SpecificationException {
		
		super(name, baseURL.toString());
		
		try {
			this.endPoint = new URL(endPoint);
			this.username = username;
			this.password = password;
			
			this.wshtClient = new WSHTClient(this.endPoint, username, password) {
				@Override
				public GetMyTasksResponse getTaskList(String taskName,
						Enum[] state) throws IOException, XmlException,
						ParserConfigurationException, SAXException {
					GetMyTasksResponse taskList = super.getTaskList(taskName, state);
					
					filterOutPreviouslyExistingTasks(taskList);
					
					return taskList;
				}
				
			};
			
		} catch (MalformedURLException e) {
			throw new SpecificationException("Invalid WS-HT Endpoint (URL) for human partner " + name, e); 
		}
	}

	protected void filterOutPreviouslyExistingTasks(
			GetMyTasksResponse taskList) {
		List<XMLTTask> tasksToBeRemoved = new ArrayList<XMLTTask>(); 
		
		for(XMLTTask t : taskList.getTaskAbstractList()) {
			if(t.getCreatedOn().before(startTime)) {
				tasksToBeRemoved.add(t);
			}
		}
		taskList.getTaskAbstractList().removeAll(tasksToBeRemoved);
	}
}
