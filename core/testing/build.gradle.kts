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

            // Coroutines Test
            api(libs.kotlinx.coroutines.test)

            // Koin Test
            api(libs.koin.test)

            // Kotlin Test
            api(libs.kotlin.test)
        }

        androidMain.dependencies {
            // Android Test
            api(libs.androidx.test.ext.junit)
            api(libs.androidx.test.rules)
            api(libs.androidx.test.espresso.core)

            // Compose Test
            api(libs.androidx.compose.ui.test)
            api(libs.androidx.compose.ui.test.manifest)

            // Turbine for Flow testing
            api(libs.turbine)

            // Mockito
            api(libs.mockito.core)

            // Truth assertions
            api(libs.truth)

            // Koin Android Test
            api(libs.koin.test.junit4)
        }
    }
}
