/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.ws;

import net.bpelunit.framework.control.run.TestCaseRunner;

import org.apache.log4j.Logger;
import org.mortbay.http.HttpContext;
import org.mortbay.http.HttpServer;
import org.mortbay.http.SocketListener;
import org.mortbay.http.handler.NotFoundHandler;

/**
 * The class LocalHTTPServer implements the necessary functionality for starting
 * a local HTTP server and registering handlers for receiving incoming SOAP
 * calls from the process under test.
 * 
 * @version $Id$
 * @author Philip Mayer
 */
public class LocalHTTPServer {

	@SuppressWarnings("serial")
	public static class HttpServerException extends Exception {

		public HttpServerException(String msg, Exception e) {
			super(msg, e);
		}

	}

	private HttpServer fServer;

	private WebServiceHandler fHandler;

	private Logger wsLogger = Logger.getLogger(this.getClass());

	private SocketListener listener2 = null;

	public LocalHTTPServer(int portNumber, String rootPath) {

		fServer = new HttpServer();
		SocketListener listener = new SocketListener();
		listener.setPort(portNumber);

		fServer.addListener(listener);

		// Create the context for the root path
		HttpContext context = new HttpContext();
		context.setContextPath(rootPath);
		wsLogger.info("!!!!ROOTPATH " + rootPath);
		context.setResourceBase("");
		fHandler = new WebServiceHandler();

		// Add the ws handler first
		context.addHandler(fHandler);

		// Add a 404 handler last
		context.addHandler(new NotFoundHandler());

		fServer.addContext(context);
	}

	public void startTest(TestCaseRunner runner) {
		fHandler.initialize(runner);
	}

	public void startServer() throws HttpServerException {
		Logger.getLogger(getClass()).info("Starting local HTTP Server...");
		try {
			fServer.start();
		} catch (Exception e) {
			throw new HttpServerException("Error while starting HTTP Server: " + e.getMessage(), e);
		}
		Logger.getLogger(getClass()).info("Local HTTP server was started.");
	}

	public void stopServer() throws InterruptedException {
		wsLogger.info("Connections=" + fServer.getConnections());
		wsLogger.info("ConnectionsOpen=" + fServer.getConnectionsOpen());
		wsLogger.info("ConnectionsRequests=" + fServer.getRequests());
		if (listener2 != null) {
			wsLogger.info("ACCEPTQUESIZE=" + listener2.getAcceptQueueSize());
		}
		fServer.stop(true);
	}

	public void stopTest(TestCaseRunner runner) {
		fHandler.deinitialize();

	}

}
