package org.convention.keystore

import org.gradle.api.DefaultTask
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.work.DisableCachingByDefault
import java.io.File

/**
 * Base class for keystore management tasks following your existing convention patterns
 */
@DisableCachingByDefault(because = "Keystore generation is not a cacheable task")
abstract class BaseKeystoreTask : DefaultTask() {

    @get:Input
    @get:Optional
    abstract val keystoreConfig: Property<KeystoreConfig>

    @get:Input
    @get:Optional
    abstract val secretsConfig: Property<SecretsConfig>

    init {
        group = "keystore"
        description = "Base task for keystore management operations"

        // Set default configurations
        keystoreConfig.convention(KeystoreConfig())
        secretsConfig.convention(SecretsConfig())
    }

    /**
     * Logs task execution with consistent formatting
     */
    protected fun logInfo(message: String) {
        logger.lifecycle("[KEYSTORE] $message")
    }

    protected fun logWarning(message: String) {
        logger.warn("[KEYSTORE] $message")
    }

    protected fun logError(message: String) {
        logger.error("[KEYSTORE] $message")
    }

    /**
     * Validates configurations before task execution
     */
    protected fun validateConfiguration(): Boolean {
        val keystoreErrors = keystoreConfig.get().validate()
        if (keystoreErrors.isNotEmpty()) {
            logError("Keystore configuration errors:")
            keystoreErrors.forEach { logError("  - $it") }
            return false
        }
        return true
    }

    /**
     * Creates directory if it doesn't exist
     */
    protected fun ensureDirectoryExists(directory: File): Boolean {
        return if (!directory.exists()) {
            val created = directory.mkdirs()
            if (created) {
                logInfo("Created directory: ${directory.absolutePath}")
            } else {
                logError("Failed to create directory: ${directory.absolutePath}")
            }
            created
        } else {
            true
        }
    }

    /**
     * Checks if keytool is available (matches script check_keytool function)
     */
    protected fun checkKeytoolAvailable(): Boolean {
        return try {
            val process = ProcessBuilder("keytool", "-help").start()
            val exitCode = process.waitFor()
            if (exitCode == 0) {
                logInfo("keytool is available")
                true
            } else {
                logError("keytool command failed")
                false
            }
        } catch (e: Exception) {
            logError("keytool not found. Please ensure JDK is installed and keytool is in PATH")
            false
        }
    }
}