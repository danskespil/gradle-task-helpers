Build Status master branch [![Build Status master branch](https://travis-ci.org/jwermuth/gradle-task-helpers.svg?branch=master)](https://travis-ci.org/jwermuth/gradle-task-helpers)

** This repository is not fully operational yet. If you use the watch feature I will notify once its operational **

# Whats this for
It helps us at Danske Spil and Lund&Bendsen to test our gradle plugins.

Does this sound familiar :

I want to test the plugin i am writing. In particular, I want to mock it, so I do not _actually_ perform the actions when 
 testing. Its not easy, because the test is run in a separate VM, so anyhing I set in a 
static var, a factory or other code based state is not present when the test is run.

If so, this jar can help you. It gives you a base class that you can extend to build a mock-able gradle 'service', 
e.g. a service that commits changes to you code (which you do not want to do when testing the plugin)

# Releasing and publishing
This is just for the maintainer (me, for the moment).

* Release, following ajoberstar plugin https://github.com/ajoberstar/gradle-git/wiki/Release%20Plugins
  * ./gradlew clean release -Prelease.scope=major_minor_OR_patch -Prelease.stage=final_OR_rc_OR_milestone_OR_dev
  * e.g. ./gradlew clean release -Prelease.scope=patch -Prelease.stage=final
  * ./gradlew clean release # snapshot version
  * ./gradlew clean release -Prelease.scope=patch -Prelease.stage=dev # e.g. fiddling with readme
* Deployment to bintray.
  * Provide credentials. I have added mine to ~/.gradle/gradle.properties:
  ** bintrayUser=USER
  ** bintrayApiKey=KEY
  * ./gradlew clean bintrayUpload
  * ./gradlew clean generatePomFileForMyPublicationPublication bintrayUpload
