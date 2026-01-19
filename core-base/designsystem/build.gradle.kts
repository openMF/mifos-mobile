/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
plugins {
    alias(libs.plugins.kmp.library.convention)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "template.core.base.designsystem"
}

kotlin {
    sourceSets{
        androidMain.dependencies {
            implementation(libs.androidx.compose.ui.tooling)
        }
        commonMain.dependencies {
            implementation(compose.ui)
            implementation(compose.material3)
            implementation(compose.foundation)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(compose.materialIconsExtended)

            api(compose.material3AdaptiveNavigationSuite)
            api(libs.jetbrains.compose.material3.adaptive)
            api(libs.jetbrains.compose.material3.adaptive.layout)
            api(libs.jetbrains.compose.material3.adaptive.navigation)

            implementation(libs.jb.lifecycleViewmodel)
            implementation(libs.window.size)
            implementation(libs.ui.backhandler)
        }
    }
}

compose.resources {
    publicResClass = true
    generateResClass = always
    packageOfResClass = "template.core.base.designsystem.generated.resources"
}