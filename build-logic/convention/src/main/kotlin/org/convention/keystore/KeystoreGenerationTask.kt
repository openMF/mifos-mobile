package org.convention.keystore

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * Gradle task for generating Android keystores using keytool
 * 
 * This task replicates the functionality of the keystore-manager.sh script's
 * generate_keystore and generate_keystores functions, providing native Gradle DSL
 * support for cross-platform keystore generation.
 */
@DisableCachingByDefault(because = "Keystore generation is not a cacheable task")
abstract class KeystoreGenerationTask : BaseKeystoreTask() {

    @get:Input
    @get:Optional
    abstract val generateOriginal: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val generateUpload: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val originalConfig: Property<KeystoreConfig>

    @get:Input
    @get:Optional
    abstract val uploadConfig: Property<KeystoreConfig>

    @get:OutputDirectory
    abstract val outputDirectory: DirectoryProperty

    init {
        description = "Generates Android keystores (ORIGINAL and UPLOAD) using keytool"
        
        // Set default values
        generateOriginal.convention(true)
        generateUpload.convention(true)
        outputDirectory.convention(project.layout.projectDirectory.dir("keystores"))
        
        // Set default keystore configurations
        originalConfig.convention(KeystoreConfig.original())
        uploadConfig.convention(KeystoreConfig.upload())
    }

    @TaskAction
    fun generateKeystores() {
        logInfo("Starting keystore generation task")

        // Validate keytool availability
        if (!checkKeytoolAvailable()) {
            throw IllegalStateException("keytool is not available. Please ensure JDK is installed and keytool is in PATH.")
        }

        // Create keystores directory
        val keystoreDir = outputDirectory.asFile.get()
        if (!ensureDirectoryExists(keystoreDir)) {
            throw IllegalStateException("Failed to create keystores directory: ${keystoreDir.absolutePath}")
        }

        var originalResult = true
        var uploadResult = true

        // Generate ORIGINAL keystore if requested
        if (generateOriginal.get()) {
            logInfo("=".repeat(66))
            logInfo("Generating ORIGINAL keystore")
            logInfo("=".repeat(66))
            
            val originalKeystoreConfig = originalConfig.get()
            originalResult = generateKeystore(
                environment = "ORIGINAL",
                config = originalKeystoreConfig,
                keystorePath = File(keystoreDir, originalKeystoreConfig.originalKeystoreName)
            )
        }

        // Generate UPLOAD keystore if requested
        if (generateUpload.get()) {
            logInfo("=".repeat(66))
            logInfo("Generating UPLOAD keystore")
            logInfo("=".repeat(66))
            
            val uploadKeystoreConfig = uploadConfig.get()
            uploadResult = generateKeystore(
                environment = "UPLOAD",
                config = uploadKeystoreConfig,
                keystorePath = File(keystoreDir, uploadKeystoreConfig.uploadKeystoreName)
            )
        }

        // Print summary
        printSummary(originalResult, uploadResult, keystoreDir)

        // Fail the task if any keystore generation failed
        if (!originalResult || !uploadResult) {
            throw IllegalStateException("One or more keystores failed to generate")
        }
    }

    /**
     * Generates a single keystore using keytool (matches script's generate_keystore function)
     */
    private fun generateKeystore(
        environment: String,
        config: KeystoreConfig,
        keystorePath: File
    ): Boolean {
        try {
            // Log keystore parameters
            logKeystoreParameters(environment, config, keystorePath)

            // Check if keystore already exists and handle overwrite behavior
            if (keystorePath.exists()) {
                if (config.overwriteExisting) {
                    logInfo("Overwriting existing keystore file '${keystorePath.name}'")
                } else {
                    logInfo("Keystore file '${keystorePath.name}' already exists and OVERWRITE is not set to 'true'")
                    logInfo("Using existing keystore")
                    return true
                }
            }

            // Build keytool command
            val command = buildKeytoolCommand(config, keystorePath)
            
            // Execute keytool command
            logInfo("Executing keytool command...")
            val success = executeKeytoolCommand(command)

            if (success && keystorePath.exists()) {
                logInfo("")
                logInfo("===== $environment Keystore created successfully! =====")
                logInfo("Keystore location: ${keystorePath.absolutePath}")
                logInfo("Keystore alias: ${config.keyAlias}")
                logInfo("")
                return true
            } else {
                logError("")
                logError("Error: Failed to create $environment keystore. Please check the error messages above.")
                return false
            }

        } catch (e: Exception) {
            logError("Exception during $environment keystore generation: ${e.message}")
            e.printStackTrace()
            return false
        }
    }

