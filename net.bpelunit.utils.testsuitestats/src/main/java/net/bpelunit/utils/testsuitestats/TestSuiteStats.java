package net.bpelunit.utils.testsuitestats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import net.bpelunit.framework.xml.suite.XMLTestSuite;
import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.IOUtils;
import org.apache.xmlbeans.XmlException;

public final class TestSuiteStats {

	private TestSuiteStats() {
	}
	
	public static void main(String[] args) {
		PrintStream out = System.out;
		
		Options options = createOptions();
		
		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			@SuppressWarnings("unchecked")
			List<String> bptsFileList = cmd.getArgList();
			
			if(bptsFileList.size() == 0) {
				System.err.println("No test suite specified");
				showHelpAndExit(options);
			}

			if(cmd.hasOption('c')) {
				out = new PrintStream(new FileOutputStream(cmd.getOptionValue('c')));
			}
		
			IStatisticEntry stats = null;
			if(bptsFileList.size() == 1) {
				stats = calculateStatisticsForTestSuite(bptsFileList.get(0));
			} else {
				StatisticGroup g = new StatisticGroup("Overall");
				
				for(String bptsFile : bptsFileList) {
					g.add(calculateStatisticsForTestSuite(bptsFile));
				}
				
				stats = g;
			}
			
			writeStatistics(out, stats);
			
		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			showHelpAndExit(options);
		} catch (FileNotFoundException e) {
			System.err.println("Problem while writing file: " + e.getMessage());
		} catch (XmlException e) {
			System.err.println("Problem parsing BPTS file: " + e.getMessage());
		} catch (IOException e) {
			System.err.println("Problem while writing file: " + e.getMessage());
		} finally {
			if(out != System.out) {
				IOUtils.closeQuietly(out);
			}
		}
		
	}

	private static void writeStatistics(PrintStream out, IStatisticEntry stats) {
		out.println(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
				"Name", 
				"All Receives",
				"All Sends",
				"Complete Human Tasks",
				"Receive Only",
				"Receive Send",
				"Send Only",
				"Send Receive",
				"Send Receive Async",
				"Receive Send Async",
				"Wait",
				"Client?"));
		printStats(stats, out);
	}

	private static IStatisticEntry calculateStatisticsForTestSuite(
			String fileName) throws XmlException, IOException {
		File bptsFile = new File(fileName);
		XMLTestSuite testSuite = XMLTestSuiteDocument.Factory.parse(bptsFile).getTestSuite();
		
		return new StatisticsGatherer().gatherStatistics(testSuite);
	}

	private static void showHelpAndExit(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(
			"TestSuiteStats file.bpts... [options]",  
			options
		);
		System.exit(1);
	}

	@SuppressWarnings("static-access")
	private static Options createOptions() {
		Options options = new Options();
		
		options.addOption(OptionBuilder
                .withDescription("Writes statistics as a CSV file with tabs as column delimiters.") //$NON-NLS-1$
                .hasArg()
                .withArgName("FILE")
                .create("c") );
		
		return options;
	}

	private static void printStats(IStatisticEntry stats, PrintStream out) {
		for(IStatisticEntry e : stats.getSubStatistics()) {
			printStats(e, out);
		}
		
		out.println(stats.toString());
	}
	
}
