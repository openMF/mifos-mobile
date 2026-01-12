package org.convention.keystore

import org.gradle.internal.impldep.org.testng.Assert.assertEquals
import org.gradle.internal.impldep.org.testng.Assert.assertTrue
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path
import java.util.Base64

/**
 * Test class for SecretsEnvUpdateTask
 * 
 * Tests various update scenarios as required by KMPPT-57:
 * - secrets.env file creation and updates
 * - Base64 encoding functionality  
 * - Multiline value formatting with heredoc
 * - File merge logic for existing variables
 * - Output format validation for GitHub CLI compatibility
 */
class SecretsEnvUpdateTaskTest {

    @TempDir
    lateinit var tempDir: Path

    private lateinit var project: org.gradle.api.Project
    private lateinit var task: SecretsEnvUpdateTask
    private lateinit var secretsFile: File
    private lateinit var originalKeystoreFile: File
    private lateinit var uploadKeystoreFile: File

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder()
            .withProjectDir(tempDir.toFile())
            .build()

        task = project.tasks.create("testUpdateSecretsEnv", SecretsEnvUpdateTask::class.java)
        
        secretsFile = tempDir.resolve("secrets.env").toFile()
        originalKeystoreFile = tempDir.resolve("original.keystore").toFile()
        uploadKeystoreFile = tempDir.resolve("upload.keystore").toFile()

