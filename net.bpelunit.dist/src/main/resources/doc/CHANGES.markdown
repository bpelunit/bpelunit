Changelog
=========

Staged for next version
-----------------------

* _Feature_: Support of parallel activities within a partner by the use of the parallel activity
* _Feature_: Velocity templates are now explicitly forbidden to override predefined variables. Trying to do so will result in an exception being thrown.
* _Feature_: CDATA sections are now supported for the 'template' element, as an alternative to using 'src' for templates that are not well-formed XML.
* _Feature_: Eclipse runner does now allow to make a launch config with selected test cases
* _Feature_: Eclipse tooling supports running a test case from the context menu
* _Feature_: Eclipse runner supports "halt on error" and "halt on failure"
* _Feature_: Activities can have execution dependencies across partner tracks. This way it is possible to defer a send activity until another activity has been finished
* _Feature_: the ActiveBPEL deployer can now take a username and password for deployment. Authentication for composition invocation still needs to be provided using custom HTTP headers in the send activities of the client track ("protocol options").
* _Feature_: Allow WS-HT Partner Tracks without password
* _Feature_: Editor supports deleting WS-HT Complete Task activities 
* _Feature_: Editor supports data sources, suite/test setup sections and Velocity templates
* _Feature_: Support -t option in command-line runner for setting the global time-out (#12)
* _Feature_: Support --haltonerror and --haltonfailure in command-line runner for stopping the test run in case of errors (#18)
* _Feature_: Introduce a memory efficient mode for the command-line runner to support stress tests. This option will break the XML log (#21)
* _Feature_: When using the ActiveBPEL deployment, the new "BPELFileForGeneratingBPR" option can be used to generate the BPR file automatically. It takes the path to the BPEL file, relative from the .bpts file.
* _Feature_: Ability to specify an expected HTTP response code for One-Way operations in order to also test interaction with infrastructure components (which might send non-202 replies).
* _Feature_: Ability to use header processors in one-way activities. This allows, e.g., WSA-headers to be sent with one-way messages.
* _Improvement_: Set a default base URL when creating a new test case in Eclipse tooling
* _Improvement_: Do not allow duplicate test case names when running a test suite
* _Improvement_: Internal clean-ups in framework
* _Improvement_: Upgrade to Jetty 8 (uses Servlet 3.0, same as recent ODE releases)
* _Improvement_: Log test case duration in command-line client
* _Improvement_: Exit with status 2 if test cases have failed in command-line client
* _Architecture_: Old code coverage code is in progress of being replaced with a new implementation
* _Fix_: Add defined WS-HT Tracks to a test case when creating a new one
* _Fix_: Place RPC wrapper in the empty namespace when the soap:body binding element does not use the 'namespace' attribute
* _Fix_: Validate on loading a Test Suite whether condition groups that are used in normal SOAP activities are declared in the test suite (Issue BPELUnit-9)
* _Fix_: Read ID from CompleteHumanTask activities so that dependsOn works also when target activity is triggered by a complete human task
* _Fix_: In case of dependsOn failing test cases might not terminate (Issue BPELUnit-16)
* _Fix_: When using WS-A Header Processor multiple times with send and receive activities, BPELUnit errors can occur (Issue BPELUnit-11)
* _Fix_: When the .bpts is not in the project root, browsing to select a WSDL file would result in incorrect paths (Issue BPELUnit-4)
* _Fix_: WS-HT Client send xsi:type declarations for task output (Issue BPELUnit-22)
* _Fix_: Honour fault=true on two-way async activities (Issue BPELUnit-24)
* _Fix_: Correctly handle UTF-8 characters (Issue BPELUnit-25)

Version 1.6.1
-----------------------

* _Feature_: the 'delay' and 'delayExpression' attributes now accept floating-point values (such as "2.5" or "0.5") in addition to integer values.
* _Feature_: BPELUnit automatically ensures that rpc/literal part accessors are unqualified XML elements, according to WS-I BP 1.1 R2735, even if the .bpts file (incorrectly) qualifies these elements.
* _Fix_: BPELUnit did not wrap rpc/literal responses using the operation name plus the 'Response' suffix suggested by WS-I BP 1.1 and enforced by engines such as Apache ODE.
* _Fix_: BPELUnit previously produced test timeouts by waiting for skipped activities or partner tracks (using the `assume` attribute) to provide replies to requests from the composition. This has now been fixed.
* _Fix_: do not allow for relative paths from the current directory in the `src` attribute of the `<dataSource>` element. Only URLs, absolute paths and relative paths from the BPTS file should be accepted.

Version 1.6.0
-----------------------
* _Change_: BPELUnit uses a different HTTP connection for each sendReceive activity. This ensures sendReceive activities are kept separate from each other and avoids race conditions.
* _Change_: the `<script>` in a test case `<setUp>` block is now run after loading the current row of the data source, so it can access its variables as well.
* _Change_: upgraded Velocity to 1.7 and integrated Velocity Tools 2.0 into the templates.
* _Feature_: `<send>` and `<sendOnly>` accept the new delay attribute, which contains an XPath expression that computes the delay to be used. The XPath expression can use variables in the data source. The old delaySequence attribute is still supported. However, it is forbidden to use delay and delaySequence in the same activity.
* _Feature_: The XPath expression in a `<condition>` can be generated by replacing the fixed `<expression>` element with a Velocity template in a `<template>` element.
* _Feature_: Support for setting HTTP headers on outgoing SOAP/HTTP connections.
* _Feature_: Support for having two WSDLs per partnerlink so that BPEL partner link types can be better imitated.
* _Feature_: Support for condition groups. Allows reuse of conditions across different receives.
* _Feature_: `<data>` and `<template>` in `<send>` can use the `src` attribute to load an XML document or Velocity template from a separate file.
* _Feature_: templates can use the new `printer` predefined context variable to print DOM elements back as XML.

Version 1.5.0
-----------------------
* _Feature_: BPTS files can now include Apache Velocity-based test case templates. This feature is rather large: check `TEMPLATES.markdown` for a full description.
* _Change_: the XML Schema for the BPTS has been slightly relaxed to allow some constraints which were already implicitly accepted.
   * The specific order of the elements inside `<deployment>`, `<put>` and `<condition>` does not matter anymore.
   * Receives with no `<conditions>`s and empty `<partnerTrack>`s are accepted.
* _Change_: Switched to Maven for all builds, finally!
* _Fix_: When using ActiveBPEL, detect deployment errors through ActiveBPEL's deployment summary, in addition to the deployment web service's HTTP response code.
* _Fix_: validate BPTS during loading. Previously, invalid elements were not reported, leading to confusing behaviour for new users who used a text editor to create the BPTS.
* _Fix_: list imported services in the BPTS editor's service picker and let the user select them.
* _Fix_: don't produce a NullPointerException when importing a WSDL whose schema uses a prefix that takes a different value than in the main WSDL file, or that is missing from the main WSDL file.
* _Fix_: when several prefixes refer to the same namespace URL, do not clobber the previous ones by mistake.
* _Fix_: fixed file descriptor leak which crashed BPTS with 600+ test cases, by reusing connection managers in TestCaseRunner.

Version 1.4.0
-----------------------

* _Feature_: Better error reporting for failing test cases and receive conditions.
* _Feature_: Kill lingering processes in ActiveBPEL after each test case and when the test suite is completed.
* _Feature_: Add faultcode and faultstring to the *send* and *receive* activities. These allow the user to control manually the SOAP fault code and SOAP fault to be sent.
* _Feature_: Let the user select from a list in the graphical BPTS editor whether they want to send a regular input/output, a custom fault or one of the declared faults.
* _Feature_: Generate sample messages for RPC/literal operations and faults.
* _Change_: messages for RPC/lit messages can now optionally include their wrapper element in the `<data>` elements of the BPTS.
* _Fix_: Avoid missing namespace nodes by dumping XML as strings using a null XSLT transform instead of internal Sun APIs.
* _Fix_: Show XML Schema parsing errors while using the BPTS editor in the Problems view.
* _Fix_: Make message generation in the BPTS editor work with XSD imports with relative URLs and imported messages which use their WSDL's XML Schema definitions.
* _Fix_: Parse correctly anonymous simple types and nested model groups for generating messages in the BPTS editor. Currently all model groups are processed as if they were `<xsd:sequence>`s.
* _Fix_: Make sure in the BPTS editor that all generated fault messages follow the doc/lit style, according to section 3.6 of the WS-I Basic Profile 1.1.
* _Fix_: the message for the send part in an async receive/send activity is the input for the WS, not its output.

Version 1.3.0
-------------

* _Feature_: Allow activities that are not bound to a SOAP operation.
* _Feature_: Implemented the *wait* activity and added it to the Eclipse editor.
* _Fix_: Avoid stack overflow exception.
* _Fix_: Ensure xmlns:""="" is not added to namespace list.
* _Fix_: Have an error message if the Test Suite PUT Name does not match any BPEL Process name in the deployment.
* _Fix_: Fixed Undeploy Exception for ODE Deployment.
* _Fix_: Fixed charset handling in Eclipse Editor.

Version 1.2.2 (2009/09/25)
--------------------------

* Missing info, sorry!
* _Fix_: Wrong cursor positioning in input fields.

Version 1.2.1 (2009/09/22)
--------------------------

* _Fix_: Message editor does not load message if only one port and one
  operation exists.
* _Fix_: Input fields for text nodes cuts content.

Version 1.2.0 (2009/09/18)
--------------------------

* No information for now, sorry!

Version 1.1
-----------

* No information for now, sorry!

Version 1.0
-----------

* Initial release, available at the [Sourceforge site](http://bpelunit.sourceforge.net).
