/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
plugins {
    alias(libs.plugins.kmp.library.convention)
}

android {
    namespace = "org.mifos.mobile.core.testing"

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(projects.core.common)
            api(projects.core.data)
            api(projects.core.model)
            api(projects.core.network)

            // Coroutines Test - KMP compatible
            api(libs.kotlinx.coroutines.test)

            // Koin Test - KMP compatible
            api(libs.koin.test)

            // Kotlin Test - KMP compatible
            api(libs.kotlin.test)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            // Android Test
            api(libs.androidx.test.ext.junit)
            api(libs.androidx.test.rules)
            api(libs.androidx.test.espresso.core)

            // Note: Compose UI Test dependencies (ui-test-junit4, ui-test-manifest)
            // should be added by consuming modules that need them, as they require
            // the Compose BOM for version management.

            // Turbine for Flow testing
            api(libs.turbine)

            // Mockito
            api(libs.mockito.core)

            // Truth assertions
            api(libs.truth)

            // Koin Android Test
            api(libs.koin.test.junit4)
        }

        iosMain.dependencies {
            // iOS-specific test utilities if needed
        }

        desktopMain.dependencies {
            // Desktop-specific test utilities
            api(libs.kotlinx.coroutines.swing)
        }

        jsMain.dependencies {
            // JS-specific test utilities
        }

        nativeMain.dependencies {
            // Native-specific test utilities
        }
    }
}
