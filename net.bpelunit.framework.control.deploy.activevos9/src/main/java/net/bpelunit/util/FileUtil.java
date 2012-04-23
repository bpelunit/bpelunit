package net.bpelunit.util;

import java.io.File;
import java.io.IOException;

public class FileUtil {

	public static File createTempDirectory() throws IOException {
		File tmp = File.createTempFile("bpelunit", "");
		tmp.delete();
		tmp.mkdir();
		return tmp;
	}
	
}
