package co.uzzu.danger.plugins.checkstyle

/**
 * Entity class for checkstyle issue reports
 */
data class CheckStyleIssue(
    val filename: String,
    val line: Int,
    val column: Int?,
    val severity: Severity,
    val message: String,
    val source: String
)
