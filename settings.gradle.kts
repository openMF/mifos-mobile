import org.ajoberstar.reckon.gradle.ReckonExtension

// ── Workspace Library Linker (managed by /lib-integrate) ──────────────────
// Edit lib-integrate.properties to add/remove libraries. Never edit this block.
// Path-existence guard: if library not cloned locally → silently uses Maven Central.
val libProps = java.util.Properties().apply {
    file("lib-integrate.properties").takeIf { it.exists() }?.inputStream()?.use { load(it) }
}
libProps.stringPropertyNames()
    .filter { it.endsWith(".local") && libProps[it] == "true" }
    .forEach { key ->
        val lib      = key.removeSuffix(".local")
        val path     = libProps["$lib.path"]     as? String ?: return@forEach
        val module   = libProps["$lib.module"]   as? String ?: return@forEach
        val artifact = libProps["$lib.artifact"] as? String ?: return@forEach
        if (!file(path).exists()) {
            println("📦 [lib-integrate] $lib → Maven Central (source not found at $path)")
            return@forEach
        }
        println("⚡ [lib-integrate] $lib → local source ($path)")
        includeBuild(path) {
            dependencySubstitution {
                substitute(module(artifact)).using(project(module))
            }
        }
    }
// ── End lib-integrate managed block ───────────────────────────────────────

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
include(":core:analytics")
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
include(":core-base:security")
include(":core-base:store")

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
