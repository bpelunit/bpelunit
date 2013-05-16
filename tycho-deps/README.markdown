Tycho dependencies
------------------

Most of the libraries required to work on the BPELUnit Eclipse plugins from Eclipse are embedded into the ``framework`` project, as they are easier to manage this way. However, some libraries may need to be split off into separate OSGi bundles in order to avoid conflicts with existing Eclipse bundles:

Each of these dependencies is a separate module in this directory. These dependencies should be available at the p2 repository below (which is referenced from the Tycho parent POM):

    https://neptuno.uca.es/nexus/content/repositories/thirdparty/.meta/p2/

Nevertheless, if you find that this repository is unavailable, you could simply install these bundles into your local Maven repository by going into each folder and running ``mvn install``. The ``prepare-eclipse-projects.sh`` script should have already done that for you.

The dependencies we use are listed below:

- net.sf.wsdl4j is WSDL4J 1.6.2. We do not need it from Tycho, as the Eclipse Juno repository already has an OSGi bundle for it. We only need it when working for Eclipse: importing the project is fine.
