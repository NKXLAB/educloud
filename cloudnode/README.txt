Install notes:


Step by step:

Cloud Server:

  $ wget http://educloud.googlecode.com/files/cloudserver.zip
  $ unzip cloudserver.zip
  $ mkdir /opt/educloud
  $ mv cloudserver /opt/educloud/
  $ groupadd educloud
  $ sudo useradd -d /opt/educloud -M -r -g educloud educloud
  $ chown -R educloud:educloud /opt/educloud
  $ sudo apt-get install nfs-kernel-server
  $ vi /etc/exports
  
    # educloud shared folder config:
    /opt/educloud/cloudserver/storagedir 10.0.2.1/24(rw,sync,no_subtree_check)
    /opt/educloud/cloudserver/templatedir 10.0.2.1/24(rw,sync,no_subtree_check)

  $ /etc/init.d/nfs-kernel-server restart
    
Cloud Node:
  $ wget http://educloud.googlecode.com/files/cloudnode.zip
  $ wget http://download.virtualbox.org/virtualbox/4.0.8/virtualbox-4.0_4.0.8-71778~Ubuntu~maverick_i386.deb
  $ wget http://download.virtualbox.org/virtualbox/4.0.8/Oracle_VM_VirtualBox_Extension_Pack-4.0.8-71778.vbox-extpack
  
  $ sudo dpkg -i virtualbox-4.0_4.0.8-71778~Ubuntu~maverick_i386.deb
  $ vboxmanage extpack install Oracle_VM_VirtualBox_Extension_Pack-4.0.8-71778.vbox-extpack
  $ vboxmanage setproperty websrvauthlibrary null
  $ sudo apt-get install nfs-common
  
  $ unzip cloudnode.zip
  $ sudo mv cloudnode /opt/educloud/
  $ groupadd educloud
  $ sudo useradd -d /opt/educloud -M -r -g educloud educloud
  $ chown -R educloud:educloud /opt/educloud
  $ 
 