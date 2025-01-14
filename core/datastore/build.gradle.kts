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
    id("kotlinx-serialization")
}

android {
    namespace = "org.mifos.mobile.core.datastore"

    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }

}

kotlin{

    sourceSets{
        commonMain.dependencies {
            implementation(libs.multiplatform.settings)
            implementation(libs.multiplatform.settings.serialization)
            implementation(libs.multiplatform.settings.coroutines)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.core)
            implementation(projects.core.common)
        }
    }
}

