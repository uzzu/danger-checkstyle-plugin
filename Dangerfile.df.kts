@file:DependsOn("co.uzzu.danger.plugins:checkstyle:0.1.0")
@file:DependsOn("co.uzzu.strikts:strikts:0.2.0")

import co.uzzu.strikts.*
import co.uzzu.danger.plugins.checkstyle.*
import systems.danger.kotlin.Danger
import systems.danger.kotlin.register

register plugin CheckStyle

val d = Danger(args)

println(System.getProperty("user.dir"))
"**/build/reports/ktlint/ktlint*Check.xml".glob(".")
    .map { it.toFile() }
    .filter { !it.isDirectory }
    .forEach {
        println(it.readText())
    }
val env = DotEnv()
val GITHUB_WORKSPACE by env.orElse("not set")
println("workspace: $GITHUB_WORKSPACE")
println("hoge: ${System.getProperty("user.home")}")

fun ensureConvertingFilename(parent: String, basePath: String): String =
    if (!basePath.startsWith("/github/workspace")) {
        parent.replace(Regex("^$basePath/"), "")
    } else {
        try {
            findGitDirectory(Paths.get(parent))
        } catch (e: IllegalStateException) {
            throw IllegalStateException("Could not find report target file. $parent")
        }
    }

fun findGitDirectory(path: Path): String {
    check(path.toString() == "/")
    val file = path.toFile()
    file.isRooted
    if (!file.isDirectory) {
        return findGitDirectory(path.parent)
    }
    val gitFilePath = Paths.get(path.toString(), ".git")
    println(gitFilePath.toString())
    val gitDirectory = gitFilePath.toFile()
    if (!gitDirectory.exists() || !gitDirectory.isDirectory) {
        return findGitDirectory(path.parent)
    }
    return path.toString()
}


val filepath = "/home/runner/work/danger-checkstyle-plugin/danger-checkstyle-plugin/src/main/kotlin/CheckStyle.kt"

val relativeFilename = ensureConvertingFilename(filepath, System.getProperty("user.dir"))

CheckStyle.severities = listOf(Severity.IGNORE, Severity.INFO, Severity.WARNING, Severity.ERROR)
CheckStyle.reporter = Inline
CheckStyle.report("glob:**/build/reports/ktlint/ktlint*Check.xml")
