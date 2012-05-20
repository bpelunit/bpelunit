/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.framework.control.deploy.oracle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 * A thread which reads input from a given stream and stores it in a buffer for later use.
 * 
 * @version $Id$
 * @author Philip Mayer
 * 
 */
public class StreamReader extends Thread {

	private InputStream fInputStream;
	private StringBuffer fResult;

	public StreamReader(InputStream is) {
		this.fInputStream= is;
		fResult= new StringBuffer();
	}

	@Override
	public void run() {
		try {
			InputStreamReader isr= new InputStreamReader(fInputStream);
			BufferedReader br= new BufferedReader(isr);
			String line= null;
			while ( (line= br.readLine()) != null) {
				Logger.getLogger(getClass()).debug(line);
				if (!"".equals(line)) {
					fResult.append(line + "\n");
				}
			}
		} catch (IOException ioe) {
			Logger.getLogger(getClass()).debug("Failed to read from Stream.");
			Logger.getLogger(getClass()).debug(ioe.getMessage());
		}
	}

	public String getAsString() {
		return fResult.toString();
	}

}
