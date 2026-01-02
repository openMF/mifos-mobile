package org.convention.keystore

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

/**
 * Unit tests for SecretsEnvParser covering edge cases and various file formats
 * Tests the parsing logic against the keystore-manager.sh script behavior
 */
class SecretsEnvParserTest {

    private lateinit var parser: SecretsEnvParser
    private lateinit var config: SecretsConfig

    @TempDir
    lateinit var tempDir: File

    @BeforeEach
    fun setup() {
        config = SecretsConfig()
        parser = SecretsEnvParser(config)
    }

    @Test
    fun `should parse simple key-value pairs`() {
        val testFile = createTestFile(
            """
            # Simple key-value pairs
            KEY1=value1
            KEY2=value2
            KEY3="quoted value"
            KEY4='single quoted'
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertTrue(result.isValid)
        assertEquals("value1", result.secrets["KEY1"])
        assertEquals("value2", result.secrets["KEY2"])
        assertEquals("quoted value", result.secrets["KEY3"])
        assertEquals("single quoted", result.secrets["KEY4"])
    }

    @Test
    fun `should parse multiline heredoc values`() {
        val testFile = createTestFile(
            """
            # Multiline value test
            MULTILINE_KEY<<EOF
            line 1
            line 2
            line 3
            EOF
            
            ANOTHER_KEY=simple_value
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertTrue(result.isValid)
        assertEquals("line 1\nline 2\nline 3", result.multilineSecrets["MULTILINE_KEY"])
        assertEquals("simple_value", result.secrets["ANOTHER_KEY"])
    }

    @Test
    fun `should handle different heredoc delimiters`() {
        val testFile = createTestFile(
            """
            KEY1<<END
            content 1
            END
            
            KEY2<<DELIMITER
            content 2
            DELIMITER
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertTrue(result.isValid)
        assertEquals("content 1", result.multilineSecrets["KEY1"])
        assertEquals("content 2", result.multilineSecrets["KEY2"])
    }

    @Test
    fun `should handle empty multiline values`() {
        val testFile = createTestFile(
            """
            EMPTY_MULTILINE<<EOF
            EOF
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertTrue(result.isValid)
        assertEquals("", result.multilineSecrets["EMPTY_MULTILINE"])
    }

    @Test
    fun `should handle quotes in various formats`() {
        val testFile = createTestFile(
            """
            UNQUOTED=value
            DOUBLE_QUOTED="value with spaces"
            SINGLE_QUOTED='value with spaces'
            MIXED_QUOTES="value with 'inner' quotes"
            ESCAPED="value with \"escaped\" quotes"
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertTrue(result.isValid)
        assertEquals("value", result.secrets["UNQUOTED"])
        assertEquals("value with spaces", result.secrets["DOUBLE_QUOTED"])
        assertEquals("value with spaces", result.secrets["SINGLE_QUOTED"])
        assertEquals("value with 'inner' quotes", result.secrets["MIXED_QUOTES"])
        val value = "value with \"escaped\" quotes"
        assertEquals(value, result.secrets["ESCAPED"])
    }

    @Test
    fun `should preserve comments when configured`() {
        val testFile = createTestFile(
            """
            # This is a comment
            KEY1=value1
            # Another comment
            KEY2=value2
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertTrue(result.isValid)
        assertEquals(2, result.comments.size)
        assertTrue(result.comments.contains("# This is a comment"))
        assertTrue(result.comments.contains("# Another comment"))
    }

    @Test
    fun `should handle malformed multiline blocks`() {
        val testFile = createTestFile(
            """
            # Missing delimiter
            BAD_KEY<<
            some content
            
            # Unterminated block
            UNTERMINATED<<EOF
            content without end
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("Empty delimiter") })
        assertTrue(result.errors.any { it.contains("Unterminated multiline block") })
    }

    @Test
    fun `should flag mismatched heredoc terminator`() {
        val testFile = createTestFile(
            """
            MISMATCHED<<EOF
            some content
            END
            """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertFalse(result.isValid)
        // Should report unterminated block mentioning the expected delimiter
        assertTrue(result.errors.any { it.contains("Unterminated multiline block") && it.contains("EOF") })
    }

    @Test
    fun `should flag stray terminator without start`() {
        val testFile = createTestFile(
            """
            # A terminator without a corresponding start
            END
            """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("Unrecognized line format: END") })
    }

    @Test
    fun `should handle empty and whitespace-only lines`() {
        val testFile = createTestFile(
            """
            KEY1=value1
            
                
            KEY2=value2
                    
            KEY3=value3
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertTrue(result.isValid)
        assertEquals(3, result.secrets.size)
    }

    @Test
    fun `should handle special characters in values`() {
        val testFile = createTestFile(
            """
            SPECIAL_CHARS="value with @#$%^&*()+={}[]|\\:;\"'<>,.?/~"
            URL_VALUE="https://example.com/path?param=value&other=123"
            BASE64_LIKE="SGVsbG8gV29ybGQ="
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertTrue(result.isValid)
        assertNotNull(result.secrets["SPECIAL_CHARS"])
        assertNotNull(result.secrets["URL_VALUE"])
        assertNotNull(result.secrets["BASE64_LIKE"])
    }

    @Test
    fun `should handle real keystore file format`() {
        val testFile = createTestFile(
            """
            # Keystore configuration
            ORIGINAL_KEYSTORE_FILE_PASSWORD=password123
            ORIGINAL_KEYSTORE_ALIAS=myalias
            ORIGINAL_KEYSTORE_ALIAS_PASSWORD=aliaspass
            
            ORIGINAL_KEYSTORE_FILE<<EOF
            MIILEAIBAzCCCroGCSqGSIb3DQEHAaCCCqsEggqnMIIKozCCBcoGCSqGSIb3DQEHAaCCBbsEggW3
            MIIFszCCBa8GCyqGSIb3DQEMCgECoIIFQDCCBTwwZgYJKoZIhvcNAQUNMFkwOAYJKoZIhvcNAQUM
            EOF
            
            UPLOAD_KEYSTORE_FILE_PASSWORD=password456
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertTrue(result.isValid)
        assertEquals("password123", result.secrets["ORIGINAL_KEYSTORE_FILE_PASSWORD"])
        assertEquals("myalias", result.secrets["ORIGINAL_KEYSTORE_ALIAS"])
        assertTrue(result.multilineSecrets["ORIGINAL_KEYSTORE_FILE"]?.contains("MIILEAIBAzCCCroGCSqGSIb3DQEH") == true)
        assertEquals("password456", result.secrets["UPLOAD_KEYSTORE_FILE_PASSWORD"])
    }

    @Test
    fun `should validate required keystore secrets`() {
        val testFile = createTestFile(
            """
            # Missing some required secrets
            ORIGINAL_KEYSTORE_FILE_PASSWORD=password
            # Missing ORIGINAL_KEYSTORE_ALIAS
            ORIGINAL_KEYSTORE_ALIAS_PASSWORD=aliaspass
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)
        val validationErrors = parser.validateKeystoreSecrets(result)

        assertTrue(result.isValid) // Parsing is valid
        assertFalse(validationErrors.isEmpty()) // But validation fails
        assertTrue(validationErrors.any { it.contains("ORIGINAL_KEYSTORE_ALIAS") })
    }

    @Test
    fun `should handle values with equals signs`() {
        val testFile = createTestFile(
            """
            URL_WITH_PARAMS="https://api.example.com/v1/auth?token=abc123&user=test"
            EQUATION="x = y + z"
            BASE64_VALUE="YWxnb3JpdGhtPVJTQSZrZXlzaXplPTIwNDg="
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        assertTrue(result.isValid)
        assertTrue(result.secrets["URL_WITH_PARAMS"]?.contains("token=abc123&user=test") == true)
        assertEquals("x = y + z", result.secrets["EQUATION"])
        assertNotNull(result.secrets["BASE64_VALUE"])
    }

    @Test
    fun `should format secrets table correctly`() {
        val testFile = createTestFile(
            """
            SHORT_KEY=value
            VERY_LONG_KEY_NAME_THAT_EXCEEDS_NORMAL_LENGTH=short
            MULTILINE<<EOF
            line1
            line2
            EOF
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)
        val tableOutput = parser.formatSecretsTable(result)

        assertTrue(tableOutput.contains("SHORT_KEY"))
        assertTrue(tableOutput.contains("VERY_LONG_KEY_NAME_THAT_EXCEEDS_NORMAL_LENGTH"))
        assertTrue(tableOutput.contains("[MULTILINE VALUE]"))
        assertTrue(tableOutput.contains("═"))
        assertTrue(tableOutput.contains("║"))
    }

    @Test
    fun `should handle file not found gracefully`() {
        val nonExistentFile = File(tempDir, "nonexistent.env")
        val result = parser.parseFile(nonExistentFile)

        assertFalse(result.isValid)
        assertTrue(result.errors.any { it.contains("File not found") })
        assertTrue(result.secrets.isEmpty())
        assertTrue(result.multilineSecrets.isEmpty())
    }

    @Test
    fun `should handle malformed key-value pairs`() {
        val testFile = createTestFile(
            """
            VALID_KEY=valid_value
            =value_without_key
            key_without_value=
            just_text_no_equals
            =
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        // Should parse what it can and report errors for malformed lines
        assertTrue(result.secrets.containsKey("VALID_KEY"))
        assertTrue(result.secrets.containsKey("key_without_value"))
        assertEquals("", result.secrets["key_without_value"])
        assertFalse(result.isValid) // Should have errors for malformed lines
    }

    @Test
    fun `should handle nested heredoc delimiters`() {
        val testFile = createTestFile(
            """
            OUTER_KEY<<OUTER
            Some content
            INNER_KEY<<INNER
            nested content
            INNER
            More outer content
            OUTER
        """.trimIndent(),
        )

        val result = parser.parseFile(testFile)

        // The parser should handle this by treating everything between OUTER delimiters as content
        assertTrue(result.isValid)
        val content = result.multilineSecrets["OUTER_KEY"]
        assertNotNull(content)
        assertTrue(content?.contains("INNER_KEY<<INNER") == true)
        assertTrue(content?.contains("nested content") == true)
    }

    private fun createTestFile(content: String): File {
        val file = File(tempDir, "test-secrets.env")
        file.writeText(content)
        return file
    }
}