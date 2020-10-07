package co.uzzu.danger.plugins.checkstyle

import systems.danger.kotlin.sdk.DangerContext

interface Reporter {
    fun report(issues: Sequence<CheckStyleIssue>)
}

interface ReporterFactory {
    fun create(context: DangerContext, severities: List<Severity>): Reporter
}
