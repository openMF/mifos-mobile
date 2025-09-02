/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.factory

import com.russhwolf.settings.Settings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import template.core.base.datastore.cache.CacheManager
import template.core.base.datastore.cache.LruCacheManager
import template.core.base.datastore.contracts.ReactiveDataStore
import template.core.base.datastore.handlers.BooleanTypeHandler
import template.core.base.datastore.handlers.DoubleTypeHandler
import template.core.base.datastore.handlers.FloatTypeHandler
import template.core.base.datastore.handlers.IntTypeHandler
import template.core.base.datastore.handlers.LongTypeHandler
import template.core.base.datastore.handlers.StringTypeHandler
import template.core.base.datastore.handlers.TypeHandler
import template.core.base.datastore.reactive.ChangeNotifier
import template.core.base.datastore.reactive.DefaultChangeNotifier
import template.core.base.datastore.reactive.DefaultValueObserver
import template.core.base.datastore.repository.DefaultReactivePreferencesRepository
import template.core.base.datastore.repository.ReactivePreferencesRepository
import template.core.base.datastore.serialization.JsonSerializationStrategy
import template.core.base.datastore.serialization.SerializationStrategy
import template.core.base.datastore.store.ReactiveUserPreferencesDataStore
import template.core.base.datastore.validation.DefaultPreferencesValidator
import template.core.base.datastore.validation.PreferencesValidator

/**
 * Factory for constructing reactive data store repositories and data stores with customizable configuration.
 *
 * This class uses the builder pattern to allow flexible configuration of settings, dispatcher,
 * cache size, validator, serialization strategy, and change notifier.
 *
 * Example usage:
 * ```kotlin
 * // Simple usage with defaults
 * val repository = DataStoreFactory.create()
 *
 * // Custom configuration
 * val repository = DataStoreFactory()
 *     .cacheSize(500)
 *     .dispatcher(Dispatchers.IO)
 *     .settings(MyCustomSettings())
 *     .build()
 * ```
 */
@Deprecated("Use Settings Library instead")
class DataStoreFactory {
    private var settings: Settings? = null
    private var dispatcher: CoroutineDispatcher = Dispatchers.Default
    private var cacheSize: Int = 200
    private var validator: PreferencesValidator? = null
    private var serializationStrategy: SerializationStrategy? = null
    private var changeNotifier: ChangeNotifier? = null

    /**
     * Sets a custom [Settings] implementation for the data store.
     *
     * If not provided, the default [Settings] implementation will be used.
     *
     * @param settings The [Settings] instance to use.
     * @return This [DataStoreFactory] instance for chaining.
     */
    fun settings(settings: Settings) = apply {
        this.settings = settings
    }

    /**
     * Sets the coroutine [dispatcher] for data store operations.
     *
     * The default is [Dispatchers.Default].
     *
     * @param dispatcher The [CoroutineDispatcher] to use.
     * @return This [DataStoreFactory] instance for chaining.
     */
    fun dispatcher(dispatcher: CoroutineDispatcher) = apply {
        this.dispatcher = dispatcher
    }

    /**
     * Sets the cache size for the LRU cache.
     *
     * The default is 200 entries.
     *
     * @param size The maximum number of entries in the cache.
     * @return This [DataStoreFactory] instance for chaining.
     */
    fun cacheSize(size: Int) = apply {
        this.cacheSize = size
    }

    /**
     * Sets a custom [PreferencesValidator] for validating keys and values.
     *
     * If not provided, the default validator will be used.
     *
     * @param validator The [PreferencesValidator] to use.
     * @return This [DataStoreFactory] instance for chaining.
     */
    fun validator(validator: PreferencesValidator) = apply {
        this.validator = validator
    }

    /**
     * Sets a custom [SerializationStrategy] for serializing and deserializing values.
     *
     * If not provided, the default [JsonSerializationStrategy] will be used.
     *
     * @param strategy The [SerializationStrategy] to use.
     * @return This [DataStoreFactory] instance for chaining.
     */
    fun serializationStrategy(strategy: SerializationStrategy) = apply {
        this.serializationStrategy = strategy
    }

    /**
     * Sets a custom [ChangeNotifier] for broadcasting change events.
     *
     * If not provided, the default [DefaultChangeNotifier] will be used.
     *
     * @param notifier The [ChangeNotifier] to use.
     * @return This [DataStoreFactory] instance for chaining.
     */
    fun changeNotifier(notifier: ChangeNotifier) = apply {
        this.changeNotifier = notifier
    }

