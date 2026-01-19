package org.convention.keystore

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.io.File

/**
 * Gradle task for updating configuration files with keystore information
 *
 * This task replicates the functionality of the keystore-manager.sh script's
 * update_fastlane_config and update_gradle_config functions, providing native
 * Gradle DSL support for updating configuration files after keystore generation.
 */
@DisableCachingByDefault(because = "Configuration file updates should always run")
abstract class ConfigurationFileUpdatesTask : BaseKeystoreTask() {

    @get:Input
    abstract val uploadKeystoreConfig: Property<KeystoreConfig>

    @get:Input
    @get:Optional
    abstract val fastlaneConfigPath: Property<String>

    @get:Input
    @get:Optional
    abstract val gradleBuildPath: Property<String>

    @get:Input
    @get:Optional
    abstract val updateFastlane: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val updateGradle: Property<Boolean>

    @get:InputFile
    @get:Optional
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    abstract val uploadKeystoreFile: RegularFileProperty

    init {
        description = "Updates configuration files (fastlane and gradle) with keystore information"

        // Set default values
        fastlaneConfigPath.convention("fastlane-config/android_config.rb")
        gradleBuildPath.convention("cmp-android/build.gradle.kts")
        updateFastlane.convention(true)
        updateGradle.convention(true)
        uploadKeystoreConfig.convention(KeystoreConfig.upload())
    }

    @TaskAction
    fun updateConfigurationFiles() {
        logInfo("Starting configuration file updates task")

        val config = uploadKeystoreConfig.get()
        val keystoreFile = uploadKeystoreFile.orNull?.asFile

        // Validate keystore configuration
        val validationErrors = config.validate()
        if (validationErrors.isNotEmpty()) {
            throw IllegalArgumentException("Invalid keystore configuration: ${validationErrors.joinToString(", ")}")
        }

        var fastlaneUpdated = false
        var gradleUpdated = false

        // Update fastlane configuration if requested
        if (updateFastlane.get()) {
            val fastlaneFile = File(project.rootDir, fastlaneConfigPath.get())
            try {
                updateFastlaneConfig(fastlaneFile, config, keystoreFile)
                fastlaneUpdated = true
                logInfo("Fastlane configuration updated successfully")
            } catch (e: Exception) {
                logError("Failed to update fastlane configuration: ${e.message}")
                throw e
            }
        }

        // Update gradle build file if requested
        if (updateGradle.get()) {
            val gradleFile = File(project.rootDir, gradleBuildPath.get())
            try {
                updateGradleConfig(gradleFile, config, keystoreFile)
                gradleUpdated = true
                logInfo("Gradle build file updated successfully")
            } catch (e: Exception) {
                logError("Failed to update gradle configuration: ${e.message}")
                throw e
            }
        }

        // Print summary
        printSummary(fastlaneUpdated, gradleUpdated)
    }

    /**
     * Updates fastlane-config/android_config.rb with keystore information
     * Matches the update_fastlane_config() function from keystore-manager.sh
     */
    private fun updateFastlaneConfig(configFile: File, config: KeystoreConfig, keystoreFile: File?) {
        logInfo("Updating fastlane configuration with keystore information...")

        // Determine keystore file name
        val keystoreName = keystoreFile?.name ?: config.uploadKeystoreName

        // Create the fastlane-config directory if it doesn't exist
        val configDir = configFile.parentFile
        if (!configDir.exists()) {
            logInfo("Creating '${configDir.name}' directory...")
            if (!ensureDirectoryExists(configDir)) {
                throw IllegalStateException("Failed to create fastlane-config directory")
            }
        }

        // Check if the config file exists
        if (configFile.exists()) {
            logInfo("Updating existing ${configFile.name}")
            updateExistingFastlaneConfig(configFile, config, keystoreName)
        } else {
            logInfo("Creating new ${configFile.name}")
            createNewFastlaneConfig(configFile, config, keystoreName)
        }
    }

