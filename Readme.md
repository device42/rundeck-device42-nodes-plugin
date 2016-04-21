# Description #

D42Rundesk Resource plugin (D42 Rundesk Nodes plugin) allows to collect nodesets from the Device42 instance and set them up as the node sets for the particular projects.
You can use Device42 filtering option to limit the amount of devices collected for the project.
If you wish to use smart filters for the specific jobs feel free to use the default filtering mechanizm of the Rundesk - all collected Device42 attributes are stored as Node properties, tags or attributes.

=====================================

# Build system #

The plugin uses gradle build system. To build the plugin execute "gradlew" application
For Windows run `gradlew.bat`

For Linux run `gradlew`

The pom file is irrelative to the build, maven cannot be used to build the plugin in this version.

Gradle uses the smart versioning system. If you want to change the version - check [Axion Release plugin documentation](http://axion-release-plugin.readthedocs.org/en/latest/) for details.