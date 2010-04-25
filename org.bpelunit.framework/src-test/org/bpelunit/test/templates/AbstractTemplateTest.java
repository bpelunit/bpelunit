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
	protected static final File TC_K2R_TEMP = new File(TEST_BPTS_DIR, "BookSearch-key2res-t.bpts");
	protected static final File TC_K2R_NOTEMP = new File(TEST_BPTS_DIR, "BookSearch-key2res-nt.bpts");
	protected static final File TC_TI_NOTEMP = new File(TEST_BPTS_DIR, "BookSearch-testinfo-nt.bpts");
	protected static final File TC_TI_TEMP = new File(TEST_BPTS_DIR, "BookSearch-testinfo-t.bpts");
	protected static final File TC_SUP_NOTEMP = new File(TEST_BPTS_DIR, "BookSearch-setUp-nt.bpts");
	protected static final File TC_SUP_TEMP = new File(TEST_BPTS_DIR, "BookSearch-setUp-t.bpts");

	protected static final File TC_VDS_TCDS_NOTEMP = new File(TEST_BPTS_DIR, "tacService-casesrc-nt.bpts");
	protected static final File TC_VDS_TCDS_TEMP = new File(TEST_BPTS_DIR, "tacService-casesrc-t.bpts");
	protected static final File TC_VDS_TSDS_TEMP = new File(TEST_BPTS_DIR, "tacService-suitesrc-t.bpts");

	protected static final File TC_COND = new File(TEST_BPTS_DIR, "tacService-casesrc-t-cond.bpts");
}