package net.bpelunit.utils.bpelstats;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

public final class BpelStats {

        private BpelStats() {}

        @SuppressWarnings("unchecked")
        public static void main(String[] args) {
                PrintStream out = System.out;
                final Options options = createOptions();
                try {
                        final CommandLineParser parser = new PosixParser();
                        final CommandLine cmd = parser.parse(options, args);
                        final List<String> bpelFileList = cmd.getArgList();
                        if (bpelFileList.size() == 0) {
                                System.err.println("No .bpel file specified");
                                showHelpAndExit(options);
                        }
                        if (cmd.hasOption('c')) {
                                out = new PrintStream(new FileOutputStream(cmd.getOptionValue('c')));
                        }

                        boolean bPrintHeader = cmd.hasOption('h');
                        for (String bpelFileName : bpelFileList) {
                                final IProcess bpelProcess = BpelFactory.INSTANCE.loadProcess(new FileInputStream(bpelFileName));
                                final StatisticGathererVisitor v = new StatisticGathererVisitor();
                                bpelProcess.visit(v);

                                final Map<String, String> fields = getFieldMap(bpelFileName, v);
                                if (bPrintHeader) {
                                	bPrintHeader = false;
                        			printWithSeparator(out, "\t", fields.keySet());
                        			out.println();
                                }
                                printWithSeparator(out, "\t", fields.values());
                                out.println();
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

		private static Map<String, String> getFieldMap(final String bpelFileName, final StatisticGathererVisitor v) {
			final Map<String, String> fields = new LinkedHashMap<String, String>();

			fields.put("Filename", bpelFileName);
			fields.put("#Assign", "" + v.getCountAssign());
			fields.put("#Catch", "" + v.getCountCatch());
			fields.put("#CatchAll", "" + v.getCountCatchAll());
			fields.put("#Compensate", "" + v.getCountCompensate());
			fields.put("#CompensateScope", "" + v.getCountCompensateScope());
			fields.put("#CompensationHandler", "" + v.getCountCompensationHandler());
			fields.put("#Copy", "" + v.getCountCopy());
			fields.put("#Else", "" + v.getCountElse());
			fields.put("#ElseIf", "" + v.getCountElseIf());
			fields.put("#Empty", "" + v.getCountEmpty());
			fields.put("#Exit", "" + v.getCountExit());
			fields.put("#Flow", "" + v.getCountFlow());
			fields.put("#ForEach", "" + v.getCountForEach());
			fields.put("#If", "" + v.getCountIf());
			fields.put("#Invoke", "" + v.getCountInvoke());
			fields.put("#Link", "" + v.getCountLink());
			fields.put("#OnAlarm", "" + v.getCountOnAlarm());
			fields.put("#OnAlarmHandler", "" + v.getCountOnAlarmHandler());
			fields.put("#OnMessage", "" + v.getCountOnMessage());
			fields.put("#OnMessageHandler", "" + v.getCountOnMessageHandler());
			fields.put("#PartnerLink", "" + v.getCountPartnerLink());
			fields.put("#Pick", "" + v.getCountPick());
			fields.put("#Receive", "" + v.getCountReceive());
			fields.put("#RepeatUntil", "" + v.getCountRepeatUntil());
			fields.put("#Reply", "" + v.getCountReply());
			fields.put("#Rethrow", "" + v.getCountRethrow());
			fields.put("#Scope", "" + v.getCountScope());
			fields.put("#Sequence", "" + v.getCountSequence());
			fields.put("#Throw", "" + v.getCountThrow());
			fields.put("#Validate", "" + v.getCountValidate());
			fields.put("#Variable", "" + v.getCountVariable());
			fields.put("#AllActivities", "" + v.getCountAllActivities());
			fields.put("#BasicActivities", "" + v.getCountBasicActivities());
			fields.put("#Structured Activities", "" + v.getCountStructuredActivities());
			fields.put("#Nonlinear Activities", "" + v.getCountNonLinearActivities());

			return fields;
		}

		private static void printWithSeparator(final PrintStream out, final String sep, final Iterable<String> strings) {
			boolean bFirst = true;
			for (String key : strings) {
				if (!bFirst) {
					out.print(sep);
				}
				else {
					bFirst = false;
				}
				out.print(key);
			}
		}

        private static void showHelpAndExit(final Options options) {
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp(
                        "bpelstats file.bpel... [options]",
                        options
                );
                System.exit(1);
        }

        @SuppressWarnings("static-access")
        private static Options createOptions() {
                final Options options = new Options();

                options.addOption(OptionBuilder
                    .withDescription("Writes statistics as a CSV file with tabs as column delimiters.")
                    .hasArg().withArgName("FILE").create("c")
                );
                options.addOption(OptionBuilder
                    .withDescription("Writes out the table header")
                    .create("h"));

                return options;
        }

}
