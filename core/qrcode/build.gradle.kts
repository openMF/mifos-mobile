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
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "org.mifos.mobile.core.qrcode"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.core.model)
            implementation(compose.ui)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.coil.kt.compose)
            implementation(libs.filekit.core)
            implementation(libs.filekit.compose)
            api(libs.kermit.logging)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.qrose)
        }

        androidMain.dependencies {
            implementation(libs.androidx.camera.view)
            implementation(libs.androidx.camera.camera2)
            implementation(libs.androidx.camera.lifecycle)
            implementation(libs.accompanist.permissions)
            implementation(libs.mlkit.barcode.scanning)
            implementation(libs.guava)
            implementation(libs.zxing.core)
        }

        nativeMain.dependencies {
            implementation(libs.moko.permission.compose)
        }
    }
}
