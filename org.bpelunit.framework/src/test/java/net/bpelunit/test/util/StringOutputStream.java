/**
 * This file belongs to the BPELUnit utility and Eclipse plugin set. See enclosed
 * license file for more information.
 * 
 */
package net.bpelunit.test.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A String Output Stream.
 * 
 * @version $Id: StringOutputStream.java,v 1.2 2006/07/11 14:27:43 phil Exp $
 * @author Philip Mayer
 * 
 */
public class StringOutputStream extends OutputStream {

	private StringBuffer fStr;

	public StringOutputStream() {
		fStr= new StringBuffer();
	}

	@Override
	public void write(int b) throws IOException {
		fStr.append((char) b);
	}

	public String getString() {
		return fStr.toString();
	}

}
