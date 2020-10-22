package co.uzzu.danger.plugins.checkstyle

import org.dom4j.DocumentHelper
import org.dom4j.Element

internal class Parser(
    private val fileRootPath: String,
    private val severities: List<Severity>,
) {

    fun parse(text: String): Sequence<CheckStyleIssue> {
        val document = DocumentHelper.parseText(text)
        return document.selectNodes("//checkstyle/file").asSequence()
            .filter { it.hasContent() }
            .flatMap { parent ->
                parent.selectNodes("*").asSequence()
                    .map { child -> child as Element }
                    .filter { severities.contains(it.severity()) }
                    .map { child -> createIssue(parent as Element, child, fileRootPath) }
            }
    }

    private fun createIssue(parent: Element, child: Element, fileRootPath: String): CheckStyleIssue =
        CheckStyleIssue(
            filename = parent.name().replace(Regex("^$fileRootPath/"), ""),
            line = child.line(),
            column = child.column(),
            severity = child.severity(),
            message = child.message(),
            source = child.source()
        )

    private fun Element.name(): String = attribute("name").value
    private fun Element.line(): Int = attribute("line").value.toInt()
    private fun Element.column(): Int? = attribute("column")?.value?.toIntOrNull()
    private fun Element.severity(): Severity = Severity.valueOf(attribute("severity").value.toUpperCase())
    private fun Element.message(): String = attribute("message").value
    private fun Element.source(): String = attribute("source").value
}
