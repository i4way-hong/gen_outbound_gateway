  ===============================================================================
   Genesys Platform SDK Cluster Protocol Application Block 
  ===============================================================================

  The Cluster Protocol Application Block provides a kind of HA connection 
  to a cluster of UCS (or similar) servers. It has been designed using industry 
  best practices and provided with source code so it can be used “as is,” extended, 
  or tailored if you need to. Please see the License Agreement for details. 

  To build the project, follow one of the next instructions.
  
  Build with Maven.
  --------------------

  Install parent pom files to the local repo (or upload to your company repository manager, for example Nexus):
   mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=<Platform SDK Folder>\lib\pom\appblocks-bom.pom -DpomFile=<Platform SDK Folder>\lib\pom\appblocks-bom.pom
   mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=<Platform SDK Folder>\lib\pom\protocols-bom.pom -DpomFile=<Platform SDK Folder>\lib\pom\protocols-bom.pom
  
  Install all Platform SDK libraries from <Platform SDK Folder>\lib to maven local repository.
  For example: 
  mvn org.apache.maven.plugins:maven-install-plugin:2.5.2:install-file -Dfile=<Platform SDK Folder>\lib\kvlists.jar -DpomFile=<Platform SDK Folder>\lib\pom\kvlists.pom

  The folder <Platform SDK Folder>\applicationblocks\clusterprotocol contains the sources and project files
  for the Genesys Cluster Protocol.
 
  Import Maven project from <Platform SDK Folder>\applicationblocks\clusterprotocol to you IDE. Or run   
  "mvn clean install" command under the <Platform SDK Folder>\applicationblocks\clusterprotocol directory.
  It will build and install Cluster Protocol Application Block artifact to the local Maven repository.




  