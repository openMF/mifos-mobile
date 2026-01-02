package org.convention

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.named

/**
 * Configures the Detekt plugin with the [extension] configuration.
 * This includes setting the JVM target to 17 and enabling all reports.
 * Additionally, it adds the `detekt-formatting` and `twitter-detekt-compose` plugins.
 * @see DetektExtension
 * @see Detekt
 */
internal fun Project.configureDetekt(extension: DetektExtension) = extension.apply {
    tasks.named<Detekt>("detekt") {
        mustRunAfter(":cmp-android:dependencyGuard")
        jvmTarget = "17"
        source(files(rootDir))
        include("**/*.kt")
        exclude("**/*.kts")
        exclude("**/resources/**")
        exclude("**/build/**")
        exclude("**/generated/**")
        exclude("**/build-logic/**")
        exclude("**/spotless/**")
        // TODO:: Remove this exclusion
        exclude("core-base/designsystem/**")
        exclude("feature/home/**")
        reports {
            xml.required.set(true)
            html.required.set(true)
            txt.required.set(true)
            sarif.required.set(true)
            md.required.set(true)
        }
    }
    dependencies {
        "detektPlugins"(libs.findLibrary("detekt-formatting").get())
        "detektPlugins"(libs.findLibrary("twitter-detekt-compose").get())
    }
}