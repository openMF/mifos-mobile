/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package template.core.base.datastore.integration

import app.cash.turbine.test
import com.russhwolf.settings.MapSettings
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.Serializable
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import template.core.base.datastore.di.CoreDatastoreModule
import template.core.base.datastore.reactive.PreferenceFlowOperators
import template.core.base.datastore.repository.ReactivePreferencesRepository
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ReactiveIntegrationTest : KoinTest {

    private val repository: ReactivePreferencesRepository by inject()
    private val operators: PreferenceFlowOperators by inject()
    private val testDispatcher = StandardTestDispatcher()

    @Serializable
    data class UserProfile(
        val name: String,
        val email: String,
        val theme: String,
    )

    @BeforeTest
    fun setup() {
        startKoin {
            modules(
                CoreDatastoreModule,
                module {
                    // Override dispatcher for testing
                    single<CoroutineDispatcher>(named("IO")) { testDispatcher }
                    single { PreferenceFlowOperators(get()) }
                    single<com.russhwolf.settings.Settings> { MapSettings() }
                },
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun endToEndReactiveTest_ComplexWorkflow() = runTest(testDispatcher) {
        val defaultProfile = UserProfile("", "", "light")

        // Start observing user profile
        repository.observeSerializablePreference(
            "user_profile",
            defaultProfile,
            UserProfile.serializer(),
        ).test {
            // Initial default profile
            assertEquals(defaultProfile, awaitItem())

            // Save initial profile
            val initialProfile = UserProfile("John", "john@example.com", "light")
            repository.saveSerializablePreference(
                "user_profile",
                initialProfile,
                UserProfile.serializer(),
            )
            assertEquals(initialProfile, awaitItem())

            // Update theme
            val darkProfile = initialProfile.copy(theme = "dark")
            repository.saveSerializablePreference(
                "user_profile",
                darkProfile,
                UserProfile.serializer(),
            )
            assertEquals(darkProfile, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun combinedPreferencesWorkflow() = runTest(testDispatcher) {
        // Observe combined user state
        operators.combinePreferences(
            "username",
            "",
            "is_premium",
            false,
            "login_count",
            0,
        ) { username, isPremium, loginCount ->
            "User: $username, Premium: $isPremium, Logins: $loginCount"
        }.test {
            // Initial state
            assertEquals("User: , Premium: false, Logins: 0", awaitItem())

            // Set username
            repository.savePreference("username", "alice")
            assertEquals("User: alice, Premium: false, Logins: 0", awaitItem())

            // Upgrade to premium
            repository.savePreference("is_premium", true)
            assertEquals("User: alice, Premium: true, Logins: 0", awaitItem())

            // Increment login count
            repository.savePreference("login_count", 1)
            assertEquals("User: alice, Premium: true, Logins: 1", awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun saveAndRetrieveAllPrimitiveTypes() = runTest(testDispatcher) {
        // Int
        repository.savePreference("intKey", 42)
        assertEquals(42, repository.getPreference("intKey", 0).getOrThrow())
        // String
        repository.savePreference("stringKey", "hello")
        assertEquals("hello", repository.getPreference("stringKey", "").getOrThrow())
        // Boolean
        repository.savePreference("boolKey", true)
        assertEquals(true, repository.getPreference("boolKey", false).getOrThrow())
        // Long
        repository.savePreference("longKey", 123456789L)
        assertEquals(123456789L, repository.getPreference("longKey", 0L).getOrThrow())
        // Float
        repository.savePreference("floatKey", 3.14f)
        assertEquals(3.14f, repository.getPreference("floatKey", 0f).getOrThrow())
        // Double
        repository.savePreference("doubleKey", 2.718)
        assertEquals(2.718, repository.getPreference("doubleKey", 0.0).getOrThrow())
    }
}
