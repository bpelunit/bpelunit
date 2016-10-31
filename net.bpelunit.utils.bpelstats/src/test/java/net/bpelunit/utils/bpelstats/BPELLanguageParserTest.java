package net.bpelunit.utils.bpelstats;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import net.bpelunit.utils.bpelstats.languagestats.BPELSubLanguageParser;
import net.bpelunit.utils.bpelstats.languagestats.BpelSubLanguageStatsGatherer;
import net.bpelunit.utils.bpelstats.languagestats.FileStats;

import org.junit.Test;
import org.xml.sax.SAXException;

public class BPELLanguageParserTest {

	@Test
	public void testExtractConditions() throws Exception {
		BpelSubLanguageStatsGatherer gatherer = new BpelSubLanguageStatsGatherer();
		List<FileStats> imports = gatherer.gather(new File("C:/data/workspaces/terravis-av92/ch.terravis.egvt.process.egvt-ui-bank/bpel/egvt-ui-bank.bpel"), null);
    }
	
}
