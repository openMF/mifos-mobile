/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.di

import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Koin test module for dependency injection in tests.
 *
 * Usage:
 * ```kotlin
 * class MyViewModelTest : KoinTest {
 *     @get:Rule
 *     val koinTestRule = KoinTestRule.create {
 *         modules(testModule, fakeRepositoryModule)
 *     }
 *
 *     private val viewModel: MyViewModel by inject()
 * }
 * ```
 */
val testModule: Module = module {
    // Common test dependencies go here
}

/**
 * Creates a test Koin module with fake repositories.
 *
 * @param additionalModules Additional modules to include
 * @return Combined test module
 */
fun createTestModule(vararg additionalModules: Module): Module = module {
    includes(testModule)
    includes(*additionalModules)
}
