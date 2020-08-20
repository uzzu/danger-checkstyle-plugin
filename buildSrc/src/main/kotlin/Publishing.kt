import org.gradle.api.Project

private const val majorVersion: Int = 0
private const val minorVersion: Int = 0
private val patchVersion: Int = 1

val Project.publishingGroupId: String
    get() = "co.uzzu.danger.plugins"

val Project.publishingArtifactId: String
    get() = "checkstyle"

val Project.publishingArtifactVersion: String
    get() = "$majorVersion.$minorVersion.$patchVersion"

object MavenPublications {
    const val description = "Checkstyle plugin for danger-kotlin"
    const val url = "https://github.com/uzzu/danger-checkstyle-plugin"
    const val license = "The Apache Software License, Version 2.0"
    const val licenseUrl = "http://www.apache.org/licenses/LICENSE-2.0.txt"
    const val licenseDistribution = "repo"
    const val developersId = "uzzu"
    const val developersName = "Hirokazu Uzu"
    const val organization = developersId
    const val organizationUrl = "https://uzzu.co"
    const val scmUrl = "https://github.com/uzzu/danger-checkstyle-plugin"
}

val Project.bintrayUser: String?
    get() = findProperty("bintrayUser") as String?
val Project.bintrayApiKey: String?
    get() = findProperty("bintrayApiKey") as String?

object Bintray {
    const val mavenUrl = "https://dl.bintray.com/uzzu/maven"
    const val repo = "maven"
    const val packageName = "danger-checkstyle-plugin"
    const val desc = MavenPublications.description
    const val userOrg = MavenPublications.organization
    const val websiteUrl = MavenPublications.url
    const val issueTrackerUrl = "https://github.com/uzzu/danger-checkstyle-plugin/issues"
    const val vcsUrl = "https://github.com/uzzu/danger-checkstyle-plugin.git"
    const val githubRepo = "uzzu/danger-checkstyle-plugin"
    const val githubReleaseNoteFile = "CHANGELOG.md"
    val licenses = arrayOf("Apache-2.0")
    val labels = arrayOf("Kotlin")
    val publicDownloadNumbers = true
}
