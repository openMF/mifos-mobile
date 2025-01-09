package org.mifos.mobile

/**
 * This is shared between :app and :benchmarks module to provide configurations type safety.
 */
enum class MifosBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}