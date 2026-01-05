/**
 * Kotlin Multiplatform project hierarchy template configuration.
 *
 * This file defines a structured hierarchy for organizing source sets in Kotlin Multiplatform
 * projects. It establishes a logical grouping of platform targets that enables efficient code
 * sharing across platforms with similar characteristics.
 *
 * The hierarchy template creates the following logical groupings:
 * - `common`: Base shared code for all platforms
 * - `nonAndroid`: Code shared between JVM, JS, and native platforms, excluding Android
 * - `jsCommon`: Code shared between JavaScript and WebAssembly JavaScript targets
 * - `nonJsCommon`: Code shared between JVM and native platforms, excluding JS platforms
 * - `jvmCommon`: Code shared between Android and JVM targets
 * - `nonJvmCommon`: Code shared between JS and native platforms, excluding JVM platforms
 * - `native`: Code shared across all native platforms
 *   - `apple`: Code shared across Apple platforms (iOS, macOS)
 *     - `ios`: iOS-specific code
 *     - `macos`: macOS-specific code
 * - `nonNative`: Code shared between JS and JVM platforms
 *
 * This template applies to both main and test source sets, establishing a consistent
 * structure throughout the project.
 *
 * Note: This implementation uses experimental Kotlin Gradle plugin APIs and may be subject
 * to change in future Kotlin releases.
 */
@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

package org.convention

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyBuilder
import org.jetbrains.kotlin.gradle.plugin.KotlinHierarchyTemplate
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

/**
 * Defines the hierarchical structure for source set organization.
 *
 * This template establishes the relationships between different platform targets,
 * creating logical groupings based on platform similarities to facilitate code sharing.
 */
private val hierarchyTemplate = KotlinHierarchyTemplate {
    withSourceSetTree(
        KotlinSourceSetTree.main,
        KotlinSourceSetTree.test,
    )

    common {
        withCompilations { true }

        groupNonAndroid()
        groupJsCommon()
        groupNonJsCommon()
        groupJvmCommon()
        groupNonJvmCommon()
        groupNative()
        groupNonNative()
        groupJvmJsCommon()
        groupMobile()
    }
}

/**
 * Creates a group of non-Android platforms (JVM, JS, and native).
 */
private fun KotlinHierarchyBuilder.groupNonAndroid() {
    group("nonAndroid") {
        withJvm()
        groupJsCommon()
        groupNative()
    }
}

/**
 * Creates a group of JavaScript-related platforms (JS and WebAssembly JS).
 */
private fun KotlinHierarchyBuilder.groupJsCommon() {
    group("jsCommon") {
        withJs()
        withWasmJs()
    }
}

/**
 * Creates a group of non-JavaScript platforms (JVM-based and native).
 */
private fun KotlinHierarchyBuilder.groupNonJsCommon() {
    group("nonJsCommon") {
        groupJvmCommon()
        groupNative()
    }
}

/**
 * Creates a group of JVM-based platforms (Android and JVM).
 */
private fun KotlinHierarchyBuilder.groupJvmCommon() {
    group("jvmCommon") {
        withAndroidTarget()
        withJvm()
    }
}

/**
 * Creates a group of non-JVM platforms (JavaScript and native).
 */
private fun KotlinHierarchyBuilder.groupNonJvmCommon() {
    group("nonJvmCommon") {
        groupJsCommon()
        groupNative()
    }
}

/**
 * Creates a group of JVM, JS platforms (JavaScript and JVM).
 */
private fun KotlinHierarchyBuilder.groupJvmJsCommon() {
    group("jvmJsCommon") {
        groupJsCommon()
        withJvm()
    }
}

/**
 * Creates a hierarchical group of native platforms with subgroups for Apple platforms.
 */
private fun KotlinHierarchyBuilder.groupNative() {
    group("native") {
        withNative()

        group("apple") {
            withApple()

            group("ios") {
                withIos()
            }

            group("macos") {
                withMacos()
            }
        }
    }
}

/**
 * Creates a group of non-native platforms (JavaScript and JVM-based).
 */
private fun KotlinHierarchyBuilder.groupNonNative() {
    group("nonNative") {
        groupJsCommon()
        groupJvmCommon()
    }
}

private fun KotlinHierarchyBuilder.groupMobile() {
    group("mobile") {
        withAndroidTarget()
        withApple()
    }
}

/**
 * Applies the predefined hierarchy template to a Kotlin Multiplatform project.
 *
 * This extension function should be called within the `kotlin` block of a Multiplatform
 * project's build script to establish the source set hierarchy defined in this file.
 *
 * Example usage:
 * ```
 * kotlin {
 *     applyProjectHierarchyTemplate()
 *     // Configure targets...
 * }
 * ```
 */
fun KotlinMultiplatformExtension.applyProjectHierarchyTemplate() {
    applyHierarchyTemplate(hierarchyTemplate)
}