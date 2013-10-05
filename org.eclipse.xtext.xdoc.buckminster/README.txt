The projects-platform.cquery is used to materialize the target platform and the
projects in the workspace.

You can create a local eclipse mirror of the target platform using 
the b3 aggregator on a b3aggr file in the directrory aggregator; 

The buckminster-mirror.properties maps
the URLs for eclipse repositories (used in the RMAP) to the
local mirror using b3 and the b3aggr file in the aggregator directory.

build.ant can be used to run an headless build. 

You can also pass additional properties as -D<key>=<value>, for instance, if you
want to use the local aggregated mirror, you must pass

-Dbuild.properties=file:/path/to/buckminster-local.properties

Happy building with Buckminster! :)