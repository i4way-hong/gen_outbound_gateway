  ===============================================================================
   Genesys Platform SDK Protocol Manager Application Block 
  ===============================================================================

  One of the two main functions of the Platform SDK is to enable your applications 
  to establish and maintain connections with Genesys servers. Genesys recommends that
  you use the Protocol Manager Application Block to manage your connections to Genesys servers
  unless your application clearly requires a different solution.

  The Protocol Manager Application Block is a reusable production-quality component
  that provides unified management of server protocol objects. It takes care of opening
  and closing connections to many different servers, as well as reconfiguration of 
  high availability connections. It has been designed using industry best practices 
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

  Import Maven project from <Platform SDK Folder>\applicationblocks\protocolmanager to you IDE. Or run   
  "mvn clean install" command under the <Platform SDK Folder>\applicationblocks\protocolmanager directory.
  It will build and install Protocol Manager Application Block artifact to the local Maven repository.
  