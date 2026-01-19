import org.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * Plugin that applies the CMP feature plugin and configures it.
 * This plugin applies the following plugins:
 * - org.mifos.kmp.library - Kotlin Multiplatform Library
 * - org.mifos.kmp.koin - Koin for Kotlin Multiplatform
 * - org.jetbrains.kotlin.plugin.compose - Kotlin Compose
 * - org.jetbrains.compose - Compose Multiplatform
 * - org.mifos.detekt.plugin - Detekt Plugin
 * - org.mifos.spotless.plugin - Spotless Plugin
 *
 */
class CMPFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply {
                apply("org.convention.kmp.library")
                apply("org.convention.kmp.koin")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
                apply("org.convention.detekt.plugin")
                apply("org.convention.spotless.plugin")
            }

            dependencies {
                add("commonMainImplementation", project(":core:ui"))
                add("commonMainImplementation", project(":core-base:ui"))
                add("commonMainImplementation", project(":core:designsystem"))
                add("commonMainImplementation", project(":core-base:designsystem"))
                add("commonMainImplementation", project(":core:data"))
                add("commonMainImplementation", project(":core-base:designsystem"))
                add("commonMainImplementation", project(":core:analytics"))

                add("commonMainImplementation", libs.findLibrary("koin.compose").get())
                add("commonMainImplementation", libs.findLibrary("koin.compose.viewmodel").get())

                add("commonMainImplementation", libs.findLibrary("jb.composeRuntime").get())
                add("commonMainImplementation", libs.findLibrary("jb.composeViewmodel").get())
                add("commonMainImplementation", libs.findLibrary("jb.lifecycleViewmodel").get())
                add("commonMainImplementation", libs.findLibrary("jb.lifecycle.compose").get())

                add(
                    "commonMainImplementation",
                    libs.findLibrary("jb.lifecycleViewmodelSavedState").get(),
                )
                add("commonMainImplementation", libs.findLibrary("jb.savedstate").get())
                add("commonMainImplementation", libs.findLibrary("jb.bundle").get())
                add("commonMainImplementation", libs.findLibrary("jb.composeNavigation").get())
                add(
                    "commonMainImplementation",
                    libs.findLibrary("kotlinx.collections.immutable").get(),
                )

                add("androidMainImplementation", platform(libs.findLibrary("koin-bom").get()))
                add("androidMainImplementation", libs.findLibrary("koin-android").get())
                add("androidMainImplementation", libs.findLibrary("koin.androidx.compose").get())

                add("androidMainImplementation", libs.findLibrary("koin.android").get())
                add("androidMainImplementation", libs.findLibrary("koin.androidx.navigation").get())
                add("androidMainImplementation", libs.findLibrary("koin.androidx.compose").get())
                add("androidMainImplementation", libs.findLibrary("koin.core.viewmodel").get())

            }
        }
    }
}
