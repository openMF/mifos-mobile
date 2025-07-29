/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.ui.utils

import kotlinx.coroutines.flow.Flow

/**
 * Interface for emitting and observing result-based navigation outcomes.
 *
 * This is typically used for communicating results between view models and screens
 * in a decoupled and reactive manner using [Flow].
 */
interface ResultNavigator {

    /**
     * Emits a result value (e.g., authentication result, form submission status).
     *
     * @param result The result to emit, can be any object.
     */
    suspend fun emit(result: Any)

    /**
     * Returns a shared [Flow] of results that observers can collect.
     *
     * @return A [Flow] emitting all results pushed through [emit].
     */
    fun resultFlow(): Flow<Any>
}

/**
 * Data class representing the result of an authentication process.
 *
 * @property success Indicates whether authentication was successful.
 */
data class AuthResult(val success: Boolean)
