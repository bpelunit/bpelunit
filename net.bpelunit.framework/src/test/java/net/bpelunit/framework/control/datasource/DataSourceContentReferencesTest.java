package net.bpelunit.framework.control.datasource;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import net.bpelunit.framework.exception.DataSourceException;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the creation of InputStreams for each of the supported data source
 * content reference types.
 * 
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public class DataSourceContentReferencesTest {

	// Source file information
	private static final String FILE_SOURCE_DIR = "src/test/resources/templates/";
	private static final String FILE_SOURCE_BASENAME = "tacService-data.vm";
	private static final String EXPECTED_FILE_CONTENTS = "#set($lines = [[], ['A'], ['A','B'], ['A','B','C']])";

	// Relative/absolute paths and file URLs
	private static final String FILE_SOURCE_PATH = FILE_SOURCE_DIR
			+ File.separator + FILE_SOURCE_BASENAME;
	private static String FILE_SOURCE_ABSPATH;

	// HTTP URLs
	private Server fHttpServer;
	private static final int HTTP_PORT = 7945;
	private static final String HTTP_SOURCE_URL = "http://127.0.0.1:"
			+ HTTP_PORT + "/" + FILE_SOURCE_BASENAME;

	@Before
	public void setUp() throws Exception {
		FILE_SOURCE_ABSPATH = new File(FILE_SOURCE_PATH).getCanonicalPath();

		fHttpServer = new Server(HTTP_PORT);

		ContextHandler context = new ContextHandler();
		context.setContextPath("/");
		context.setResourceBase(FILE_SOURCE_DIR);
		context.setHandler(new ResourceHandler());
		fHttpServer.setHandler(context);
		fHttpServer.start();
	}

	@After
	public void tearDown() throws Exception {
		fHttpServer.stop();
	}

	@Test
	public void bothMissingIsReported() {
		try {
			DataSourceUtil.getStreamForDataSource(null, null, null);
			fail("A DataSourceException was expected");
		} catch (DataSourceException ex) {}
	}

	@Test
	public void bothSpecifiedIsReported() {
		try {
			DataSourceUtil.getStreamForDataSource("foo", "bar", null);
			fail("A DataSourceException was expected");
		} catch (DataSourceException ex) {}
	}
	@Test
	public void inlineContents() throws Exception {
		final String contents = "test";
		InputStream is = DataSourceUtil.getStreamForDataSource(contents, null, null);
		BufferedReader rIs = new BufferedReader(new InputStreamReader(is));
		rIs.readLine().equals(contents);
	}

	@Test
	public void relativeFilePath() throws Exception {
		canReadContentsFrom(FILE_SOURCE_PATH);
	}

	@Test
	public void absoluteFilePath() throws Exception {
		canReadContentsFrom(FILE_SOURCE_ABSPATH);
	}

	@Test
	public void fileURL() throws Exception {
		canReadContentsFrom(new File(FILE_SOURCE_ABSPATH).toURI().toURL().toExternalForm());
	}

	@Test
	public void httpURL() throws Exception {
		canReadContentsFrom(HTTP_SOURCE_URL);
	}

	private void canReadContentsFrom(final String path)
			throws DataSourceException, IOException {
		InputStream is = DataSourceUtil.getStreamForDataSource(null, path, new File("."));
		BufferedReader rIs = new BufferedReader(new InputStreamReader(is));
		rIs.readLine().equals(EXPECTED_FILE_CONTENTS);
	}

}
