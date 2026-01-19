
import com.android.build.api.dsl.ApplicationExtension
import org.convention.configureGradleManagedDevices
import org.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

/**
 * Plugin that applies the Android application plugin and configures it.
 */
class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("com.dropbox.dependency-guard")
                apply("org.convention.detekt.plugin")
                apply("org.convention.spotless.plugin")
                apply("org.convention.git.hooks")
                apply("org.convention.android.application.lint")
                apply("org.convention.android.application.firebase")
            }

            extensions.configure<ApplicationExtension> {
                configureKotlinAndroid(this)
                defaultConfig.targetSdk = 36
                @Suppress("UnstableApiUsage")
                testOptions.animationsDisabled = true
                configureGradleManagedDevices(this)
            }
        }
    }
}
