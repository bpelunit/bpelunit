Tycho dependencies
------------------

Most of the libraries required to work on the BPELUnit Eclipse plugins from Eclipse are embedded into the ``framework`` project, as they are easier to manage this way. However, some libraries may need to be split off into separate OSGi bundles in order to avoid conflicts with existing Eclipse bundles.

Each of these dependencies is a separate project in this directory. These dependencies should be available at a p2 repository in http://update.bpelunit.net/eclipse-deps-YYYYMMDDHHmm, where ``YYYYMMDDHHmm`` is the timestamp of the time when the update site was uploaded. The appropriate update site for the current commit is referenced in the target platform definition in the ``tycho/net.bpelunit.eclipse.target`` project.

For this reason, it is *not* necessary to import these projects when simply developing BPELUnit: using that target platform definition is much better, as it will refer to the proper snapshot of the third party dependencies. The only case in which it might be necessary to do so is if http://update.bpelunit.net were unavailable at some point.
