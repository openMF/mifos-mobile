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
    alias(libs.plugins.mifos.kmp.library)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "org.mifos.mobile.core.ui"
}

kotlin{
    sourceSets{
        androidMain.dependencies {
            api(libs.androidx.metrics)
            implementation(libs.androidx.compose.runtime)
            implementation(libs.accompanist.pager)
        }
        commonMain.dependencies {
            api(projects.core.designsystem)
            api(libs.kotlinx.datetime)
            implementation(libs.jb.composeViewmodel)
            implementation(libs.jb.lifecycleViewmodel)
            implementation(libs.jb.lifecycleViewmodelSavedState)
            implementation(libs.coil.kt)
            implementation(libs.coil.kt.compose)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.jb.composeNavigation)
            implementation(libs.filekit.compose)
            implementation(libs.filekit.core)
        }
    }
}