    /**
     * Logs keystore generation parameters (matches script output format)
     */
    private fun logKeystoreParameters(environment: String, config: KeystoreConfig, keystorePath: File) {
        logInfo("Generating keystore with the following parameters:")
        logInfo("- Environment: $environment")
        logInfo("- Keystore Name: ${keystorePath.absolutePath}")
        logInfo("- Key Alias: ${config.keyAlias}")
        logInfo("- Validity: ${config.validity} years")
        logInfo("- Key Algorithm: ${config.keyAlgorithm}")
        logInfo("- Key Size: ${config.keySize}")
        logInfo("- Distinguished Name: ${config.distinguishedName}")
    }

    /**
     * Builds the keytool command arguments (matches script's keytool invocation)
     */
    private fun buildKeytoolCommand(config: KeystoreConfig, keystorePath: File): List<String> {
        return listOf(
            "keytool",
            "-genkey",
            "-v",
            "-keystore", keystorePath.absolutePath,
            "-alias", config.keyAlias,
            "-keyalg", config.keyAlgorithm,
            "-keysize", config.keySize.toString(),
            "-validity", (config.validity * 365).toString(), // Convert years to days
            "-storepass", config.keystorePassword,
            "-keypass", config.keyPassword,
            "-dname", config.distinguishedName
        )
    }

