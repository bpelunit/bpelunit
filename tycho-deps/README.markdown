Tycho dependencies
------------------

Most of the libraries required by the BPELUnit Eclipse plugins are embedded into the ``framework`` project, as they are easier to manage this way. However, some libraries may need to be split off into separate OSGi bundles in order to avoid conflicts with existing Eclipse bundles.

Each of these dependencies is a separate module in this directory. These dependencies should be available at the p2 repository below (which is referenced from the Tycho parent POM):

    https://neptuno.uca.es/nexus/content/repositories/thirdparty/.meta/p2/

Nevertheless, if you find that this repository is unavailable, you could simply install these bundles into your local Maven repository by going into each folder and running ``mvn install``. The ``prepare-eclipse-projects.sh`` script should have already done that for you.
