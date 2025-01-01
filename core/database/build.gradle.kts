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
    alias(libs.plugins.mifos.android.hilt)
    id("com.google.devtools.ksp") version "2.0.20-1.0.24"
    id("kotlin-parcelize")
}

android {
    namespace = "org.mifos.mobile.core.database"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

}

dependencies {
    api(projects.core.common)
    api(projects.core.model)
    implementation(libs.squareup.retrofit.converter.gson)

    //rxjava Dependencies
    implementation(libs.reactivex.rxjava2.android)
    implementation(libs.reactivex.rxjava2)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    kspTest(libs.androidx.room.compiler)

    implementation(libs.gson)
    testImplementation(libs.androidx.room.testing)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.jetbrains.kotlin.stdlib)
    implementation(libs.androidx.appcompat)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}