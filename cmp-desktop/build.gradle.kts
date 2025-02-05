import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlin.serialization)
}

kotlin {
    jvm("desktop") {
        withJava()
    }

    jvmToolchain(17)

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(projects.core.common)
                implementation(projects.core.data)
                implementation(projects.core.model)
                implementation(projects.core.datastore)

                implementation(projects.cmpShared)

                implementation(libs.kotlinx.coroutines.swing)
                implementation(compose.desktop.currentOs)
                implementation(libs.jb.kotlin.stdlib)
                implementation(libs.kotlin.reflect)
            }
        }
    }
}

val packageName: String = libs.versions.packageName.get()
val packageNameSpace: String = libs.versions.packageNamespace.get()
val packageVersion: String = libs.versions.packageVersion.get()

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Exe, TargetFormat.Deb)
            packageName = this@Build_gradle.packageName
            packageVersion = this@Build_gradle.packageVersion
            description = "Mifos Mobile Desktop Application"
            copyright = "© 2024 Mifos Initiative. All rights reserved."
            vendor = "Mifos Initiative"
            licenseFile.set(project.file("../LICENSE"))
            includeAllModules = true

            macOS {
                bundleID = packageNameSpace
                dockName = this@Build_gradle.packageName
                iconFile.set(project.file("icons/ic_launcher.icns"))
                notarization {
                    val providers = project.providers
                    appleID.set(providers.environmentVariable("NOTARIZATION_APPLE_ID"))
                    password.set(providers.environmentVariable("NOTARIZATION_PASSWORD"))
                    teamID.set(providers.environmentVariable("NOTARIZATION_TEAM_ID"))
                }
            }

            windows {
                menuGroup = this@Build_gradle.packageName
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
            configurationFiles.from(file("compose-desktop.pro"))
            obfuscate.set(true)
            optimize.set(true)
        }
    }
}
