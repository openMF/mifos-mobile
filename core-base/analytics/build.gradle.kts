/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
plugins {
    alias(libs.plugins.kmp.library.convention)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "template.core.base.analytics"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.koin.core)
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(libs.kermit.logging)
            
            // For timing and performance tracking
            implementation(libs.kotlinx.datetime)
        }

        androidMain.dependencies {
            api(libs.gitlive.firebase.analytics)
        }

        nonJsCommonMain.dependencies {
            api(libs.gitlive.firebase.analytics)
        }

        nativeMain.dependencies {
            api(libs.gitlive.firebase.analytics)
        }

        desktopMain.dependencies {
            api(libs.gitlive.firebase.analytics)
        }

        mobileMain.dependencies {
            api(libs.gitlive.firebase.crashlytics)
        }
        
        // Test dependencies for all platforms
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
