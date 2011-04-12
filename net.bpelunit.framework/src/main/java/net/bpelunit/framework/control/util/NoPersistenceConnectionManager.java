package net.bpelunit.framework.control.util;

import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

/**
 * Connection manager which always force closes connections, so they
 * are <b>not</b> reused, which can lead to trouble when testing.
 */
public class NoPersistenceConnectionManager extends MultiThreadedHttpConnectionManager {
	@Override
	public void releaseConnection(HttpConnection conn) {
		super.releaseConnection(conn);
		conn.close();
	}
}