        // Configure task
        task.secretsEnvFile.set(secretsFile)
        task.originalKeystoreFile.set(originalKeystoreFile)
        task.uploadKeystoreFile.set(uploadKeystoreFile)
        task.secretsConfig.set(SecretsConfig())
    }

    @Test
    fun `test creates new secrets file when none exists`() {
        // Arrange
        createTestKeystoreFiles()

        // Act
        task.updateSecretsEnv()

        // Assert
        assertTrue(secretsFile.exists(), "Secrets file should be created")
        val content = secretsFile.readText()
        
        assertTrue(content.contains("# GitHub Secrets Environment File"), "Should contain header comment")
        assertTrue(content.contains("ORIGINAL_KEYSTORE_FILE<<EOF"), "Should contain original keystore heredoc")
        assertTrue(content.contains("UPLOAD_KEYSTORE_FILE<<EOF"), "Should contain upload keystore heredoc")
        assertTrue(content.contains("EOF"), "Should contain EOF delimiters")
    }

    @Test
    fun `test updates existing secrets file preserving variables`() {
        // Arrange
        createTestKeystoreFiles()
        createExistingSecretsFile()

        // Act
        task.updateSecretsEnv()

        // Assert
        val content = secretsFile.readText()
        
        // Check that existing variables are preserved
        assertTrue(content.contains("EXISTING_SIMPLE_VAR=existing_value"), "Should preserve existing simple variable")
        assertTrue(content.contains("ANOTHER_VAR=\"value with spaces\""), "Should preserve quoted variable")
        
        // Check that keystore variables are updated
        assertTrue(content.contains("ORIGINAL_KEYSTORE_FILE<<EOF"), "Should update original keystore")
        assertTrue(content.contains("UPLOAD_KEYSTORE_FILE<<EOF"), "Should update upload keystore")
    }

    @Test
    fun `test preserves comments when updating file`() {
        // Arrange
        createTestKeystoreFiles()
        createSecretsFileWithComments()
        task.preserveComments.set(true)

        // Act
        task.updateSecretsEnv()

        // Assert
        val content = secretsFile.readText()
        assertTrue(content.contains("# This is a custom comment"), "Should preserve custom comments")
        assertTrue(content.contains("# Another important comment"), "Should preserve multiple comments")
    }

    @Test
    fun `test base64 encoding produces valid output`() {
        // Arrange
        createTestKeystoreFiles()

        // Act
        task.updateSecretsEnv()

        // Assert
        val content = secretsFile.readText()
        
        // Extract base64 content
        val originalB64 = extractBase64Content(content, "ORIGINAL_KEYSTORE_FILE")
        val uploadB64 = extractBase64Content(content, "UPLOAD_KEYSTORE_FILE")

        // Verify base64 is valid
        assertTrue(isValidBase64(originalB64), "Original keystore base64 should be valid")
        assertTrue(isValidBase64(uploadB64), "Upload keystore base64 should be valid")

        // Verify decoded content matches original
        val originalDecoded = Base64.getDecoder().decode(originalB64.replace("\n", ""))
        val uploadDecoded = Base64.getDecoder().decode(uploadB64.replace("\n", ""))

        assertEquals(originalKeystoreFile.readBytes().toList(), originalDecoded.toList(), 
            "Decoded original keystore should match original file")
        assertEquals(uploadKeystoreFile.readBytes().toList(), uploadDecoded.toList(), 
            "Decoded upload keystore should match original file")
    }

    @Test
    fun `test heredoc formatting is correct`() {
        // Arrange
        createTestKeystoreFiles()
        task.useHeredocFormat.set(true)
        task.heredocDelimiter.set("EOF")

        // Act
        task.updateSecretsEnv()

        // Assert
        val content = secretsFile.readText()
        val lines = content.lines()

        // Find heredoc blocks
        val originalStart = lines.indexOfFirst { it == "ORIGINAL_KEYSTORE_FILE<<EOF" }
        val uploadStart = lines.indexOfFirst { it == "UPLOAD_KEYSTORE_FILE<<EOF" }
        
        // Find the corresponding EOF markers after each start
        val originalEnd = if (originalStart >= 0) {
            lines.drop(originalStart + 1).indexOfFirst { it == "EOF" }.let { 
                if (it >= 0) it + originalStart + 1 else -1 
            }
        } else -1
        
        val uploadEnd = if (uploadStart >= 0) {
            lines.drop(uploadStart + 1).indexOfFirst { it == "EOF" }.let { 
                if (it >= 0) it + uploadStart + 1 else -1 
            }
        } else -1

        assertTrue(originalStart >= 0, "Should find original keystore heredoc start")
        assertTrue(originalEnd > originalStart, "Should find original keystore heredoc end")
        assertTrue(uploadStart >= 0, "Should find upload keystore heredoc start")
        assertTrue(uploadEnd > uploadStart, "Should find upload keystore heredoc end")

        // Verify content between heredoc markers is base64
        val originalB64Lines = lines.subList(originalStart + 1, originalEnd)
        assertTrue(originalB64Lines.isNotEmpty(), "Should have base64 content in original heredoc")
        assertTrue(originalB64Lines.all { isValidBase64Line(it) }, "All lines in original heredoc should be valid base64")
    }

    @Test
    fun `test line length wrapping for base64 content`() {
        // Arrange
        createTestKeystoreFiles()
        task.base64LineLength.set(50) // Shorter line length for testing

        // Act
        task.updateSecretsEnv()

        // Assert
        val content = secretsFile.readText()
        val originalB64 = extractBase64Content(content, "ORIGINAL_KEYSTORE_FILE")
        val lines = originalB64.split("\n")

        // Verify line length wrapping
        lines.forEach { line ->
            assertTrue(line.length <= 50, "Base64 lines should not exceed configured length: '${line}' (length: ${line.length})")
        }
    }

    @Test
    fun `test creates backup when enabled`() {
        // Arrange
        createTestKeystoreFiles()
        createExistingSecretsFile()
        task.createBackup.set(true)
        
        // Configure backup directory to be in temp directory
        val customSecretsConfig = SecretsConfig(
            backupDir = tempDir.resolve("secrets-backup").toFile()
        )
        task.secretsConfig.set(customSecretsConfig)

        // Act
        task.updateSecretsEnv()

        // Assert
        val backupDir = tempDir.resolve("secrets-backup").toFile()
        assertTrue(backupDir.exists(), "Backup directory should be created")
        assertTrue(backupDir.listFiles()?.any { it.name.startsWith("secrets.env.backup") } == true, 
            "Backup file should be created")
    }

    @Test
    fun `test validates output format for GitHub CLI compatibility`() {
        // Arrange
        createTestKeystoreFiles()
        task.validateOutput.set(true)

        // Act & Assert (should not throw exception)
        task.updateSecretsEnv()

        // Verify file can be parsed successfully
        val parser = SecretsEnvParser(task.secretsConfig.get())
        val parseResult = parser.parseFile(secretsFile)
        assertTrue(parseResult.isValid, "Generated file should be valid: ${parseResult.errors}")
    }

    @Test
    fun `test handles missing keystore files gracefully`() {
        // Arrange - don't create keystore files, just set non-existent paths
        task.originalKeystoreFile.set(File(tempDir.toFile(), "nonexistent.keystore"))
        task.uploadKeystoreFile.set(File(tempDir.toFile(), "alsoNonexistent.keystore"))

        // Act & Assert (should not throw exception)
        task.updateSecretsEnv()

        // Verify file is still created with proper content
        assertTrue(secretsFile.exists(), "Secrets file should still be created")
        val content = secretsFile.readText()
        assertTrue(content.contains("# GitHub Secrets Environment File"), "Should contain header")
        
        // Should not contain any keystore secrets since files don't exist
        assertTrue(!content.contains("ORIGINAL_KEYSTORE_FILE<<EOF"), "Should not contain original keystore")
        assertTrue(!content.contains("UPLOAD_KEYSTORE_FILE<<EOF"), "Should not contain upload keystore")
    }

    @Test
    fun `test additional secrets are included`() {
        // Arrange
        createTestKeystoreFiles()
        val additionalSecrets = mapOf(
            "CUSTOM_SECRET" to "custom_value",
            "ANOTHER_SECRET" to "another_value"
        )
        task.additionalSecrets.set(additionalSecrets)

        // Act
        task.updateSecretsEnv()

        // Assert
        val content = secretsFile.readText()
        assertTrue(content.contains("CUSTOM_SECRET=custom_value"), "Should include custom secret")
        assertTrue(content.contains("ANOTHER_SECRET=another_value"), "Should include another secret")
    }

    @Test
    fun `test multiline values use proper escaping when heredoc disabled`() {
        // Arrange
        createTestKeystoreFiles()
        task.useHeredocFormat.set(false)
        task.heredocDelimiter.set("EOF")
        
        // Verify the property is set correctly before execution
        assertEquals(false, task.useHeredocFormat.get(), "useHeredocFormat should be false")

        // Act
        task.updateSecretsEnv()

        // Assert
        val content = secretsFile.readText()
        
        // Check for the specific patterns we expect when heredoc is disabled
        val hasOriginalHeredoc = content.contains("ORIGINAL_KEYSTORE_FILE<<EOF")
        val hasUploadHeredoc = content.contains("UPLOAD_KEYSTORE_FILE<<EOF")
        val hasOriginalQuoted = content.contains("ORIGINAL_KEYSTORE_FILE=\"")
        val hasUploadQuoted = content.contains("UPLOAD_KEYSTORE_FILE=\"")
        
        // Debug output if test fails
        if (hasOriginalHeredoc || hasUploadHeredoc || !hasOriginalQuoted || !hasUploadQuoted) {
            println("=== DEBUG: Unexpected content format ===")
            println("Has original heredoc: $hasOriginalHeredoc (should be false)")
            println("Has upload heredoc: $hasUploadHeredoc (should be false)")
            println("Has original quoted: $hasOriginalQuoted (should be true)")
            println("Has upload quoted: $hasUploadQuoted (should be true)")
            println("Content:")
            println(content)
            println("=== END DEBUG ===")
        }
        
        // Main assertions
        assertTrue(!hasOriginalHeredoc, "Should not contain original keystore heredoc start")
        assertTrue(!hasUploadHeredoc, "Should not contain upload keystore heredoc start")
        assertTrue(hasOriginalQuoted, "Should use quoted format for original keystore")
        assertTrue(hasUploadQuoted, "Should use quoted format for upload keystore")
    }

    @Test
    fun `test handles large keystore files appropriately`() {
        // Arrange - create a larger test file
        val largeContent = "large keystore content ".repeat(1000)
        originalKeystoreFile.writeBytes(largeContent.toByteArray())
        uploadKeystoreFile.writeBytes("smaller content".toByteArray())

        // Act
        task.updateSecretsEnv()

        // Assert - should still work but log warnings for large files
        assertTrue(secretsFile.exists(), "Secrets file should be created")
        val content = secretsFile.readText()
        assertTrue(content.contains("ORIGINAL_KEYSTORE_FILE<<EOF"), "Should contain original keystore")
        assertTrue(content.contains("UPLOAD_KEYSTORE_FILE<<EOF"), "Should contain upload keystore")
    }

    @Test
    fun `test validation detects GitHub CLI compatibility issues`() {
        // Arrange
        createTestKeystoreFiles()
        val problematicSecrets = mapOf(
            "secret-with-dash" to "value1",
            "secret with space" to "value2",
            "lowercaseSecret" to "value3"
        )
        task.additionalSecrets.set(problematicSecrets)
        task.validateOutput.set(true)

        // Act & Assert - should complete but log warnings
        task.updateSecretsEnv()
        
        // Verify file contains the problematic secrets
        val content = secretsFile.readText()
        assertTrue(content.contains("secret-with-dash=value1"), "Should include dash secret")
        assertTrue(content.contains("secret with space"), "Should include space secret")
        assertTrue(content.contains("lowercaseSecret=value3"), "Should include lowercase secret")
    }

    @Test
    fun `test empty keystore files are handled gracefully`() {
        // Arrange - create empty keystore files
        originalKeystoreFile.writeBytes(byteArrayOf())
        uploadKeystoreFile.writeBytes(byteArrayOf())

        // Act
        task.updateSecretsEnv()

        // Assert - should create file but log warnings about empty files
        assertTrue(secretsFile.exists(), "Secrets file should be created")
        val content = secretsFile.readText()
        assertTrue(content.contains("# GitHub Secrets Environment File"), "Should contain header")
        
        // Empty files should not produce keystore secrets
        assertTrue(!content.contains("ORIGINAL_KEYSTORE_FILE<<EOF"), "Should not contain original keystore")
        assertTrue(!content.contains("UPLOAD_KEYSTORE_FILE<<EOF"), "Should not contain upload keystore")
    }

    // Helper methods

    private fun createTestKeystoreFiles() {
        // Create dummy keystore files with some content
        originalKeystoreFile.writeBytes("dummy original keystore content".toByteArray())
        uploadKeystoreFile.writeBytes("dummy upload keystore content for testing".toByteArray())
    }

    private fun createExistingSecretsFile() {
        secretsFile.writeText("""
            # Existing secrets file
            EXISTING_SIMPLE_VAR=existing_value
            ANOTHER_VAR="value with spaces"
            
            ORIGINAL_KEYSTORE_FILE<<EOF
            old_base64_content_here
            EOF
            
            UPLOAD_KEYSTORE_FILE<<EOF
            old_upload_content_here
            EOF
        """.trimIndent())
    }

    private fun createSecretsFileWithComments() {
        secretsFile.writeText("""
            # This is a custom comment
            EXISTING_VAR=value
            # Another important comment
            
            ORIGINAL_KEYSTORE_FILE<<EOF
            old_content
            EOF
        """.trimIndent())
    }

    private fun extractBase64Content(content: String, key: String): String {
        val lines = content.lines()
        val startIndex = lines.indexOfFirst { it == "$key<<EOF" }
        
        if (startIndex >= 0) {
            val endIndex = lines.drop(startIndex + 1).indexOfFirst { it == "EOF" }.let { 
                if (it >= 0) it + startIndex + 1 else -1 
            }
            
            if (endIndex > startIndex) {
                return lines.subList(startIndex + 1, endIndex).joinToString("\n")
            }
        }
        return ""
    }

    private fun isValidBase64(content: String): Boolean {
        return try {
            Base64.getDecoder().decode(content.replace("\n", ""))
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    private fun isValidBase64Line(line: String): Boolean {
        return line.matches(Regex("[A-Za-z0-9+/=]*"))
    }

    @Test
    fun `test task property configuration works correctly`() {
        // Test that we can set and read task properties correctly
        task.useHeredocFormat.set(false)
        task.heredocDelimiter.set("CUSTOM_EOF")
        task.base64LineLength.set(64)
        
        assertEquals(false, task.useHeredocFormat.get(), "Should be able to set useHeredocFormat to false")
        assertEquals("CUSTOM_EOF", task.heredocDelimiter.get(), "Should be able to set custom delimiter")
        assertEquals(64, task.base64LineLength.get(), "Should be able to set custom line length")
    }
}
