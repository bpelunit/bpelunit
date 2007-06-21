/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package org.bpelunit.framework.control.ws;

import org.apache.log4j.Logger;
import org.bpelunit.framework.BPELUnitRunner;
import org.bpelunit.framework.control.run.TestCaseRunner;
import org.bpelunit.framework.coverage.CoverageConstants;
import org.bpelunit.framework.coverage.receiver.ServiceHandler;
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

	private HttpServer fServer;

	private WebServiceHandler fHandler;
	


	private Logger wsLogger = Logger.getLogger(this.getClass());

	private HttpServer server;

	private ServiceHandler fHandler2;

	private SocketListener listener2=null;

	public LocalHTTPServer(int portNumber, String rootPath) {

		

		fServer = new HttpServer();
		SocketListener listener = new SocketListener();
		listener.setPort(portNumber);
//		listener.setBufferSize(15000);
//		listener.setBufferReserve(1024);
//		listener.setAcceptQueueSize(100);
//		listener.getAcceptQueueSize();
//		listener.getAcceptQ();
		wsLogger.info("!!!!!!!!AcceptQueueSize "+listener.getAcceptQueueSize());
		wsLogger.info("!!!!!!!!AcceptThreads "+listener.getAcceptorThreads());
//		wsLogger.info("!!!!!!!!BUFFERSIZE "+listener.getBufferSize());//8192
//		wsLogger.info("!!!!!!!!BUFFERSIZE "+listener.getBufferSize());//8192
//		wsLogger.info("!!!!!!!!BUFFERRserve "+listener.getBufferReserve());//512
//		wsLogger.info("!!!!!!!!BUFFERRserve "+listener.getMaxIdleTimeMs());//10000
//		wsLogger.info("!!!!!!!!BUFFERRserve "+listener.getMaxThreads());//256
//		wsLogger.info("!!!!!!!!BUFFERRserve "+listener.getLingerTimeSecs());//30
		fServer.addListener(listener);

		// Create the context for the root path
		HttpContext context = new HttpContext();
		context.setContextPath(rootPath);
		wsLogger.info("!!!!ROOTPATH "+rootPath);
		context.setResourceBase("");
		fHandler = new WebServiceHandler();

		// Add the ws handler first
		context.addHandler(fHandler);

		// Add a 404 handler last
		context.addHandler(new NotFoundHandler());
		
		fServer.addContext(context);
		

		if (BPELUnitRunner.measureTestCoverage()) {

			listener2 = new SocketListener();
			listener2.setPort(CoverageConstants.SERVICE_PORT);
			listener2.setBufferSize(20000);
			listener2.setBufferReserve(1024);
//			listener2.setMinThreads(4);
//			listener2.setAcceptQueueSize(200);
			fServer.addListener(listener2);
			// Create the context for the root path
			HttpContext context2 = new HttpContext();
			context2.setContextPath(CoverageConstants.SERVICE_CONTEXT);
			context2.setResourceBase("");
			fHandler2 = new ServiceHandler();
			// Add the ws handler first
			context2.addHandler(fHandler2);

			// Add a 404 handler last
			context2.addHandler(new NotFoundHandler());
			fServer.addContext(context2);
			}

	}

	public void startTest(TestCaseRunner runner) {
		fHandler.initialize(runner);
	}

	public void startServer() throws Exception {
		Logger.getLogger(getClass()).info("Starting local HTTP Server...");
		fServer.start();
		Logger.getLogger(getClass()).info("Local HTTP server was started.");
	}

	public void stopServer() throws InterruptedException {
		wsLogger.info("Connections="+fServer.getConnections());
		wsLogger.info("ConnectionsOpen="+fServer.getConnectionsOpen());
		wsLogger.info("ConnectionsRequests="+fServer.getRequests());
		if(listener2!=null){
			wsLogger.info("ACCEPTQUESIZE="+listener2.getAcceptQueueSize());
		}
		fServer.stop(true);
//		fServer.destroy();
	}

	public void stopTest(TestCaseRunner runner) {
		fHandler.deinitialize();

	}

}
