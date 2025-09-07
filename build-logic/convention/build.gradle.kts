import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "org.mifos.mobile.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.detekt.gradlePlugin)
    compileOnly(libs.ktlint.gradlePlugin)
    compileOnly(libs.spotless.gradlePlugin)
    implementation(libs.truth)

    compileOnly(libs.firebase.crashlytics.gradlePlugin)
    compileOnly(libs.firebase.performance.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "mifos.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "mifos.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidFlavors") {
            id = "mifos.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }

        register("androidFirebase") {
            id = "org.convention.android.application.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }

        register("androidLint") {
            id = "org.convention.android.application.lint"
            implementationClass = "AndroidLintConventionPlugin"
        }

        // This can removed after migration
        register("androidLibrary") {
            id = "mifos.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "mifos.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidFeature") {
            id = "mifos.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }

        // Room Plugin
        register("kmpRoom") {
            id = "mifos.kmp.room"
            implementationClass = "KMPRoomConventionPlugin"
        }

        // Utility Plugins
        register("detekt") {
            id = "mifos.detekt.plugin"
            implementationClass = "MifosDetektConventionPlugin"
            description = "Configures detekt for the project"
        }
        register("spotless") {
            id = "mifos.spotless.plugin"
            implementationClass = "MifosSpotlessConventionPlugin"
            description = "Configures spotless for the project"
        }
        register("gitHooks") {
            id = "mifos.git.hooks"
            implementationClass = "MifosGitHooksConventionPlugin"
            description = "Installs git hooks for the project"
        }

        // KMP & CMP Plugins
        register("cmpFeature") {
            id = "org.convention.cmp.feature"
            implementationClass = "CMPFeatureConventionPlugin"
        }

        register("kmpKoin") {
            id = "org.convention.kmp.koin"
            implementationClass = "KMPKoinConventionPlugin"
        }

        register("kmpLibrary") {
            id = "org.convention.kmp.library"
            implementationClass = "KMPLibraryConventionPlugin"
        }
    }
}
