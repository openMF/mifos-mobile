package org.convention.keystore

import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.work.DisableCachingByDefault
import java.io.File
import java.util.Base64

/**
 * Gradle task for updating secrets.env file with base64-encoded keystores
 *
 * This task implements the functionality specified in KMPPT-57:
 * - Updates secrets.env with base64-encoded keystore content
 * - Maintains proper heredoc formatting for multiline values
 * - Preserves existing environment variables
 * - Handles file creation and updates seamlessly
 * - Provides base64 encoding functionality
 * - Implements multiline value formatting with proper heredoc syntax
 * - Includes file merge logic for existing variables
 * - Validates output format for GitHub CLI compatibility
 */
@DisableCachingByDefault(because = "Secrets file updates should always run")
abstract class SecretsEnvUpdateTask : BaseKeystoreTask() {

    @get:InputFile
    @get:Optional
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    abstract val originalKeystoreFile: RegularFileProperty

    @get:InputFile
    @get:Optional
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    abstract val uploadKeystoreFile: RegularFileProperty

    @get:OutputFile
    abstract val secretsEnvFile: RegularFileProperty

    @get:Input
    @get:Optional
    abstract val additionalSecrets: MapProperty<String, String>

    @get:Input
    @get:Optional
    abstract val preserveComments: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val createBackup: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val validateOutput: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val base64LineLength: Property<Int>

    @get:Input
    @get:Optional
    abstract val useHeredocFormat: Property<Boolean>

    @get:Input
    @get:Optional
    abstract val heredocDelimiter: Property<String>

    init {
        description = "Updates secrets.env file with base64-encoded keystores and maintains proper formatting"

        // Set default values
        preserveComments.convention(true)
        createBackup.convention(true)
        validateOutput.convention(true)
        base64LineLength.convention(76)
        useHeredocFormat.convention(true)
        heredocDelimiter.convention("EOF")
        secretsEnvFile.convention(project.layout.projectDirectory.file("secrets.env"))
        additionalSecrets.convention(emptyMap())
    }

    @TaskAction
    fun updateSecretsEnv() {
        logInfo("Starting secrets.env file update task")

        val secretsFile = secretsEnvFile.asFile.get()
        val config = secretsConfig.get()

        try {
            // Validate inputs first
            validateInputs()

            // Create backup if requested and file exists
            if (createBackup.get() && secretsFile.exists()) {
                createBackupFile(secretsFile, config)
            }

            // Parse existing secrets.env file if it exists
            val existingSecrets = if (secretsFile.exists()) {
                parseExistingSecretsFile(secretsFile, config)
            } else {
                logInfo("Creating new secrets.env file")
                ParsedSecretsData()
            }

            // Encode available keystores to base64
            val keystoreSecrets = encodeKeystoresToBase64()

            // Merge all secrets
            val mergedSecrets = mergeSecrets(existingSecrets, keystoreSecrets)

            // Ensure output directory exists
            secretsFile.parentFile?.mkdirs()

            // Write updated secrets.env file
            writeSecretsFile(secretsFile, mergedSecrets)

            // Validate output if requested
            if (validateOutput.get()) {
                validateSecretsFile(secretsFile, config)
            }

            logInfo("Secrets.env file updated successfully")
            printUpdateSummary(keystoreSecrets, mergedSecrets)

        } catch (e: Exception) {
            logError("Failed to update secrets.env file: ${e.message}")
            throw e
        }
    }

    /**
     * Data class to hold parsed secrets file content
     */
    private data class ParsedSecretsData(
        val simpleSecrets: MutableMap<String, String> = mutableMapOf(),
        val multilineSecrets: MutableMap<String, String> = mutableMapOf(),
        val comments: MutableList<String> = mutableListOf(),
        val originalOrder: MutableList<String> = mutableListOf(), // Track order of keys
    )

