Test Data Externalizer
======================

Introduction
------------

Since BPELUnit 1.6 it is possible to reference XML data for service calls and 
responses from external files. This helps the reuse of test data and therefore
increases maintainability of the test suites.

The Test Data Externalizer tool extracts the XML data from a given bpts file
and writes it into separate XML files. It detects same XML messages will will
reuse the XML file if possible.
  
Syntax
------

testdataexternalizer [-B] [-d dir] file.bpts...

-B Does not create backup copies of the changed files
-d dir Writes the XML data files to the directory dir

Examples
--------

testdataexternalizer suite.bpts

Will change suite1.bpts, write all data files to the same directory, and will 
create a backup file suite.bpts.old.

testdataexternalizer suite1.bpts suite2.bpts

Will change both suite1.bpts and suite2.bpts, and creates two backup files 
suite1.bpts.old and suite2.bpts.old. If same XML data is used for operations
in both files, only one XML file is created and both suites will reference it.

testdataexternalizer -B -d suite1 suite1.bpts

Will change suite.bpts without creating a backup and write all data files to the directory suite1.

