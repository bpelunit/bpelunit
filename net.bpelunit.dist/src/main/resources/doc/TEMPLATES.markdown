Test case templates in BPELUnit
===============================

BPELUnit can now build the body of the SOAP messages to be sent from the mockups using [Velocity](http://velocity.apache.org) templates. These templates allow the user to easily define many test cases which have the same activities but have different content in their messages. For more information on the Velocity Template Language, check the official [guide](http://velocity.apache.org/engine/devel/developer-guide.html) and [reference](http://velocity.apache.org/engine/devel/vtl-reference-guide.html).

Template variables can also be used to skip partner tracks and activities and inside receive conditions, control delays when sending messages or reuse pieces of previously exchanged messages. See below for more details.

In addition, BPELUnit integrates all the generic tools in Velocity Tools 2.0, using the default configuration and keys listed [here](http://velocity.apache.org/tools/releases/2.0/summary.html). These can be useful if you need to perform more advanced tasks in your templates, such as comparing or manipulating dates, converting to/from strings, and so on. You can also extend the available tools: for details, please check the relevant section near the end of this file.

How to build messages using templates
-------------------------------------

If your template is a valid XML fragment, you can simply replace the usual `<data>` element in the send activity with a `<template>` element. Suppose we had this fragment to start with:

    <send fault="false">
      <data>
        <tac:reverseRequest>
          <tac:lines>
            <tac:line>A</tac:line>
            <tac:line>B</tac:line>
            <tac:line>C</tac:line>
          </tac:lines>
        </tac:reverseRequest>
      </data>
    </send>

Then we could change to a template and build its output using the `#foreach` Velocity command, so the test case is now a *test case template*:

    <send fault="false">
      <template>
        <tac:reverseRequest>
          <tac:lines>
    #foreach($l in ['A', 'B', 'C'])
            <tac:line>$l</tac:line>
    #endfor
          </tac:lines>
        </tac:reverseRequest>
      </template>
    </send>

Now, suppose your template is *not* a valid XML fragment, like this one:

    <template>
    #set( $i = 0 )
      <foo>$i</foo>
    </template>

That `<template>` element would not be parsed correctly. You will need to use one of these options to get it working:

1. Put all VTL commands inside the first child element of `<template>`, so it is valid XML again:

        <template>
          <foo>
        #set( $i = 0 )
          $i
          </foo>
        </template>

2. Put the entire template inside a CDATA section, so it is interpreted as text:

        <template>
          <![CDATA[<foo>
        #set( $i = 0 )
          $i
          </foo>]]>
        </template>

3. Put the template in an external file. This is also useful for reusing the sample template over multiple activities or test cases. Supposing `template.vm` contains:

        #set($i = 0)
        <foo>$i</foo>

   Then you can safely refer to it from the `.bpts` file, like this:

        <template src="template.vm"/>


Template variables
------------------

The Velocity template that produces the SOAP body for an activity has access to several of variables. The following subsections describe what variables are available by default and how can you change and extend them. The subsections follow the order these variables are set: in case there is a name collision, the last variable will replace the previous ones. Please keep this in mind when writing your setup scripts and loading your data sources (see below).

### Predefined test suite variables ###

- `$baseURL` (String): base URL for the simulated URLs.
- `$collections` (java.util.Collections class): utility methods for handling lists and other collections.
- `$putName` (String): name of the process under test.
- `$testCaseCount` (int): number of test cases.
- `$testSuiteName` (String): name of the test suite.

### Custom test suite variables ###

To add a new variable or change the value of an existing variable for all test cases, a `<script>` element with the proper Velocity template needs to be added to the test suite `<setUp>` element. The template has access to the predefined test suite variables and nothing else. It would look like this:

    <testSuite>
      ...
      <setUp>
        ...
        <script>
          <!-- Velocity template -->
        </script>
        ...
      </setUp>
      ...
    </testSuite>

### Predefined test case variables ###

- `$testCaseName` (String): name of the current test case.

### Data source variables ###

If you have set up a data source, it will place some variables into the context at this point. The values will depend on the current "row" of the contents of the data source. Please see the "Working with data sources" section below for more details.

### Custom test case variables ###

To customize the variables for all activities in a specific test case, a `<script>` element with the proper Velocity template must be added to the test case `<setUp>` block. This template has access to all the test suite variables, the predefined test case variables and the variables loaded from the data source. It would look like this:

    <testCase>
      ...
      <setUp>
        ...
        <script>
          <!-- Velocity template -->
        </script>
        ...
      </setUp>
      ...
    </testCase>

Changes done in a test case setup block will _not_ be carried over to the next test case, to keep them isolated from each other and ensure no hidden data dependencies are introduced. All variables that were added or changed in a test case setup block will be removed and reverted in the following test case, even if there is no setup block there.

Setup blocks are _not_ inherited, even if the `basedOn` attribute is used. Please use test suite setup blocks if you want to have the same variables in several test cases (see above).

### Predefined partner track variables ###

- `$partnerTrackName` (String): name of the current partner track.
- `$partnerTrackURL` (String): URL of the current partner track.
- `$request` (org.w3c.dom.Element): SOAP body of the last received message in the current partner track.
- `$partnerTrackReceived` (java.util.List<org.w3c.dom.Element>): list with all SOAP messages received in the current partner track.
- `$partnerTrackSent` (java.util.List<org.w3c.dom.Element>): list with all SOAP messages sent from the current partner track.

### Predefined activity variables ###

- `$xpath` (net.bpelunit.framework.util.control.XPathTool): utility object for running XPath queries on DOM nodes. For more information, please see below.
- `$printer` (net.bpelunit.framework.util.control.XMLPrinterTool): utility object for printing DOM nodes back as XML using its `print(Node)` method.

### Custom activity variables ###

As usual, the template in the send activity can `#set` its own variables and change the existing variables. However, just like with test case setup blocks, all changes will be gone in the next activity, to keep the test structure clean. If you want to share data among several activities, please use a test case setup block (see above).

XPath 1.0 query support
-----------------------

SOAP message templates have access to a variable called `$xpath`, an instance of XPathTool, an utility class for doing XPath queries on DOM nodes. Currently, this class has two methods:

- `List<Node> evaluateAsList(String query, Object item)`, which evaluates a XPath 1.0 `query` using `item` as context and returns its results as a list of `Node`s, which can be used straight away with the `#foreach` Velocity command, for instance.
- `String evaluateAsString(String query, Object item)`, which evaluates a XPath 1.0 `query` using `item` as context and returns its results as a `String`.

Here's an example:

    #foreach($r as $xpath.evaluateAsList("//foo/bar", $request))
    <myitem>$r.getTextContent()</myitem>
    #endfor

All the namespace prefixes which are in scope in the enclosing `<send>` element can be used in the XPath expression.

Working with data sources
-------------------------

Up to this point, we can only produce a single test case from a test case template. To generate several test cases from a single test case template, we need to load a *data source*. Data sources contain a sequence of *rows*. Each row sets the data source variables to a specific combination of values. When combined with a test case template, we will obtain as many test cases as there are rows in the data source.

### Use the data source in the BPTS ###

Data sources can be defined in the test suite `<setUp>` element and/or in the test case `<setUp>` element. Test case data sources take precedence over test suite data sources, replacing them for that test case. Data sources are *not* inherited between test cases.

Data sources can be freely combined with test suite and test case `<script>` elements. Scripts and data sources are interpreted in the order shown in the "Template variables" section. To use them, add a `<dataSource>` element with the right content to a `<setUp>` element, like this:

    <testCase>
      ...
      <setUp>
        ...
        <dataSource type="mytype" [src="externalref"]>
          <!-- zero or more properties -->
          <property name="myname">myvalue</property>
          <!-- either <contents> or the src attribute must be set, but not both -->
          <contents>
            <!-- inline contents -->
          </contents>
        </dataSource>
        ...
      </setUp>
      ...
    </testCase>

There are several things to keep in mind:

- Data sources belong to one of the *types* registered in the extension registry. You can use one of the predefined data source types, or you can implement and register your own. Each of the predefined data source types are described in the "Predefined data source types" section.
- Data sources load their contents either from the text content of their `<contents>` nested element, or from the external reference linked to from the `src` attribute of the `<dataSource>` element. Both cannot be used at the same time. You can use these values in the `src` attribute:
  - Absolute file paths
  - Relative file paths (from the directory with the `.bpts` file)
  - `http://`, `file://` and all other URL types `java.net.URL` can load
- The behaviour of a data source can be customized by setting the right *properties*. Please refer to the documentation for that data source type to obtain a list of the available properties and their accepted values.

### Register a new data source type ###

To use a new data source type, you will need to register it in the extension registry, located in the `conf/extensions.xml` file in a standalone BPELUnit installation. Add the proper `<dataSource>` element inside the main `<extensionRegistry>` element, like this:

    <extensionRegistry>
      ...
      <dataSource type="typename" extensionClass="fully.qualified.class.name"/>
      ...
    </extensionRegistry>

The following section describes the requirements that must be met by the class that implements the new data source type.

### Implement the data source class ###

Data source classes should belong to the `net.bpelunit.framework.control.datasource` package and implement the `net.bpelunit.framework.control.ext.IDataSource` interface, which has the following methods:

- `void close()`, which frees any resources attached to it.
- `String[] getFieldNames()`, which returns an array of Strings with the names for each field in each row. These names will be used for the names of the template variables.
- `int getNumberOfRows()`, which returns the number of rows which have been read from the call to `loadFromStream`.
- `Object getValueFor(String)`, which returns the value for the specified field in this row.
- `void loadFromStream(InputStream)`, which loads the contents of the `InputStream` into the data source.
- `void setRow(int)`, which places the internal cursor of the data source in the n-th row (starting from 0). By default, data sources point to the first row. Will throw an exception if you try to change to a row which is out of bounds.

Data source classes should also be annotated with the `@DataSource` annotation, which has the following attributes:

- `name`, with the long name for the data source (such as "Velocity Data Source").
- `shortName`, with the short name that should be used in the `type` attribute of the `<dataSource>` element.
- `contentTypes`, with a list of all MIME types which should be associated to this class.

Accepted properties are defined using setter methods. For instance, to define the property `iteratedVars`, you should add a `setIteratedVars` method, and annotate it using the `@ConfigurationOption` annotation, which has the following two fields:

- `description`, with a long description of what can be customized with this property.
- `defaultValue`, with a default value to be used in the Eclipse BPTS editor.

Data source classes are expected to load their full contents in memory and validate their contents and properties on their own as soon as possible. However, they do not have access to the contents of the `<dataSource>` XML element. The BPELUnit core automatically calls the methods in the `IDataSource` interface as required. The `InputStream` is created from the contents of the XML element. For more information, refer to the `getStreamForDataSource` methods of the `net.bpelunit.framework.control.datasource.DataSourceUtil` class.

Predefined data sources
-----------------------

### Velocity ###

_Type: "velocity"_

_Properties: "iteratedVars" (required)_

Velocity templates can be used as a data source. Just `#set` some variables with list literals and list their names (separated by spaces) in the `iteratedVars` property, like this:

    <dataSource type="velocity">
      <property name="iteratedVars">lines</property>
      <contents>
        #set($lines = [[], ['A'], ['A','B'], ['A','B','C']])
      </contents>
    </dataSource>

This data source would have 4 rows: the first row would set `$lines` to the empty list, the second row would set `$lines` to a list with one `String` (`'A'`), and so on.

There are some constraints, though:

- The `iteratedVars` is required and must list at least one variable.
- All variables listed separated by spaces in the `iteratedVars` property must contain list literals with the exact same number of elements.
- All variables listed in the `iteratedVars` property must be set somewhere in the template.

Variables which are set in the contents of the data source but are not listed in the `iteratedVars` property will be copied as-is. Suppose we had this data source:

    <dataSource type="velocity">
      <property name="iteratedVars">v w</property>
      <contents>
         #set($v = [1, 2, 3])
         #set($w = [2, 4, 6])
         #set($z = 3)
      </contents>
    </dataSource>

This data source would have 3 rows: the first would have `v = 1` and `w = 2`, the second would have `v = 2` and `w = 4`, and the last one would have `v = 3` and `w = 6`. All three rows would have `z = 3`.

### CSV ###

_Type: "csv"_

_Properties: "separator" (optional: "\t" by default), "headers" (optional)_

This data source type loads plain text files with one row per line. Each row is split in several fields using a specific separator, set in the `separator` property. By default, this separator is the ASCII TAB character, but it could be a comma or a space, for instance.

The property `headers`, if set, will contain a comma-separated list of the names of the variables which will store the values of each field. If it is not set, the names of the variables will be read from the first row.

Here is an example, using both properties:

    <dataSource type="csv">
      <property name="separator">,</property>
      <property name="headers">name,amount</property>
      <contents>
        Fred,100
        Bob,200
      </contents>
    </dataSource>

This data source would have 2 rows, and set two variables: `$name`, and `$amount`. If we wanted to avoid setting the `headers` property, we'd need something like this instead:

    <dataSource type="csv">
      <property name="separator">,</property>
      <contents>
        name,amount
        Fred,100
        Bob,200
      </contents>
    </dataSource>

### Microsoft Excel (.xls and .xlsx) ###

_Type: "excel"_

_Properties: sheet (optional: 0 by default)_

This data source can read data from Microsoft Excel .xls and .xlsx spreadsheets. By default, data is read from the first sheet in the file, but this can be changed through the `sheet` property. It stores the 0-based index of the sheet to be read (0 is the first one, 1 is the second one, and so on). The names of the variables are extracted from the first row of the selected sheet.

Since the BPTS format does not allow for embedding binary files in its `<contents>` element, you will need to refer to an external document using the `src` attribute.

### OpenDocument Spreadsheet (.ods) ###

_Type: "ods"_

_Properties: sheet (optional: 0 by default)_

This data source reads data from OpenDocument Spreadsheet .ods documents, as produced by OpenOffice Calc, among others. Usage is similar to the Excel data source type. By default, data is read from the first sheet, but it can be changed by setting the `sheet` property to the 0-based index of the sheet to be used: 0 for the first one, 1 for the second one, and so one. The names of the variables are also extracted from the first row of the selected stylesheet.

Since the BPTS format does not allow for embedding binary files in its `<contents>` element, you will need to refer to an external document using the `src` attribute.

### HTML ###

_Type: "html"_

_Properties: table (optional: 1 by default)_

This data source reads data from HTML documents. They do not need to be valid XHTML: the parser should handle most cases just fine (please notify us if you have problems). The data source loops over all tables in the HTML webpage and stops at the `table`-th table (`<table>` tag).

By default, `table` is 1, so it parses the first table in the document. If you want to read a different table, set `table` to the 1-based index of the table: 1 for the first one, 2 for the second one, and so on.

Each cell (`<td>` tag) in the first row (`<tr>` tag) is used for the variable names. The rest of the rows are used to set the variables in each test case.

Note that if you want to embed the contents of the data source in the BPTS file, you will most probably have to place its contents inside a CDATA section so it is not parsed as XML. This is especially important if it is not valid XHTML, like here:

    <dataSource type="html">
      <contents>
        <![CDATA[
          <table>
              <th>A
              <th>B
              <th>C</th>
            <tr>
              <td>1
              <td>2
              <td>3
        ]]>
      </contents>
    </dataSource>

Using template variables in receive conditions
----------------------------------------------

XPath expressions in the `<expression>` and `<value>` child elements of each `<condition>` in a receive activity can access the current partner track's template variables (see above for a listing). Template variables are available under the same names: for instance, the name of the current partner track can be accessed with `$partnerTrackName`, just like a regular XPath variable. For consistency, `$request` contains the document element of the incoming SOAP message, but you don't really need it: those XPath queries are run with the SOAP message's document element node as context.

Template variables which contain Velocity list literals (or any Java object which implements the Iterable interface) are transparently converted into regular node lists, so you do not need to do anything special with them. Suppose we had this data source:

    <dataSource type="velocity">
      <property name="iteratedVars">lines</property>
      <contents>
        #set($lines = [[],['A']])
      </contents>
    </dataSource>

In the first row, `$lines` would be mapped to an empty node list. In the second row, `$lines` would be mapped to a node list with a single `<element>` element, with "A" as its text content. Here are some sample XPath queries:

- `count($lines)`, number of lines: 0 for the first row, 1 for the second row.
- `$lines[1]/text()`, text content of the first element. Empty string for the first row (empty nodeset), "A" for the second row.

Please note XPath expressions differ in some important ways from Velocity templates. For instance, `$x.get(0)` (first element of the list in `$x`) in a Velocity template would be `$x[1]` in an XPath expression.

Generating receive conditions using templates
---------------------------------------------

In some cases, it might be useful to generate the XPath expression for the condition using a Velocity template. In that case, the `<expression>` element inside `<condition>` should be replaced with `<template>`. The available variables are the same as when generating messages. Here is an example, which tests that the `i`-th result has the expected value:

    <condition>
      <template<![CDATA[
        true()
        #foreach($line in $lines)
          and //result[$velocityCount] = '$line'
        #end
      ]]></template>
      <value>true()</value>
    </condition>

Using template variables to skip activities
-------------------------------------------

Activities or entire partner tracks can now be skipped if certain XPath expressions evaluate to false. These XPath expressions should be added to the `assume` attribute of the proper partner track or activity element. Partner track and activity assumptions have access to all partner track template variables (see above for a listing).

The `assume` attribute is available in the following elements:

- `<partnerTrack>`
- `<sendReceive>`
- `<receiveSend>`
- `<sendReceiveAsynchronous>`
- `<receiveSendAsynchronous>`
- `<sendOnly>`
- `<receiveOnly>`
- `<wait>`

It is *not* available in:

- `<clientTrack>` (the element itself: activities in the client track can have `assume`)
- `<send>` (inside a two-way activity)
- `<receive>` (inside a two-way activity)

Using template variables to specify delays
------------------------------------------

To delay the sending of a message from a `<send>` or `<sendOnly>` activity, you can specify an XPath expression in the `delay` attribute. This expression will compute the number of seconds BPELUnit should wait before sending the message.

For instance, `<send delay="2">` would always delay the message by 2 seconds, and `<send delay="$x+1">` would delay the message by one more second than the value of the data source variable `x`.

It is forbidden to use both the `delay` and the `delaySequence` attributes in the same activity: BPELUnit will reject the BPTS in that case.

Reusing pieces of previous messages in templates
------------------------------------------------

When testing stateful Web Service compositions, it may be necessary to reuse a piece of a previously received message in another message (e.g. a correlation identifier).

BPELUnit provides the `<dataExtraction>` child element for the `<receiveOnly>`, `<receive>` and `<completeHumanTask>` elements to this effect. The `<dataExtraction>` element should have the following attributes:

* `expression` (mandatory): the XPath expression to be evaluated on the received message to extract the desired information.
* `variable` (mandatory): the name of the Velocity template variable which should store the extracted information.
* `scope` (optional): the scope of the Velocity template variable. Valid values are `testsuite` (available for the entire test suite), `testcase` (only for the current test case), `partnertrack` (only for the current partner track) and `activity` (only for the current activity). By default, it is set to `testcase`.
* `type` (optional): the type of the extracted information. With `string`, the variable specified in `variable` will contain a Java String. With `node`, it will be an `org.w3c.dom.Node` object, and with `nodeset` it will be an `org.w3c.dom.NodeList` object. By default, it is set to `testsuite`.

For instance, here is a `<receiveSend>` activity that extracts a correlation identifier from the `<in>` element of the received message, expecting it to be `MYPROPERTY`, and uses it inside the `<out>` element of the reply:

    <receiveSend service="pin:pingpong" port="pongSOAP" operation="pingpong">
      <receive service="pin:pingpong" fault="false">
        <condition>
          <expression>//in</expression>
          <value>'MYPROPERTY'</value>
        </condition>
        <dataExtraction expression="//in/text()"
                        variable="correlationproperty"
                        scope="testsuite" type="string"/>
      </receive>
      <send service="pin:pingpong" fault="false">
        <template>
          <pin:pingpongResponse>
            <out>$correlationproperty</out>
          </pin:pingpongResponse>
        </template>
      </send>
    </receiveSend>

Expanding data sources back into regular test cases
---------------------------------------------------

In some cases, you might want to "inline" the setup scripts and data sources back into the test cases. This might be useful, for instance, when you notice that some rows should have additional activities and partner tracks and using the assume attribute is not enough. It should also be useful for splitting the test suite into independent test cases which can be cut and joined by automatic tools.

BPELUnit includes an utility which does just this. To run it, add the proper wrapper script to your PATH (`dsexpand.bat` for Windows and `dsexpand.sh` for UNIX-like environments) and execute:

    (dsexpand script) (bpts) > expanded.bpts

The script will create an instance of every test case template for each row in their data sources, removing all data sources and adding setup blocks with the old test suite setup script, the old test case setup script, and variable assignments for that row of the data source. Test cases which do not have data sources are mostly left as-is, except for the setup script, which has the old test suite setup script and the old test case setup script.

Extending Velocity Tools in BPELUnit with additional tools
----------------------------------------------------------

BPELUnit integrates Velocity Tools using the provided `org.apache.velocity.tools.ToolManager` class. In addition to the default tools, Velocity Tools will check these paths by default:

- `tools.xml` at the root of your classpath,
- `tools.properties` at the root of your classpath,
- `tools.xml` at the current directory,
- `tools.properties` at the current directory and
- the path specified by the `org.apache.velocity.tools` system property.

You can integrate additional tools through these paths. For details, please check the [configuration](http://velocity.apache.org/tools/releases/2.0/config.html) and [tool creation](http://velocity.apache.org/tools/releases/2.0/creatingtools.html) sections of the official Velocity Tools documentation.-

Pending tasks
-------------

### Nice ###

- Log template output from setup blocks and Velocity data sources (currently discarded)
