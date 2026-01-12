package org.convention.keystore

/**
 * Validates configuration parameters for keystore management
 * Implements comprehensive validation logic matching keystore-manager.sh script validation
 */
class ConfigurationValidator {

    /**
     * Validation result containing errors and warnings
     */
    data class ValidationResult(
        val errors: List<String>,
        val warnings: List<String>
    ) {
        val isValid: Boolean get() = errors.isEmpty()
        val hasWarnings: Boolean get() = warnings.isNotEmpty()
    }

    /**
     * Validates keystore configuration
     */
    fun validateKeystoreConfig(config: KeystoreConfig): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // Validate passwords
        validatePassword(config.keystorePassword, "keystore password", errors, warnings)
        validatePassword(config.keyPassword, "key password", errors, warnings)

        // Validate key alias
        if (config.keyAlias.isBlank()) {
            errors.add("Key alias cannot be blank")
        } else if (config.keyAlias.length < 3) {
            warnings.add("Key alias is very short (${config.keyAlias.length} characters)")
        }

        // Validate algorithm and key size
        validateKeyAlgorithm(config.keyAlgorithm, config.keySize, errors, warnings)

        // Validate validity period
        if (config.validity <= 0) {
            errors.add("Validity period must be positive")
        } else if (config.validity < 1) {
            warnings.add("Validity period is very short (${config.validity} years)")
        } else if (config.validity > 50) {
            warnings.add("Validity period is very long (${config.validity} years)")
        }

        // Validate distinguished name components
        validateDistinguishedName(config, errors, warnings)

        // Validate file paths
        validateFilePaths(config, errors, warnings)

