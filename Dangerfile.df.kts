@file:DependsOn("co.uzzu.danger.plugins:checkstyle:0.2.0")

import co.uzzu.danger.plugins.checkstyle.*
import systems.danger.kotlin.Danger
import systems.danger.kotlin.register

register plugin CheckStyle

val d = Danger(args)
CheckStyle.severities = listOf(Severity.IGNORE, Severity.INFO, Severity.WARNING, Severity.ERROR)
CheckStyle.reporter = Inline
CheckStyle.report("glob:**/build/reports/ktlint/ktlint*Check.xml")
