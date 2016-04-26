# Description #

D42Rundesk Resource plugin (D42 Rundesk Nodes plugin) allows to collect nodesets from the Device42 instance and set them up as the node sets for the particular projects.
You can use Device42 filtering option to limit the amount of devices collected for the project.
If you wish to use smart filters for the specific jobs feel free to use the default filtering mechanizm of the Rundesk - all collected Device42 attributes are stored as Node properties, tags or attributes.

# Download #

For your convenience, compiled jar file is located at: http://www.device42.com/integrations/rundeck/. You can build per the instructions below, or use the pre-compiled jar file.

# Build system #

The plugin uses gradle build system. To build the plugin execute "gradlew" application
For Windows run `gradlew.bat`

For Linux run `gradlew`

After the build completes, the compiled jar file can be found in the build/libs sub-directory under the plugin directory gradlew was executed from.
The pom file is irrelative to the build, maven cannot be used to build the plugin in this version.

Gradle uses the smart versioning system. If you want to change the version - check [Axion Release plugin documentation](http://axion-release-plugin.readthedocs.org/en/latest/) for details.

# Installation #

Installing the plugin is as easy as copying the .jar file to your Rundeck plugins folder. On Ubuntu, the default directory for this is /etc/rundeck/libext. 
Alternatively you can build the plugin using Gradle by running the gradlew script included in the repository.

# Documentation #

Coming soon at http://docs.device42.com

# Support #
Please reach out to us at support at device42.com or open a support ticket from [device42 support page](https://support.device42.com)