package co.uzzu.danger.plugins.checkstyle

import systems.danger.kotlin.sdk.DangerContext

object Inline : ReporterFactory {
    override fun create(context: DangerContext, severities: List<Severity>): Reporter =
        InlineReporter(context, InlineReporterConfig())
}

@Suppress("FunctionName")
fun Inline(block: InlineReporterConfig.() -> Unit): ReporterFactory =
    object : ReporterFactory {
        override fun create(context: DangerContext, severities: List<Severity>): Reporter =
            InlineReporter(context, InlineReporterConfig().apply(block))
    }

class InlineReporterConfig {
    var reportMethod: ReportMethod = ReportMethod.FAIL
}

private class InlineReporter(
    private val context: DangerContext,
    reporterConfig: InlineReporterConfig
) : Reporter {

    private val reportFunction: (CheckStyleIssue) -> Unit

    init {
        reportFunction = when (reporterConfig.reportMethod) {
            ReportMethod.MESSAGE -> this::message
            ReportMethod.WARN -> this::warn
            ReportMethod.FAIL -> this::fail
        }
    }

    override fun report(issues: Sequence<CheckStyleIssue>) {
        issues.forEach(reportFunction)
    }

    private fun message(issue: CheckStyleIssue) {
        context.message(message = issue.message, file = issue.filename, line = issue.line)
    }

    private fun warn(issue: CheckStyleIssue) {
        context.warn(message = issue.message, file = issue.filename, line = issue.line)
    }

    private fun fail(issue: CheckStyleIssue) {
        context.fail(message = issue.message, file = issue.filename, line = issue.line)
    }
}
