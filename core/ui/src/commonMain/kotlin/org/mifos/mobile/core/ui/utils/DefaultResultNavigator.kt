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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance

/**
 * Default implementation of [ResultNavigator] using a [MutableSharedFlow]
 * to enable one-time result delivery across navigation destinations.
 */
class DefaultResultNavigator : ResultNavigator {

    private val shared = MutableSharedFlow<Any>(extraBufferCapacity = 1)

    /**
     * Emits a result object to the flow, making it available for observers.
     *
     * @param result The result object to emit.
     */
    override suspend fun emit(result: Any) {
        shared.emit(result)
    }

    /**
     * Returns a shared flow of emitted results for collection.
     *
     * @return A [Flow] of result objects.
     */
    override fun resultFlow(): Flow<Any> = shared
}

/**
 * Observes result emissions filtered by the given type [T].
 *
 * Usage:
 * ```
 * navigator.observe<AuthResult>().collect { result -> ... }
 * ```
 *
 * @return A [Flow] emitting only results of type [T].
 */
inline fun <reified T : Any> ResultNavigator.observe(): Flow<T> {
    return resultFlow().filterIsInstance<T>()
}
