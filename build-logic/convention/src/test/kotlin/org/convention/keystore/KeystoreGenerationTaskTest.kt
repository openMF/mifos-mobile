package org.convention.keystore

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.io.TempDir
import java.io.File

/**
 * Tests for KeystoreGenerationTask
 */
class KeystoreGenerationTaskTest {

    @TempDir
    lateinit var tempDir: File

    private lateinit var project: Project
    private lateinit var task: KeystoreGenerationTask

    @BeforeEach
    fun setup() {
        project = ProjectBuilder.builder()
            .withProjectDir(tempDir)
            .build()

        task = project.tasks.register("testGenerateKeystores", KeystoreGenerationTask::class.java).get()
    }

    @Test
    fun `task is created with correct configuration`() {
        // Verify task is configured correctly
        assertEquals("keystore", task.group)
        assertTrue(task.description.contains("Generates Android keystores"))
        assertTrue(task.generateOriginal.get())
        assertTrue(task.generateUpload.get())
    }

    @Test
    fun `task has correct default configurations`() {
        // Verify default configurations
        val originalConfig = task.originalConfig.get()
        val uploadConfig = task.uploadConfig.get()

        // Check ORIGINAL keystore defaults
        assertEquals("androiddebugkey", originalConfig.keyAlias)
        assertEquals("original.keystore", originalConfig.originalKeystoreName)
        assertEquals("Android Debug", originalConfig.companyName)

        // Check UPLOAD keystore defaults
        assertEquals("upload", uploadConfig.keyAlias)
        assertEquals("upload.keystore", uploadConfig.uploadKeystoreName)
        assertEquals("Android Release", uploadConfig.companyName)
    }

    @Test
    fun `task creates correct output directory`() {
        // Set a custom output directory
        val outputDir = File(tempDir, "custom-keystores")
        task.outputDirectory.set(outputDir)

        assertEquals(outputDir, task.outputDirectory.asFile.get())
    }

    @Test
    fun `task builds correct distinguished name from config`() {
        val config = KeystoreConfig(
            keystorePassword = "testpass",
            keyAlias = "testalias",
            keyPassword = "keypass",
            keyAlgorithm = "RSA",
            keySize = 2048,
            validity = 25,
            companyName = "Test Company",
            department = "Test Dept",
            organization = "Test Org",
            city = "Test City",
            state = "Test State",
            country = "US"
        )

        task.originalConfig.set(config)
        
        // Verify distinguished name construction
        val expectedDN = "CN=Test Company, OU=Test Dept, O=Test Org, L=Test City, ST=Test State, C=US"
        assertEquals(expectedDN, config.distinguishedName)
        assertEquals(config, task.originalConfig.get())
    }

    @Test
    fun `configuration loading from secrets works correctly`() {
        // Create a test secrets.env file
        val secretsFile = File(tempDir, "secrets.env")
        secretsFile.writeText("""
            # Test secrets file
            ORIGINAL_KEYSTORE_FILE_PASSWORD=original_password
            ORIGINAL_KEYSTORE_ALIAS=original_alias
            ORIGINAL_KEYSTORE_ALIAS_PASSWORD=original_alias_password
            
            UPLOAD_KEYSTORE_FILE_PASSWORD=upload_password
            UPLOAD_KEYSTORE_ALIAS=upload_alias
            UPLOAD_KEYSTORE_ALIAS_PASSWORD=upload_alias_password
            
            COMPANY_NAME=Test Company From Secrets
            DEPARTMENT=Test Department
            ORGANIZATION=Test Organization
            CITY=Test City
            STATE=Test State
            COUNTRY=TS
            
            KEYALG=RSA
            KEYSIZE=4096
            VALIDITY=30
            OVERWRITE=true
        """.trimIndent())

        // Create SecretsConfig pointing to our test file
        val secretsConfig = SecretsConfig(secretsEnvFile = secretsFile)

        // Apply configuration from secrets
        KeystoreGenerationTask.createWithSecretsConfig(task, secretsConfig)

        // Verify configurations were loaded
        val originalConfig = task.originalConfig.get()
        val uploadConfig = task.uploadConfig.get()

        // Check ORIGINAL keystore configuration
        assertEquals("original_password", originalConfig.keystorePassword)
        assertEquals("original_alias", originalConfig.keyAlias)
        assertEquals("original_alias_password", originalConfig.keyPassword)
        assertEquals("Test Company From Secrets", originalConfig.companyName)
        assertEquals("Test Department", originalConfig.department)
        assertEquals("Test Organization", originalConfig.organization)
        assertEquals("Test City", originalConfig.city)
        assertEquals("Test State", originalConfig.state)
        assertEquals("TS", originalConfig.country)
        assertEquals("RSA", originalConfig.keyAlgorithm)
        assertEquals(4096, originalConfig.keySize)
        assertEquals(30, originalConfig.validity)
        assertTrue(originalConfig.overwriteExisting)

        // Check UPLOAD keystore configuration
        assertEquals("upload_password", uploadConfig.keystorePassword)
        assertEquals("upload_alias", uploadConfig.keyAlias)
        assertEquals("upload_alias_password", uploadConfig.keyPassword)
        assertEquals("Test Company From Secrets", uploadConfig.companyName)
        assertEquals("Test Department", uploadConfig.department)
        assertEquals("Test Organization", uploadConfig.organization)
        assertEquals("Test City", uploadConfig.city)
        assertEquals("Test State", uploadConfig.state)
        assertEquals("TS", uploadConfig.country)
        assertEquals("RSA", uploadConfig.keyAlgorithm)
        assertEquals(4096, uploadConfig.keySize)
        assertEquals(30, uploadConfig.validity)
        assertTrue(uploadConfig.overwriteExisting)
    }

