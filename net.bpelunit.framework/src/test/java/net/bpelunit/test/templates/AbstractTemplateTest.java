package net.bpelunit.test.templates;

import java.io.File;

/**
 * Base class for all tests of the Velocity-based test case templates.
 *
 * @author Antonio García-Domínguez
 * @version 1.0
 */
public abstract class AbstractTemplateTest {

	protected static final File TEST_BPTS_DIR = new File(AbstractTemplateTest.class.getResource("/templates").getPath());
	protected static final File TC_4R_TEMP = new File(TEST_BPTS_DIR, "BookSearch-4results-t.bpts");
	protected static final File TC_3R_NOTEMP = new File(TEST_BPTS_DIR, "BookSearch-3results-nt.bpts");
	protected static final File TC_4R_NOTEMP = new File(TEST_BPTS_DIR, "BookSearch-4results-nt.bpts");
	protected static final File TC_K2R_TEMP = new File(TEST_BPTS_DIR, "BookSearch-key2res-t.bpts");
	protected static final File TC_K2R_NOTEMP = new File(TEST_BPTS_DIR, "BookSearch-key2res-nt.bpts");
	protected static final File TC_TI_NOTEMP = new File(TEST_BPTS_DIR, "BookSearch-testinfo-nt.bpts");
	protected static final File TC_TI_TEMP = new File(TEST_BPTS_DIR, "BookSearch-testinfo-t.bpts");
	protected static final File TC_SUP_NOTEMP = new File(TEST_BPTS_DIR, "BookSearch-setUp-nt.bpts");
	protected static final File TC_SUP_TEMP = new File(TEST_BPTS_DIR, "BookSearch-setUp-t.bpts");
	protected static final File TC_SUP_DUPVARS = new File(TEST_BPTS_DIR, "BookSearch-setUp-t-dupvars.bpts");

	protected static final File TC_VDS_TCDS_NOTEMP = new File(TEST_BPTS_DIR, "tacService-casesrc-nt.bpts");
	protected static final File TC_VDS_TCDS_TEMP = new File(TEST_BPTS_DIR, "tacService-casesrc-t.bpts");
	protected static final File TC_VDS_TSDS_TEMP = new File(TEST_BPTS_DIR, "tacService-suitesrc-t.bpts");
	protected static final File TC_VDS_TSDS_TEMP_RECVCOND = new File(TEST_BPTS_DIR, "tacService-suitesrc-recv-t.bpts");
	protected static final File TC_VDS_TSDS_TEMP_SRCATTRIB = new File(TEST_BPTS_DIR, "tacService-suitesrc-t-src.bpts");
	protected static final File TC_VDS_TSDS_TEMP_CDATA = new File(TEST_BPTS_DIR, "tacService-suitesrc-t-cdata.bpts");

	protected static final File TC_COND = new File(TEST_BPTS_DIR, "tacService-casesrc-t-cond.bpts");

	protected static final File TC_ASSUME_PTRACK = new File(TEST_BPTS_DIR, "tacService-casesrc-t-assume-pt.bpts");
	protected static final File TC_ASSUME_ACTIVITY = new File(TEST_BPTS_DIR, "tacService-casesrc-t-assume-act.bpts");
	protected static final File TC_ASSUME_CTRACK = new File(TEST_BPTS_DIR, "doubleWithHistory-clientassume.bpts");

	protected static final File TC_PTRACKHIST_ONLYREQ = new File(TEST_BPTS_DIR, "doubleWithHistory-onlyrequest.bpts");
	protected static final File TC_PTRACKHIST_ALLVARS = new File(TEST_BPTS_DIR, "doubleWithHistory-allvars.bpts");
	protected static final File TC_PTRACKHIST_VELOCITYTOOLS = new File(TEST_BPTS_DIR, "doubleWithHistory-velocity-tools.bpts");

	protected static final File TC_PRINTER_NOTEMP = new File(TEST_BPTS_DIR, "tacService-printer-nt.bpts");
	protected static final File TC_PRINTER_TEMP = new File(TEST_BPTS_DIR, "tacService-printer-t.bpts");

	protected static final File TC_DATAEXTRACTION_DEFAULT = new File(TEST_BPTS_DIR, "dataextraction-default.bpts");
	protected static final File TC_DATAEXTRACTION_STRING = new File(TEST_BPTS_DIR, "dataextraction-string.bpts");
	protected static final File TC_DATAEXTRACTION_NODE = new File(TEST_BPTS_DIR, "dataextraction-node.bpts");
	protected static final File TC_DATAEXTRACTION_NODESET = new File(TEST_BPTS_DIR, "dataextraction-nodeset.bpts");
}