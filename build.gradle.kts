// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.google.oss.licenses.plugin) {
            exclude(group = "com.google.protobuf")
        }

        classpath(libs.spotless.gradle)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.dependencyGuard) apply false
    alias(libs.plugins.firebase.crashlytics) apply false
    alias(libs.plugins.firebase.perf) apply false
    alias(libs.plugins.gms) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.secrets) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.spotbugs) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.detekt) apply true
    alias(libs.plugins.spotless) apply true
}

val detektFormatting = libs.detekt.formatting
val twitterComposeRules = libs.twitter.detekt.compose
val ktlintVersion = "1.0.1"

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
        plugin("com.diffplug.spotless")
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        config.from(rootProject.files("config/detekt/detekt.yml"))
        reports.xml.required.set(true)
        reports.html.required.set(true)
    }

    extensions.configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            targetExclude("**/build/**/*.kt")
            ktlint(ktlintVersion).editorConfigOverride(
                mapOf(
                    "android" to "true",
                ),
            )
            licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
        }
        format("kts") {
            target("**/*.kts")
            targetExclude("**/build/**/*.kts")
            // Look for the first line that doesn't have a block comment (assumed to be the license)
            licenseHeaderFile(rootProject.file("spotless/copyright.kts"), "(^(?![\\/ ]\\*).*$)")
        }
        format("xml") {
            target("**/*.xml")
            targetExclude("**/build/**/*.xml")
            // Look for the first XML tag that isn't a comment (<!--) or the xml declaration (<?xml)
            licenseHeaderFile(rootProject.file("spotless/copyright.xml"), "(<[^!?])")
        }
    }

    dependencies {
        detektPlugins(detektFormatting)
        detektPlugins(twitterComposeRules)
    }
}