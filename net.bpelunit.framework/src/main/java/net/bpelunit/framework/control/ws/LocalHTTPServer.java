/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.ws;

import net.bpelunit.framework.control.run.TestCaseRunner;

import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;

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

	private Server fServer;

	private WebServiceHandler fHandler;

	private final static Logger LOGGER = Logger.getLogger(LocalHTTPServer.class);

	public LocalHTTPServer(int portNumber, String rootPath) {
		if (rootPath.endsWith("/")) {
			// Jetty 9 does not like context paths with trailing slashes - remove them
			rootPath = rootPath.substring(0, rootPath.length() - 1);
		}
		fServer = new Server(portNumber);

		fHandler = new WebServiceHandler();
		final ContextHandler context = new ContextHandler();
		context.setContextPath(rootPath);
		LOGGER.info("!!!!ROOTPATH " + rootPath);
		context.setResourceBase("");
		context.setHandler(fHandler);

		fServer.setHandler(context);
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

	public void stopServer() throws Exception {
		final Connector firstConnector = fServer.getConnectors()[0];

		LOGGER.info("Connections = " + firstConnector.getConnections());
		LOGGER.info("ConnectionsOpen = " + firstConnector.getConnectionsOpen());
		LOGGER.info("ConnectionsRequests = " + firstConnector.getRequests());
		fServer.stop();
	}

	public void stopTest(TestCaseRunner runner) {
		fHandler.deinitialize();

	}

}