        return ValidationResult(errors, warnings)
    }

    /**
     * Validates secrets configuration
     */
    fun validateSecretsConfig(config: SecretsConfig): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // Validate file paths
        if (config.secretsEnvFile.name.isBlank()) {
            errors.add("Secrets file name cannot be blank")
        }

        if (!config.secretsEnvFile.name.endsWith(".env")) {
            warnings.add("Secrets file should have .env extension")
        }

        // Validate backup settings
        if (config.createBackup && config.backupDir.name.isBlank()) {
            errors.add("Backup directory name cannot be blank when backup is enabled")
        }

        // Validate heredoc settings
        if (config.useHeredocFormat && config.heredocDelimiter.isBlank()) {
            errors.add("Heredoc delimiter cannot be blank when heredoc format is enabled")
        }

        if (config.base64LineLength <= 0) {
            errors.add("Base64 line length must be positive")
        }

        // Validate secret key names
        validateSecretKeyNames(config, errors, warnings)

        return ValidationResult(errors, warnings)
    }

    /**
     * Validates environment configuration by checking system properties and environment variables
     */
    fun validateEnvironmentConfiguration(): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // Check Java version for keytool compatibility
        val javaVersion = System.getProperty("java.version")
        if (javaVersion != null) {
            val majorVersion = extractJavaMajorVersion(javaVersion)
            if (majorVersion < 8) {
                errors.add("Java 8 or higher is required for keytool. Current version: $javaVersion")
            }
        } else {
            warnings.add("Unable to determine Java version")
        }

        // Check OS compatibility
        val osName = System.getProperty("os.name")?.lowercase()
        if (osName?.contains("windows") == true) {
            warnings.add("Windows detected. Ensure proper path handling for keystore files")
        }

        // Check available memory
        val maxMemory = Runtime.getRuntime().maxMemory()
        val availableMemory = maxMemory / (1024 * 1024) // Convert to MB
        if (availableMemory < 256) {
            warnings.add("Low available memory ($availableMemory MB). Keystore operations may be slow")
        }

        return ValidationResult(errors, warnings)
    }

    /**
     * Validates parsed secrets from secrets.env file
     */
    fun validateParsedSecrets(parseResult: SecretsEnvParser.ParseResult, config: SecretsConfig): ValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // First check if parsing was successful
        if (!parseResult.isValid) {
            errors.addAll(parseResult.errors)
            return ValidationResult(errors, warnings)
        }

        val allSecrets = parseResult.allSecrets

        // Validate required keystore secrets
        validateRequiredKeystoreSecrets(allSecrets, config, errors)

        // Validate secret values
        validateSecretValues(allSecrets, warnings)

        // Check for potential issues
        validateSecretPatterns(allSecrets, warnings)

        return ValidationResult(errors, warnings)
    }

    private fun validatePassword(password: String, passwordType: String, errors: MutableList<String>, warnings: MutableList<String>) {
        when {
            password.isBlank() -> errors.add("$passwordType cannot be blank")
            password.length < 6 -> warnings.add("$passwordType is weak (less than 6 characters)")
            password == "android" || password == "password" -> warnings.add("$passwordType is using a default/common value")
            !password.any { it.isDigit() } -> warnings.add("$passwordType should contain at least one digit")
            !password.any { it.isLetter() } -> warnings.add("$passwordType should contain at least one letter")
        }
    }

    private fun validateKeyAlgorithm(algorithm: String, keySize: Int, errors: MutableList<String>, warnings: MutableList<String>) {
        when (algorithm.uppercase()) {
            "RSA" -> {
                when {
                    keySize < 2048 -> errors.add("RSA key size must be at least 2048 bits")
                    keySize > 4096 -> warnings.add("RSA key size is very large ($keySize bits), may slow down operations")
                }
            }
            "DSA" -> {
                if (keySize < 1024) {
                    errors.add("DSA key size must be at least 1024 bits")
                }
                warnings.add("DSA algorithm is less commonly used than RSA")
            }
            "EC" -> {
                if (keySize < 256) {
                    errors.add("EC key size must be at least 256 bits")
                }
            }
            else -> {
                warnings.add("Unknown or uncommon key algorithm: $algorithm")
            }
        }
    }

    private fun validateDistinguishedName(config: KeystoreConfig, errors: MutableList<String>, warnings: MutableList<String>) {
        // Validate country code
        if (config.country.length != 2) {
            errors.add("Country code must be exactly 2 characters")
        } else if (!config.country.matches(Regex("[A-Z]{2}"))) {
            warnings.add("Country code should be uppercase letters")
        }

        // Check for common DN issues
        if (config.companyName.contains("Android Debug") && config.keyAlias != "androiddebugkey") {
            warnings.add("Using debug certificate info for non-debug keystore")
        }

        // Validate required DN components
        if (config.companyName.isBlank()) {
            warnings.add("Company name (CN) is blank")
        }

        if (config.organization.isBlank()) {
            warnings.add("Organization (O) is blank")
        }
    }

    private fun validateFilePaths(config: KeystoreConfig, errors: MutableList<String>, warnings: MutableList<String>) {
        // Check keystore directory
        if (!config.keystoreDir.exists() && !config.keystoreDir.mkdirs()) {
            errors.add("Cannot create keystore directory: ${config.keystoreDir.absolutePath}")
        }

        // Check file names
        if (!config.originalKeystoreName.endsWith(".keystore") && !config.originalKeystoreName.endsWith(".jks")) {
            warnings.add("Original keystore file should have .keystore or .jks extension")
        }

        if (!config.uploadKeystoreName.endsWith(".keystore") && !config.uploadKeystoreName.endsWith(".jks")) {
            warnings.add("Upload keystore file should have .keystore or .jks extension")
        }

        // Check for file conflicts
        if (config.originalKeystorePath.exists() && !config.overwriteExisting) {
            warnings.add("Original keystore file already exists and overwrite is disabled")
        }

        if (config.uploadKeystorePath.exists() && !config.overwriteExisting) {
            warnings.add("Upload keystore file already exists and overwrite is disabled")
        }
    }

    private fun validateSecretKeyNames(config: SecretsConfig, errors: MutableList<String>, warnings: MutableList<String>) {
        val secretKeys = listOf(
            config.originalKeystorePasswordKey,
            config.originalKeystoreAliasKey,
            config.originalKeystoreAliasPasswordKey,
            config.originalKeystoreFileKey,
            config.uploadKeystorePasswordKey,
            config.uploadKeystoreAliasKey,
            config.uploadKeystoreAliasPasswordKey,
            config.uploadKeystoreFileKey
        )

        secretKeys.forEach { key ->
            if (key.isBlank()) {
                errors.add("Secret key name cannot be blank")
            } else if (!key.matches(Regex("[A-Z_][A-Z0-9_]*"))) {
                warnings.add("Secret key '$key' should follow UPPER_SNAKE_CASE convention")
            }
        }
    }

    private fun validateRequiredKeystoreSecrets(secrets: Map<String, String>, config: SecretsConfig, errors: MutableList<String>) {
        val requiredKeys = listOf(
            config.originalKeystorePasswordKey,
            config.originalKeystoreAliasKey,
            config.originalKeystoreAliasPasswordKey,
            config.uploadKeystorePasswordKey,
            config.uploadKeystoreAliasKey,
            config.uploadKeystoreAliasPasswordKey
        )

        requiredKeys.forEach { key ->
            when {
                !secrets.containsKey(key) -> errors.add("Missing required secret: $key")
                secrets[key].isNullOrBlank() -> errors.add("Empty value for required secret: $key")
            }
        }
    }

    private fun validateSecretValues(secrets: Map<String, String>, warnings: MutableList<String>) {
        secrets.forEach { (key, value) ->
            when {
                key.contains("PASSWORD") && value.length < 6 ->
                    warnings.add("Password secret '$key' is weak (less than 6 characters)")

                key.contains("ALIAS") && value.length < 3 ->
                    warnings.add("Alias secret '$key' is very short")

                value.contains("android") && !key.contains("ORIGINAL") ->
                    warnings.add("Secret '$key' contains 'android' which might be a default value")
            }
        }
    }

    private fun validateSecretPatterns(secrets: Map<String, String>, warnings: MutableList<String>) {
        // Check for duplicate values
        val valueGroups = secrets.values.groupBy { it }
        valueGroups.forEach { (value, occurrences) ->
            if (occurrences.size > 1 && value.isNotBlank()) {
                val keys = secrets.filterValues { it == value }.keys
                warnings.add("Duplicate value found in secrets: ${keys.joinToString(", ")}")
            }
        }

        // Check for suspicious patterns
        secrets.forEach { (key, value) ->
            when {
                value.matches(Regex(".*test.*", RegexOption.IGNORE_CASE)) ->
                    warnings.add("Secret '$key' contains 'test' - ensure this is not a test value")

                value.matches(Regex(".*demo.*", RegexOption.IGNORE_CASE)) ->
                    warnings.add("Secret '$key' contains 'demo' - ensure this is not a demo value")

                value.matches(Regex(".*example.*", RegexOption.IGNORE_CASE)) ->
                    warnings.add("Secret '$key' contains 'example' - ensure this is not an example value")
            }
        }
    }

    private fun extractJavaMajorVersion(version: String): Int {
        return try {
            val parts = version.split(".")
            if (parts[0] == "1" && parts.size > 1) {
                // Java 1.8 format
                parts[1].toInt()
            } else {
                // Java 9+ format
                parts[0].toInt()
            }
        } catch (e: Exception) {
            0 // Unknown version
        }
    }
}