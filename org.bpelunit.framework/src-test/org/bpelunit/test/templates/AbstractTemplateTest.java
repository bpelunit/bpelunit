package org.bpelunit.test.templates;

import java.io.File;

/**
 * Base class for all tests of the Velocity-based test case templates.
 *
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public abstract class AbstractTemplateTest {

	protected static final File TEST_BPTS_DIR = new File(new File("resources"), "templates");
	protected static final File TC_4R_TEMP = new File(TEST_BPTS_DIR, "BookSearch-4results-t.bpts");
	protected static final File TC_3R_NOTEMP = new File(TEST_BPTS_DIR, "BookSearch-3results-nt.bpts");
	protected static final File TC_4R_NOTEMP = new File(TEST_BPTS_DIR, "BookSearch-4results-nt.bpts");

}