    @Test
    fun `task handles missing secrets file gracefully`() {
        // Create SecretsConfig pointing to non-existent file
        val secretsConfig = SecretsConfig(secretsEnvFile = File(tempDir, "nonexistent.env"))

        // Apply configuration from secrets (should not throw)
        assertDoesNotThrow {
            KeystoreGenerationTask.createWithSecretsConfig(task, secretsConfig)
        }

        // Should use default configurations
        val originalConfig = task.originalConfig.get()
        val uploadConfig = task.uploadConfig.get()

        assertEquals("Android Debug", originalConfig.companyName)
        assertEquals("Android Release", uploadConfig.companyName)
    }

    @Test
    fun `task configuration can be overridden programmatically`() {
        // Create custom configurations
        val customOriginalConfig = KeystoreConfig(
            keystorePassword = "custom_original_pass",
            keyAlias = "custom_original_alias",
            keyPassword = "custom_original_key_pass",
            companyName = "Custom Original Company",
            keySize = 4096,
            validity = 50
        )

        val customUploadConfig = KeystoreConfig(
            keystorePassword = "custom_upload_pass",
            keyAlias = "custom_upload_alias",
            keyPassword = "custom_upload_key_pass",
            companyName = "Custom Upload Company",
            keySize = 4096,
            validity = 50
        )

        // Apply custom configurations
        task.originalConfig.set(customOriginalConfig)
        task.uploadConfig.set(customUploadConfig)

        // Verify configurations were applied
        val appliedOriginalConfig = task.originalConfig.get()
        val appliedUploadConfig = task.uploadConfig.get()

        assertEquals("custom_original_pass", appliedOriginalConfig.keystorePassword)
        assertEquals("custom_original_alias", appliedOriginalConfig.keyAlias)
        assertEquals("Custom Original Company", appliedOriginalConfig.companyName)
        assertEquals(4096, appliedOriginalConfig.keySize)
        assertEquals(50, appliedOriginalConfig.validity)

        assertEquals("custom_upload_pass", appliedUploadConfig.keystorePassword)
        assertEquals("custom_upload_alias", appliedUploadConfig.keyAlias)
        assertEquals("Custom Upload Company", appliedUploadConfig.companyName)
        assertEquals(4096, appliedUploadConfig.keySize)
        assertEquals(50, appliedUploadConfig.validity)
    }

    @Test
    fun `individual keystore generation flags work correctly`() {
        // Test generating only ORIGINAL keystore
        task.generateOriginal.set(true)
        task.generateUpload.set(false)

        assertTrue(task.generateOriginal.get())
        assertFalse(task.generateUpload.get())

        // Test generating only UPLOAD keystore
        task.generateOriginal.set(false)
        task.generateUpload.set(true)

        assertFalse(task.generateOriginal.get())
        assertTrue(task.generateUpload.get())

        // Test generating both keystores
        task.generateOriginal.set(true)
        task.generateUpload.set(true)

        assertTrue(task.generateOriginal.get())
        assertTrue(task.generateUpload.get())
    }

    @Test
    fun `task validates keystore configurations`() {
        // Test with valid configuration
        val validConfig = KeystoreConfig(
            keystorePassword = "validpassword",
            keyAlias = "validalias",
            keyPassword = "validkeypass",
            keySize = 2048,
            validity = 25,
            country = "US"
        )

        val validationErrors = validConfig.validate()
        assertTrue(validationErrors.isEmpty(), "Valid configuration should have no errors")

        // Test with invalid configuration
        val invalidConfig = KeystoreConfig(
            keystorePassword = "", // Invalid: blank password
            keyAlias = "", // Invalid: blank alias
            keyPassword = "", // Invalid: blank key password
            keySize = 512, // Invalid: too small key size
            validity = -1, // Invalid: negative validity
            country = "USA" // Invalid: country code too long
        )

        val invalidErrors = invalidConfig.validate()
        assertFalse(invalidErrors.isEmpty(), "Invalid configuration should have errors")
        assertTrue(invalidErrors.any { it.contains("password cannot be blank") })
        assertTrue(invalidErrors.any { it.contains("alias cannot be blank") })
        assertTrue(invalidErrors.any { it.contains("Key size must be at least 1024") })
        assertTrue(invalidErrors.any { it.contains("Validity period must be positive") })
        assertTrue(invalidErrors.any { it.contains("Country code must be exactly 2 characters") })
    }

    @Test
    fun `distinguished name is formatted correctly`() {
        val config = KeystoreConfig(
            companyName = "Test Company",
            department = "Engineering",
            organization = "Test Org",
            city = "San Francisco",
            state = "California",
            country = "US"
        )

        val expectedDN = "CN=Test Company, OU=Engineering, O=Test Org, L=San Francisco, ST=California, C=US"
        assertEquals(expectedDN, config.distinguishedName)
    }

    @Test
    fun `keystore file paths are constructed correctly`() {
        val config = KeystoreConfig(
            keystoreDir = File(tempDir, "test-keystores"),
            originalKeystoreName = "debug.keystore",
            uploadKeystoreName = "release.keystore"
        )

        val expectedOriginalPath = File(File(tempDir, "test-keystores"), "debug.keystore")
        val expectedUploadPath = File(File(tempDir, "test-keystores"), "release.keystore")

        assertEquals(expectedOriginalPath, config.originalKeystorePath)
        assertEquals(expectedUploadPath, config.uploadKeystorePath)
    }
}
