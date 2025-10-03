/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-wallet/blob/master/LICENSE.md
 */
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm {
        withJava()
    }

    jvmToolchain(21)

    sourceSets {
        jvmMain.dependencies {
            implementation(projects.cmpShared)

            implementation(libs.kotlinx.coroutines.swing)
            implementation(compose.desktop.currentOs)
            implementation(libs.jb.kotlin.stdlib)
            implementation(libs.kotlin.reflect)

            implementation(libs.koin.core)

            implementation(compose.components.resources)
        }
    }
}

val appName: String = libs.versions.packageName.get()
val packageNameSpace: String = libs.versions.packageNamespace.get()
val appVersion: String = libs.versions.packageVersion.get()

compose.desktop {
    application {
        mainClass = "MainKt"

        val buildNumber: String = (project.findProperty("buildNumber") as String?) ?: "1"
        val isAppStoreRelease: Boolean =
            (project.findProperty("macOsAppStoreRelease") as String?)?.toBoolean() ?: false

        nativeDistributions {
            targetFormats(
                TargetFormat.Pkg,
                TargetFormat.Dmg,
                TargetFormat.Msi,
                TargetFormat.Exe,
                TargetFormat.Deb
            )
            packageName = appName
            packageVersion = appVersion
            description = "Desktop Application"
            copyright = "© 2024 Mifos Initiative. All rights reserved."
            vendor = "Mifos Initiative"
            licenseFile.set(project.file("../LICENSE"))
            includeAllModules = true
            outputBaseDir.set(project.layout.buildDirectory.dir("release"))

            macOS {
                bundleID = packageNameSpace
                dockName = appName
                iconFile.set(project.file("icons/ic_launcher.icns"))
                minimumSystemVersion = "12.0"
                appStore = isAppStoreRelease

                infoPlist {
                    packageBuildVersion = buildNumber
                    extraKeysRawXml = """
                    <key>ITSAppUsesNonExemptEncryption</key>
                    <false/>
                """.trimIndent()
                }

                if (isAppStoreRelease) {
                    signing {
                        sign.set(true)
                        identity.set("The Mifos Initiative")
                    }
                    provisioningProfile.set(project.file("embedded.provisionprofile"))
                    runtimeProvisioningProfile.set(project.file("runtime.provisionprofile"))
                    entitlementsFile.set(project.file("entitlements.plist"))
                    runtimeEntitlementsFile.set(project.file("runtime-entitlements.plist"))
                } else {
                    notarization {
                        val providers = project.providers
                        appleID.set(providers.environmentVariable("NOTARIZATION_APPLE_ID"))
                        password.set(providers.environmentVariable("NOTARIZATION_PASSWORD"))
                        teamID.set(providers.environmentVariable("NOTARIZATION_TEAM_ID"))
                    }
                }
            }

            windows {
                menuGroup = appName
                shortcut = true
                dirChooser = true
                perUserInstall = true
                iconFile.set(project.file("icons/ic_launcher.ico"))
            }

            linux {
                modules("jdk.security.auth")
                iconFile.set(project.file("icons/ic_launcher.png"))
            }
        }
        buildTypes.release.proguard {
            isEnabled = false
//            configurationFiles.from(file("compose-desktop.pro"))
//            obfuscate.set(true)
//            optimize.set(true)
        }
    }
}

/**
 * Removes the `com.apple.quarantine` extended attribute from the built `.app`.
 *
 * Why:
 * Gatekeeper may mark files from the Internet with `com.apple.quarantine`.
 * If any such file ends up inside the `.app`, App Store validation can fail.
 */
val unquarantineApp = tasks.register<Exec>("unquarantineMacApp") {
    group = "macOS"
    description = "Remove com.apple.quarantine from the built .app before signing"
    onlyIf { org.gradle.internal.os.OperatingSystem.current().isMacOsX }

    dependsOn("createReleaseDistributable")

    val appName = "$appName.app" // set to your final .app name
    val appPath = layout.buildDirectory
        .dir("release/main-release/app/$appName")
        .map { it.asFile.absolutePath }

    commandLine("xattr", "-dr", "com.apple.quarantine", appPath.get())
}

tasks.matching { it.name == "packageReleasePkg" }.configureEach {
    dependsOn(unquarantineApp)
}
