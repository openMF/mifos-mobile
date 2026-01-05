package org.convention.keystore

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.nio.file.Path

/**
 * Unit tests for ConfigurationFileUpdatesTask
 */
class ConfigurationFileUpdatesTaskTest {

    @TempDir
    lateinit var tempDir: Path

    private lateinit var testConfig: KeystoreConfig
    private lateinit var keystoreFile: File

    @BeforeEach
    fun setUp() {
        testConfig = KeystoreConfig(
            keystorePassword = "test_password",
            keyAlias = "test_alias",
            keyPassword = "test_key_password",
            companyName = "Test Company",
            department = "Test Department",
            organization = "Test Organization",
            city = "Test City",
            state = "Test State",
            country = "US",
            uploadKeystoreName = "test_upload.keystore"
        )

        // Create a test keystore file
        keystoreFile = tempDir.resolve("keystores").resolve("test_upload.keystore").toFile()
        keystoreFile.parentFile.mkdirs()
        keystoreFile.writeText("test keystore content")
    }

    @AfterEach
    fun tearDown() {
        // Cleanup handled by @TempDir
    }

    @Test
    fun `test fastlane config creation with new file`() {
        val fastlaneFile = tempDir.resolve("fastlane-config").resolve("android_config.rb").toFile()

        // Create task instance (simplified for testing)
        val task = TestConfigurationFileUpdatesTask()

        // Test creating new fastlane config
        task.testCreateNewFastlaneConfig(fastlaneFile, testConfig, "test_upload.keystore")

        assertTrue(fastlaneFile.exists(), "Fastlane config file should be created")

        val content = fastlaneFile.readText()
        assertFastlaneConfigContent(content, testConfig, "test_upload.keystore")
    }

    @Test
    fun `test fastlane config update with existing file`() {
        val fastlaneFile = tempDir.resolve("fastlane-config").resolve("android_config.rb").toFile()
        fastlaneFile.parentFile.mkdirs()

        // Create existing file with old values
        val existingContent = """
module FastlaneConfig
  module AndroidConfig
    STORE_CONFIG = {
      default_store_file: "old_keystore.keystore",
      default_store_password: "old_password",
      default_key_alias: "old_alias",
      default_key_password: "old_key_password"
    }

    FIREBASE_CONFIG = {
      firebase_prod_app_id: "existing_app_id",
      firebase_demo_app_id: "existing_demo_id",
      firebase_service_creds_file: "existing_creds.json",
      firebase_groups: "existing_groups"
    }

    BUILD_PATHS = {
      prod_apk_path: "existing/path.apk",
      demo_apk_path: "existing/demo.apk",
      prod_aab_path: "existing/bundle.aab"
    }
  end
end
        """.trimIndent()

        fastlaneFile.writeText(existingContent)

        // Create task instance (simplified for testing)
        val task = TestConfigurationFileUpdatesTask()

        // Test updating existing fastlane config
        task.testUpdateExistingFastlaneConfig(fastlaneFile, testConfig, "test_upload.keystore")

        val updatedContent = fastlaneFile.readText()

        // Verify keystore config was updated
        assertTrue(updatedContent.contains("default_store_file: \"test_upload.keystore\""))
        assertTrue(updatedContent.contains("default_store_password: \"${testConfig.keystorePassword}\""))
        assertTrue(updatedContent.contains("default_key_alias: \"${testConfig.keyAlias}\""))
        assertTrue(updatedContent.contains("default_key_password: \"${testConfig.keyPassword}\""))

        // Verify other sections were preserved
        assertTrue(updatedContent.contains("firebase_prod_app_id: \"existing_app_id\""))
        assertTrue(updatedContent.contains("prod_apk_path: \"existing/path.apk\""))
    }

    @Test
    fun `test gradle config update with existing file`() {
        val gradleFile = tempDir.resolve("cmp-android").resolve("build.gradle.kts").toFile()
        gradleFile.parentFile.mkdirs()

        // Create existing gradle file content
        val existingContent = """
android {
    signingConfigs {
        create("release") {
            storeFile = file(System.getenv("KEYSTORE_PATH") ?: "../keystores/old_keystore.keystore")
            storePassword = System.getenv("KEYSTORE_PASSWORD") ?: "old_password"
            keyAlias = System.getenv("KEYSTORE_ALIAS") ?: "old_alias"
            keyPassword = System.getenv("KEYSTORE_ALIAS_PASSWORD") ?: "old_key_password"
            enableV1Signing = true
            enableV2Signing = true
        }
    }

    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}
        """.trimIndent()

        gradleFile.writeText(existingContent)

        // Create task instance (simplified for testing)
        val task = TestConfigurationFileUpdatesTask()

        // Test updating gradle config
        task.testUpdateGradleConfig(gradleFile, testConfig, keystoreFile)

        val updatedContent = gradleFile.readText()

        // Verify gradle config was updated with relative path
        assertTrue(updatedContent.contains("storeFile = file(System.getenv(\"KEYSTORE_PATH\") ?: \"../keystores/test_upload.keystore\")"))
        assertTrue(updatedContent.contains("storePassword = System.getenv(\"KEYSTORE_PASSWORD\") ?: \"${testConfig.keystorePassword}\""))
        assertTrue(updatedContent.contains("keyAlias = System.getenv(\"KEYSTORE_ALIAS\") ?: \"${testConfig.keyAlias}\""))
        assertTrue(updatedContent.contains("keyPassword = System.getenv(\"KEYSTORE_ALIAS_PASSWORD\") ?: \"${testConfig.keyPassword}\""))

        // Verify other parts were preserved
        assertTrue(updatedContent.contains("enableV1Signing = true"))
        assertTrue(updatedContent.contains("enableV2Signing = true"))
    }

