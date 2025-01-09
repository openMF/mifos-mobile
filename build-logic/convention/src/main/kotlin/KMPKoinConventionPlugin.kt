import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.mifos.mobile.libs

class KMPKoinConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target){
            with(pluginManager){
                apply("com.google.devtools.ksp")
            }

            dependencies {
                val bom = libs.findLibrary("koin-bom").get()
                add("commonMainImplementation", platform(bom))
                add("commonMainImplementation", libs.findLibrary("koin.core").get())
                add("commonMainImplementation", libs.findLibrary("koin.annotations").get())
                add("kspCommonMainMetadata", libs.findLibrary("koin.ksp.compiler").get())
                add("commonTestImplementation", libs.findLibrary("koin.test").get())
            }
        }
    }

}