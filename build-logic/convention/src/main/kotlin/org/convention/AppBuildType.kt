package org.convention

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class AppBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
