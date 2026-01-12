import org.convention.keystore.ConfigurationFileUpdatesTask
import org.convention.keystore.KeystoreConfig
import org.convention.keystore.KeystoreGenerationTask
import org.convention.keystore.SecretsConfig
import org.convention.keystore.SecretsEnvUpdateTask
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Convention plugin for keystore management following your existing patterns
 */
class KeystoreManagementConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            // Create extension for configuration
            val keystoreExtension = extensions.create("keystoreManagement", KeystoreManagementExtension::class.java)

            // Set default configurations
            keystoreExtension.keystoreConfig.convention(KeystoreConfig())
            keystoreExtension.secretsConfig.convention(SecretsConfig())

            // Register the keystore generation task
            val generateKeystoresTask = tasks.register("generateKeystores", KeystoreGenerationTask::class.java) {
                // Configure task with extension values
                keystoreConfig.set(keystoreExtension.keystoreConfig)
                secretsConfig.set(keystoreExtension.secretsConfig)

                // Load configuration from secrets.env if it exists
                KeystoreGenerationTask.createWithSecretsConfig(this, keystoreExtension.secretsConfig.get())
            }

            // Register configuration file updates task
            val updateConfigFilesTask = tasks.register("updateConfigurationFiles", ConfigurationFileUpdatesTask::class.java) {
                // Load configuration from secrets.env if it exists
                ConfigurationFileUpdatesTask.createWithSecretsConfig(this, keystoreExtension.secretsConfig.get())
            }

            // Register secrets.env update task (KMPPT-57)
            val updateSecretsEnvTask = tasks.register("updateSecretsEnv", SecretsEnvUpdateTask::class.java) {
                // Configure to use keystores from generation task
                SecretsEnvUpdateTask.createFromKeystoreGeneration(this, generateKeystoresTask.get(), keystoreExtension.secretsConfig.get())
            }

            // Register combined task that generates keystores and updates config files
            tasks.register("generateKeystoresAndUpdateConfigs", KeystoreGenerationTask::class.java) {
                keystoreConfig.set(keystoreExtension.keystoreConfig)
                secretsConfig.set(keystoreExtension.secretsConfig)

                KeystoreGenerationTask.createWithSecretsConfig(this, keystoreExtension.secretsConfig.get())

                // Configure the update tasks to run after this task
                finalizedBy(updateConfigFilesTask)
                finalizedBy(updateSecretsEnvTask)
            }

            // Configure the update task to use generated keystores
            updateConfigFilesTask.configure {
                // Set dependency on keystore generation
                dependsOn(generateKeystoresTask)

                // Configure to use upload keystore from generation task
                ConfigurationFileUpdatesTask.createForUploadKeystore(this, generateKeystoresTask.get())
            }

            // Register convenience tasks for individual keystore types
            tasks.register("generateOriginalKeystore", KeystoreGenerationTask::class.java) {
                keystoreConfig.set(keystoreExtension.keystoreConfig)
                secretsConfig.set(keystoreExtension.secretsConfig)
                generateOriginal.set(true)
                generateUpload.set(false)

                KeystoreGenerationTask.createWithSecretsConfig(this, keystoreExtension.secretsConfig.get())
            }

            tasks.register("generateUploadKeystore", KeystoreGenerationTask::class.java) {
                keystoreConfig.set(keystoreExtension.keystoreConfig)
                secretsConfig.set(keystoreExtension.secretsConfig)
                generateOriginal.set(false)
                generateUpload.set(true)

                KeystoreGenerationTask.createWithSecretsConfig(this, keystoreExtension.secretsConfig.get())
            }

            // Add task group description
            tasks.register("keystoreHelp") {
                group = "keystore"
                description = "Shows available keystore management commands"
                doLast {
                    logger.lifecycle("""
                        |Keystore Management Plugin - Available Tasks:
                        |
                        |Generation Tasks:
                        |  - generateKeystores: Generate both ORIGINAL and UPLOAD keystores
                        |  - generateOriginalKeystore: Generate only the ORIGINAL (debug) keystore
                        |  - generateUploadKeystore: Generate only the UPLOAD (release) keystore
                        |  - generateKeystoresAndUpdateConfigs: Generate keystores and update config files
                        |
                        |Configuration Update Tasks:
                        |  - updateConfigurationFiles: Update fastlane and gradle config files with keystore info
                        |  - updateSecretsEnv: Update secrets.env with base64-encoded keystores (KMPPT-57)
                        |
                        |Help Tasks:
                        |  - keystoreHelp: Shows this help message
                        |
                        |Configuration:
                        |  The plugin automatically loads configuration from 'secrets.env' if it exists.
                        |  You can also configure manually in build.gradle.kts:
                        |
                        |  keystoreManagement {
                        |    keystoreConfig {
                        |      companyName = "Your Company Name"
                        |      department = "Your Department"
                        |      organization = "Your Organization"
                        |      city = "Your City"
                        |      state = "Your State"
                        |      country = "US"
                        |      keyAlgorithm = "RSA"
                        |      keySize = 2048
                        |      validity = 25
                        |      overwriteExisting = false
                        |    }
                        |    secretsConfig {
                        |      secretsEnvFile = file("secrets.env")
                        |      preserveComments = true
                        |      createBackup = true
                        |    }
                        |  }
                        |
                        |Usage Examples:
                        |  ./gradlew generateKeystores                    # Generate both keystores
                        |  ./gradlew generateOriginalKeystore             # Generate debug keystore only
                        |  ./gradlew generateUploadKeystore               # Generate release keystore only
                        |  ./gradlew generateKeystoresAndUpdateConfigs    # Generate keystores and update configs
                        |  ./gradlew updateConfigurationFiles            # Update config files only
                        |  ./gradlew updateSecretsEnv                    # Update secrets.env with base64 keystores
                        |
                        |Note: This task replicates the functionality of keystore-manager.sh
                        |      with better cross-platform compatibility and Gradle integration.
                    """.trimMargin())
                }
            }
        }
    }
}

/**
 * Extension class for keystore management configuration
 */
abstract class KeystoreManagementExtension {
    abstract val keystoreConfig: org.gradle.api.provider.Property<KeystoreConfig>
    abstract val secretsConfig: org.gradle.api.provider.Property<SecretsConfig>
}