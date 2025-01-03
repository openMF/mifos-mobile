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
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "org.mifos.mobile.core.datastore"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.model)

    
}