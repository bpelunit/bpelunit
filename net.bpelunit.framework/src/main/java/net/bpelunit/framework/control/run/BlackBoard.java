/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.run;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import net.bpelunit.framework.BPELUnitRunner;
import net.bpelunit.framework.control.util.BPELUnitConstants;

/**
 * Class used for thread communication in the test runner.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 * @param <KEY>
 * @param <OBJECT>
 */
public class BlackBoard<KEY extends BlackBoardKey, OBJECT> {

	private Map<KEY, OBJECT> map;

	public BlackBoard() {
		map= new HashMap<KEY, OBJECT>();
	}

	public synchronized void putObject(KEY key, OBJECT object) throws InterruptedException {
		// XXX Synchronization is not right
		while (map.containsKey(key)) {
			wait(BPELUnitConstants.TIMEOUT_SLEEP_TIME);
		}
		map.put(key, object);
		notifyAll();
	}

	public synchronized OBJECT getObject(KEY key) throws TimeoutException, InterruptedException {
		// XXX Synchronization is not right
		int timeout = 0;

		while (!map.containsKey(key) && timeout < BPELUnitRunner.getTimeout() && key.canStillProvideValue(this)) {
			timeout+= BPELUnitConstants.TIMEOUT_SLEEP_TIME;
			wait(BPELUnitConstants.TIMEOUT_SLEEP_TIME);
		}

		if (map.containsKey(key)) {
			OBJECT object= map.get(key);
			map.remove(key);
			return object;
		}
		else if (timeout >= BPELUnitRunner.getTimeout()) {
			throw new TimeoutException("Waiting for object for key " + key + " took too long.");
		}
		else {
			throw new TimeoutException("Key " + key + " cannot provide the requested value anymore");
		}
	}

}
