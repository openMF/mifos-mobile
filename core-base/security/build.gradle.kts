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
    alias(libs.plugins.kmp.core.base.library.convention)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "template.core.base.security"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(project(":core-base:common"))
            implementation(libs.kotlinx.coroutines.core)
            api(libs.koin.core)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(libs.jb.lifecycle.compose)
            implementation(libs.koin.compose)
        }

        androidMain.dependencies {
            implementation(libs.androidx.security.crypto)
        }

        desktopMain.dependencies {
            implementation(libs.bouncycastle)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}
