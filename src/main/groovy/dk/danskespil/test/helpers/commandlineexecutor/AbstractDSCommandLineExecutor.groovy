package dk.danskespil.test.helpers.commandlineexecutor

import org.gradle.api.DefaultTask
import org.gradle.api.Project

abstract class AbstractDSCommandLineExecutor {
    Project project

    AbstractDSCommandLineExecutor(Project project) {
        this.project = project
    }

    abstract def executeExecSpec(DefaultTask task, Closure e)
}
