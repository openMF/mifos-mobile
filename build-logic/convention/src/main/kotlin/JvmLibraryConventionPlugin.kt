import org.gradle.api.Plugin
import org.gradle.api.Project
import org.mifos.mobile.configureKotlinJvm

class JvmLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.jvm")
                apply("mifos.android.lint")
            }
            configureKotlinJvm()
        }
    }
}
