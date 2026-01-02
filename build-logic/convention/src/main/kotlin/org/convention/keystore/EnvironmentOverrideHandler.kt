package org.convention.keystore

import java.io.File

/**
 * Handles environment variable overrides for keystore configuration
 * Matches the behavior of keystore-manager.sh script's environment variable handling
 */
class EnvironmentOverrideHandler {

    /**
     * Result of applying environment overrides
     */
    data class OverrideResult(
        val updatedSecrets: Map<String, String>,
        val overriddenKeys: Set<String>,
        val warnings: List<String>
    )

    /**
     * Applies environment variable overrides to parsed secrets
     * Environment variables take precedence over secrets.env file values
     */
    fun applyEnvironmentOverrides(
        parsedSecrets: Map<String, String>,
        config: SecretsConfig
    ): OverrideResult {
        val updatedSecrets = parsedSecrets.toMutableMap()
        val overriddenKeys = mutableSetOf<String>()
        val warnings = mutableListOf<String>()

        // Get all environment variables
        val envVars = System.getenv()

        // Apply overrides for all secrets
        parsedSecrets.keys.forEach { key ->
            val envValue = envVars[key]
            if (envValue != null) {
                if (envValue != parsedSecrets[key]) {
                    updatedSecrets[key] = envValue
                    overriddenKeys.add(key)

                    // Warn about excluded keys being overridden
                    if (config.isKeyExcluded(key)) {
                        warnings.add("Environment variable override for excluded key: $key")
                    }
                }
            }
        }

        // Check for additional environment variables that might be keystore-related
        val keystoreRelatedEnvVars = findKeystoreRelatedEnvVars(envVars)
        keystoreRelatedEnvVars.forEach { (key, value) ->
            if (!updatedSecrets.containsKey(key)) {
                updatedSecrets[key] = value
                overriddenKeys.add(key)
                warnings.add("Added new secret from environment variable: $key")
            }
        }

        return OverrideResult(
            updatedSecrets = updatedSecrets,
            overriddenKeys = overriddenKeys,
            warnings = warnings
        )
    }

    /**
     * Creates a KeystoreConfig with environment variable overrides applied
     */
    fun createOverriddenKeystoreConfig(baseConfig: KeystoreConfig): KeystoreConfig {
        val env = System.getenv()

        return baseConfig.copy(
            // Basic keystore parameters
            keystorePassword = env["KEYSTORE_PASSWORD"] ?: baseConfig.keystorePassword,
            keyAlias = env["KEY_ALIAS"] ?: env["KEYSTORE_ALIAS"] ?: baseConfig.keyAlias,
            keyPassword = env["KEY_PASSWORD"] ?: env["KEYSTORE_KEY_PASSWORD"] ?: baseConfig.keyPassword,
            keyAlgorithm = env["KEYALG"] ?: baseConfig.keyAlgorithm,
            keySize = env["KEYSIZE"]?.toIntOrNull() ?: baseConfig.keySize,
            validity = env["VALIDITY"]?.toIntOrNull() ?: baseConfig.validity,

            // Certificate DN components (using both descriptive and traditional names)
            companyName = env["COMPANY_NAME"] ?: env["CN"] ?: baseConfig.companyName,
            department = env["DEPARTMENT"] ?: env["OU"] ?: baseConfig.department,
            organization = env["ORGANIZATION"] ?: env["O"] ?: baseConfig.organization,
            city = env["CITY"] ?: env["L"] ?: baseConfig.city,
            state = env["STATE"] ?: env["ST"] ?: baseConfig.state,
            country = env["COUNTRY"] ?: env["C"] ?: baseConfig.country,

            // File paths
            keystoreDir = env["KEYSTORE_DIR"]?.let { File(it) } ?: baseConfig.keystoreDir,
            originalKeystoreName = env["ORIGINAL_KEYSTORE_NAME"] ?: baseConfig.originalKeystoreName,
            uploadKeystoreName = env["UPLOAD_KEYSTORE_NAME"] ?: baseConfig.uploadKeystoreName,

            // Behavior flags
            overwriteExisting = env["OVERWRITE"]?.toBoolean() ?: baseConfig.overwriteExisting
        )
    }

