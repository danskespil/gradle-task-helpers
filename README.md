Build Status master branch [![Build Status master branch](https://travis-ci.org/jwermuth/gradle-task-helpers.svg?branch=master)](https://travis-ci.org/jwermuth/gradle-task-helpers)

# What is this repository for
It helps you testing gradle plugins.

We are using it at [Danske Spil](https://danskespil.dk/) and [Lund&Bendsen](https://home.lundogbendsen.dk/), perhaps you
can also benefit from it.

Does this sound familiar :

I want to test the plugin i am writing. In particular, I want to mock it, so I do not _actually_ perform the actions when 
 testing. Its not easy, because the test is run in a separate VM, so anyhing I set in a 
static var, a factory or other code based state is not present when the test is run.

If so, this jar can help you. It gives you a base class that you can extend to build a mock-able gradle 'service', 
e.g. a service that commits changes to you code (which you do not want to do when testing the plugin)

# How does it work

First, you have to set up your test, so it enables the mocking. Here, this is done by creating a base class for 
testing (this is a spock test, called a Specification), that you can extend to make your actual tests

```groovy
package YOUR.PACKAGE

import dk.danskespil.gradle.task.helpers.GradleServiceExecuteOnOSFactory
import spock.lang.Specification

class BaseSpecification extends Specification {
    def setup() {
        setupTestStubsByAddingMarkerFilesToAConventionNamedDirectory()
    }

    void setupTestStubsByAddingMarkerFilesToAConventionNamedDirectory() {
-S->    GradleServiceExecuteOnOSFactory.instance.enableStub()
    }
}
```
at ```-S->``` you tell all your tests not to actually run anything when using GradleServiceExecuteOnOSFactory

Then, you create a custom groovy task, extending default task. Here is an example of a task that executes git 
 on your platform 
```groovy
class GitCommand extends DefaultTask {
    @Internal
    CommandLine commandLine = new CommandLine()
    @Internal
    GradleServiceExecuteOnOS executor = GradleServiceExecuteOnOSFactory.instance.createService(project)
    @Input
    String commandsFlagsAndArgs = ""

    @TaskAction
    action() {
        commandLine.addToEnd('git')
        commandLine.addToEnd(commandsFlagsAndArgs)
-A->        executor.executeExecSpec(this, { ExecSpec e ->
            e.commandLine this.commandLine
        })
    }
}
```
The magic happens at ```-A->``` where the ```executor``` is used to actually run the command. When under test, as given in this
example

```groovy
class GitCommandTest extends BaseSpecification {
    def "can create task"() {
        given:
        buildFile << """
         plugins {
                id 'dk.lundogbendsen.gradle.plugins.auditwithgit'
         }
         
         task cut(type:dk.lundogbendsen.gradle.plugins.auditwithgit.commands.GitCommand) 
"""
        when:
        def build = buildWithTasks(':cut')

        then:
        build
        build.task(':cut').outcome == TaskOutcome.SUCCESS
        build.output.contains('git')
    }
```
the GitCommand is not actually run, because you set  GradleServiceExecuteOnOSFactory to be stubbed at 
```-S->```.

#Example project
https://github.com/jwermuth/gradle-plugin-terraform


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
  * ./gradlew clean build generatePomFileForMyPublicationPublication bintrayUpload