    /**
     * Validates task inputs before execution
     */
    private fun validateInputs() {
        val originalFile = originalKeystoreFile.orNull?.asFile
        val uploadFile = uploadKeystoreFile.orNull?.asFile

        // Check if at least one keystore file is provided and exists
        val hasValidOriginal = originalFile?.exists() == true
        val hasValidUpload = uploadFile?.exists() == true

        if (!hasValidOriginal && !hasValidUpload) {
            logWarning("No valid keystore files found. Task will only process existing secrets and additional secrets.")
        }

        // Validate file readability
        originalFile?.let { file ->
            if (file.exists() && !file.canRead()) {
                throw IllegalStateException("Cannot read original keystore file: ${file.absolutePath}")
            }
        }

        uploadFile?.let { file ->
            if (file.exists() && !file.canRead()) {
                throw IllegalStateException("Cannot read upload keystore file: ${file.absolutePath}")
            }
        }

        // Validate base64 line length
        if (base64LineLength.get() < 0) {
            throw IllegalArgumentException("Base64 line length cannot be negative")
        }

        // Validate heredoc delimiter
        if (useHeredocFormat.get() && heredocDelimiter.get().isBlank()) {
            throw IllegalArgumentException("Heredoc delimiter cannot be blank when heredoc format is enabled")
        }
    }

    /**
     * Creates a backup of the existing secrets file
     */
    private fun createBackupFile(secretsFile: File, config: SecretsConfig) {
        try {
            val timestamp = System.currentTimeMillis()
            val backupFile = config.getBackupFile(timestamp.toString())

            if (!ensureDirectoryExists(backupFile.parentFile)) {
                throw IllegalStateException("Failed to create backup directory: ${backupFile.parentFile.absolutePath}")
            }

            secretsFile.copyTo(backupFile, overwrite = true)
            logInfo("Created backup: ${backupFile.absolutePath}")
        } catch (e: Exception) {
            logWarning("Failed to create backup: ${e.message}")
            // Continue execution even if backup fails, unless it's critical
        }
    }

    /**
     * Parses existing secrets.env file while preserving structure and comments
     */
    private fun parseExistingSecretsFile(secretsFile: File, config: SecretsConfig): ParsedSecretsData {
        try {
            val parser = SecretsEnvParser(config)
            val parseResult = parser.parseFile(secretsFile)

            if (!parseResult.isValid) {
                logWarning("Issues parsing existing secrets file:")
                parseResult.errors.forEach { error -> logWarning("  - $error") }
                logWarning("Continuing with partial parsing...")
            }

            val parsedData = ParsedSecretsData()

            // Add simple secrets
            parseResult.secrets.forEach { (key, value) ->
                parsedData.simpleSecrets[key] = value
                parsedData.originalOrder.add(key)
            }

            // Add multiline secrets
            parseResult.multilineSecrets.forEach { (key, value) ->
                parsedData.multilineSecrets[key] = value
                parsedData.originalOrder.add(key)
            }

            // Preserve comments if requested
            if (preserveComments.get()) {
                parsedData.comments.addAll(parseResult.comments)
            }

            logInfo("Parsed existing secrets file: ${parsedData.simpleSecrets.size} simple + ${parsedData.multilineSecrets.size} multiline secrets")
            return parsedData

        } catch (e: Exception) {
            logError("Failed to parse existing secrets file: ${e.message}")
            logWarning("Starting with empty secrets data")
            return ParsedSecretsData()
        }
    }

