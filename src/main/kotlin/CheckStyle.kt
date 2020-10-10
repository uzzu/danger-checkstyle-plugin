package co.uzzu.danger.plugins.checkstyle

import systems.danger.kotlin.sdk.DangerPlugin
import java.io.File
import java.io.FileNotFoundException

/**
 * The checkstyle report plugin for danger-kotlin
 */
object CheckStyle : DangerPlugin() {

    override val id: String
        get() = this::javaClass.name

    /**
     * **[Optional]** Set your working directory path if needed.
     * @throws IllegalArgumentException basePath must be specified absolute path.
     */
    var basePath: String = System.getProperty("user.dir")
        set(value) {
            field = formattedBasePath(value)
        }

    /**
     * **[Optional]** Determine to use report format. [Inline] comment reporter is used by the default.
     * [Markdown] reporter is available.
     *
     * @sample
     * ```Kotlin
     * // Inline comment
     * Checkstyle.reporter = Inline
     *
     * // Inline comment with specified report method
     * Checkstyle.reporter = Inline { reportMethod = ReportMethod.WARN }
     *
     * // Markdown comment
     * Checkstyle.reporter = Markdown
     *
     * // Markdown comment with specified label
     * Checkstyle.reporter = Markdown { label = "My checkstyle" }
     * ```
     */
    var reporter: ReporterFactory = Inline { reportMethod = ReportMethod.FAIL }

    /**
     * **[Optional]** The list of checkstyle severity to report. A listOf([Severity.ERROR]) is used by the default
     */
    var severities: List<Severity> = listOf(Severity.ERROR)

    /**
     * Report by using specified checkstyle result file.
     *
     * ```Kotlin
     * // Basically use
     * Checkstyle.report("path/to/check_result.xml")
     *
     * // Use with globe matcher
     * Checkstyle.report("glob:**path/to*check_result.xml")
     * ```
     *
     * @param path A path of checkstyle result.
     */
    fun report(path: String) {
        report { this.path = path }
    }

    /**
     * Report by specified configuration.
     *
     * ```Kotlin
     * Checkstyle.report {
     *     path = "path/to/check_result.xml"
     *     //
     * }
     * ```
     */
    fun report(block: Config.() -> Unit) {
        val config = Config(
            basePath = basePath,
            severities = severities,
            reporter = reporter
        )
        block(config)
        run(config)
    }

    private fun run(config: Config) {
        val collector =   FileCollector(config.basePath)
        val parser = Parser(config.basePath, config.severities)
        val reporter = config.reporter.create(context, config.severities)
        val issues = collector.collect(config.path)
            .throwIfNotExists()
            .map { it.readText() }
            .flatMap(parser::parse)
        reporter.report(issues)
    }

    private fun Sequence<File>.throwIfNotExists(): Sequence<File> =
        map {
            if (!it.exists()) {
                throw FileNotFoundException(it.absolutePath)
            }
            return@map it
        }
}
