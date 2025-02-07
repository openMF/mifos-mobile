import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.mifos.mobile.libs

class CMPFeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("mifos.kmp.library")
                apply("mifos.kmp.koin")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            dependencies {
                add("commonMainImplementation", project(":core:ui"))
                add("commonMainImplementation", project(":core:designsystem"))
//                add("commonMainImplementation", project(":core:testing"))
                add("commonMainImplementation", project(":core:data"))

                add("commonMainImplementation", libs.findLibrary("koin.compose").get())
                add("commonMainImplementation", libs.findLibrary("koin.compose.viewmodel").get())

                add("commonMainImplementation", libs.findLibrary("jb.composeRuntime").get())
                add("commonMainImplementation", libs.findLibrary("jb.composeViewmodel").get())
                add("commonMainImplementation", libs.findLibrary("jb.lifecycleViewmodel").get())
                add("commonMainImplementation", libs.findLibrary("jb.lifecycleViewmodelSavedState").get())
                add("commonMainImplementation", libs.findLibrary("jb.savedstate").get())
                add("commonMainImplementation", libs.findLibrary("jb.bundle").get())
                add("commonMainImplementation", libs.findLibrary("jb.composeNavigation").get())
                add("commonMainImplementation", libs.findLibrary("kotlinx.collections.immutable").get())

                add("androidMainImplementation", libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                add("androidMainImplementation", libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                add("androidMainImplementation", libs.findLibrary("androidx.tracing.ktx").get())

                add("androidMainImplementation", platform(libs.findLibrary("koin-bom").get()))
                add("androidMainImplementation", libs.findLibrary("koin-android").get())
                add("androidMainImplementation", libs.findLibrary("koin.androidx.compose").get())

                add("androidMainImplementation", libs.findLibrary("koin.android").get())
                add("androidMainImplementation", libs.findLibrary("koin.androidx.navigation").get())
                add("androidMainImplementation", libs.findLibrary("koin.androidx.compose").get())
                add("androidMainImplementation", libs.findLibrary("koin.core.viewmodel").get())

                add("androidTestImplementation", libs.findLibrary("koin.test.junit4").get())

                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx.navigation.testing").get())
                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx.compose.ui.test").get())
                add("androidInstrumentedTestImplementation", libs.findLibrary("androidx.lifecycle.runtimeTesting").get())
            }
        }
    }
}