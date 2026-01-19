package org.convention.keystore

import java.io.File

/**
 * Parser for secrets.env file with multiline (heredoc) support
 * Matches the functionality of the keystore-manager.sh script's load_env_vars function
 */
class SecretsEnvParser(private val config: SecretsConfig) {

    /**
     * Parsed result containing all secrets and metadata
     */
    data class ParseResult(
        val secrets: Map<String, String>,
        val multilineSecrets: Map<String, String>,
        val comments: List<String>,
        val errors: List<String>
    ) {
        val allSecrets: Map<String, String>
            get() = secrets + multilineSecrets

        val isValid: Boolean
            get() = errors.isEmpty()
    }

    /**
     * Parses the secrets.env file with full heredoc support
     * Matches the exact behavior of the bash script's multiline parsing
     */
    fun parseFile(file: File = config.secretsEnvFile): ParseResult {
        if (!file.exists()) {
            return ParseResult(
                secrets = emptyMap(),
                multilineSecrets = emptyMap(),
                comments = emptyList(),
                errors = listOf("File not found: ${file.absolutePath}")
            )
        }

        val secrets = mutableMapOf<String, String>()
        val multilineSecrets = mutableMapOf<String, String>()
        val comments = mutableListOf<String>()
        val errors = mutableListOf<String>()

        var lineNumber = 0
        var inMultilineBlock = false
        var multilineKey = ""
        var multilineEnd = ""
        val multilineValue = StringBuilder()

        try {
            file.forEachLine { line ->
                lineNumber++

                when {
                    // Handle multiline block termination
                    inMultilineBlock && line.trim() == multilineEnd -> {
                        multilineSecrets[multilineKey] = multilineValue.toString()
                        inMultilineBlock = false
                        multilineKey = ""
                        multilineEnd = ""
                        multilineValue.clear()
                    }

                    // Handle content inside multiline blocks
                    inMultilineBlock -> {
                        if (multilineValue.isNotEmpty()) {
                            multilineValue.appendLine()
                        }
                        multilineValue.append(line)
                    }

                    // Skip empty lines and comments when not in multiline mode
                    line.isBlank() || line.trimStart().startsWith("#") -> {
                        if (config.preserveComments && line.trimStart().startsWith("#")) {
                            comments.add(line)
                        }
                    }

                    // Check for multiline block start (KEY<<DELIMITER)
                    line.contains("<<") -> {
                        val parts = line.split("<<", limit = 2)
                        if (parts.size == 2) {
                            multilineKey = parts[0].trim()
                            multilineEnd = parts[1].trim()

                            if (multilineKey.isBlank()) {
                                errors.add("Line $lineNumber: Empty key in multiline declaration")
                            } else if (multilineEnd.isBlank()) {
                                errors.add("Line $lineNumber: Empty delimiter in multiline declaration")
                            } else {
                                inMultilineBlock = true
                                multilineValue.clear()
                            }
                        } else {
                            errors.add("Line $lineNumber: Invalid multiline syntax")
                        }
                    }

                    // Handle regular KEY=VALUE pairs
                    line.contains("=") -> {
                        val parts = line.split("=", limit = 2)
                        if (parts.size == 2) {
                            val key = parts[0].trim()
                            val rawValue = parts[1]

                            if (key.isBlank()) {
                                errors.add("Line $lineNumber: Empty key")
                            } else {
                                val cleanValue = stripQuotes(rawValue)
                                secrets[key] = cleanValue
                            }
                        } else {
                            errors.add("Line $lineNumber: Invalid key=value syntax")
                        }
                    }

                    else -> {
                        errors.add("Line $lineNumber: Unrecognized line format: $line")
                    }
                }
            }

            // Check for unterminated multiline blocks
            if (inMultilineBlock) {
                errors.add("Unterminated multiline block. Missing closing delimiter: $multilineEnd")
            }

        } catch (e: Exception) {
            errors.add("Failed to read file: ${e.message}")
        }

        return ParseResult(
            secrets = secrets,
            multilineSecrets = multilineSecrets,
            comments = comments,
            errors = errors
        )
    }

