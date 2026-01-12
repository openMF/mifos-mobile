package org.convention.keystore

import java.io.File

/**
 * Configuration for secrets.env file management matching keystore-manager.sh functionality
 */
data class SecretsConfig(
    // File paths
    val secretsEnvFile: File = File("secrets.env"),
    val backupDir: File = File("secrets-backup"),

    // GitHub secret names from script
    val originalKeystorePasswordKey: String = "ORIGINAL_KEYSTORE_FILE_PASSWORD",
    val originalKeystoreAliasKey: String = "ORIGINAL_KEYSTORE_ALIAS",
    val originalKeystoreAliasPasswordKey: String = "ORIGINAL_KEYSTORE_ALIAS_PASSWORD",
    val originalKeystoreFileKey: String = "ORIGINAL_KEYSTORE_FILE",

    val uploadKeystorePasswordKey: String = "UPLOAD_KEYSTORE_FILE_PASSWORD",
    val uploadKeystoreAliasKey: String = "UPLOAD_KEYSTORE_ALIAS",
    val uploadKeystoreAliasPasswordKey: String = "UPLOAD_KEYSTORE_ALIAS_PASSWORD",
    val uploadKeystoreFileKey: String = "UPLOAD_KEYSTORE_FILE",

    // Excluded keys from GitHub (matching script EXCLUDED_GITHUB_KEYS)
    val excludedGitHubKeys: Set<String> = setOf(
        "COMPANY_NAME", "DEPARTMENT", "ORGANIZATION", "CITY", "STATE", "COUNTRY",
        "VALIDITY", "KEYALG", "KEYSIZE", "OVERWRITE",
        "ORIGINAL_KEYSTORE_NAME", "UPLOAD_KEYSTORE_NAME",
        "CN", "OU", "O", "L", "ST", "C"
    ),

    // Base64 encoding settings
    val base64LineLength: Int = 76,
    val useHeredocFormat: Boolean = true,
    val heredocDelimiter: String = "EOF",

    // File processing options
    val createBackup: Boolean = true,
    val preserveComments: Boolean = true
) {

    /**
     * Checks if a key should be excluded from GitHub secrets
     */
    fun isKeyExcluded(key: String): Boolean = excludedGitHubKeys.contains(key)

    /**
     * Formats a multiline value using heredoc syntax (matching script format)
     */
    fun formatMultilineValue(key: String, value: String): String {
        return if (useHeredocFormat) {
            "$key<<$heredocDelimiter\n$value\n$heredocDelimiter"
        } else {
            val escapedValue = value.replace("\n", "\\n").replace("\"", "\\\"")
            "$key=\"$escapedValue\""
        }
    }

    /**
     * Gets backup file path with timestamp
     */
    fun getBackupFile(timestamp: String = System.currentTimeMillis().toString()): File {
        return File(backupDir, "secrets.env.backup.$timestamp")
    }
}