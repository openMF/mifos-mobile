import org.ajoberstar.reckon.gradle.ReckonExtension

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_PROJECT)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://plugins.gradle.org/m2/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version("0.8.0")
    id("org.ajoberstar.reckon.settings") version("0.18.3")
}

extensions.configure<ReckonExtension> {
    setDefaultInferredScope("patch")
    stages("beta", "final")
    setScopeCalc { java.util.Optional.of(org.ajoberstar.reckon.core.Scope.PATCH) }
    setScopeCalc(calcScopeFromProp().or(calcScopeFromCommitMessages()))
    setStageCalc(calcStageFromProp())
    setTagWriter { it.toString() }
}

rootProject.name = "mifos-mobile"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":cmp-shared")
include(":cmp-android")
include(":cmp-desktop")
include(":cmp-web")
include(":cmp-navigation")

// Core Modules
include(":core:ui")
include(":core:designsystem")
include(":core:logs")
include(":core:model")
include(":core:common")
include(":core:data")
include(":core:network")
include(":core:database")
include(":core:datastore")
include(":core:qrcode")
include(":core:testing")

include(":core-base:datastore")
include(":core-base:common")
include(":core-base:database")
include(":core-base:network")
include(":core-base:designsystem")
include(":core-base:platform")
include(":core-base:ui")
include(":core-base:analytics")

// Feature Modules
include(":feature:beneficiary")
include(":feature:guarantor")
include(":feature:qr")
include(":feature:transfer-process")
include(":feature:recent-transaction")
include(":feature:client-charge")
include(":feature:third-party-transfer")
include(":feature:notification")
include(":feature:location")
include(":feature:settings")
include(":feature:auth")
include(":feature:home")
include(":feature:accounts")
include(":feature:share-account")
include(":feature:loan-account")
include(":feature:savings-account")
include(":feature:onboarding-language")
include(":feature:passcode")
include(":feature:status")
include(":feature:loan-application")
include(":feature:savings-application")
include(":feature:share-application")

// Lint Modules
//include(":lint")

// Library Modules
include(":libs:country-code-picker")
include(":libs:pullrefresh")
include(":libs:material3-navigation")
include(":libs:mifos-passcode")
