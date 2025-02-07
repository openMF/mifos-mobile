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
    alias(libs.plugins.mifos.cmp.feature)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "org.mifos.mobile.feature.auth"
    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jb.kotlin.stdlib)
            implementation(libs.kotlin.reflect)
        }
    }
}
//dependencies {
//    implementation(projects.libs.countryCodePicker)
//    implementation(libs.squareup.okhttp)
//}
