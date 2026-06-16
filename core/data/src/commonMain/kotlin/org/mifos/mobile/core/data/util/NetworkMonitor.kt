/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import org.mifos.mobile.core.common.DataState

/**
 * Utility for reporting app connectivity status
 */
interface NetworkMonitor {
    val isOnline: Flow<Boolean>
}

/**
 * Wraps an [upstream] [DataState] [Flow] with a reactive network guard.
 *
 * Uses [combine] internally so the merge function re-runs whenever *either*
 * [isOnline] **or** [upstream] emits a new value.
 *
 * Emission priority (evaluated top-to-bottom on every pair of values):
 *  1. **[DataState.Success]**  — always forwarded; cached data survives going offline.
 *  2. **[DataState.Loading]**  — always forwarded; lets the UI render a spinner
 *                                before any network error is surfaced.
 *  3. **offline**              — emits [NetworkUnavailableException].
 *  4. **otherwise**            — forwards whatever error the upstream emitted.
 *
 * ```kotlin
 * override fun getLoans(): Flow<DataState<List<Loan>>> =
 *     networkMonitor.withNetworkCheck(
 *         dataManager.getLoans().asDataStateFlow()
 *     ).flowOn(ioDispatcher)
 * ```
 */
fun <T> NetworkMonitor.withNetworkCheck(
    upstream: Flow<DataState<T>>,
): Flow<DataState<T>> = combine(isOnline, upstream) { isOnline, dataState ->
    when {
        dataState is DataState.Success -> dataState
        dataState is DataState.Loading -> dataState
        !isOnline -> DataState.Error(NetworkUnavailableException())
        else -> dataState
    }
}
