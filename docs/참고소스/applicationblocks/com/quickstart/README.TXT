  ===============================================================================
   Genesys Platform SDK Configuration Object Model Application Block QuickStart Application
  ===============================================================================

  To build the project, follow one of the next instructions.

  Build with Maven.
  --------------------

  Install parent pom files to the local repo (or upload to your company repository manager, for example Nexus):
   mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=<Platform SDK Folder>\lib\pom\appblocks-bom.pom -DpomFile=<Platform SDK Folder>\lib\pom\appblocks-bom.pom
   mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=<Platform SDK Folder>\lib\pom\protocols-bom.pom -DpomFile=<Platform SDK Folder>\lib\pom\protocols-bom.pom
  
  Install all Platform SDK libraries from <Platform SDK Folder>\lib to maven local repository.
  For example: 
  mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=<Platform SDK Folder>\lib\kvlists.jar -DpomFile=<Platform SDK Folder>\lib\pom\kvlists.pom

  Import Maven project from <Platform SDK Folder>\applicationblocks\com\quickstart to you IDE. Or run   
  "mvn clean package" command under the <Platform SDK Folder>\applicationblocks\com\quickstart directory.
  Sample application will be put to the <Platform SDK Folder>\applicationblocks\com\quickstart\target 
  directory.
  
  Run Sample.
  -----------
  
  QuickStart application uses values specified by the quickstart.properties configuration file. 
  Edit file with appropriate values
 
  Change dist to target folder in the quickstart.bat or quickstart.sh in case of Maven builds.

  Run either quickstart.bat or quickstart.sh, depending on your platform.


