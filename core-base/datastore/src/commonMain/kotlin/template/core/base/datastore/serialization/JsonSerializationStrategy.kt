/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import template.core.base.datastore.exceptions.DeserializationException
import template.core.base.datastore.exceptions.SerializationException

/**
 * Implementation of [SerializationStrategy] that uses kotlinx.serialization's [Json]
 * for serializing and deserializing data.
 *
 * This strategy provides a convenient way to store and retrieve complex data objects in the
 * data store by converting them to and from JSON strings.
 *
 * Example usage:
 * ```kotlin
 * // Define a serializable data class
 * @Serializable
 * data class Settings(val theme: String, val fontSize: Int)
 *
 * // Create a JsonSerializationStrategy instance
 * val serializationStrategy = JsonSerializationStrategy()
 * val settingsSerializer = Settings.serializer()
 *
 * // Serialize an object
 * val settings = Settings("dark", 14)
 * val serializedData = serializationStrategy.serialize(settings, settingsSerializer)
 * println("Serialized data: $serializedData")
 * // Example output: Result.success("{"theme":"dark","fontSize":14}")
 *
 * // Deserialize a string
 * val dataString = "{"theme":"light","fontSize":12}"
 * val deserializedSettings = serializationStrategy.deserialize(dataString, settingsSerializer)
 * println("Deserialized settings: $deserializedSettings")
 * // Example output: Result.success(Settings(theme=light, fontSize=12))
 * ```
 *
 * @property json The [Json] instance used for serialization and deserialization.
 * Configurable with default lenient settings.
 */
class JsonSerializationStrategy(
    private val json: Json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        isLenient = true
    },
) : SerializationStrategy {

    /**
     * Serializes the given [value] of type [T] to a JSON [String] using the provided [serializer]
     * and the configured [Json] instance.
     *
     * @param value The object to serialize.
     * @param serializer The [KSerializer] for type [T].
     * @return A [Result.success] containing the JSON string, or [Result.failure]
     * if serialization fails (e.g., due to serialization errors).
     */
    override suspend fun <T> serialize(value: T, serializer: KSerializer<T>): Result<String> {
        return try {
            val result = json.encodeToString(serializer, value)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(
                SerializationException(
                    "Failed to serialize value of type ${value?.let { it::class.simpleName }}",
                    e,
                ),
            )
        }
    }

    /**
     * Deserializes the given JSON [data] string back into an object of type [T] using
     * the provided [serializer] and the configured [Json] instance.
     *
     * @param data The JSON string data to deserialize.
     * @param serializer The [KSerializer] for type [T].
     * @return A [Result.success] containing the deserialized object, or [Result.failure] if
     * deserialization fails (e.g., due to invalid JSON format or deserialization errors).
     */
    override suspend fun <T> deserialize(data: String, serializer: KSerializer<T>): Result<T> {
        return try {
            if (data.isBlank()) {
                return Result.failure(
                    DeserializationException("Cannot deserialize blank string"),
                )
            }

            val result = json.decodeFromString(serializer, data)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(
                DeserializationException(
                    "Failed to deserialize data: ${data.take(100)}${if (data.length > 100) "..." else ""}",
                    e,
                ),
            )
        }
    }
}
