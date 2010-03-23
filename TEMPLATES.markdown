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

Template variables
------------------

The Velocity template that produces the SOAP body for an activity has access to a set of variables. The following subsections describe what variables are available by default and how can the user change and extend them.

### Predefined test suite variables ###

- `$baseURL` (String): base URL for the simulated URLs.
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
