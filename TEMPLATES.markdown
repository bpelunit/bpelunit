Test case templates in BPELUnit
===============================

BPELUnit can now build the body of the SOAP messages to be sent from the mockups using [Velocity](http://velocity.apache.org) templates. These templates allow the user to easily define many test cases which have the same activities but have different content in their messages. For more information on the Velocity Template Language, check the official [guide](http://velocity.apache.org/engine/devel/developer-guide.html) and [reference](http://velocity.apache.org/engine/devel/vtl-reference-guide.html).

How to build messages using templates
-------------------------------------

Simply replace the usual `<data>` element in the send activity with a `<template>` element. Suppose we had this fragment to start with:

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

Then we could change to a template and build its output using the `#foreach` Velocity command:

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

You should place Velocity commands at the start of their own lines. They will still work even if you place them later in a line, but you will get unwanted whitespace in the resulting SOAP message.

*NOTE*: from now on, we will call test cases which use one or more templates *test case templates*.

Template variables
------------------

The Velocity template that produces the SOAP body for an activity has access to several of variables. The following subsections describe what variables are available by default and how can you change and extend them. The subsections are sorted in the order these variables are set: in case there is a name collision, the last variable will replace the previous ones. Please be careful when writing your setup scripts and loading your data sources (see below).

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

### Custom test case variables ###

To customize the variables for all activities in a specific test case, a `<script>` element with the proper Velocity template must be added to the test case `<setUp>` block. This template has access to all the test suite variables and the predefined test case variables. It would look like this:

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

### Data source variables ###

If you have set up a data source, it will place some variables into the context at this point. The values will depend on the current "row" of the contents of the data source. Please see the "Working with data sources" section below for more details.

### Predefined partner track variables ###

- `$partnerTrackName` (String): name of the current partner track.
- `$partnerTrackURL` (String): URL of the current partner track.

### Predefined activity variables ###

- `$request` (org.w3c.dom.Element): SOAP body of the incoming request (for `<receiveSend>` and `<receiveSendOnly>` activities).
- `$xpath` (org.bpelunit.framework.util.control.XPathTool): utility object for running XPath queries on DOM nodes. For more information, please see below.

### Custom activity variables ###

As usual, the template in the send activity can `#set` its own variables and change the existing variables. However, just like with test case setup blocks, all changes will be gone in the next activity, to keep the test structure clean. If you want to share data among several activities, please use a test case setup block (see above).

XPath 1.0 query support
-----------------------

SOAP message templates have access to a variable called `$xpath`, an instance of XPathTool, an utility class for doing XPath queries on DOM nodes. Currently, this class has two methods:

- `List<Node> evaluateAsList(String query, Object item)`, which evaluates a XPath 1.0 `query` using `ìtem` as context and returns its results as a list of `Node`s, which can be used straight away with the `#foreach` Velocity command, for instance.
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

Data source classes should belong to the `org.bpelunit.framework.control.datasource` package and implement the `org.bpelunit.framework.control.ext.IDataSource` interface, which has the following methods:

- `int getNumberOfRows()`, which returns the number of rows it contains.
- `void loadFromStream(InputStream)`, which loads the contents of the `InputStream` into the data source.
- `void initializeContext(Context, int)`, which places the values of the n-th row of the contents of the data source into the specified Velocity template context.
- `void setProperty(String, String)`, which sets the value of a property.

Data source classes are expected to validate their contents and properties on their own. However, they do not have access to the contents of the `<dataSource>` XML element. The BPELUnit core automatically calls the methods in the `IDataSource` interface as required. The `InputStream` is created from the contents of the XML element. For more information, refer to the `getStreamForDataSource` methods of the `org.bpelunit.framework.control.datasource.DataSourceUtil` class.

Predefined data sources
-----------------------

### Velocity ###

_Type: "velocity"_

_Properties: "iterated\_vars" (required)_

Velocity templates can be used as a data source. Just `#set` some variables with list literals and list their names (separated by spaces) in the `iterated_vars` property, like this:

    <dataSource type="velocity">
      <property name="iterated_vars">lines</property>
      <contents>
        #set($lines = [[], ['A'], ['A','B'], ['A','B','C']])
      </contents>
    </dataSource>

This data source would have 4 rows: the first row would set `$lines` to the empty list, the second row would set `$lines` to a list with one `String` (`'A'`), and so on.

There are some constraints, though:

- The `iterated_vars` is required and must list at least one variable.
- All variables listed separated by spaces in the `iterated_vars` property must contain list literals with the exact same number of elements.
- All variables listed in the `iterated_vars` property must be set somewhere in the template.

Variables which are set in the contents of the data source but are not listed in the `iterated_vars` property will be copied as-is. Suppose we had this data source:

    <dataSource type="velocity">
      <property name="iterated_vars">v w</property>
      <contents>
         #set($v = [1, 2, 3])
         #set($w = [2, 4, 6])
         #set($z = 3)
      </contents>
    </dataSource>

This data source would have 3 rows: the first would have `v = 1` and `w = 2`, the second would have `v = 2` and `w = 4`, and the last one would have `v = 3` and `w = 6`. All three rows would have `z = 3`.

Pending tasks
-------------

### Important ###

- Add variables to access previously received and sent messages in the current partner track
- Implement more data source types (CSV, XLS, ODS)

### Nice ###

- Access template variables from XPath expressions in conditions
- Log template output from setup blocks and Velocity data source (currently discarded)

### Not sure yet ###

- Build conditions using templates?
