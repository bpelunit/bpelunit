package net.bpelunit.utils.bpelstats;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.List;

import net.bpelunit.model.bpel.IProcess;
import net.bpelunit.model.bpel._2_0.BpelFactory;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.IOUtils;

import de.schlichtherle.io.FileInputStream;

public final class BpelStats {

	private BpelStats() {
	}
	
	public static void main(String[] args) {
		PrintStream out = System.out;
		
		Options options = createOptions();
		
		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);
			@SuppressWarnings("unchecked")
			List<String> bpelFileList = cmd.getArgList();
			
			if(bpelFileList.size() == 0) {
				System.err.println("No test suite specified");
				showHelpAndExit(options);
			}

			if(cmd.hasOption('c')) {
				out = new PrintStream(new FileOutputStream(cmd.getOptionValue('c')));
			}
			if(cmd.hasOption('h')) {
				out.println(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s\t%s",
						"Filename",
						"#Assign",
						"#Catch",
						"#CatchAll",
						"#Compensate",
						"#CompensateScope",
						"#CompensationHandler",
						"#Copy",
						"#Else",
						"#ElseIf",
						"#Empty",
						"#Exit",
						"#Flow",
						"#ForEach",
						"#If",
						"#Invoke",
						"#Link",
						"#OnAlarm",
						"#OnAlarmHandler",
						"#OnMessage",
						"#OnMessageHandler",
						"#PartnerLink",
						"#Pick",
						"#Receive",
						"#RepeatUntil",
						"#Reply",
						"#Rethrow",
						"#Scope",
						"#Sequence",
						"#Throw",
						"#Validate",
						"#Variable"));
			}
		
			for(String bpelFileName : bpelFileList) {
				IProcess bpelProcess = BpelFactory.INSTANCE.loadProcess(new FileInputStream(bpelFileName));
				StatisticGathererVisitor v = new StatisticGathererVisitor();
				bpelProcess.visit(v);
				out.println(String.format("%s\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d\t%d",
						bpelFileName,
						v.getCountAssign(),
						v.getCountCatch(),
						v.getCountCatchAll(),
						v.getCountCompensate(),
						v.getCountCompensateScope(),
						v.getCountCompensationHandler(),
						v.getCountCopy(),
						v.getCountElse(),
						v.getCountElseIf(),
						v.getCountEmpty(),
						v.getCountExit(),
						v.getCountFlow(),
						v.getCountForEach(),
						v.getCountIff(),
						v.getCountInvoke(),
						v.getCountLink(),
						v.getCountOnAlarm(),
						v.getCountOnAlarmHandler(),
						v.getCountOnMessage(),
						v.getCountOnMessageHandler(),
						v.getCountPartnerLink(),
						v.getCountPick(),
						v.getCountReceive(),
						v.getCountRepeatUntil(),
						v.getCountReply(),
						v.getCountRethrow(),
						v.getCountScope(),
						v.getCountSequence(),
						v.getCountThrow(),
						v.getCountValidate(),
						v.getCountVariable()));
			}
			
		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			showHelpAndExit(options);
		} catch (FileNotFoundException e) {
			System.err.println("Problem while writing file: " + e.getMessage());
		} finally {
			if(out != System.out) {
				IOUtils.closeQuietly(out);
			}
		}
		
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
                .withDescription("Writes statistics as a CSV file with tabs as column delimiters.")
                .hasArg()
                .withArgName("FILE")
                .create("c") );
		options.addOption(OptionBuilder
				.withDescription("Writes out the table header") 
				.create("h") );
		
		return options;
	}

}
