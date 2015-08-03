# Development Tools #

  * Java IDE: eg [Eclipse](http://www.eclipse.org/downloads/packages/eclipse-ide-java-ee-developers/heliossr2)
  * [VirtualBox 4.0.4](http://www.virtualbox.org/wiki/Downloads)
  * [JDK 1.6](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
  * [SVN Client](http://tortoisesvn.net/downloads.html)

# Setup projects #

  1. Download source code from https://educloud.googlecode.com/svn/trunk/
  1. Import projects from java IDE. For eclipse users: import -> existing projects into workspace -> select root diretory -> educloud -> select all projects -> **uncheck Copy Projects into workspace** -> finish

see also: [Repository Organization](http://code.google.com/p/educloud/wiki/RepositoryOrganization)

VirtualBox 4.0.4 Oracle VM VirtualBox Extension Pack [All platforms](http://download.virtualbox.org/virtualbox/4.0.4/Oracle_VM_VirtualBox_Extension_Pack-4.0.4-70112.vbox-extpack)


# Run configs #

Name: Start Node
Project: cloudnode
Main-Class: com.google.educloud.cloudnode.App
VM arguments: -Dlog4j.configuration=conf/log4j.properties
Classpath: Add user entries->advanced->ok

Name: Start Node
Project: cloudserver
Main-Class: com.google.educloud.cloudserver.App
VM arguments: -Dlog4j.configuration=conf/log4j.properties
Classpath: Add user entries->advanced->ok