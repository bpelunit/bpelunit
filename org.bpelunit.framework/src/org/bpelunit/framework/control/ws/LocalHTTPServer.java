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
import org.bpelunit.framework.coverage.WebServiceHandler2;
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
	
	private WebServiceHandler2 fHandler2=null;
	


	private Logger wsLogger = Logger.getLogger(this.getClass());

	private HttpServer server;

	public LocalHTTPServer(int portNumber, String rootPath) {

		

		fServer = new HttpServer();

		SocketListener listener = new SocketListener();
		listener.setPort(portNumber);
		listener.setBufferSize(25000);
//		listener.setMaxIdleTimeMs(5000);
		listener.setBufferReserve(1024);
		wsLogger.info("!!!!!!!!BUFFERSIZE "+listener.getBufferSize());//8192
		wsLogger.info("!!!!!!!!BUFFERRserve "+listener.getBufferReserve());//512
		wsLogger.info("!!!!!!!!BUFFERRserve "+listener.getMaxIdleTimeMs());//10000
		wsLogger.info("!!!!!!!!BUFFERRserve "+listener.getMaxThreads());//256
		wsLogger.info("!!!!!!!!BUFFERRserve "+listener.getLingerTimeSecs());//30
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
		if (BPELUnitRunner.getCoverageMeasurmentTool() != null) {

//			listener = new SocketListener();
//			listener.setPort(CoverageConstants.SERVICE_PORT);
//			
//			fServer.addListener(listener);
//
//			// Create the context for the root path
//			context = new HttpContext();
//			context.setContextPath("/coverage/");
//			context.setResourceBase("");
//
//			fHandler2=new WebServiceHandler2();
//			// Add the ws handler first
//			context.addHandler(fHandler2);
//
//			// Add a 404 handler last
//			context.addHandler(new NotFoundHandler());
//			
//			fServer.addContext(context);
//			 server = new HttpServer();
//			listener = new SocketListener();
//			listener.setPort(CoverageConstants.SERVICE_PORT);
//			
//			server.addListener(listener);
//
//			// Create the context for the root path
//			context = new HttpContext();
//			context.setContextPath("/coverage/");
//			context.setResourceBase("");
//
//			fHandler2=new WebServiceHandler2();
//			// Add the ws handler first
//			context.addHandler(fHandler2);
//
//			// Add a 404 handler last
//			context.addHandler(new NotFoundHandler());
//			
//			server.addContext(context);
			
			
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
		fServer.stop();
	}

	public void stopTest(TestCaseRunner runner) {
		fHandler.deinitialize();

	}

}
