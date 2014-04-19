BPELUnit
========

BPELUnit is a unit testing framework for WS-BPEL compositions. It provides facilities for deploying, invoking and undeploying a composition running in a WS-BPEL engine. External services can be replaced with mockups and replies can be delayed at will. Additionally, BPELUnit can derive test cases by reading rows from an external data source and combining it with Apache Velocity templates.

More documentation is available in the directory ``net.bpelunit.dist/src/main/resources/doc``. The rest of this document is dedicated to explaining the build infrastructure available.

Basic structure of the code
---------------------------

In terms of functionality, BPELUnit is divided into:

* The core functionality of the framework:
  * Most of the testing logic is in ``net.bpelunit.framework``.
  * Some utility classes are in ``net.bpelunit.util``.
  * An abstract model of a WS-BPEL composition (useful for extracting information) is available in ``net.bpelunit.model.bpel``.
* A set of interfaces to access the core logic:
  * From the command line: ``net.bpelunit.framework.client.command``.
  * From Ant: ``net.bpelunit.framework.client.ant``.
  * From Maven: ``net.bpelunit.framework.client.maven``.
  * From Eclipse: the projects in ``tycho``.
* A set of external data source types for various file formats (the core library only has support for Apache Velocity ``.vm`` files):
  * CSV: ``net.bpelunit.framework.control.datasource.csv``.
  * XLS and XLSX: ``net.bpelunit.framework.control.datasource.excel``.
  * HTML tables: ``net.bpelunit.framework.control.datasource.html``.
  * ooCalc: ``net.bpelunit.framework.control.datasource.ods``.
* Several additional utilities:
  * A deployer extension for ActiveVOS 9: ``net.bpelunit.framework.control.deploy.activevos9``.
  * The ``net.bpelunit.utils.*`` projects have small command-line tools for:
    * Obtaining stats from ``.bpel`` or ``.bpts`` files.
    * Generating a basic ``.bpts`` file from a ``.bpel`` file.
    * Inlining external data sources into ``.bpts`` files.
    * Extracting test data from a ``.bpts``.
    * Splitting ``.bpts`` files.
* A project for generating the standalone (CLI + Ant) distribution: ``net.bpelunit.dist``.

BPELUnit is built from the command line using Maven and is developed using Eclipse. There are roughly four kinds of projects within BPELUnit:

* The core functionality, the data source types and the ActiveVOS 9 deployer extension are usable as regular Java libraries *and* OSGi bundles. The OSGi metadata is generated with the Apache Felix ``maven-bundle-plugin`` and is made available to Eclipse and Tycho.
* The rest of the projects in the main directory are only usable as plain Java command-line tools. They do not have OSGi metadata.
* The projects in ``tycho`` are Eclipse plug-ins, features and update sites based on the core OSGi bundles and the target platform definition in ``net.bpelunit.eclipse.target``. They can be built from the command line with Tycho as well.
* The projects in ``eclipse-deps`` repackage several dependencies which are not available in well-known public P2 repositories as OSGi bundles, for their use by the ``tycho`` projects. These dependencies are regularly uploaded to http://update.bpelunit.net.

Building with Maven
-------------------

In order to build the code with Maven 3.0 or higher (needed for the Tycho-based projects), you only need to run ``./build-all.sh``. It will build and install into your local Maven repository everything: core projects, Eclipse plugins, the Eclipse feature and update site, and the standalone distribution.

Please note that due to a limitation in Tycho, the core projects and the Eclipse plugins have been split into separate reactors. Also, you *must* install the core projects into your Maven repository before you can build the Tycho-based projects or the ``net.bpelunit.dist`` project.

Note for the maintainer: if you want to deploy a release to the Sonatype OSS server, please use the ``sonatype-deploy`` profile, and make sure you have a GPG keypair which has been published to ``pgp.mit.edu``. You can run the profile like this:

    mvn -P sonatype-deploy

If you experience a StackOverflowException most likely you will need to increase the per thread stack size. To do so use the following command

	set MAVEN_OPTS="-Xss12m" (Windows)
	export MAVEN_OPTS="-Xss12m" (Linux)

Building with Eclipse PDE
-------------------------

BPELUnit is developed using the Eclipse Plug-in Development Environment. To do so, you will still need Maven to bootstrap the environment. Just run ``./prepare-eclipse-projects.sh``from this directory. It will ensure that the dependencies will be available in the ``target/dependency`` directory in each core project, and that the required code and ``MANIFEST.MF`` files are automatically generated.

You should now import the core and ``tycho`` projects into your Eclipse workspace: the ``eclipse-deps`` projects are not needed. Open the ``.target`` file in the ``net.bpelunit.eclipse.target`` and click on "Set as Target Platform". This will tell Eclipse that the plugins should be developed on top of the official BPELUnit target platform definition.

To test them out, right-click on any of the projects and select "Run As > Eclipse Application". This will open a nested Eclipse instance with the BPELUnit plugins running inside it.

*Note*: please avoid using M2Eclipse, as it modifies the original ``pom.xml`` in unwanted ways.