    /**
     * Encodes available keystores to base64 format
     */
    private fun encodeKeystoresToBase64(): Map<String, String> {
        val keystoreSecrets = mutableMapOf<String, String>()
        val config = secretsConfig.get()

        // Encode original keystore if provided and exists
        originalKeystoreFile.orNull?.asFile?.let { file ->
            when {
                !file.exists() -> {
                    logInfo("Original keystore file not found: ${file.absolutePath}")
                }

                file.length() == 0L -> {
                    logWarning("Original keystore file is empty: ${file.absolutePath}")
                }

                file.length() > 50 * 1024 * 1024 -> { // 50MB limit
                    logWarning("Original keystore file is very large (${file.length() / (1024 * 1024)}MB): ${file.absolutePath}")
                    val base64Content = encodeFileToBase64(file)
                    keystoreSecrets[config.originalKeystoreFileKey] = base64Content
                    logInfo("Encoded ORIGINAL keystore: ${file.name}")
                }

                else -> {
                    val base64Content = encodeFileToBase64(file)
                    keystoreSecrets[config.originalKeystoreFileKey] = base64Content
                    logInfo("Encoded ORIGINAL keystore: ${file.name}")
                }
            }
        }

        // Encode upload keystore if provided and exists
        uploadKeystoreFile.orNull?.asFile?.let { file ->
            when {
                !file.exists() -> {
                    logInfo("Upload keystore file not found: ${file.absolutePath}")
                }

                file.length() == 0L -> {
                    logWarning("Upload keystore file is empty: ${file.absolutePath}")
                }

                file.length() > 50 * 1024 * 1024 -> { // 50MB limit
                    logWarning("Upload keystore file is very large (${file.length() / (1024 * 1024)}MB): ${file.absolutePath}")
                    val base64Content = encodeFileToBase64(file)
                    keystoreSecrets[config.uploadKeystoreFileKey] = base64Content
                    logInfo("Encoded UPLOAD keystore: ${file.name}")
                }

                else -> {
                    val base64Content = encodeFileToBase64(file)
                    keystoreSecrets[config.uploadKeystoreFileKey] = base64Content
                    logInfo("Encoded UPLOAD keystore: ${file.name}")
                }
            }
        }

        return keystoreSecrets
    }

    /**
     * Encodes a file to base64 with proper line wrapping and error handling
     */
    private fun encodeFileToBase64(file: File): String {
        try {
            val bytes = file.readBytes()
            val base64String = Base64.getEncoder().encodeToString(bytes)

            return if (base64LineLength.get() > 0) {
                // Wrap lines to specified length for better readability
                base64String.chunked(base64LineLength.get()).joinToString("\n")
            } else {
                base64String
            }
        } catch (e: Exception) {
            logError("Failed to encode file to base64: ${file.absolutePath}")
            throw IllegalStateException("Failed to encode keystore file: ${e.message}", e)
        }
    }

    /**
     * Merges existing secrets with new keystore secrets and additional secrets
     */
    private fun mergeSecrets(
        existingSecrets: ParsedSecretsData,
        keystoreSecrets: Map<String, String>,
    ): ParsedSecretsData {
        val mergedSecrets = ParsedSecretsData()

        // Copy existing secrets (preserving order)
        existingSecrets.originalOrder.forEach { key ->
            when {
                existingSecrets.simpleSecrets.containsKey(key) -> {
                    mergedSecrets.simpleSecrets[key] = existingSecrets.simpleSecrets[key]!!
                    mergedSecrets.originalOrder.add(key)
                }

                existingSecrets.multilineSecrets.containsKey(key) -> {
                    mergedSecrets.multilineSecrets[key] = existingSecrets.multilineSecrets[key]!!
                    mergedSecrets.originalOrder.add(key)
                }
            }
        }

        // Add or update keystore secrets (as multiline)
        keystoreSecrets.forEach { (key, value) ->
            if (mergedSecrets.originalOrder.contains(key)) {
                // Update existing keystore secret
                mergedSecrets.multilineSecrets[key] = value
                // Remove from simple secrets if it was there before
                mergedSecrets.simpleSecrets.remove(key)
                logInfo("Updated existing keystore secret: $key")
            } else {
                // Add new keystore secret
                mergedSecrets.multilineSecrets[key] = value
                mergedSecrets.originalOrder.add(key)
                logInfo("Added new keystore secret: $key")
            }
        }

        // Add additional secrets
        additionalSecrets.get().forEach { (key, value) ->
            if (!mergedSecrets.originalOrder.contains(key)) {
                mergedSecrets.simpleSecrets[key] = value
                mergedSecrets.originalOrder.add(key)
                logInfo("Added additional secret: $key")
            } else {
                logWarning("Additional secret '$key' conflicts with existing secret, skipping")
            }
        }

        // Preserve comments
        mergedSecrets.comments.addAll(existingSecrets.comments)

        return mergedSecrets
    }

