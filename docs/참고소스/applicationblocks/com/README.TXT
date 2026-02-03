  ===============================================================================
   Genesys Platform SDK Configuration Object Model Application Block 
  ===============================================================================

  The Configuration Object Model Application Block is a reusable production-quality component 
  that provides developers with a consistent and intuitive object model for working 
  with Configuration Server objects. It has been designed using industry best practices
  and provided with source code so it can be used “as is,” extended, or tailored if you need to. 
  Please see the License Agreement for details. 

  To build the project, follow one of the next instructions.

  
  Build with Maven.
  --------------------

  Install parent pom files to the local repo (or upload to your company repository manager, for example Nexus):
   mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=<Platform SDK Folder>\lib\pom\appblocks-bom.pom -DpomFile=<Platform SDK Folder>\lib\pom\appblocks-bom.pom
   mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=<Platform SDK Folder>\lib\pom\protocols-bom.pom -DpomFile=<Platform SDK Folder>\lib\pom\protocols-bom.pom
  
  Install all Platform SDK libraries from <Platform SDK Folder>\lib to maven local repository.
  For example: 
  mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=<Platform SDK Folder>\lib\kvlists.jar -DpomFile=<Platform SDK Folder>\lib\pom\kvlists.pom

  Import Maven project from <Platform SDK Folder>\applicationblocks\com to you IDE.
  Pay attention, that “generated” folder should be added to Source Path in you IDE to avoid errors highlighting. 
  However, during Maven build, “generated” folder will be automatically attached to source path 
  with build-helper-maven-plugin, defined in the pom file. 

  To build the project from command line, run "mvn clean install" command under the 
  <Platform SDK Folder>\applicationblocks\com directory. It will build and install 
  Configuration Object Model Application Block artifact to the local Maven repository.
  