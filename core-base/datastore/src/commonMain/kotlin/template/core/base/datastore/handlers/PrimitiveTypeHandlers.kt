/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.handlers

import com.russhwolf.settings.Settings

/**
 * Handler for storing and retrieving [Int] values in the data store.
 *
 * Example usage:
 * ```kotlin
 * val handler = IntTypeHandler()
 * handler.put(settings, "count", 42)
 * val value = handler.get(settings, "count", 0)
 * ```
 */
class IntTypeHandler : TypeHandler<Int> {
    override suspend fun put(settings: Settings, key: String, value: Int): Result<Unit> {
        println("[IntTypeHandler] put: key=$key, value=$value")
        return runCatching { settings.putInt(key, value) }
    }

    override suspend fun get(settings: Settings, key: String, default: Int): Result<Int> {
        val result = runCatching { settings.getInt(key, default) }
        println("[IntTypeHandler] get: key=$key, result=$result")
        return result
    }

    override fun canHandle(value: Any?): Boolean = value is Int
}

/**
 * Handler for storing and retrieving [String] values in the data store.
 *
 * Example usage:
 * ```kotlin
 * val handler = StringTypeHandler()
 * handler.put(settings, "username", "admin")
 * val value = handler.get(settings, "username", "default")
 * ```
 */
class StringTypeHandler : TypeHandler<String> {
    override suspend fun put(settings: Settings, key: String, value: String): Result<Unit> {
        println("[StringTypeHandler] put: key=$key, value=$value")
        return runCatching { settings.putString(key, value) }
    }

    override suspend fun get(settings: Settings, key: String, default: String): Result<String> {
        val result = runCatching { settings.getString(key, default) }
        println("[StringTypeHandler] get: key=$key, result=$result")
        return result
    }

    override fun canHandle(value: Any?): Boolean = value is String
}

/**
 * Handler for storing and retrieving [Boolean] values in the data store.
 *
 * Example usage:
 * ```kotlin
 * val handler = BooleanTypeHandler()
 * handler.put(settings, "enabled", true)
 * val value = handler.get(settings, "enabled", false)
 * ```
 */
class BooleanTypeHandler : TypeHandler<Boolean> {
    override suspend fun put(settings: Settings, key: String, value: Boolean): Result<Unit> {
        println("[BooleanTypeHandler] put: key=$key, value=$value")
        return runCatching { settings.putBoolean(key, value) }
    }

    override suspend fun get(settings: Settings, key: String, default: Boolean): Result<Boolean> {
        val result = runCatching { settings.getBoolean(key, default) }
        println("[BooleanTypeHandler] get: key=$key, result=$result")
        return result
    }

    override fun canHandle(value: Any?): Boolean = value is Boolean
}

/**
 * Handler for storing and retrieving [Long] values in the data store.
 *
 * Example usage:
 * ```kotlin
 * val handler = LongTypeHandler()
 * handler.put(settings, "timestamp", 123456789L)
 * val value = handler.get(settings, "timestamp", 0L)
 * ```
 */
class LongTypeHandler : TypeHandler<Long> {
    override suspend fun put(settings: Settings, key: String, value: Long): Result<Unit> {
        println("[LongTypeHandler] put: key=$key, value=$value")
        return runCatching { settings.putLong(key, value) }
    }

    override suspend fun get(settings: Settings, key: String, default: Long): Result<Long> {
        val result = runCatching { settings.getLong(key, default) }
        println("[LongTypeHandler] get: key=$key, result=$result")
        return result
    }

    override fun canHandle(value: Any?): Boolean = value is Long
}

/**
 * Handler for storing and retrieving [Float] values in the data store.
 *
 * Example usage:
 * ```kotlin
 * val handler = FloatTypeHandler()
 * handler.put(settings, "ratio", 0.5f)
 * val value = handler.get(settings, "ratio", 0.0f)
 * ```
 */
class FloatTypeHandler : TypeHandler<Float> {
    override suspend fun put(settings: Settings, key: String, value: Float): Result<Unit> {
        println("[FloatTypeHandler] put: key=$key, value=$value")
        return runCatching { settings.putFloat(key, value) }
    }

    override suspend fun get(settings: Settings, key: String, default: Float): Result<Float> {
        val result = runCatching { settings.getFloat(key, default) }
        println("[FloatTypeHandler] get: key=$key, result=$result")
        return result
    }

    override fun canHandle(value: Any?): Boolean = value is Float
}

/**
 * Handler for storing and retrieving [Double] values in the data store.
 *
 * Example usage:
 * ```kotlin
 * val handler = DoubleTypeHandler()
 * handler.put(settings, "score", 99.9)
 * val value = handler.get(settings, "score", 0.0)
 * ```
 */
class DoubleTypeHandler : TypeHandler<Double> {
    override suspend fun put(settings: Settings, key: String, value: Double): Result<Unit> {
        println("[DoubleTypeHandler] put: key=$key, value=$value")
        return runCatching { settings.putDouble(key, value) }
    }

    override suspend fun get(settings: Settings, key: String, default: Double): Result<Double> {
        val result = runCatching { settings.getDouble(key, default) }
        println("[DoubleTypeHandler] get: key=$key, result=$result")
        return result
    }

    override fun canHandle(value: Any?): Boolean = value is Double
}
