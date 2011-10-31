Condition Groups in BPELUnit
============================

BPELUnit is now able to structure assertions on incoming data better. Originally,
it used a list of <condition>s that are XPath expressions and an expected value.

Whenever a test suite needed to validate lots of common fields, e.g. embedded header data,
these <condition>s needed to be repeated for every <receive>. When the message
schema changed or conditions changed this had a huge impact on the test suite
requiring much manual work.

How to define Condition Groups
------------------------------

Condition Groups are defined at the top level of the test suite file. 

<tes:conditionGroups>
  <tes:conditionGroup>
    <tes:name>cg1</tes:name>
    <tes:condition>
      <tes:expression>count(//exa:result)</tes:expression>
      <tes:value>4</tes:value>
    </tes:condition>
  </tes:conditionGroup>
</tes:conditionGroups>

This snippet defines a condition group named "cg1" that comprises one condition.
However, a condition group can define as many condition as it likes.

To make use of the condition group one has to reference it from a receive block
in a receive asynchronous or any two-way activity like in the following snippet:

<tes:receive fault="false">
  <tes:conditionGroup>cg1</tes:conditionGroup>
</tes:receive>

A <receive> can still define its own <condition>s and can reference as many 
condition groups as needed.
 