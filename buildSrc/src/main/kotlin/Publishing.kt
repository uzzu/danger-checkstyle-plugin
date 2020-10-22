import org.gradle.api.Project

private const val majorVersion: Int = 0
private const val minorVersion: Int = 2
private const val patchVersion: Int = 0

val Project.publishingGroupId: String
    get() = "co.uzzu.danger.plugins"

val Project.publishingArtifactId: String
    get() = "checkstyle"

val Project.publishingArtifactVersion: String
    get() = "$majorVersion.$minorVersion.$patchVersion"

fun Project.publishingArtifactVersion(isPublishProduction: Boolean): String =
    if (isPublishProduction) {
        publishingArtifactVersion
    } else {
        "$publishingArtifactVersion-SNAPSHOT"
    }

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