    /**
     * Creates a SecretsConfig with environment variable overrides applied
     */
    fun createOverriddenSecretsConfig(baseConfig: SecretsConfig): SecretsConfig {
        val env = System.getenv()

        return baseConfig.copy(
            // File paths
            secretsEnvFile = env["SECRETS_ENV_FILE"]?.let { File(it) } ?: baseConfig.secretsEnvFile,
            backupDir = env["SECRETS_BACKUP_DIR"]?.let { File(it) } ?: baseConfig.backupDir,

            // Base64 encoding settings
            base64LineLength = env["BASE64_LINE_LENGTH"]?.toIntOrNull() ?: baseConfig.base64LineLength,
            useHeredocFormat = env["USE_HEREDOC_FORMAT"]?.toBoolean() ?: baseConfig.useHeredocFormat,
            heredocDelimiter = env["HEREDOC_DELIMITER"] ?: baseConfig.heredocDelimiter,

            // File processing options
            createBackup = env["CREATE_BACKUP"]?.toBoolean() ?: baseConfig.createBackup,
            preserveComments = env["PRESERVE_COMMENTS"]?.toBoolean() ?: baseConfig.preserveComments
        )
    }

    /**
     * Gets environment variables that override keystore file paths
     */
    fun getKeystorePathOverrides(): Map<String, String> {
        val env = System.getenv()
        val pathOverrides = mutableMapOf<String, String>()

        // Check for common keystore path environment variables
        env["KEYSTORE_PATH"]?.let { pathOverrides["KEYSTORE_PATH"] = it }
        env["ORIGINAL_KEYSTORE_PATH"]?.let { pathOverrides["ORIGINAL_KEYSTORE_PATH"] = it }
        env["UPLOAD_KEYSTORE_PATH"]?.let { pathOverrides["UPLOAD_KEYSTORE_PATH"] = it }
        env["RELEASE_KEYSTORE_PATH"]?.let { pathOverrides["RELEASE_KEYSTORE_PATH"] = it }
        env["DEBUG_KEYSTORE_PATH"]?.let { pathOverrides["DEBUG_KEYSTORE_PATH"] = it }

        return pathOverrides
    }

    /**
     * Checks if environment contains CI/CD specific variables
     */
    fun isRunningInCiCd(): Boolean {
        val env = System.getenv()

        // Common CI/CD environment indicators
        val ciIndicators = listOf(
            "CI", "CONTINUOUS_INTEGRATION",
            "GITHUB_ACTIONS", "GITLAB_CI", "JENKINS_URL", "TEAMCITY_VERSION",
            "TRAVIS", "CIRCLECI", "BUILDKITE", "BUILD_NUMBER"
        )

        return ciIndicators.any { env.containsKey(it) }
    }

    /**
     * Gets CI/CD specific environment information
     */
    fun getCiCdEnvironmentInfo(): Map<String, String> {
        val env = System.getenv()
        val ciInfo = mutableMapOf<String, String>()

        // GitHub Actions
        env["GITHUB_ACTIONS"]?.let { ciInfo["platform"] = "GitHub Actions" }
        env["GITHUB_REPOSITORY"]?.let { ciInfo["repository"] = it }
        env["GITHUB_REF"]?.let { ciInfo["ref"] = it }

        // GitLab CI
        env["GITLAB_CI"]?.let { ciInfo["platform"] = "GitLab CI" }
        env["CI_PROJECT_PATH"]?.let { ciInfo["repository"] = it }
        env["CI_COMMIT_REF_NAME"]?.let { ciInfo["ref"] = it }

        // Jenkins
        env["JENKINS_URL"]?.let { ciInfo["platform"] = "Jenkins" }
        env["JOB_NAME"]?.let { ciInfo["job"] = it }
        env["BUILD_NUMBER"]?.let { ciInfo["build"] = it }

        // Generic CI info
        env["CI"]?.let { if (ciInfo["platform"] == null) ciInfo["platform"] = "Generic CI" }

        return ciInfo
    }

    /**
     * Validates that required environment variables are set for CI/CD
     */
    fun validateCiCdEnvironment(requiredSecrets: List<String>): List<String> {
        val env = System.getenv()
        val errors = mutableListOf<String>()

        if (isRunningInCiCd()) {
            requiredSecrets.forEach { secret ->
                if (!env.containsKey(secret)) {
                    errors.add("Required environment variable not set in CI/CD: $secret")
                } else if (env[secret].isNullOrBlank()) {
                    errors.add("Required environment variable is empty in CI/CD: $secret")
                }
            }

            // Check for common CI/CD keystore secrets
            val commonCiSecrets = listOf(
                "KEYSTORE_PASSWORD", "KEY_PASSWORD", "KEYSTORE_ALIAS"
            )

            commonCiSecrets.forEach { secret ->
                if (!env.containsKey(secret)) {
                    errors.add("Common CI/CD keystore secret not found: $secret")
                }
            }
        }

        return errors
    }

