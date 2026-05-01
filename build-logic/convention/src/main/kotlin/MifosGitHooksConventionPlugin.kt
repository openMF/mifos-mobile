
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Copy
import org.gradle.api.tasks.Exec
import org.gradle.kotlin.dsl.register
import java.util.Locale

class MifosGitHooksConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Define a function to check if the OS is Linux or MacOS
        fun isLinuxOrMacOs(): Boolean {
            val osName = System.getProperty("os.name").lowercase(Locale.getDefault())
            return osName.contains("linux") || osName.contains("mac os") || osName.contains("macos")
        }

        // Resolve the actual .git directory (handles submodules where .git is a file)
        fun resolveGitDir(): java.io.File {
            val dotGit = java.io.File(project.rootDir, ".git")
            return if (dotGit.isFile) {
                // Submodule: .git is a file with "gitdir: <path>"
                val gitdirLine = dotGit.readText().trim()
                val relativePath = gitdirLine.removePrefix("gitdir:").trim()
                java.io.File(project.rootDir, relativePath).canonicalFile
            } else {
                dotGit
            }
        }

        val gitDir = resolveGitDir()
        val hooksDir = java.io.File(gitDir, "hooks")

        // Define the copyGitHooks task
        project.tasks.register<Copy>("copyGitHooks") {
            description = "Copies the git hooks from /scripts to the .git/hooks folder."
            from("${project.rootDir}/scripts/") {
                include("**/*.sh")
                rename { it.removeSuffix(".sh") }
            }
            into(hooksDir)
        }

        // Define the installGitHooks task
        project.tasks.register<Exec>("installGitHooks") {
            description = "Installs the pre-commit git hooks from the scripts directory."
            group = "git hooks"
            workingDir = project.rootDir

            if (isLinuxOrMacOs()) {
                commandLine("chmod", "-R", "+x", hooksDir.absolutePath)
            }else {
                commandLine("cmd", "/c", "attrib", "-R", "+X", "${hooksDir.absolutePath}/*.*")
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