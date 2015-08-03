# Repository organization #

Listed below are the sub-projects that compose the EduCloud

```
 /
 |
 +--cloudnode - Provide integration with VirtualBox
 +--cloudserver - Manages events in the cloud and provides the management interface of the cloud
 +--cloudserver-gui - web project that provides graphical interface for end user
 +--educloud-api - Java API that provides transparent access to cloudserver
 +--educloud-build - Tool for packaging application
 +--educloud-entities - Entities that are exchanged between cloudserver and cloudnode
 +--educloud-external-entities - Entities that are exchanged between Java API and cloudserver
```