    /**
     * Updates existing fastlane config file by replacing keystore values
     */
    private fun updateExistingFastlaneConfig(configFile: File, config: KeystoreConfig, keystoreName: String) {
        val content = configFile.readText()

        // Use regex to replace the values while preserving file structure
        val updatedContent = content
            .replace(Regex("default_store_file:\\s*\"[^\"]*\""), "default_store_file: \"$keystoreName\"")
            .replace(
                Regex("default_store_password:\\s*\"[^\"]*\""),
                "default_store_password: \"${config.keystorePassword}\"",
            )
            .replace(Regex("default_key_alias:\\s*\"[^\"]*\""), "default_key_alias: \"${config.keyAlias}\"")
            .replace(Regex("default_key_password:\\s*\"[^\"]*\""), "default_key_password: \"${config.keyPassword}\"")

        configFile.writeText(updatedContent)
    }

    /**
     * Creates new fastlane config file with complete structure
     */
    private fun createNewFastlaneConfig(configFile: File, config: KeystoreConfig, keystoreName: String) {
        val content = """
module FastlaneConfig
  module AndroidConfig
    STORE_CONFIG = {
      default_store_file: "$keystoreName",
      default_store_password: "${config.keystorePassword}",
      default_key_alias: "${config.keyAlias}",
      default_key_password: "${config.keyPassword}"
    }

    FIREBASE_CONFIG = {
      firebase_prod_app_id: "1:728433984912738:android:3902eb32kjaska3363b0938f1a1dbb",
      firebase_demo_app_id: "1:72843493212738:android:8392hjks3298ak9032skja",
      firebase_service_creds_file: "secrets/firebaseAppDistributionServiceCredentialsFile.json",
      firebase_groups: "mifos-mobile-apps"
    }

    BUILD_PATHS = {
      prod_apk_path: "cmp-android/build/outputs/apk/prod/release/cmp-android-prod-release.apk",
      demo_apk_path: "cmp-android/build/outputs/apk/demo/release/cmp-android-demo-release.apk",
      prod_aab_path: "cmp-android/build/outputs/bundle/prodRelease/cmp-android-prod-release.aab"
    }
  end
end
        """.trimIndent()

        configFile.writeText(content)
    }

    /**
     * Updates cmp-android/build.gradle.kts with keystore information
     * Matches the update_gradle_config() function from keystore-manager.sh
     */
    private fun updateGradleConfig(gradleFile: File, config: KeystoreConfig, keystoreFile: File?) {
        logInfo("Updating Gradle build file with keystore information...")

        // Check if the file exists
        if (!gradleFile.exists()) {
            logWarning("Gradle file not found: ${gradleFile.absolutePath}")
            logWarning("Skipping Gradle build file update")
            return
        }

        logInfo("Updating existing ${gradleFile.name}")

        // Determine keystore path - use relative path from cmp-android directory
        val keystorePath = if (keystoreFile != null) {
            val gradleDir = gradleFile.parentFile
            val relativePath = gradleDir.toPath().relativize(keystoreFile.toPath()).toString()
            relativePath.replace('\\', '/') // Ensure forward slashes for Gradle
        } else {
            "../keystores/${config.uploadKeystoreName}"
        }

        // Read the current content
        val content = gradleFile.readText()

        // Create backup
        val backupFile = File(gradleFile.absolutePath + ".bak")
        gradleFile.copyTo(backupFile, overwrite = true)

        try {
            // Use regex to update the signing configuration lines
            val updatedContent = content
                .replace(
                    Regex("storeFile = file\\(System\\.getenv\\(\"KEYSTORE_PATH\"\\) \\?\\: \"[^\"]*\"\\)"),
                    "storeFile = file(System.getenv(\"KEYSTORE_PATH\") ?: \"$keystorePath\")",
                )
                .replace(
                    Regex("storePassword = System\\.getenv\\(\"KEYSTORE_PASSWORD\"\\) \\?\\: \"[^\"]*\""),
                    "storePassword = System.getenv(\"KEYSTORE_PASSWORD\") ?: \"${config.keystorePassword}\"",
                )
                .replace(
                    Regex("keyAlias = System\\.getenv\\(\"KEYSTORE_ALIAS\"\\) \\?\\: \"[^\"]*\""),
                    "keyAlias = System.getenv(\"KEYSTORE_ALIAS\") ?: \"${config.keyAlias}\"",
                )
                .replace(
                    Regex("keyPassword = System\\.getenv\\(\"KEYSTORE_ALIAS_PASSWORD\"\\) \\?\\: \"[^\"]*\""),
                    "keyPassword = System.getenv(\"KEYSTORE_ALIAS_PASSWORD\") ?: \"${config.keyPassword}\"",
                )

            gradleFile.writeText(updatedContent)

            // Remove backup file if update was successful
            backupFile.delete()

        } catch (e: Exception) {
            // Restore from backup if update failed
            if (backupFile.exists()) {
                backupFile.copyTo(gradleFile, overwrite = true)
                backupFile.delete()
            }
            throw e
        }
    }

