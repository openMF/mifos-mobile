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
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "org.mifos.mobile.core.model"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {

    api(projects.core.common)

    implementation(libs.jetbrains.kotlin.jdk7)
    implementation(libs.kotlinx.serialization.json)

    // For Serialized name
    implementation(libs.squareup.retrofit.converter.gson)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}