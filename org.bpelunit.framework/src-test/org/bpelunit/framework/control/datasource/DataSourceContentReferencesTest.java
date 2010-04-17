package org.bpelunit.framework.control.datasource;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.bpelunit.framework.control.datasource.DataSourceUtil;
import org.bpelunit.framework.exception.DataSourceException;
import org.junit.Test;

/**
 * Tests for the creation of InputStreams for each of the supported data source
 * content reference types.
 * 
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public class DataSourceContentReferencesTest {

	@Test
	public void bothMissingIsReported() {
		try {
			DataSourceUtil.getStreamForDataSource(null, null);
			fail("A DataSourceException was expected");
		} catch (DataSourceException ex) {}
	}

	@Test
	public void bothSpecifiedIsReported() {
		try {
			DataSourceUtil.getStreamForDataSource("foo", "bar");
			fail("A DataSourceException was expected");
		} catch (DataSourceException ex) {}
	}
	@Test
	public void inlineContents() throws Exception {
		final String contents = "test";
		InputStream is = DataSourceUtil.getStreamForDataSource(contents, null);
		BufferedReader rIs = new BufferedReader(new InputStreamReader(is));
		rIs.readLine().equals(contents);
	}

}