    /**
     * Writes the merged secrets to the secrets.env file with proper formatting
     */
    private fun writeSecretsFile(
        secretsFile: File,
        secrets: ParsedSecretsData,
    ) {
        try {
            secretsFile.printWriter().use { writer ->
                // Write header comment if it's a new file or no comments exist
                if (!secretsFile.exists() || secrets.comments.isEmpty()) {
                    writer.println("# GitHub Secrets Environment File")
                    writer.println("# Generated by Gradle Keystore Management Plugin")
                    writer.println("# Format: KEY=VALUE")
                    writer.println("# Use <<EOF and EOF to denote multiline values")
                    writer.println("# Run this command to format these secrets: dos2unix secrets.env")
                    writer.println()
                }

                // Write preserved comments
                if (preserveComments.get() && secrets.comments.isNotEmpty()) {
                    secrets.comments.forEach { comment ->
                        writer.println(comment)
                    }
                    writer.println()
                }

                // Write secrets in preserved order
                secrets.originalOrder.forEach { key ->
                    when {
                        secrets.simpleSecrets.containsKey(key) -> {
                            val value = secrets.simpleSecrets[key]!!
                            writer.println("$key=${quoteValueIfNeeded(value)}")
                        }

                        secrets.multilineSecrets.containsKey(key) -> {
                            val value = secrets.multilineSecrets[key]!!
                            if (useHeredocFormat.get()) {
                                writer.println("$key<<${heredocDelimiter.get()}")
                                writer.println(value)
                                writer.println(heredocDelimiter.get())
                            } else {
                                // Fallback to escaped format
                                val escapedValue = value.replace("\n", "\\n").replace("\"", "\\\"")
                                writer.println("$key=\"$escapedValue\"")
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            throw IllegalStateException("Failed to write secrets file: ${secretsFile.absolutePath}", e)
        }
    }

    /**
     * Quotes value if it contains spaces or special characters
     */
    private fun quoteValueIfNeeded(value: String): String {
        return if (value.contains(" ") || value.contains("\t") || value.contains("\n") || value.contains("\"")) {
            // Escape quotes in the value
            val escapedValue = value.replace("\"", "\\\"")
            "\"$escapedValue\""
        } else {
            value
        }
    }

    /**
     * Validates the generated secrets file for GitHub CLI compatibility
     */
    private fun validateSecretsFile(secretsFile: File, config: SecretsConfig) {
        logInfo("Validating secrets.env file format...")

        try {
            val parser = SecretsEnvParser(config)
            val parseResult = parser.parseFile(secretsFile)

            if (parseResult.isValid) {
                logInfo("✅ Secrets file validation passed")

                // Additional GitHub CLI compatibility checks
                val allSecrets = parseResult.allSecrets
                val warnings = mutableListOf<String>()

                // Check for potential GitHub CLI issues
                allSecrets.forEach { (key, value) ->
                    when {
                        key.contains(" ") -> warnings.add("Key '$key' contains spaces")
                        key.contains("-") -> warnings.add("Key '$key' contains hyphens (consider using underscores)")
                        !key.matches(Regex("[A-Z_][A-Z0-9_]*")) -> warnings.add("Key '$key' doesn't follow UPPER_SNAKE_CASE convention")
                        value.isEmpty() -> warnings.add("Key '$key' has empty value")
                        key.length > 100 -> warnings.add("Key '$key' is very long (${key.length} chars)")
                        value.length > 1024 * 1024 -> warnings.add("Value for '$key' is very large (${value.length / 1024}KB)")
                    }
                }

                if (warnings.isNotEmpty()) {
                    logWarning("GitHub CLI compatibility warnings:")
                    warnings.forEach { warning -> logWarning("  - $warning") }
                } else {
                    logInfo("✅ No GitHub CLI compatibility issues found")
                }

            } else {
                logError("❌ Secrets file validation failed:")
                parseResult.errors.forEach { error -> logError("  - $error") }
                throw IllegalStateException("Generated secrets.env file is invalid: ${parseResult.errors.joinToString("/")} ")
            }
        } catch (e: Exception) {
            logError("Failed to validate secrets file: ${e.message}")
            throw e
        }
    }

    /**
     * Prints a summary of the update operation
     */
    private fun printUpdateSummary(keystoreSecrets: Map<String, String>, finalSecrets: ParsedSecretsData) {
        logInfo("")
        logInfo("=".repeat(66))
        logInfo("                    UPDATE SUMMARY")
        logInfo("=".repeat(66))

        if (keystoreSecrets.isNotEmpty()) {
            logInfo("Keystore secrets added/updated:")
            keystoreSecrets.forEach { (key, _) ->
                logInfo("  ✅ $key")
            }
        } else {
            logInfo("No keystore secrets processed")
        }

        val totalSecrets = finalSecrets.simpleSecrets.size + finalSecrets.multilineSecrets.size
        logInfo("")
        logInfo("Total secrets in file: $totalSecrets")
        logInfo("  - Simple secrets: ${finalSecrets.simpleSecrets.size}")
        logInfo("  - Multiline secrets: ${finalSecrets.multilineSecrets.size}")

        if (preserveComments.get() && finalSecrets.comments.isNotEmpty()) {
            logInfo("  - Comments preserved: ${finalSecrets.comments.size}")
        }

        logInfo("")
        logInfo("File location: ${secretsEnvFile.asFile.get().absolutePath}")

        if (createBackup.get()) {
            logInfo("Backup directory: ${secretsConfig.get().backupDir.absolutePath}")
        }

        logInfo("")
        logInfo("✅ Secrets.env file ready for GitHub CLI integration")
    }

    companion object {
        /**
         * Creates a task configured to update secrets from keystore generation task
         */
        fun createFromKeystoreGeneration(
            task: SecretsEnvUpdateTask,
            keystoreGenerationTask: KeystoreGenerationTask,
            secretsConfig: SecretsConfig = SecretsConfig(),
        ) {
            // Set keystore files from generation task output
            val originalKeystoreFile = keystoreGenerationTask.outputDirectory.file(
                keystoreGenerationTask.originalConfig.map { it.originalKeystoreName },
            )
            val uploadKeystoreFile = keystoreGenerationTask.outputDirectory.file(
                keystoreGenerationTask.uploadConfig.map { it.uploadKeystoreName },
            )

            task.originalKeystoreFile.set(originalKeystoreFile)
            task.uploadKeystoreFile.set(uploadKeystoreFile)
            task.secretsConfig.set(secretsConfig)

            // Make this task depend on keystore generation
            task.dependsOn(keystoreGenerationTask)
        }

        /**
         * Creates a task with explicit keystore file paths
         */
        fun createWithKeystoreFiles(
            task: SecretsEnvUpdateTask,
            originalKeystoreFile: File?,
            uploadKeystoreFile: File?,
            secretsConfig: SecretsConfig = SecretsConfig(),
        ) {
            originalKeystoreFile?.let { file ->
                task.originalKeystoreFile.set(file)
            }
            uploadKeystoreFile?.let { file ->
                task.uploadKeystoreFile.set(file)
            }
            task.secretsConfig.set(secretsConfig)
        }

        /**
         * Creates a task with additional environment secrets
         */
        fun createWithAdditionalSecrets(
            task: SecretsEnvUpdateTask,
            additionalSecrets: Map<String, String>,
            secretsConfig: SecretsConfig = SecretsConfig(),
        ) {
            task.additionalSecrets.set(additionalSecrets)
            task.secretsConfig.set(secretsConfig)
        }
    }
}
