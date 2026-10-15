/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */

plugins {
    alias(libs.plugins.cmp.feature.convention)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlinCocoapods)
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            // Navigation Modules
            implementation(projects.cmpNavigation)
            implementation(compose.components.resources)
            implementation(projects.coreBase.platform)
            implementation(projects.coreBase.ui)
            api(projects.core.data)
            api(projects.core.network)
            implementation(libs.coil.kt.compose)

            //put your multiplatform dependencies here
            implementation(compose.material)
            implementation(compose.material3)
        }

        desktopMain.dependencies {
            // Desktop specific dependencies
            implementation(compose.desktop.currentOs)
            implementation(compose.desktop.common)
        }
    }

    cocoapods {
        summary = "KMP Shared Module"
        homepage = "https://github.com/openMF/mifos-mobile"
        version = project.version.toString().substringBefore("-").substringBefore("+")
        ios.deploymentTarget = "16.0"
        podfile = project.file("../cmp-ios/Podfile")

        framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
}

android {
    namespace = "cmp.shared"
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "cmp.shared.generated.resources"
}