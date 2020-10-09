import org.jetbrains.dokka.gradle.DokkaTask
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    id("co.uzzu.dotenv.gradle") version "1.1.0"
    kotlin("jvm") version "1.4.0"
    id("org.jlleitschuh.gradle.ktlint") version "9.3.0"
    id("org.jetbrains.dokka") version "1.4.0"
    `java-library`
    `maven-publish`
    signing
}

repositories {
    jcenter()
}

dependencies {
    implementation("systems.danger:danger-kotlin-sdk:1.2")
    api("org.dom4j:dom4j:2.1.3") { isTransitive = true }
    api("jaxen:jaxen:1.1.6") { isTransitive = true }

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

java {
    targetCompatibility = JavaVersion.VERSION_1_8
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

ktlint {
    debug.set(true)
    verbose.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(true)
    reporters {
        reporter(ReporterType.CHECKSTYLE)
    }
}

val sourcesJar by tasks.creating(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").allSource)
}

val dokkaJavadoc = tasks.getByName("dokkaJavadoc", DokkaTask::class)
val dokkaJar by tasks.creating(Jar::class) {
    archiveClassifier.set("javadoc")
    dependsOn(dokkaJavadoc)
    from(dokkaJavadoc.outputDirectory)
}

group = publishingGroupId
version = publishingArtifactVersion(env.PUBLISH_PRODUCTION.isPresent)
setProperty("archivesBaseName", publishingArtifactId)

publishing {
    repositories {
        maven {
            url = env.PUBLISH_PRODUCTION.orNull()
                ?.run { uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/") }
                ?: uri("https://oss.sonatype.org/content/repositories/snapshots/")
            credentials {
                username = env.OSSRH_USERNAME.orElse("")
                password = env.OSSRH_PASSWORD.orElse("")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components.getByName("java"))
            groupId = publishingGroupId
            artifactId = publishingArtifactId
            version = publishingArtifactVersion(env.PUBLISH_PRODUCTION.isPresent)

            artifact(sourcesJar)
            artifact(dokkaJar)

            pom {
                name.set(publishingArtifactId)
                description.set(MavenPublications.description)
                url.set(MavenPublications.url)
                licenses {
                    license {
                        name.set(MavenPublications.license)
                        url.set(MavenPublications.licenseUrl)
                        distribution.set(MavenPublications.licenseDistribution)
                    }
                }
                developers {
                    developer {
                        id.set(MavenPublications.developersId)
                        name.set(MavenPublications.developersName)
                        organization.set(MavenPublications.organization)
                        organizationUrl.set(MavenPublications.organizationUrl)
                    }
                }
                scm {
                    url.set(MavenPublications.scmUrl)
                }
            }
        }
    }
}

signing {
    if (env.PUBLISH_PRODUCTION.isPresent) {
        setRequired { gradle.taskGraph.hasTask("publish") }
        sign(publishing.publications)

        @Suppress("UnstableApiUsage")
        useInMemoryPgpKeys(
            env.SIGNING_KEYID.orElse(""),
            env.SIGNING_KEY.orElse(""),
            env.SIGNING_PASSWORD.orElse("")
        )
    }
}
