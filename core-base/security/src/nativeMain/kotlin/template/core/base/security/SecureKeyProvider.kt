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
import platform.CoreFoundation.CFDictionaryRef
import platform.CoreFoundation.CFTypeRefVar
import platform.Foundation.NSData
import platform.Foundation.NSDictionary
import platform.Foundation.create
import platform.Foundation.dictionaryWithObjects
import platform.Security.SecItemAdd
import platform.Security.SecItemCopyMatching
import platform.Security.SecItemDelete
import platform.Security.errSecSuccess
import platform.Security.kSecAttrAccount
import platform.Security.kSecAttrService
import platform.Security.kSecClass
import platform.Security.kSecClassGenericPassword
import platform.Security.kSecReturnData
import platform.Security.kSecValueData

private const val SERVICE_NAME = "org.mifos.secure"
private const val ACCOUNT_NAME = "field_encryptor_key"

// NSDictionary and CFDictionary are toll-free bridged on Apple platforms.
// The Kotlin compiler warns about these casts at compile time, but they
// succeed at runtime because the underlying memory layout is identical.
@Suppress("CAST_NEVER_SUCCEEDS")
@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
actual class SecureKeyProvider {

    actual fun getKey(): ByteArray? {
        val query = buildKeychainQuery(kSecReturnData to true)
        val cfQuery = query as CFDictionaryRef

        val result = memScoped {
            val ref = alloc<CFTypeRefVar>()
            val status = SecItemCopyMatching(cfQuery, ref.ptr)
            if (status == errSecSuccess) {
                ref.value as? NSData
            } else {
                null
            }
        }

        return result?.let { nsData ->
            ByteArray(nsData.length.toInt()).also { bytes ->
                bytes.usePinned { pinned ->
                    platform.posix.memcpy(pinned.addressOf(0), nsData.bytes, nsData.length)
                }
            }
        }
    }

    actual fun generateKey(): ByteArray {
        deleteKey()
        val key = SecureRandom().nextBytes(32)
        val nsData = key.usePinned { pinned ->
            NSData.create(bytes = pinned.addressOf(0), length = key.size.toULong())
        }
        val query = buildKeychainQuery(kSecValueData to nsData)
        val cfQuery = query as CFDictionaryRef
        val status = SecItemAdd(cfQuery, null)
        if (status != errSecSuccess) {
            error("Failed to store key in Keychain: $status")
        }
        return key
    }

    actual fun deleteKey() {
        val query = buildKeychainQuery()
        val cfQuery = query as CFDictionaryRef
        SecItemDelete(cfQuery)
    }

    private fun buildKeychainQuery(
        vararg extras: Pair<Any?, Any?>,
    ): Map<Any?, *> {
        val keys = mutableListOf<Any?>(kSecClass, kSecAttrService, kSecAttrAccount)
        val values = mutableListOf<Any?>(kSecClassGenericPassword, SERVICE_NAME, ACCOUNT_NAME)
        for ((k, v) in extras) {
            keys.add(k)
            values.add(v)
        }
        return NSDictionary.dictionaryWithObjects(objects = values, forKeys = keys)
    }
}
