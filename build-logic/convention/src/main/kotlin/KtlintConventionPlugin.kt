import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Plugin that applies the Ktlint plugin and configures it.
 */
class KtlintConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins()
        }
    }

    private fun Project.applyPlugins() {
        pluginManager.apply {
            apply("org.jlleitschuh.gradle.ktlint")
        }
    }
}