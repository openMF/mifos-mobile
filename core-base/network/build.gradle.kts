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
}

android {
    namespace = "template.core.base.network"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.ktor.client.core)
            api(libs.ktor.client.logging)
            api(libs.ktor.client.content.negotiation)
            api(libs.ktor.serialization.kotlinx.json)
            api(libs.ktor.client.auth)
            api(libs.ktorfit.lib)
            api(libs.kermit.logging)
            implementation(projects.coreBase.security)
        }

        androidMain.dependencies {
            api(libs.ktor.client.okhttp)
            api(libs.koin.android)
        }

        nativeMain.dependencies {
            api(libs.ktor.client.darwin)
        }

        desktopMain.dependencies {
            api(libs.ktor.client.okhttp)
        }

        jsMain.dependencies {
            api(libs.ktor.client.js)
        }

        wasmJsMain.dependencies {
            api(libs.ktor.client.js)
        }
    }
}
