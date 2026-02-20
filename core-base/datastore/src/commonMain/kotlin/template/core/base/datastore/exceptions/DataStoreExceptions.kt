/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.exceptions

/**
 * Base exception for all preference-related errors in the data store.
 *
 * @param message The error message.
 * @param cause The cause of the exception, if any.
 *
 * Example:
 * ```kotlin
 * throw PreferencesException("Something went wrong")
 * ```
 */
sealed class PreferencesException(message: String, cause: Throwable? = null) :
    Exception(message, cause)

/**
 * Thrown when an invalid key is used in the data store.
 *
 * @param key The invalid key.
 *
 * Example:
 * ```kotlin
 * throw InvalidKeyException("invalid-key")
 * ```
 */
class InvalidKeyException(key: String) : PreferencesException("Invalid key: $key")

/**
 * Thrown when serialization of a value fails.
 *
 * @param message The error message.
 * @param cause The cause of the exception, if any.
 *
 * Example:
 * ```kotlin
 * throw SerializationException("Failed to serialize value")
 * ```
 */
class SerializationException(message: String, cause: Throwable? = null) :
    PreferencesException("Serialization failed: $message", cause)

/**
 * Thrown when deserialization of a value fails.
 *
 * @param message The error message.
 * @param cause The cause of the exception, if any.
 *
 * Example:
 * ```kotlin
 * throw DeserializationException("Failed to deserialize value")
 * ```
 */
class DeserializationException(message: String, cause: Throwable? = null) :
    PreferencesException("Deserialization failed: $message", cause)

/**
 * Thrown when an unsupported type is used in the data store.
 *
 * @param type The unsupported type.
 *
 * Example:
 * ```kotlin
 * throw UnsupportedTypeException("CustomType")
 * ```
 */
class UnsupportedTypeException(type: String) :
    PreferencesException("Unsupported type: $type")

/**
 * Thrown when a cache operation fails in the data store.
 *
 * @param message The error message.
 * @param cause The cause of the exception, if any.
 *
 * Example:
 * ```kotlin
 * throw CacheException("Failed to cache value")
 * ```
 */
class CacheException(message: String, cause: Throwable? = null) :
    PreferencesException("Cache operation failed: $message", cause)