    /**
     * Executes the keytool command with proper error handling
     */
    private fun executeKeytoolCommand(command: List<String>): Boolean {
        return try {
            logInfo("Command: ${command.joinToString(" ") { if (it.contains(" ")) "\"$it\"" else it }}")
            
            val processBuilder = ProcessBuilder(command)
            processBuilder.redirectErrorStream(true)
            
            val process = processBuilder.start()
            
            // Capture output
            val output = process.inputStream.bufferedReader().readText()
            
            // Wait for process to complete with timeout
            val finished = process.waitFor(60, TimeUnit.SECONDS)
            
            if (!finished) {
                logError("Keytool command timed out after 60 seconds")
                process.destroyForcibly()
                return false
            }
            
            val exitCode = process.exitValue()
            
            if (exitCode == 0) {
                logInfo("Keytool command completed successfully")
                if (output.isNotBlank()) {
                    logInfo("Keytool output:")
                    output.lines().forEach { line ->
                        if (line.isNotBlank()) {
                            logInfo("  $line")
                        }
                    }
                }
                true
            } else {
                logError("Keytool command failed with exit code: $exitCode")
                if (output.isNotBlank()) {
                    logError("Keytool error output:")
                    output.lines().forEach { line ->
                        if (line.isNotBlank()) {
                            logError("  $line")
                        }
                    }
                }
                false
            }
            
        } catch (e: Exception) {
            logError("Failed to execute keytool command: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    /**
     * Prints summary of keystore generation results (matches script summary format)
     */
    private fun printSummary(originalResult: Boolean, uploadResult: Boolean, keystoreDir: File) {
        logInfo("")
        logInfo("=".repeat(66))
        logInfo("                          SUMMARY")
        logInfo("=".repeat(66))

        if (generateOriginal.get()) {
            val originalKeystorePath = File(keystoreDir, originalConfig.get().originalKeystoreName)
            if (originalResult) {
                logInfo("ORIGINAL keystore: SUCCESS - ${originalKeystorePath.absolutePath}")
            } else {
                logError("ORIGINAL keystore: FAILED")
            }
        }

        if (generateUpload.get()) {
            val uploadKeystorePath = File(keystoreDir, uploadConfig.get().uploadKeystoreName)
            if (uploadResult) {
                logInfo("UPLOAD keystore: SUCCESS - ${uploadKeystorePath.absolutePath}")
            } else {
                logError("UPLOAD keystore: FAILED")
            }
        }

        logInfo("")
        logInfo("IMPORTANT: Keep these keystore files and their passwords in a safe place.")
        logInfo("If you lose them, you will not be able to update your application on the Play Store.")
    }

    companion object {
        /**
         * Creates a task with configurations loaded from secrets.env file
         */
        fun createWithSecretsConfig(
            task: KeystoreGenerationTask,
            secretsConfig: SecretsConfig = SecretsConfig()
        ) {
            // Parse secrets.env file if it exists
            val parser = SecretsEnvParser(secretsConfig)
            val parseResult = parser.parseFile()

            if (parseResult.isValid) {
                val secrets = parseResult.allSecrets

                // Apply ORIGINAL keystore configuration from secrets
                val originalKeystoreConfig = KeystoreConfig(
                    keystorePassword = secrets[secretsConfig.originalKeystorePasswordKey] 
                        ?: KeystoreConfig.original().keystorePassword,
                    keyAlias = secrets[secretsConfig.originalKeystoreAliasKey] 
                        ?: KeystoreConfig.original().keyAlias,
                    keyPassword = secrets[secretsConfig.originalKeystoreAliasPasswordKey] 
                        ?: KeystoreConfig.original().keyPassword,
                    companyName = secrets["COMPANY_NAME"] ?: KeystoreConfig.original().companyName,
                    department = secrets["DEPARTMENT"] ?: KeystoreConfig.original().department,
                    organization = secrets["ORGANIZATION"] ?: KeystoreConfig.original().organization,
                    city = secrets["CITY"] ?: KeystoreConfig.original().city,
                    state = secrets["STATE"] ?: KeystoreConfig.original().state,
                    country = secrets["COUNTRY"] ?: KeystoreConfig.original().country,
                    keyAlgorithm = secrets["KEYALG"] ?: KeystoreConfig.original().keyAlgorithm,
                    keySize = secrets["KEYSIZE"]?.toIntOrNull() ?: KeystoreConfig.original().keySize,
                    validity = secrets["VALIDITY"]?.toIntOrNull() ?: KeystoreConfig.original().validity,
                    originalKeystoreName = secrets["ORIGINAL_KEYSTORE_NAME"] 
                        ?: KeystoreConfig.original().originalKeystoreName,
                    overwriteExisting = secrets["OVERWRITE"]?.toBoolean() 
                        ?: KeystoreConfig.original().overwriteExisting
                )

                // Apply UPLOAD keystore configuration from secrets
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
                    keyAlgorithm = secrets["KEYALG"] ?: KeystoreConfig.upload().keyAlgorithm,
                    keySize = secrets["KEYSIZE"]?.toIntOrNull() ?: KeystoreConfig.upload().keySize,
                    validity = secrets["VALIDITY"]?.toIntOrNull() ?: KeystoreConfig.upload().validity,
                    uploadKeystoreName = secrets["UPLOAD_KEYSTORE_NAME"] 
                        ?: KeystoreConfig.upload().uploadKeystoreName,
                    overwriteExisting = secrets["OVERWRITE"]?.toBoolean() 
                        ?: KeystoreConfig.upload().overwriteExisting
                )

                task.originalConfig.set(originalKeystoreConfig)
                task.uploadConfig.set(uploadKeystoreConfig)

                task.logger.lifecycle("[KEYSTORE] Loaded configuration from ${secretsConfig.secretsEnvFile.name}")
            } else {
                task.logger.warn("[KEYSTORE] Could not parse ${secretsConfig.secretsEnvFile.name}: ${parseResult.errors}")
                task.logger.lifecycle("[KEYSTORE] Using default configurations")
            }
        }
    }
}
