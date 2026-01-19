import androidx.room.gradle.RoomExtension
import com.google.devtools.ksp.gradle.KspExtension
import org.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class KMPRoomConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("androidx.room")
            pluginManager.apply("com.google.devtools.ksp")

            extensions.configure<KspExtension> {
                arg("room.generateKotlin", "true")
            }

            extensions.configure<RoomExtension> {
                // The schemas directory contains a schema file for each version of the Room database.
                // This is required to enable Room auto migrations.
                // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
                schemaDirectory("$projectDir/schemas")
            }

            dependencies {
                // Adding ksp dependencies for multiple platforms
                "implementation"(libs.findLibrary("androidx.room.ktx").get())
                listOf(
                    "kspDesktop",
                    "kspAndroid",
                    "kspIosArm64",
                    "kspIosX64",
                    "kspIosSimulatorArm64",
                    // Add any other platform you may support
                ).forEach { platform ->
                    add(platform, libs.findLibrary("androidx.room.compiler").get())
//                    Kotlin Extensions and Coroutines support for Room
//                    add(platform, libs.findLibrary("androidx.room.ktx").get())
                }
            }
        }
    }
}