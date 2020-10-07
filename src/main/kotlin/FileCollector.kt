package co.uzzu.danger.plugins.checkstyle

import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.asSequence

class FileCollector(private val basePath: String) {

    fun collect(path: String): Sequence<File> {
        // Support glob matcher
        if (path.startsWith("glob:")) {
            val fs = FileSystems.getDefault()
            val matcher = fs.getPathMatcher(path)
            return Files.walk(Paths.get(basePath)).filter(matcher::matches).map(Path::toFile).asSequence()
        }

        val absolutePath = Paths.get(path)
            .takeIf { it.isAbsolute }
            ?: Paths.get(basePath, path).toAbsolutePath()
        return sequenceOf(absolutePath.toFile())
    }
}
