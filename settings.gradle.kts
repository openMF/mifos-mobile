pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://www.jitpack.io")
        maven("https://plugins.gradle.org/m2/")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://www.jitpack.io")
        maven("https://plugins.gradle.org/m2/")
    }
}
rootProject.name = "mifos-mobile"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")
include(":ui")
include(":core:logs")
include(":feature:guarantor")
include(":core:model")
include(":core:common")
include(":core:data")
include(":core:network")
include(":core:datastore")
include(":feature:loan")
include(":feature:beneficiary")
include(":feature:savings")
include(":feature:qr")
include(":feature:transfer-process")
include(":feature:account")
include(":feature:recent_transaction")
include(":feature:client_charge")
include(":feature:third-party-transfer")
include(":feature:help")
include(":feature:notification")
include(":feature:location")
include(":feature:about")
include(":feature:settings")
include(":feature:auth")
include(":feature:update-password")
