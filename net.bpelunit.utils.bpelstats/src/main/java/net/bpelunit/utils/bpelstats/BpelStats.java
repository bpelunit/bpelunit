package net.bpelunit.utils.bpelstats;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import net.bpelunit.utils.bpelstats.extensions.ExtensionHandler;
import net.bpelunit.utils.bpelstats.hertis.BPELMetric;
import net.bpelunit.utils.bpelstats.hertis.CFCGatherer;
import net.bpelunit.utils.bpelstats.hertis.CWGatherer;
import net.bpelunit.utils.bpelstats.hertis.FIGatherer;
import net.bpelunit.utils.bpelstats.hertis.FOGatherer;
import net.bpelunit.utils.bpelstats.hertis.HandlerMultiplexer;
import net.bpelunit.utils.bpelstats.hertis.NDGatherer;
import net.bpelunit.utils.bpelstats.hertis.NOACGatherer;
import net.bpelunit.utils.bpelstats.hertis.NOAGatherer;
import net.bpelunit.utils.bpelstats.languagestats.BpelSubLanguageStatsGatherer;
import net.bpelunit.utils.bpelstats.languagestats.FileStats;
import net.bpelunit.utils.bpelstats.sax.SAXExecutor;
import net.bpelunit.utils.bpelstats.sax.SAXStatsGatherer;
import net.bpelunit.utils.bpelstats.sax.SAXStatsHandler;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public final class BpelStats {

	private static final String DEFAULT_SEPARATOR = "\t";

	private BpelStats() {}

    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws ParserConfigurationException {
    		String separator = DEFAULT_SEPARATOR;
            PrintStream out = System.out;
            final Options options = createOptions();
            try {
            	final CommandLineParser parser = new PosixParser();
                final CommandLine cmd = parser.parse(options, args);
                final List<String> bpelFileList = cmd.getArgList();
                
                boolean bPrintHeader = cmd.hasOption('h');
                boolean bSublanguageStatistics = cmd.hasOption('s');
                boolean bHertisMetrics = cmd.hasOption('m');
                boolean bExtensions = cmd.hasOption('e');

                List<DefaultHandler> handlers = new ArrayList<DefaultHandler>();
                
                if (bpelFileList.size() == 0 && !bSublanguageStatistics) {
                        System.err.println("No .bpel file specified");
                        showHelpAndExit(options);
                }
                if (cmd.hasOption('c')) {
                        out = new PrintStream(new FileOutputStream(cmd.getOptionValue('c')));
                }
                
                if(cmd.hasOption('d')) {
                	separator = cmd.getOptionValue('d');
                }

                if(bHertisMetrics) {
                	handlers.add(new NOAGatherer());
                	handlers.add(new NOACGatherer());
                	handlers.add(new CFCGatherer());
                	handlers.add(new NDGatherer());
                	handlers.add(new CWGatherer());
                	handlers.add(new FIGatherer());
                	handlers.add(new FOGatherer());
                	if(bPrintHeader) {
                		List<String> headers = new ArrayList<String>();
                		headers.add("Filename");
                		for(DefaultHandler dh : handlers) {
                			headers.add(((BPELMetric)dh).getName());
                		}
                		printWithSeparator(out, separator, headers);
                	}
                }
                
                if(bPrintHeader && bSublanguageStatistics) {
                	printWithSeparator(out, separator, getSublanguageHeaders());
                } else if(bPrintHeader && bExtensions) {
                	printWithSeparator(out, separator, getExtensionHeaders());
                }
                
                for (String bpelFileName : bpelFileList) {
                	try {
                	if(!bSublanguageStatistics && !bHertisMetrics && !bExtensions) {
                        bPrintHeader = gatherBpelMetrics(out,
								bPrintHeader, bpelFileName, separator);
                	} else if(bHertisMetrics) {
                		HandlerMultiplexer multiplexer = new HandlerMultiplexer(handlers);
                		List<String> values = new ArrayList<String>();
            			SAXExecutor.execute(bpelFileName, multiplexer);
            			values.add(bpelFileName);
            			for(DefaultHandler dh : handlers) {
            				BPELMetric m = (BPELMetric)dh;
            				values.add("" + m.getValue());
            			}
            			printWithSeparator(out, separator, values);
            			values.clear();
                	} else if(bExtensions) {
                		ExtensionHandler extensionHandler = new ExtensionHandler();
                		SAXExecutor.execute(bpelFileName, extensionHandler);
                		List<String> values = new ArrayList<String>();
                		values.add(bpelFileName);
                		values.add(join(extensionHandler.getExtensionsMustUnderstand(), ';'));
                		values.add(join(extensionHandler.getExtensionsNotMustUnderstand(), ';'));
                		values.add(join(extensionHandler.getExtensionActivities(), ';'));
                		printWithSeparator(out, separator, values);
                	} else {
                		gatherBpelSublanguageStatistics(out,
								bpelFileName, cmd.getOptionValue("reusedir"), separator);
	                }
                	
                	} catch(Exception e) {
                		System.err.println("Error evaluating " + bpelFileName);
                		e.printStackTrace();
                	}
                }
            } catch (ParseException e) {
                System.err.println(e.getLocalizedMessage());
                showHelpAndExit(options);
            } catch (FileNotFoundException e) {
                System.err.println("Problem while writing file: " + e.getMessage());
            } catch (IOException e) {
            	System.err.println("Problem with file: " + e.getMessage());
				e.printStackTrace();
			} finally {
                if(out != System.out) {
                    IOUtils.closeQuietly(out);
                }
            }
    }

	private static String join(Set<String> strings, char c) {
		StringBuilder sb = new StringBuilder();
		for(String s : strings) {
			if(sb.length() > 0) {
				sb.append(c);
			}
			sb.append(s);
		}
		
		return sb.toString();
	}

	private static List<String> getExtensionHeaders() {
		return Arrays.asList("Filename", "MustUnderstandExtensions", "OptionalExtensions", "ExtensionActivities");
	}

	private static void gatherBpelSublanguageStatistics(PrintStream out,
			String bpelFileName, String reuseDir, String separator) {
		try {
			BpelSubLanguageStatsGatherer gatherer = new BpelSubLanguageStatsGatherer();
			File bpelFile = new File(bpelFileName);
			
			String[] reuseDirs;
			if(reuseDir == null || "".equals(reuseDir)) {
				reuseDirs = new String[0];
			} else {
				reuseDirs = reuseDir.split(",");
			}
			List<FileStats> sublanguageResults = gatherer.gather(bpelFile, reuseDirs);
			StringBuilder importFiles = new StringBuilder();
			
			FileStats total = new FileStats();
			for(FileStats fs : sublanguageResults) {
				total.add(fs);
				if(importFiles.length() > 0) {
					importFiles.append(",");
				}
				
				String path = getRelativePath(fs.absoluteFileName.getCanonicalPath(), bpelFile.getParentFile().getCanonicalPath());
				if(path.indexOf("..") >= 0) {
					path = path.substring(path.indexOf(".."));
				}
				importFiles.append(path);
			}
			
			printWithSeparator(
					out,
					separator,
					Arrays.asList(new String[] { bpelFileName,
							"" + total.xpathExpressionsLOCs,
							"" + total.xpathExpressionOccurrences,
							"" + total.xpathQueriesLOCs,
							"" + total.xpathQueryOccurrences,
							"" + (total.xpathExpressionsLOCs+total.xpathQueriesLOCs),
							"" + (total.xpathExpressionOccurrences+total.xpathQueryOccurrences),
							"" + total.xsltExternalLOCs,
							"" + total.xsltReuseLOCs,
							"" + (total.xsltExternalLOCs + total.xsltReuseLOCs),
							"" + total.xquerySimpleExpressionLOCs,
							"" + total.xquerySimpleExpressionOccurrences,
							"" + total.xqueryComplexExpressionLOCs,
							"" + total.xqueryComplexExpressionOccurrences,
							"" + (total.xqueryComplexExpressionLOCs+total.xquerySimpleExpressionLOCs),
							"" + (total.xqueryComplexExpressionOccurrences+total.xquerySimpleExpressionOccurrences),
							"" + total.xquerySimpleQueryLOCs,
							"" + total.xquerySimpleQueryOccurrences,
							"" + total.xqueryComplexQueryLOCs,
							"" + total.xqueryComplexQueryOccurrences,
							"" + (total.xqueryComplexQueryLOCs+total.xquerySimpleQueryLOCs),
							"" + (total.xqueryComplexQueryOccurrences+total.xquerySimpleExpressionOccurrences),
							"" + total.xqueryExternalLOCs,
							"" + total.xqueryReuseLOCs,
							"" + (total.xqueryExternalLOCs + 
									total.xquerySimpleQueryLOCs + 
									total.xqueryComplexQueryLOCs + 
									total.xquerySimpleExpressionLOCs + 
									total.xqueryComplexExpressionLOCs + 
									total.xqueryReuseLOCs),
							"" + total.xqueryComplexity,
							"" + total.xsltComplexity,
							importFiles.toString()
					}));
		} catch(Exception e) {
			e.printStackTrace();
			printWithSeparator(
					out,
					separator,
					Arrays.asList(new String[] { bpelFileName,
							"-",
							"-", 
							"-",
							"-",
							"-",
							"-",
							"-",
							"-",
							"-",
							"-",
							"-",
							"-",
							"-",
							"-",
							"-",
							"-", 
							"-",
							"-",
							"-",
							"-", 
							"-",
							"-",
							"-",
							"-",
							"-" }));
		}
	}

	private static boolean gatherBpelMetrics(PrintStream out,
			boolean bPrintHeader, String bpelFileName, String separator) throws IOException,
			SAXException {
		SAXStatsHandler result = new SAXStatsGatherer().gather(bpelFileName);

		final Map<String, String> fields = getFieldMap(bpelFileName, result);
		if (bPrintHeader) {
			bPrintHeader = false;
			printWithSeparator(out, separator, fields.keySet());
		}
		printWithSeparator(out, separator, fields.values());
		return bPrintHeader;
	}

	private static Iterable<String> getSublanguageHeaders() {
		return Arrays.asList(new String[] { 
				"Filename", 
				"XPath/Expression(LOCs)", 
				"XPath/Expression(Occurrences)", 
				"XPath/Query(LOCs)", 
				"XPath/Query(Occurrences)", 
				"XPath/Total(LOCs)", 
				"XPath/Total(Occurrences)", 
				"XSLT/External", 
				"XSLT/Reuse", 
				"XSLT/Total", 
				"XQuery/Expression/Simple(LOCs)", 
				"XQuery/Expression/Simple(Occurrences)", 
				"XQuery/Expression/Complex(LOCs)", 
				"XQuery/Expression/Complex(Occurrences)", 
				"XQuery/Expression/Total(LOCs)", 
				"XQuery/Expression/Total(Occurrences)", 
				"XQuery/Query/Simple(LOCs)", 
				"XQuery/Query/Simple(Occurrences)", 
				"XQuery/Query/Complex(LOCs)", 
				"XQuery/Query/Complex(Occurrences)", 
				"XQuery/Query/Total(LOCs)", 
				"XQuery/Query/Total(Occurrences)", 
				"XQuery/External(LOCs)", 
				"XQuery/Reuse(LOCs)", 
				"XQuery/Total(LOCs)", 
				"XQuery/Complexity",
				"XSLT/Complexity",
				"Dependencies" } );
	}

	private static Map<String, String> getFieldMap(final String bpelFileName, final BpelStatsFileResult v) {
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
		fields.put("#Wait", "" + v.getCountWait());
		fields.put("#Variable", "" + v.getCountVariable());
		fields.put("#AllActivities", "" + v.getCountAllActivities());
		fields.put("#BasicActivities", "" + v.getCountBasicActivities());
		fields.put("#StructuredActivities", "" + v.getCountStructuredActivities());
		fields.put("#NonlinearActivities", "" + v.getCountNonLinearActivities());
		fields.put("#ExtensionActivities", "" + v.getCountExtensionActivities());
		fields.put("#B4PPeopleActivity", ""+v.getCountB4PPeopleActivity());
		fields.put("#AVBreak", "" + v.getCountActiveVOSBreak());
		fields.put("#AVContinue", "" + v.getCountActiveVOSContinue());
		fields.put("#AVSuspend", "" + v.getCountActiveVOSSuspend());
		fields.put("#AVOnSignal", "" + v.getCountActiveVOSOnSignal());
		fields.put("#AVSignalReceive", "" + v.getCountActiveVOSSignalReceive());
		fields.put("#AVSignalSend", "" + v.getCountActiveVOSSignalSend());
		fields.put("#WPSFlow", "" + v.getCountWPSFlow());

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
		out.println();
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
                .withDescription("Writes statistics as a CSV file.")
                .hasArg().withArgName("FILE").create("c")
            );
            options.addOption(OptionBuilder
                .withDescription("Writes out the table header")
                .create("h"));

            options.addOption(OptionBuilder.withDescription("Gathers sub-language statistics").create("s"));
            
            options.addOption(OptionBuilder.withDescription("Gather BPEL Metrics like described in Juric 2014").create("m"));
            
            options.addOption(OptionBuilder.withDescription("Gather BPEL Extensions and ExtensionActivities").create("e"));
            
            options.addOption(OptionBuilder.withDescription("Directories for re-usable assets (XSLT/XQuery) delimited by comma (,). Only applicable for -s option").withArgName("directory").withLongOpt("reusedir").hasArg(true).create());
            
            options.addOption(OptionBuilder.withDescription("Use a custom delimiter").withArgName("delimiter").hasArg().create("d"));
            
            return options;
    }
    
    public static String getRelativePath(String targetPath, String basePath) throws Exception {

        String[] base = basePath.split(Pattern.quote(File.separator));
        String[] target = targetPath.split(Pattern.quote(File.separator));

        // First get all the common elements. Store them as a string,
        // and also count how many of them there are.
        StringBuffer common = new StringBuffer();

        int commonIndex = 0;
        while (commonIndex < target.length && commonIndex < base.length
                && target[commonIndex].equals(base[commonIndex])) {
            common.append(target[commonIndex] + File.separator);
            commonIndex++;
        }

        if (commonIndex == 0) {
            // No single common path element. This most
            // likely indicates differing drive letters, like C: and D:.
            // These paths cannot be relativized.
            throw new Exception("No common path element found for '" + targetPath + "' and '" + basePath
                    + "'");
        }   

        boolean baseIsFile = true;

        File baseResource = new File(basePath);

        if (baseResource.exists()) {
            baseIsFile = baseResource.isFile();

        } else if (basePath.endsWith(File.separator)) {
            baseIsFile = false;
        }

        StringBuffer relative = new StringBuffer();

        if (base.length != commonIndex) {
            int numDirsUp = baseIsFile ? base.length - commonIndex - 1 : base.length - commonIndex;

            for (int i = 0; i < numDirsUp; i++) {
                relative.append(".." + File.separator);
            }
        }
        relative.append(targetPath.substring(common.length()));
        return relative.toString();
    }
}