    /**
     * Strips surrounding quotes from values and handles escape sequences (matches script's strip_quotes function)
     */
    private fun stripQuotes(value: String): String {
        var cleaned = value.trim()

        // Remove surrounding double quotes and handle escape sequences
        if (cleaned.startsWith("\"") && cleaned.endsWith("\"") && cleaned.length > 1) {
            cleaned = cleaned.substring(1, cleaned.length - 1)
            // Unescape common escape sequences in double-quoted strings
            cleaned = cleaned
                .replace("\\\"", "\"")  // Unescape double quotes
                .replace("\\\\", "\\")  // Unescape backslashes
                .replace("\\n", "\n")   // Unescape newlines
                .replace("\\t", "\t")   // Unescape tabs
                .replace("\\r", "\r")   // Unescape carriage returns
        }

        // Remove surrounding single quotes (no escape sequence processing for single quotes)
        if (cleaned.startsWith("'") && cleaned.endsWith("'") && cleaned.length > 1) {
            cleaned = cleaned.substring(1, cleaned.length - 1)
        }

        return cleaned
    }

    /**
     * Creates a formatted table view of secrets (matches script's view_secrets function)
     */
    fun formatSecretsTable(parseResult: ParseResult): String {
        if (!parseResult.isValid) {
            return "Error parsing secrets file:\n${parseResult.errors.joinToString("\n")}"
        }

        val keyWidth = 30
        val valueWidth = 50
        val totalWidth = keyWidth + valueWidth + 5

        val output = StringBuilder()

        // Table header
        output.appendLine("═".repeat(totalWidth))
        output.appendLine("║ %-${keyWidth}s ║ %-${valueWidth}s ║".format("SECRET KEY", "VALUE"))
        output.appendLine("═".repeat(totalWidth))

        // Regular secrets
        parseResult.secrets.forEach { (key, value) ->
            val displayValue = if (value.length > valueWidth) {
                value.take(valueWidth - 3) + "..."
            } else {
                value
            }
            output.appendLine("║ %-${keyWidth}s ║ %-${valueWidth}s ║".format(key, displayValue))
        }

        // Multiline secrets
        parseResult.multilineSecrets.forEach { (key, _) ->
            output.appendLine("║ %-${keyWidth}s ║ %-${valueWidth}s ║".format(key, "[MULTILINE VALUE]"))
        }

        // Table footer
        output.appendLine("═".repeat(totalWidth))

        return output.toString()
    }

    /**
     * Validates that all required keystore secrets are present
     */
    fun validateKeystoreSecrets(parseResult: ParseResult): List<String> {
        val errors = mutableListOf<String>()
        val allSecrets = parseResult.allSecrets

        // Required ORIGINAL keystore secrets
        listOf(
            config.originalKeystorePasswordKey,
            config.originalKeystoreAliasKey,
            config.originalKeystoreAliasPasswordKey
        ).forEach { key ->
            if (!allSecrets.containsKey(key)) {
                errors.add("Missing required ORIGINAL keystore secret: $key")
            } else if (allSecrets[key].isNullOrBlank()) {
                errors.add("Empty value for required ORIGINAL keystore secret: $key")
            }
        }

        // Required UPLOAD keystore secrets
        listOf(
            config.uploadKeystorePasswordKey,
            config.uploadKeystoreAliasKey,
            config.uploadKeystoreAliasPasswordKey
        ).forEach { key ->
            if (!allSecrets.containsKey(key)) {
                errors.add("Missing required UPLOAD keystore secret: $key")
            } else if (allSecrets[key].isNullOrBlank()) {
                errors.add("Empty value for required UPLOAD keystore secret: $key")
            }
        }

        return errors
    }
}