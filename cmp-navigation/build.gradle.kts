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
    alias(libs.plugins.mifos.cmp.feature)
    alias(libs.plugins.mifos.kmp.koin)
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.feature.loan)
            implementation(projects.feature.auth)
            implementation(projects.feature.help)
            implementation(projects.feature.home)
            implementation(projects.feature.accounts)
            implementation(projects.feature.guarantor)
            implementation(projects.feature.loanAccount)
            implementation(projects.feature.shareAccount)
            implementation(projects.feature.savingsAccount)
            implementation(projects.feature.about)
            implementation(projects.feature.recentTransaction)
            implementation(projects.feature.clientCharge)
            implementation(projects.feature.updatePassword)
            implementation(projects.feature.thirdPartyTransfer)
            implementation(projects.feature.transferProcess)
            implementation(projects.feature.beneficiary)
            implementation(projects.feature.settings)
            implementation(projects.feature.qr)
            // Core Modules
            implementation(projects.core.data)
            implementation(projects.core.common)
            implementation(projects.core.network)
            implementation(projects.libs.mifosPasscode)
            //put your multiplatform dependencies here
            implementation(compose.material3)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.components.resources)
            implementation(libs.window.size)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.tracing.ktx)
            implementation(libs.koin.android)
        }
    }
}

android {
    namespace = "cmp.navigation"
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "org.mifos.mobile.navigation.generated.resources"
}