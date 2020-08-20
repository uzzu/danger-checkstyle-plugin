package co.uzzu.danger.plugins.checkstyle

import java.nio.file.Paths

/**
 * Configuration to report checkstyle result.
 * Default values of property are taken from same named property of [Checkstyle] object.
 */
class Config {
    /**
     * **[Optional]** Set your working directory path if needed.
     * @throws IllegalArgumentException basePath must be specified absolute path.
     */
    var basePath: String
        set(value) {
            field = formattedBasePath(value)
        }

    /**
     * **[Optional]** The list of checkstyle severity to report.
     */
    var severities: List<Severity>

    /**
     * **[Optional]** Determine to use report format.
     */
    var reporter: ReporterFactory

    /**
     * **[Required]** A path to checkstyle result file as following:
     * - "path/to/check_result.xml"
     * - "glob:**path/to*check_result.xml"
     */
    lateinit var path: String

    internal constructor(
        basePath: String,
        severities: List<Severity>,
        reporter: ReporterFactory
    ) {
        this.basePath = basePath
        this.severities = severities
        this.reporter = reporter
    }
}

internal fun formattedBasePath(value: String): String {
    val path = if (value.startsWith("~")) {
        Paths.get(value).toAbsolutePath()
    } else {
        Paths.get(value)
    }
    require(path.isAbsolute) { "The basePath should be specified absolute path" }
    return path.toString()
}
