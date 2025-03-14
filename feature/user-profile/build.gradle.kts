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
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
}


android {
    namespace = "org.mifos.mobile.feature.user.profile"
}

kotlin {
    sourceSets{
        commonMain.dependencies {
            implementation(compose.components.resources)
            implementation(compose.material3)
            implementation(projects.core.ui)
            implementation(projects.core.model)
            implementation(compose.components.uiToolingPreview)
        }
    }
}