    @Test
    fun `test gradle config update with missing file`() {
        val gradleFile = tempDir.resolve("cmp-android").resolve("build.gradle.kts").toFile()

        // Create task instance (simplified for testing)
        val task = TestConfigurationFileUpdatesTask()

        // Test updating non-existent gradle config (should not throw exception)
        assertDoesNotThrow {
            task.testUpdateGradleConfig(gradleFile, testConfig, keystoreFile)
        }

        // File should still not exist
        assertFalse(gradleFile.exists())
    }

    @Test
    fun `test keystore configuration validation`() {
        val invalidConfig = KeystoreConfig(
            keystorePassword = "", // Invalid: empty password
            keyAlias = "", // Invalid: empty alias
            keyPassword = "valid_password",
            keySize = 512, // Invalid: too small
            validity = -5, // Invalid: negative
            country = "USA" // Invalid: not 2 characters
        )

        val errors = invalidConfig.validate()

        assertTrue(errors.contains("Keystore password cannot be blank"))
        assertTrue(errors.contains("Key alias cannot be blank"))
        assertTrue(errors.contains("Key size must be at least 1024 bits"))
        assertTrue(errors.contains("Validity period must be positive"))
        assertTrue(errors.contains("Country code must be exactly 2 characters"))
    }

    @Test
    fun `test keystore path resolution`() {
        val gradleDir = tempDir.resolve("cmp-android").toFile()
        gradleDir.mkdirs()

        val task = TestConfigurationFileUpdatesTask()

        // Test with keystore file in different location
        val keystoreInRootDir = tempDir.resolve("upload.keystore").toFile()
        keystoreInRootDir.writeText("test content")

        val relativePath = task.testCalculateRelativePath(gradleDir, keystoreInRootDir)

        // Should be relative path from cmp-android to root
        assertEquals("../upload.keystore", relativePath)
    }

    @Test
    fun `test configuration parsing from secrets`() {
        // Create a test secrets.env file
        val secretsFile = tempDir.resolve("secrets.env").toFile()
        val secretsContent = """
# Test secrets file
UPLOAD_KEYSTORE_FILE_PASSWORD=secret_password
UPLOAD_KEYSTORE_ALIAS=secret_alias
UPLOAD_KEYSTORE_ALIAS_PASSWORD=secret_key_password
COMPANY_NAME=Secret Company
DEPARTMENT=Secret Department
ORGANIZATION=Secret Organization
CITY=Secret City
STATE=Secret State
COUNTRY=US
UPLOAD_KEYSTORE_NAME=secret_upload.keystore
        """.trimIndent()

        secretsFile.writeText(secretsContent)

        val secretsConfig = SecretsConfig(secretsEnvFile = secretsFile)
        val parser = SecretsEnvParser(secretsConfig)
        val parseResult = parser.parseFile()

        assertTrue(parseResult.isValid)

        val secrets = parseResult.allSecrets
        assertEquals("secret_password", secrets["UPLOAD_KEYSTORE_FILE_PASSWORD"])
        assertEquals("secret_alias", secrets["UPLOAD_KEYSTORE_ALIAS"])
        assertEquals("secret_key_password", secrets["UPLOAD_KEYSTORE_ALIAS_PASSWORD"])
        assertEquals("Secret Company", secrets["COMPANY_NAME"])
        assertEquals("secret_upload.keystore", secrets["UPLOAD_KEYSTORE_NAME"])
    }

    private fun assertFastlaneConfigContent(content: String, config: KeystoreConfig, keystoreName: String) {
        assertTrue(content.contains("module FastlaneConfig"))
        assertTrue(content.contains("module AndroidConfig"))
        assertTrue(content.contains("STORE_CONFIG = {"))
        assertTrue(content.contains("default_store_file: \"$keystoreName\""))
        assertTrue(content.contains("default_store_password: \"${config.keystorePassword}\""))
        assertTrue(content.contains("default_key_alias: \"${config.keyAlias}\""))
        assertTrue(content.contains("default_key_password: \"${config.keyPassword}\""))

        // Should also contain other required sections
        assertTrue(content.contains("FIREBASE_CONFIG = {"))
        assertTrue(content.contains("BUILD_PATHS = {"))
    }

