package org.convention.keystore

import java.io.File

/**
 * Configuration for keystore generation matching keystore-manager.sh script parameters
 */
data class KeystoreConfig(
    // Basic keystore parameters
    val keystorePassword: String = "android",
    val keyAlias: String = "upload",
    val keyPassword: String = "android",
    val keyAlgorithm: String = "RSA",
    val keySize: Int = 2048,
    val validity: Int = 25, // years

    // Certificate DN components (mapped from script variables)
    val companyName: String = "Android Debug",    // COMPANY_NAME -> CN
    val department: String = "Android",           // DEPARTMENT -> OU
    val organization: String = "Android",         // ORGANIZATION -> O
    val city: String = "Unknown",                 // CITY -> L
    val state: String = "Unknown",                // STATE -> ST
    val country: String = "US",                   // COUNTRY -> C

    // File paths (matching script defaults)
    val keystoreDir: File = File("keystores"),
    val originalKeystoreName: String = "original.keystore",  // ORIGINAL_KEYSTORE_NAME
    val uploadKeystoreName: String = "upload.keystore",      // UPLOAD_KEYSTORE_NAME

    // Behavior flags
    val overwriteExisting: Boolean = false        // OVERWRITE in script
) {

    /**
     * Distinguished Name for certificate generation (matches script DN construction)
     */
    val distinguishedName: String
        get() = "CN=$companyName, OU=$department, O=$organization, L=$city, ST=$state, C=$country"

    val originalKeystorePath: File get() = File(keystoreDir, originalKeystoreName)
    val uploadKeystorePath: File get() = File(keystoreDir, uploadKeystoreName)

    /**
     * Validates configuration parameters
     */
    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (keystorePassword.isBlank()) errors.add("Keystore password cannot be blank")
        if (keyAlias.isBlank()) errors.add("Key alias cannot be blank")
        if (keyPassword.isBlank()) errors.add("Key password cannot be blank")
        if (keySize < 1024) errors.add("Key size must be at least 1024 bits")
        if (validity <= 0) errors.add("Validity period must be positive")
        if (country.length != 2) errors.add("Country code must be exactly 2 characters")
        return errors
    }

    companion object {
        /**
         * Creates default configuration matching script's ORIGINAL keystore
         */
        fun original(): KeystoreConfig = KeystoreConfig(
            companyName = "Android Debug",
            keyAlias = "androiddebugkey",
            originalKeystoreName = "original.keystore"
        )

        /**
         * Creates default configuration matching script's UPLOAD keystore
         */
        fun upload(): KeystoreConfig = KeystoreConfig(
            companyName = "Android Release",
            keyAlias = "upload",
            uploadKeystoreName = "upload.keystore"
        )
    }
}