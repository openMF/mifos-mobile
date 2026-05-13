/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.security

import org.bouncycastle.jce.provider.BouncyCastleProvider
import java.security.Security
import java.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

private const val AES_GCM = "AES/GCM/NoPadding"
private const val GCM_TAG_LENGTH = 128
private const val GCM_IV_LENGTH = 12
private const val ENCRYPTED_PREFIX = "ENC:"

actual class FieldEncryptor(private val keyProvider: SecureKeyProvider) {

    init {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(BouncyCastleProvider())
        }
    }

    actual fun encrypt(plaintext: String): String {
        val encrypted = encrypt(plaintext.encodeToByteArray())
        return ENCRYPTED_PREFIX + Base64.getEncoder().encodeToString(encrypted)
    }

    actual fun decrypt(ciphertext: String): String {
        require(ciphertext.startsWith(ENCRYPTED_PREFIX)) { "Invalid encrypted payload format" }
        val payload = ciphertext.removePrefix(ENCRYPTED_PREFIX)
        val decoded = Base64.getDecoder().decode(payload)
        return decrypt(decoded).decodeToString()
    }

    actual fun encrypt(data: ByteArray): ByteArray {
        val key = keyProvider.getOrCreateKey() as ByteArray
        val cipher = Cipher.getInstance(AES_GCM, BouncyCastleProvider.PROVIDER_NAME)
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(key, "AES"))
        val iv = cipher.iv
        val encrypted = cipher.doFinal(data)
        return iv + encrypted
    }

    actual fun decrypt(data: ByteArray): ByteArray {
        require(data.size > GCM_IV_LENGTH) { "Ciphertext is too short" }
        val key = keyProvider.getExistingKey() as? ByteArray
            ?: throw SecurityException("Encryption key not found — data unrecoverable")
        val iv = data.copyOfRange(0, GCM_IV_LENGTH)
        val ciphertext = data.copyOfRange(GCM_IV_LENGTH, data.size)
        val cipher = Cipher.getInstance(AES_GCM, BouncyCastleProvider.PROVIDER_NAME)
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(key, "AES"),
            GCMParameterSpec(GCM_TAG_LENGTH, iv),
        )
        return cipher.doFinal(ciphertext)
    }
}
