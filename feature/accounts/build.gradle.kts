/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */


plugins {
    alias(libs.plugins.mifos.cmp.feature)
}

android {
    namespace = "com.hekmatullahamin.accounts"
}

kotlin {
    sourceSets{
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.components.resources)

            api(projects.core.common)
            api(projects.core.model)

            api(projects.feature.loanAccount)
            api(projects.feature.savingsAccount)
            api(projects.feature.shareAccount)
        }
    }
}

