@file:DependsOn("co.uzzu.danger.plugins:checkstyle:0.1.0")

import co.uzzu.danger.plugins.checkstyle.*
import systems.danger.kotlin.Danger
import systems.danger.kotlin.register

register plugin CheckStyle

val d = Danger(args)
val isGitHubActionsBuild = System.getenv("GITHUB_WORKSPACE")?.map { true } ?: false

if (isGitHubActionsBuild) {
    CheckStyle.basePath = "/home/runner/work/danger-checkstyle-plugin/danger-checkstyle-plugin"
}

CheckStyle.severities = listOf(Severity.IGNORE, Severity.INFO, Severity.WARNING, Severity.ERROR)
CheckStyle.reporter = Inline
CheckStyle.report("glob:**/build/reports/ktlint/ktlint*Check.xml")