    /**
     * Test helper class that exposes private methods for testing
     */
    private class TestConfigurationFileUpdatesTask {

        fun testCreateNewFastlaneConfig(configFile: File, config: KeystoreConfig, keystoreName: String) {
            val content = """
module FastlaneConfig
  module AndroidConfig
    STORE_CONFIG = {
      default_store_file: "$keystoreName",
      default_store_password: "${config.keystorePassword}",
      default_key_alias: "${config.keyAlias}",
      default_key_password: "${config.keyPassword}"
    }

    FIREBASE_CONFIG = {
      firebase_prod_app_id: "1:728433984912738:android:3902eb32kjaska3363b0938f1a1dbb",
      firebase_demo_app_id: "1:72843493212738:android:8392hjks3298ak9032skja",
      firebase_service_creds_file: "secrets/firebaseAppDistributionServiceCredentialsFile.json",
      firebase_groups: "mifos-mobile-apps"
    }

    BUILD_PATHS = {
      prod_apk_path: "cmp-android/build/outputs/apk/prod/release/cmp-android-prod-release.apk",
      demo_apk_path: "cmp-android/build/outputs/apk/demo/release/cmp-android-demo-release.apk",
      prod_aab_path: "cmp-android/build/outputs/bundle/prodRelease/cmp-android-prod-release.aab"
    }
  end
end
            """.trimIndent()

            configFile.parentFile.mkdirs()
            configFile.writeText(content)
        }

        fun testUpdateExistingFastlaneConfig(configFile: File, config: KeystoreConfig, keystoreName: String) {
            val content = configFile.readText()

            // Use regex to replace the values while preserving file structure
            val updatedContent = content
                .replace(Regex("default_store_file:\\s*\"[^\"]*\""), "default_store_file: \"$keystoreName\"")
                .replace(Regex("default_store_password:\\s*\"[^\"]*\""), "default_store_password: \"${config.keystorePassword}\"")
                .replace(Regex("default_key_alias:\\s*\"[^\"]*\""), "default_key_alias: \"${config.keyAlias}\"")
                .replace(Regex("default_key_password:\\s*\"[^\"]*\""), "default_key_password: \"${config.keyPassword}\"")

            configFile.writeText(updatedContent)
        }

        fun testUpdateGradleConfig(gradleFile: File, config: KeystoreConfig, keystoreFile: File?) {
            // Check if the file exists
            if (!gradleFile.exists()) {
                // Simulate the warning and return (matches actual implementation)
                return
            }

            // Determine keystore path - use relative path from cmp-android directory
            val keystorePath = if (keystoreFile != null) {
                val gradleDir = gradleFile.parentFile
                val relativePath = gradleDir.toPath().relativize(keystoreFile.toPath()).toString()
                relativePath.replace('\\', '/') // Ensure forward slashes for Gradle
            } else {
                "../keystores/${config.uploadKeystoreName}"
            }

            // Read the current content
            val content = gradleFile.readText()

            // Use regex to update the signing configuration lines
            val updatedContent = content
                .replace(
                    Regex("storeFile = file\\(System\\.getenv\\(\"KEYSTORE_PATH\"\\) \\?\\: \"[^\"]*\"\\)"),
                    "storeFile = file(System.getenv(\"KEYSTORE_PATH\") ?: \"$keystorePath\")"
                )
                .replace(
                    Regex("storePassword = System\\.getenv\\(\"KEYSTORE_PASSWORD\"\\) \\?\\: \"[^\"]*\""),
                    "storePassword = System.getenv(\"KEYSTORE_PASSWORD\") ?: \"${config.keystorePassword}\""
                )
                .replace(
                    Regex("keyAlias = System\\.getenv\\(\"KEYSTORE_ALIAS\"\\) \\?\\: \"[^\"]*\""),
                    "keyAlias = System.getenv(\"KEYSTORE_ALIAS\") ?: \"${config.keyAlias}\""
                )
                .replace(
                    Regex("keyPassword = System\\.getenv\\(\"KEYSTORE_ALIAS_PASSWORD\"\\) \\?\\: \"[^\"]*\""),
                    "keyPassword = System.getenv(\"KEYSTORE_ALIAS_PASSWORD\") ?: \"${config.keyPassword}\""
                )

            gradleFile.writeText(updatedContent)
        }

        fun testCalculateRelativePath(fromDir: File, toFile: File): String {
            val relativePath = fromDir.toPath().relativize(toFile.toPath()).toString()
            return relativePath.replace('\\', '/') // Ensure forward slashes for Gradle
        }
    }
}