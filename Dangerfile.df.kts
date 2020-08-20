@file:DependsOn("co.uzzu.danger.plugins:checkstyle:0.0.1")

import co.uzzu.danger.plugins.checkstyle.CheckStyle
import co.uzzu.danger.plugins.checkstyle.Markdown
import co.uzzu.danger.plugins.checkstyle.Severity
import systems.danger.kotlin.Danger
import systems.danger.kotlin.register

register plugin CheckStyle

val d = Danger(args)

CheckStyle.severities = listOf(Severity.IGNORE, Severity.INFO, Severity.WARNING, Severity.ERROR)
CheckStyle.reporter = Markdown { label = "ktlint" }
CheckStyle.report("glob:**/build/reports/ktlint/ktlint*Check.xml")