    /**
     * Finds environment variables that appear to be keystore-related
     */
    private fun findKeystoreRelatedEnvVars(envVars: Map<String, String>): Map<String, String> {
        val keystorePatterns = listOf(
            ".*KEYSTORE.*", ".*KEY_.*", ".*ALIAS.*", ".*STORE_.*",
            ".*UPLOAD.*", ".*ORIGINAL.*", ".*RELEASE.*", ".*DEBUG.*"
        )

        return envVars.filterKeys { key ->
            keystorePatterns.any { pattern ->
                key.matches(Regex(pattern, RegexOption.IGNORE_CASE))
            }
        }
    }

    /**
     * Sanitizes environment variable values for logging (hides sensitive data)
     */
    fun sanitizeEnvVarForLogging(key: String, value: String): String {
        val sensitivePatterns = listOf(
            ".*PASSWORD.*", ".*SECRET.*", ".*KEY.*", ".*TOKEN.*", ".*PRIVATE.*"
        )

        return if (sensitivePatterns.any { key.matches(Regex(it, RegexOption.IGNORE_CASE)) }) {
            if (value.length <= 4) "***" else "${value.take(2)}${"*".repeat(value.length - 4)}${value.takeLast(2)}"
        } else {
            value
        }
    }

    /**
     * Creates a summary of all environment overrides applied
     */
    fun createOverrideSummary(overrideResult: OverrideResult): String {
        val summary = StringBuilder()

        summary.appendLine("Environment Override Summary:")
        summary.appendLine("=".repeat(50))

        if (overrideResult.overriddenKeys.isEmpty()) {
            summary.appendLine("No environment overrides applied")
        } else {
            summary.appendLine("Overridden keys (${overrideResult.overriddenKeys.size}):")
            overrideResult.overriddenKeys.sorted().forEach { key ->
                val value = overrideResult.updatedSecrets[key] ?: ""
                val sanitizedValue = sanitizeEnvVarForLogging(key, value)
                summary.appendLine("  $key = $sanitizedValue")
            }
        }

        if (overrideResult.warnings.isNotEmpty()) {
            summary.appendLine("\nWarnings:")
            overrideResult.warnings.forEach { warning ->
                summary.appendLine("  • $warning")
            }
        }

        // Add CI/CD information if applicable
        if (isRunningInCiCd()) {
            summary.appendLine("\nCI/CD Environment Detected:")
            getCiCdEnvironmentInfo().forEach { (key, value) ->
                summary.appendLine("  $key: $value")
            }
        }

        return summary.toString()
    }

    companion object {
        /**
         * Standard environment variable names for keystore configuration
         */
        object StandardEnvVars {
            const val KEYSTORE_PASSWORD = "KEYSTORE_PASSWORD"
            const val KEY_PASSWORD = "KEY_PASSWORD"
            const val KEYSTORE_ALIAS = "KEYSTORE_ALIAS"
            const val KEYSTORE_PATH = "KEYSTORE_PATH"
            const val KEYSTORE_DIR = "KEYSTORE_DIR"

            // Original keystore specific
            const val ORIGINAL_KEYSTORE_PASSWORD = "ORIGINAL_KEYSTORE_FILE_PASSWORD"
            const val ORIGINAL_KEYSTORE_ALIAS = "ORIGINAL_KEYSTORE_ALIAS"
            const val ORIGINAL_KEYSTORE_ALIAS_PASSWORD = "ORIGINAL_KEYSTORE_ALIAS_PASSWORD"

            // Upload keystore specific
            const val UPLOAD_KEYSTORE_PASSWORD = "UPLOAD_KEYSTORE_FILE_PASSWORD"
            const val UPLOAD_KEYSTORE_ALIAS = "UPLOAD_KEYSTORE_ALIAS"
            const val UPLOAD_KEYSTORE_ALIAS_PASSWORD = "UPLOAD_KEYSTORE_ALIAS_PASSWORD"
        }
    }
}