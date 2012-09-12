package net.bpelunit.utils.bptstool.functions.create;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;


public class CreateFunctionTest {

	private CreateFunction createFunction = new CreateFunction();;

	@Test
	public void testExecute() throws Exception {
		createFunction.execute(new String[]{ 
			"src/test/resources/bpel/test/Test.bpel",
			"-d",
			"../../../../../target/test"
		});
		
		assertTrue(new File("target/test").exists());
		assertTrue(new File("target/test").isDirectory());
		assertTrue(new File("target/test/Test.bpts").isFile());
	}
	
	@Test
	public void testGetBptsFile() throws Exception {
		File bpelFile = new File("src/test/resources/bpel/test/Test.bpel");
		
		File expectedBptsFile = new File("src/test/resources/bpel/test/Test.bpts");
		assertEquals(expectedBptsFile.getAbsoluteFile(), createFunction.getBptsFile(bpelFile, null));
		
		expectedBptsFile = new File("target/test/Test.bpts");
		assertEquals(expectedBptsFile.getAbsoluteFile(), createFunction.getBptsFile(bpelFile, "../../../../../target/test"));
		
		expectedBptsFile = new File("C:/test/Test.bpts");
		if(expectedBptsFile.isAbsolute()) {
			assertEquals(expectedBptsFile.getAbsoluteFile(), createFunction.getBptsFile(bpelFile, "C:/test"));
		}
		
		expectedBptsFile = new File("/test/Test.bpts");
		if(expectedBptsFile.isAbsolute()) {
			assertEquals(expectedBptsFile.getAbsoluteFile(), createFunction.getBptsFile(bpelFile, "/test"));
		}
	}
	
}
