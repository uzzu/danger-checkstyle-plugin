package co.uzzu.danger.plugins.checkstyle

import systems.danger.kotlin.sdk.DangerContext

object Markdown : ReporterFactory {
    override fun create(context: DangerContext, severities: List<Severity>): Reporter =
        MarkdownReporter(context, severities, MarkdownReporterConfig())
}

@Suppress("FunctionName")
fun Markdown(block: MarkdownReporterConfig.() -> Unit): ReporterFactory =
    object : ReporterFactory {
        override fun create(context: DangerContext, severities: List<Severity>): Reporter =
            MarkdownReporter(context, severities, MarkdownReporterConfig().apply(block))
    }

class MarkdownReporterConfig {
    var label: String = "Checkstyle"
}

private class MarkdownReporter(
    private val context: DangerContext,
    private val severities: List<Severity>,
    private val config: MarkdownReporterConfig
) : Reporter {
    override fun report(issues: Sequence<CheckStyleIssue>) {
        val map = issues.groupBy { it.severity }

        val ignore = map[Severity.IGNORE] ?: emptyList()
        val info = map[Severity.INFO] ?: emptyList()
        val warning = map[Severity.WARNING] ?: emptyList()
        val error = map[Severity.ERROR] ?: emptyList()

        reportSummary(ignore = ignore, info = info, warning = warning, error = error)
        reportDetailsWithMarkdown("Warning:", Severity.WARNING, warning)
        reportDetailsWithMarkdown("Error:", Severity.ERROR, error)
    }

    private fun reportSummary(
        ignore: List<CheckStyleIssue>,
        info: List<CheckStyleIssue>,
        warning: List<CheckStyleIssue>,
        error: List<CheckStyleIssue>
    ) {
        val ignoreCount = ignore.count()
        val ignoreSummary = ignoreCount.takeIf { severities.contains(Severity.IGNORE) }?.let { "ignore: $it" } ?: ""
        val infoCount = info.count()
        val infoSummary = infoCount.takeIf { severities.contains(Severity.INFO) }?.let { "info: $it" } ?: ""
        val warningCount = warning.count()
        val warningSummary = warningCount.takeIf { severities.contains(Severity.WARNING) }?.let { "warning: $it" } ?: ""
        val errorCount = error.count()
        val errorSummary = errorCount.takeIf { severities.contains(Severity.ERROR) }?.let { "error: $it" } ?: ""

        val summaryMessage = StringBuilder()
            .apply {
                append("[${config.label}] ")
                val body = listOf(ignoreSummary, infoSummary, warningSummary, errorSummary)
                    .filter { it.isNotBlank() }
                    .joinToString(separator = ", ", prefix = "Reported ")
                    .takeIf { it.isNotBlank() }
                    ?: "No issues found"
                append(body)
            }
            .toString()

        when {
            severities.contains(Severity.ERROR) && errorCount > 0 -> context.fail(summaryMessage)
            severities.contains(Severity.WARNING) && warningCount > 0 -> context.warn(summaryMessage)
            else -> context.message(summaryMessage)
        }
    }

    private fun reportDetailsWithMarkdown(title: String, severity: Severity, issues: List<CheckStyleIssue>) {
        issues.takeIf { severities.contains(severity) && issues.isNotEmpty() }?.let {
            context.markdown("### $title\n\n${tableContent(it)}")
        }
    }

    private fun tableContent(issues: List<CheckStyleIssue>): String = StringBuilder()
        .apply {
            appendLine("| File | Message |")
            appendLine("| --- | --- |")
            issues
                .map { "| ${it.filename}#L${it.line}${it.column?.let { c -> ":$c" } ?: ""} | ${it.message} (${it.source}) |" }
                .forEach { appendLine(it) }
        }
        .toString()
}