    /**
     * Prints a summary of the configuration file updates
     */
    private fun printSummary(fastlaneUpdated: Boolean, gradleUpdated: Boolean) {
        logInfo("")
        logInfo("=".repeat(66))
        logInfo("                    UPDATE SUMMARY")
        logInfo("=".repeat(66))

        if (updateFastlane.get()) {
            val fastlaneFile = File(project.rootDir, fastlaneConfigPath.get())
            if (fastlaneUpdated) {
                logInfo("Fastlane config: SUCCESS - ${fastlaneFile.absolutePath}")
            } else {
                logError("Fastlane config: FAILED")
            }
        }

        if (updateGradle.get()) {
            val gradleFile = File(project.rootDir, gradleBuildPath.get())
            if (gradleUpdated) {
                logInfo("Gradle config: SUCCESS - ${gradleFile.absolutePath}")
            } else {
                logError("Gradle config: FAILED")
            }
        }

        if (fastlaneUpdated || gradleUpdated) {
            logInfo("")
            logInfo("Configuration files have been updated with keystore information")
        }
    }

    companion object {
        /**
         * Creates a task configured for the upload keystore from keystore generation task
         */
        fun createForUploadKeystore(
            task: ConfigurationFileUpdatesTask,
            keystoreGenerationTask: KeystoreGenerationTask,
        ) {
            // Use the upload keystore configuration from the generation task
            task.uploadKeystoreConfig.set(keystoreGenerationTask.uploadConfig)

            // Set the keystore file from the output directory
            val keystoreFile = keystoreGenerationTask.outputDirectory.file(
                keystoreGenerationTask.uploadConfig.map { it.uploadKeystoreName },
            )
            task.uploadKeystoreFile.set(keystoreFile)

            // Make this task depend on keystore generation
            task.dependsOn(keystoreGenerationTask)
        }

        /**
         * Creates a task with configurations loaded from secrets.env file
         */
        fun createWithSecretsConfig(
            task: ConfigurationFileUpdatesTask,
            secretsConfig: SecretsConfig = SecretsConfig(),
        ) {
            // Parse secrets.env file if it exists
            val parser = SecretsEnvParser(secretsConfig)
            val parseResult = parser.parseFile()

            if (parseResult.isValid) {
                val secrets = parseResult.allSecrets

                // Apply UPLOAD keystore configuration from secrets (used for config file updates)
                val uploadKeystoreConfig = KeystoreConfig(
                    keystorePassword = secrets[secretsConfig.uploadKeystorePasswordKey]
                        ?: KeystoreConfig.upload().keystorePassword,
                    keyAlias = secrets[secretsConfig.uploadKeystoreAliasKey]
                        ?: KeystoreConfig.upload().keyAlias,
                    keyPassword = secrets[secretsConfig.uploadKeystoreAliasPasswordKey]
                        ?: KeystoreConfig.upload().keyPassword,
                    companyName = secrets["COMPANY_NAME"] ?: KeystoreConfig.upload().companyName,
                    department = secrets["DEPARTMENT"] ?: KeystoreConfig.upload().department,
                    organization = secrets["ORGANIZATION"] ?: KeystoreConfig.upload().organization,
                    city = secrets["CITY"] ?: KeystoreConfig.upload().city,
                    state = secrets["STATE"] ?: KeystoreConfig.upload().state,
                    country = secrets["COUNTRY"] ?: KeystoreConfig.upload().country,
                    uploadKeystoreName = secrets["UPLOAD_KEYSTORE_NAME"]
                        ?: KeystoreConfig.upload().uploadKeystoreName,
                )

                task.uploadKeystoreConfig.set(uploadKeystoreConfig)
                task.logger.lifecycle("[KEYSTORE] Loaded configuration from ${secretsConfig.secretsEnvFile.name}")
            } else {
                task.logger.warn("[KEYSTORE] Could not parse ${secretsConfig.secretsEnvFile.name}: ${parseResult.errors}")
                task.logger.lifecycle("[KEYSTORE] Using default configurations")
            }
        }
    }
}