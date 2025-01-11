/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
import org.mifos.mobile.MifosBuildType
import org.mifos.mobile.dynamicVersion

plugins {
    alias(libs.plugins.mifos.android.application)
    alias(libs.plugins.mifos.android.application.compose)
    alias(libs.plugins.mifos.android.application.flavors)
    alias(libs.plugins.mifos.android.hilt)
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "org.mifos.mobile"

    defaultConfig {
        versionName = project.dynamicVersion
        versionCode = System.getenv("VERSION_CODE")?.toIntOrNull() ?: 1
        applicationId = "org.mifos.mobile"
        vectorDrawables.useSupportLibrary = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
        ndk {
            abiFilters.addAll(arrayOf("armeabi-v7a", "x86", "x86_64", "arm64-v8a"))
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("KEYSTORE_PATH") ?: "../keystores/release_keystore.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "mifos1234"
            keyAlias = System.getenv("KEYSTORE_ALIAS") ?: "mifos-mobile"
            keyPassword = System.getenv("KEYSTORE_ALIAS_PASSWORD") ?: "mifos1234"
            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = MifosBuildType.DEBUG.applicationIdSuffix
        }

        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("release")
            applicationIdSuffix = MifosBuildType.RELEASE.applicationIdSuffix
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    lint {
        xmlReport = true
        checkDependencies = true
        abortOnError = false
        // Disable this rule until we ship the libraries to some maven.
        disable += "ResourceName"
        baseline = File("lint-baseline.xml")
        explainIssues = true
        htmlReport = true
    }
}

dependencyGuard {
    configuration("demoDebugRuntimeClasspath")
    configuration("demoReleaseRuntimeClasspath")
    configuration("prodDebugRuntimeClasspath")
    configuration("prodReleaseRuntimeClasspath")
}

dependencies {

    implementation (projects.shared)

    implementation(projects.core.logs)
    implementation(projects.core.common)
    implementation(projects.core.model)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.core.ui)
    implementation(projects.core.designsystem)

    implementation(projects.feature.loan)
    implementation(projects.feature.beneficiary)
    implementation(projects.feature.guarantor)
    implementation(projects.feature.savings)
    implementation(projects.feature.qr)
    implementation(projects.feature.transferProcess)
    implementation(projects.feature.account)
    implementation(projects.feature.clientCharge)
    implementation(projects.feature.recentTransaction)
    implementation(projects.feature.thirdPartyTransfer)
    implementation(projects.feature.help)
    implementation(projects.feature.notification)
    implementation(projects.feature.location)
    implementation(projects.feature.about)
    implementation(projects.feature.settings)
    implementation(projects.feature.updatePassword)
    implementation(projects.feature.home)
    implementation(projects.feature.auth)
    implementation(projects.feature.userProfile)

    implementation(projects.libs.mifosPasscode)

    // Jetpack Compose
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.core.splashscreen)

    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.google.oss.licenses)
    implementation(libs.androidx.multidex)

    testImplementation(projects.core.testing)
    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.work.testing)

    androidTestImplementation(kotlin("test"))
    androidTestImplementation(projects.core.testing)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.hilt.android.testing)

    debugApi(libs.androidx.compose.ui.tooling)
}
