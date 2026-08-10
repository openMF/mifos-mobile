/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
import org.gradle.api.tasks.testing.Test

plugins {
    alias(libs.plugins.cmp.feature.convention)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "org.mifos.mobile.feature.pocket"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(compose.material3)
            implementation(compose.foundation)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
        }

        commonTest.dependencies {
            implementation(libs.turbine)
            implementation(libs.jb.composeUiTest)
        }
    }
}

tasks.withType<Test>().configureEach {
    // Compose UI tests in commonTest run on desktopTest.
    if (name.endsWith("UnitTest")) {
        filter {
            excludeTestsMatching("org.mifos.mobile.feature.pocket.pocketDashboard.PocketDashboardScreenTest")
            excludeTestsMatching("org.mifos.mobile.feature.pocket.managePocket.ManagePocketScreenTest")
            excludeTestsMatching("org.mifos.mobile.feature.pocket.managePocket.LinkAccountsTest")
            excludeTestsMatching("org.mifos.mobile.feature.pocket.managePocket.DelinkAccountsTest")
        }
    }
}
