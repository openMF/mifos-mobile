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
    alias(libs.plugins.mifos.android.hilt)
}

android {
    namespace = "org.mifos.mobile.core.testing"
}

dependencies {
    api(libs.bundles.androidx.compose.ui.test)
    api(libs.kotlinx.coroutines.test)
    api(projects.core.data)
//    api(projects.core.logs)
//    api(projects.core.model)
    api(libs.turbine)
    api(libs.mockito.core)

    implementation(kotlin("test"))
    implementation(libs.squareup.retrofit.converter.gson)
    implementation(libs.androidx.navigation.testing)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.test.espresso.core)
    implementation(libs.androidx.test.rules)
    implementation(libs.hilt.android.testing)
}