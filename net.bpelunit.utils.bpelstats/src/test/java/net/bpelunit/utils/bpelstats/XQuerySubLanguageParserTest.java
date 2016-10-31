package net.bpelunit.utils.bpelstats;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import net.bpelunit.utils.bpelstats.languagestats.XQuerySubLanguageParser;

import org.junit.Before;
import org.junit.Test;

import es.uca.webservices.xquery.parser.util.XQueryParsingException;

public class XQuerySubLanguageParserTest {

	private XQuerySubLanguageParser parser;

	@Before
	public void setUp() {
		this.parser = new XQuerySubLanguageParser();
	}
	
	@Test
	public void testComplexXQuery() throws Exception {
		parser.parse("<a>{ for $i in (1 to 3) return $i }</a>");
		assertTrue(parser.isComplex());
		assertEquals(2, parser.getComplexity());
		
		parser.parse("<a>{ if(1=1) then 'a' else 'b' }</a>");
		assertTrue(parser.isComplex());
		assertEquals(2, parser.getComplexity());
		
		parser.parse("<a>{ let $a := 'a' return <b/>}</a>");
		assertTrue(parser.isComplex());
		assertEquals(1, parser.getComplexity());
		
		parser.parse("<a>{ $case }</a>");
		assertFalse(parser.isComplex());
		assertEquals(1, parser.getComplexity());
	}
	
	@Test
	public void testEmptyFunction() throws Exception {
		parser.parse("module namespace ns = 'http://bpelunit.net/xquerytest'; declare function ns:x() as element() { <a/> };");
		
		assertFalse(parser.isComplex());
		assertEquals(1, parser.getComplexity());
	}
	
	@Test(expected=XQueryParsingException.class)
	public void testFailingXQuery() throws Exception {
		parser.parse("<a>{ test </a>");
	}
	
}
