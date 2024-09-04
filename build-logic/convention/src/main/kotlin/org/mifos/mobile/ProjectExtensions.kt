package org.mifos.mobile

import com.diffplug.gradle.spotless.SpotlessExtension
import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

val Project.libs
    get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

inline fun Project.detektGradle(crossinline configure: DetektExtension.() -> Unit) =
    extensions.configure<DetektExtension> {
        configure()
    }

inline fun Project.spotlessGradle(crossinline configure: SpotlessExtension.() -> Unit) =
    extensions.configure<SpotlessExtension> {
        configure()
    }