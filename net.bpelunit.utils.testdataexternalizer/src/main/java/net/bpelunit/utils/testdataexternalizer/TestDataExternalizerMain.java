package net.bpelunit.utils.testdataexternalizer;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import net.bpelunit.framework.xml.suite.XMLTestSuiteDocument;
import net.bpelunit.util.Console;
import net.bpelunit.utils.testdataexternalizer.io.FileSystemFileWriter;
import net.bpelunit.utils.testdataexternalizer.io.IFileWriter.FileAlreadyExistsException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class TestDataExternalizerMain {

	private static final String PARAMETER_NOBACKUP = "B"; //$NON-NLS-1$
	private static final String PARAMETER_DATAFILEDIRECTORY = "d"; //$NON-NLS-1$

	private Console console;
	private PrintWriter screen;
	private Options options;

	private String dataFileDirectory;
	private boolean makeBackup = true;
	private List<File> testSuiteFiles = new ArrayList<File>();

	public TestDataExternalizerMain(String[] args) {
		this(new Console(), args);
	}

	public TestDataExternalizerMain(Console consoleToUse, String[] args) {
		this.console = consoleToUse;
		this.screen = console.getScreen();

		this.createOptions();
		this.parseOptionsFromCommandLine(args);
	}

	@SuppressWarnings("static-access")
	private final void createOptions() {
		options = new Options();

		options.addOption(
				PARAMETER_NOBACKUP,
				false,
				Messages.getString("TestDataExternalizerMain.PARAMETER_DESCRIPTION_NOBACKUP")); //$NON-NLS-1$
		options.addOption(OptionBuilder
				.withDescription(
						Messages.getString("TestDataExternalizerMain.PARAMETER_DESCRIPTION_DATAFILEDIRECTORY")) //$NON-NLS-1$
				.hasArg().withArgName("FILE")
				.create(PARAMETER_DATAFILEDIRECTORY));
	}

	@SuppressWarnings("unchecked")
	private final void parseOptionsFromCommandLine(String[] args) {

		CommandLineParser parser = new PosixParser();
		try {
			CommandLine cmd = parser.parse(options, args);

			makeBackup = !cmd.hasOption(PARAMETER_NOBACKUP);
			dataFileDirectory = trimEqualsSignFromStart(cmd
					.getOptionValue(PARAMETER_DATAFILEDIRECTORY));

			ArrayList<String> remainingOptions = new ArrayList<String>(
					cmd.getArgList());
			for (String fileName : remainingOptions) {
				addAndValidateTestSuiteFileName(fileName);
			}
		} catch (ParseException e) {
			showHelpAndExit();
		}
	}

	private void addAndValidateTestSuiteFileName(String testSuiteFileName) {
		File testSuiteFile = new File(testSuiteFileName);
		if (!testSuiteFile.exists()) {
			abort(String
					.format(Messages
							.getString("TestDataExternalizerMain.MSG_ERR_TESTSUITE_FILE_NOT_EXISTING"),
							testSuiteFile)); //$NON-NLS-1$
		}
		testSuiteFiles.add(testSuiteFile);
	}

	private void abort(String message) {
		abort(message, null);
	}

	private void abort(String message, Exception e) {
		screen.println(message);
		if (e != null) {
			screen.println(Messages
					.getString("TestDataExternalizerMain.MESSAGE_DESCRIPTION") + e.getMessage()); //$NON-NLS-1$
		}
		console.exit(1);
	}

	private void showHelpAndExit() {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(
				Messages.getString("TestDataExternalizerMain.MSG_HELP_USAGE"), //$NON-NLS-1$
				options);
		console.exit(1);
	}

	void run() {
		String bpelUnitRunner = Messages
				.getString("TestDataExternalizerMain.MSG_TITLE_BPELUNIT_DATAEXTERNALIZER"); //$NON-NLS-1$
		screen.println(bpelUnitRunner);
		screen.println(StringUtils.repeat("-", bpelUnitRunner.length())); //$NON-NLS-1$
		screen.println();

		makeBackupOfTestSuitesIfNecessary();
		TestDataExternalizer tde = new TestDataExternalizer(
				this.dataFileDirectory);

		List<XMLTestSuiteDocument> testSuiteDocuments = processTestSuites(tde);
		writeMessageData(tde);
		saveUpdatedTestSuites(testSuiteDocuments, testSuiteFiles);
		screen.println("Done.");
	}

	private void saveUpdatedTestSuites(
			List<XMLTestSuiteDocument> testSuiteDocuments,
			List<File> testSuiteFiles) {
		for (int i = 0; i < testSuiteDocuments.size(); i++) {
			XMLTestSuiteDocument testSuiteDocument = testSuiteDocuments.get(i);
			File testSuiteFile = testSuiteFiles.get(i);
			screen.println(String.format("Updating test suite %s...",
					testSuiteFile.getName()));
			try {
				testSuiteDocument.save(testSuiteFile);
			} catch (IOException e) {
				abort("Error writing test suite file: " + e.getMessage(), e);
			}
		}
	}

	private void writeMessageData(TestDataExternalizer tde) {
		screen.println("Extracting XML send messages...");
		try {
			tde.externalize(new FileSystemFileWriter(testSuiteFiles.get(0)
					.getParentFile()));
		} catch (FileAlreadyExistsException e) {
			abort("Error extracting XML messages because a file already exists: "
					+ e.getMessage(), e);
		}
	}

	private List<XMLTestSuiteDocument> processTestSuites(
			TestDataExternalizer tde) {
		List<XMLTestSuiteDocument> testSuiteDocuments = new ArrayList<XMLTestSuiteDocument>();
		for (File f : testSuiteFiles) {
			screen.println(String.format("Reading %s...", f.getName()));
			XMLTestSuiteDocument testSuiteDocument = null;
			try {
				testSuiteDocument = XMLTestSuiteDocument.Factory.parse(f);
				tde.replaceContentsWithSrc(testSuiteDocument);
				testSuiteDocuments.add(testSuiteDocument);
			} catch (Exception e) {
				abort("Error while reading test suite file: " + e.getMessage(),
						e);
			}
		}
		return testSuiteDocuments;
	}

	private void makeBackupOfTestSuitesIfNecessary() {
		if (makeBackup) {
			for (File testSuiteFile : testSuiteFiles) {
				String backupFileName = testSuiteFile.getAbsolutePath()
						+ ".old";
				screen.println(String.format(
						"Making backup of testsuite file (%s)...",
						backupFileName));
				try {
					FileUtils.copyFile(testSuiteFile, new File(backupFileName));
				} catch (IOException e) {
					abort("Error making backup copy", e);
				}
			}
		}
	}

	private String trimEqualsSignFromStart(String optionValue) {
		if (optionValue == null) {
			return null;
		}

		if (!optionValue.startsWith("=")) { //$NON-NLS-1$
			return optionValue;
		}

		return optionValue.substring(1);
	}

	public static void main(String[] args) {
		new TestDataExternalizerMain(args).run();
	}

}
