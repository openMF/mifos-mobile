
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register
import java.util.Locale

/**
 * Plugin that installs the pre-commit git hooks from the scripts directory.
 */
class GitHooksConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Define a function to check if the OS is Linux or MacOS
        fun isLinuxOrMacOs(): Boolean {
            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
            return osName.contains("linux") || osName.contains("mac os") || osName.contains("macos")
        }

        // Define the copyGitHooks task
        project.tasks.register<Copy>("copyGitHooks") {
            description = "Copies the git hooks from /scripts to the .git/hooks folder."
            from("${project.rootDir}/scripts/") {
                include("**/*.sh")
                rename { it.removeSuffix(".sh") }
            }
            into("${project.rootDir}/.git/hooks")
        }

        // Define the installGitHooks task
        project.tasks.register<Exec>("installGitHooks") {
            description = "Installs the pre-commit git hooks from the scripts directory."
            group = "git hooks"
            workingDir = project.rootDir

            if (isLinuxOrMacOs()) {
                commandLine("chmod", "-R", "+x", ".git/hooks/")
            }else {
                commandLine("cmd", "/c", "attrib", "-R", "+X", ".git/hooks/*.*")
            }
            dependsOn(project.tasks.named("copyGitHooks"))

            doLast {
                println("Git hooks installed successfully.")
            }
        }

        // Configure task dependencies after evaluation
        project.afterEvaluate {
            project.tasks.matching {
                it.name in listOf("preBuild", "build", "assembleDebug", "assembleRelease", "installDebug", "installRelease", "clean")
            }.configureEach {
                dependsOn(project.tasks.named("installGitHooks"))
            }
        }
    }
}