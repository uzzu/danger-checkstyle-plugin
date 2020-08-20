package co.uzzu.danger.plugins.checkstyle

import systems.danger.kotlin.sdk.DangerContext
import systems.danger.kotlin.sdk.Violation
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Paths

fun main() {
    val checkStyle = CheckStyle

    checkStyle.context = ConsoleDangerContext()
    println("basePath: ${checkStyle.basePath}")
    val fs = FileSystems.getDefault()
    val matcher = fs.getPathMatcher("glob:**/build/reports/ktlint/ktlint*Check.xml")
    Files.walk(Paths.get(checkStyle.basePath)).filter(matcher::matches).forEach {
        checkStyle.report {
            path = it.toString()
        }
    }

    checkStyle.reporter = Markdown
    checkStyle.report("glob:**/build/reports/ktlint/ktlint*Check.xml")
}

private class ConsoleDangerContext : DangerContext {
    override val fails: List<Violation>
        get() = emptyList()
    override val markdowns: List<Violation>
        get() = emptyList()
    override val messages: List<Violation>
        get() = TODO("Not yet implemented")
    override val warnings: List<Violation>
        get() = TODO("Not yet implemented")

    override fun fail(message: String) {
        println("[Fail] $message")
    }

    override fun fail(message: String, file: String, line: Int) {
        println("[Fail] $message. file: $file, line: $line")
    }

    override fun markdown(message: String) {
        println("[Markdown]\n$message\n")
    }

    override fun markdown(message: String, file: String, line: Int) {
        println("[Markdown]\n$message\nfile: $file, line: $line")
    }

    override fun message(message: String) {
        println("[Message] $message")
    }

    override fun message(message: String, file: String, line: Int) {
        println("[Message] $message. file: $file, line: $line")
    }

    override fun suggest(code: String, file: String, line: Int) {
        println("[Suggest]\n```\n$code\n```\nfile: $file, line: $line")
    }

    override fun warn(message: String) {
        println("[Warn] $message")
    }

    override fun warn(message: String, file: String, line: Int) {
        println("[Warn] $message. file: $file, line: $line")
    }
}
