package net.bpelunit.framework.verify;

import static org.junit.Assert.*;

import net.bpelunit.framework.exception.SpecificationException;
import net.bpelunit.framework.xml.suite.XMLConditionGroup;
import net.bpelunit.framework.xml.suite.XMLConditionGroupSection;
import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class NoCyclesInConditionGroupInheritanceValidatorTest {

	private NoCyclesInConditionGroupInheritanceValidator v = new NoCyclesInConditionGroupInheritanceValidator();
	private XMLTestSuiteDocument doc;
	private XMLTestSuite suite;
	private XMLConditionGroupSection cgs;
	
	@Before
	public void setUp() {
		doc = XMLTestSuiteDocument.Factory.newInstance();
		
		suite = doc.addNewTestSuite();
		cgs = suite.addNewConditionGroups();
	}

	@After
	public void tearDown() {
		doc = null;
		suite = null;
		cgs = null;
	}
	
	@Test
	public void testNoCycles() throws Exception {
		XMLConditionGroup cg = cgs.addNewConditionGroup();
		cg.setName("CG1");
		
		XMLConditionGroup cg2 = cgs.addNewConditionGroup();
		cg2.setName("CG2");
		cg2.setInheritFrom("CG1");
		
		v.validate(doc);
	}
	
	@Test(expected=SpecificationException.class)
	public void testSelfReference() throws SpecificationException {
		XMLConditionGroup cg = cgs.addNewConditionGroup();
		cg.setName("CG1");
		cg.setInheritFrom("CG1");
		
		v.validate(doc);
	}
	
	@Test(expected=SpecificationException.class)
	public void testCyclicReference() throws SpecificationException {
		XMLConditionGroup cg1 = cgs.addNewConditionGroup();
		cg1.setName("CG1");
		cg1.setInheritFrom("CG2");

		XMLConditionGroup cg2 = cgs.addNewConditionGroup();
		cg2.setName("CG2");
		cg2.setInheritFrom("CG1");
		
		v.validate(doc);
	}
}
