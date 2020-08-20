repositories {
    jcenter()
}

plugins {
    `kotlin-dsl`
}

dependencies {
    api("com.google.guava:guava:28.2-jre")
}

kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
