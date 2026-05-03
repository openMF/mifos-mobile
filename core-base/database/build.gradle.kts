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
    alias(libs.plugins.kmp.core.base.library.convention)
}

android {
    namespace = "template.core.base.database"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.androidx.room.runtime)
        }

        desktopMain.dependencies {
            api(libs.androidx.sqlite.bundled)
        }
        nativeMain.dependencies {
            api(libs.androidx.sqlite.bundled)
        }
        androidMain.dependencies {
            api(libs.androidx.sqlite.bundled)
        }

        jsMain.dependencies {
            api(libs.androidx.sqlite.web)
        }
        wasmJsMain.dependencies {
            api(libs.androidx.sqlite.web)
        }
    }
}
