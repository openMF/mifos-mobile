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
    alias(libs.plugins.mifos.android.library)
    alias(libs.plugins.mifos.android.library.compose)
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    namespace = "org.mifos.mobile.core"
}

dependencies {
    api(projects.core.designsystem)
//    api(projects.core.model)
    api(projects.core.common)
    api(libs.androidx.metrics)

    testImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
}
