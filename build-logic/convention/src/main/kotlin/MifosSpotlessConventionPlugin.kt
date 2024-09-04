import org.gradle.api.Plugin
import org.gradle.api.Project
import org.mifos.mobile.configureSpotless
import org.mifos.mobile.spotlessGradle

class MifosSpotlessConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            applyPlugins()

            spotlessGradle {
                configureSpotless(this)
            }
        }
    }

    private fun Project.applyPlugins() {
        pluginManager.apply {
            apply("com.diffplug.spotless")
        }
    }
}