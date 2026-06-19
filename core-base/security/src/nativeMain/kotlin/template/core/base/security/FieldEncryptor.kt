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

import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.usePinned
import kotlinx.cinterop.value
import platform.CoreCrypto.CCCryptorStatus
import platform.CoreCrypto.kCCAlgorithmAES
import platform.CoreCrypto.kCCDecrypt
import platform.CoreCrypto.kCCEncrypt
import platform.CoreCrypto.kCCOptionPKCS7Padding
import platform.CoreCrypto.kCCSuccess
import platform.Foundation.NSData
import platform.Foundation.base64EncodedStringWithOptions
import platform.Foundation.create
import platform.posix.size_tVar

/**
 * IV length for AES-CBC mode. CommonCrypto's `CCCrypt` with `kCCOptionPKCS7Padding`
 * uses CBC (not GCM). AES-CBC requires a 16-byte IV matching the block size.
 */
private const val CBC_IV_LENGTH = 16

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual class FieldEncryptor(private val keyProvider: SecureKeyProvider) {

    actual fun encrypt(plaintext: String): String {
        val encrypted = encrypt(plaintext.encodeToByteArray())
        val nsData = encrypted.usePinned { pinned ->
            NSData.create(
                bytes = pinned.addressOf(0),
                length = encrypted.size.toULong(),
            )
        }
        return nsData.base64EncodedStringWithOptions(0u)
    }

    actual fun decrypt(ciphertext: String): String {
        val nsData = NSData.create(
            base64EncodedString = ciphertext,
            options = 0u,
        ) ?: error("Invalid Base64 ciphertext")
        val bytes = ByteArray(nsData.length.toInt())
        bytes.usePinned { pinned ->
            platform.posix.memcpy(pinned.addressOf(0), nsData.bytes, nsData.length)
        }
        return decrypt(bytes).decodeToString()
    }

    actual fun encrypt(data: ByteArray): ByteArray {
        val key = keyProvider.getKey() ?: keyProvider.generateKey()
        val iv = SecureRandom().nextBytes(CBC_IV_LENGTH)
        val encrypted = ccCrypt(kCCEncrypt, key, iv, data)
        return iv + encrypted
    }

    actual fun decrypt(data: ByteArray): ByteArray {
        val key = keyProvider.getKey()
            ?: error("Encryption key not found — data unrecoverable")
        val iv = data.copyOfRange(0, CBC_IV_LENGTH)
        val ciphertext = data.copyOfRange(CBC_IV_LENGTH, data.size)
        return ccCrypt(kCCDecrypt, key, iv, ciphertext)
    }

    @Suppress("FunctionParameterNaming")
    private fun ccCrypt(
        op: platform.CoreCrypto.CCOperation,
        key: ByteArray,
        iv: ByteArray,
        data: ByteArray,
    ): ByteArray = memScoped {
        val outLength = alloc<size_tVar>()
        val bufferSize = data.size + 16
        val outBuffer = ByteArray(bufferSize)

        val status: CCCryptorStatus = key.usePinned { keyPin ->
            iv.usePinned { ivPin ->
                data.usePinned { dataPin ->
                    outBuffer.usePinned { outPin ->
                        platform.CoreCrypto.CCCrypt(
                            op = op,
                            alg = kCCAlgorithmAES,
                            options = kCCOptionPKCS7Padding,
                            key = keyPin.addressOf(0),
                            keyLength = key.size.toULong(),
                            iv = ivPin.addressOf(0),
                            dataIn = dataPin.addressOf(0),
                            dataInLength = data.size.toULong(),
                            dataOut = outPin.addressOf(0),
                            dataOutAvailable = bufferSize.toULong(),
                            dataOutMoved = outLength.ptr,
                        )
                    }
                }
            }
        }

        if (status != kCCSuccess) {
            error("CCCrypt failed with status: $status")
        }

        outBuffer.copyOfRange(0, outLength.value.toInt())
    }
}
