
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.convention.configureDetekt
import org.convention.detektGradle

/**
 * Plugin that applies the Detekt plugin and configures it.
 */
class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins()

            detektGradle {
                configureDetekt(this)
            }
        }
    }

    private fun Project.applyPlugins() {
        pluginManager.apply {
            apply("io.gitlab.arturbosch.detekt")
        }
    }
}