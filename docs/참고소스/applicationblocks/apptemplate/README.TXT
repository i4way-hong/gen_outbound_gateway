  ===============================================================================
   Genesys Platform SDK Application Template Application Block 
  ===============================================================================

  The Genesys Application Template (Template) application block is a set of software components,
  guides and recommendations how to use the Genesys PSDK components and how to link it together.
  So the template can be considered as a framework for creating applications based on Genesys Platform SDK. 
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

  The folder <Platform SDK Folder>\applicationblocks\apptemplate contains the sources and project files
  for the Genesys Application template.
 
  Import Maven project from <Platform SDK Folder>\applicationblocks\apptemplate to you IDE. Or run   
  "mvn clean install" command under the <Platform SDK Folder>\applicationblocks\apptemplate directory.
  It will build and install Application Template Application Block artifact to the local Maven repository.




  