    /**
     * Builds and returns a [ReactivePreferencesRepository] with the current configuration.
     *
     * @return A fully configured [ReactivePreferencesRepository].
     *
     * Example usage:
     * ```kotlin
     * val repository = DataStoreFactory().build()
     * ```
     */
    fun build(): ReactivePreferencesRepository {
        val finalSettings = settings ?: Settings()
        val finalValidator = validator ?: DefaultPreferencesValidator()
        val finalSerializationStrategy = serializationStrategy ?: JsonSerializationStrategy()
        val finalChangeNotifier = changeNotifier ?: DefaultChangeNotifier()

        val cacheManager: CacheManager<String, Any> = LruCacheManager(maxSize = cacheSize)
        val valueObserver = DefaultValueObserver(finalChangeNotifier)

        @Suppress("UNCHECKED_CAST")
        val typeHandlers: List<TypeHandler<Any>> = listOf(
            IntTypeHandler(),
            StringTypeHandler(),
            BooleanTypeHandler(),
            LongTypeHandler(),
            FloatTypeHandler(),
            DoubleTypeHandler(),
        ) as List<TypeHandler<Any>>

        val reactiveDataStore = ReactiveUserPreferencesDataStore(
            settings = finalSettings,
            dispatcher = dispatcher,
            typeHandlers = typeHandlers,
            serializationStrategy = finalSerializationStrategy,
            validator = finalValidator,
            cacheManager = cacheManager,
            changeNotifier = finalChangeNotifier,
            valueObserver = valueObserver,
        )

        return DefaultReactivePreferencesRepository(reactiveDataStore)
    }

    /**
     * Builds and returns a [ReactiveDataStore] with the current configuration, without the repository wrapper.
     *
     * Use this if you need direct access to data store methods.
     *
     * @return A fully configured [ReactiveDataStore].
     *
     * Example usage:
     * ```kotlin
     * val dataStore = DataStoreFactory().buildDataStore()
     * ```
     */
    fun buildDataStore(): ReactiveDataStore {
        val finalSettings = settings ?: Settings()
        val finalValidator = validator ?: DefaultPreferencesValidator()
        val finalSerializationStrategy = serializationStrategy ?: JsonSerializationStrategy()
        val finalChangeNotifier = changeNotifier ?: DefaultChangeNotifier()

        val cacheManager: CacheManager<String, Any> = LruCacheManager(maxSize = cacheSize)
        val valueObserver = DefaultValueObserver(finalChangeNotifier)

        @Suppress("UNCHECKED_CAST")
        val typeHandlers: List<TypeHandler<Any>> = listOf(
            IntTypeHandler(),
            StringTypeHandler(),
            BooleanTypeHandler(),
            LongTypeHandler(),
            FloatTypeHandler(),
            DoubleTypeHandler(),
        ) as List<TypeHandler<Any>>

        return ReactiveUserPreferencesDataStore(
            settings = finalSettings,
            dispatcher = dispatcher,
            typeHandlers = typeHandlers,
            serializationStrategy = finalSerializationStrategy,
            validator = finalValidator,
            cacheManager = cacheManager,
            changeNotifier = finalChangeNotifier,
            valueObserver = valueObserver,
        )
    }

    companion object {
        /**
         * Creates a [ReactivePreferencesRepository] with default configuration.
         *
         * This is the simplest way to obtain a working data store repository.
         *
         * @return A [ReactivePreferencesRepository] with default settings.
         *
         * Example usage:
         * ```kotlin
         * val repository = DataStoreFactory.create()
         * ```
         */
        fun create(): ReactivePreferencesRepository {
            return DataStoreFactory().build()
        }

        /**
         * Creates a [ReactivePreferencesRepository] with a custom [Settings] instance.
         *
         * Useful for providing platform-specific settings.
         *
         * @param settings The [Settings] instance to use.
         * @return A [ReactivePreferencesRepository] with the specified settings.
         *
         * Example usage:
         * ```kotlin
         * val repository = DataStoreFactory.create(customSettings)
         * ```
         */
        fun create(settings: Settings): ReactivePreferencesRepository {
            return DataStoreFactory()
                .settings(settings)
                .build()
        }

        /**
         * Creates a [ReactivePreferencesRepository] with a custom [Settings] instance and [CoroutineDispatcher].
         *
         * Useful for platform-specific configurations (e.g., Android/iOS).
         *
         * @param settings The [Settings] instance to use.
         * @param dispatcher The [CoroutineDispatcher] to use.
         * @return A [ReactivePreferencesRepository] with the specified settings and dispatcher.
         *
         * Example usage:
         * ```kotlin
         * val repository = DataStoreFactory.create(customSettings, Dispatchers.IO)
         * ```
         */
        fun create(
            settings: Settings,
            dispatcher: CoroutineDispatcher,
        ): ReactivePreferencesRepository {
            return DataStoreFactory()
                .settings(settings)
                .dispatcher(dispatcher)
                .build()
        }
    }
}
