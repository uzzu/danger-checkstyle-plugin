@file:DependsOn("co.uzzu.danger.plugins:checkstyle:0.1.0")
@file:DependsOn("co.uzzu.strikts:strikts:0.2.0")

import co.uzzu.strikts.*
import co.uzzu.danger.plugins.checkstyle.*
import systems.danger.kotlin.Danger
import systems.danger.kotlin.register

register plugin CheckStyle

val d = Danger(args)
val isGitHubActionsBuild = System.getenv("GITHUB_WORKSPACE")?.run { true } ?: false

if (isGitHubActionsBuild) {
    "**/build/reports/ktlint/ktlint*Check.xml".glob(".")
        .map { it.toFile() }
        .filter { !it.isDirectory }
        .forEach {
            println(it.absolutePath)
        }
    println("runner workspace: ${System.getenv()["RUNNER_WORKSPACE"]}")
    println("github workspace: ${System.getenv()["GITHUB_WORKSPACE"]}")
}

CheckStyle.severities = listOf(Severity.IGNORE, Severity.INFO, Severity.WARNING, Severity.ERROR)
CheckStyle.reporter = Inline
CheckStyle.report("glob:**/build/reports/ktlint/ktlint*Check.xml")
