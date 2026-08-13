/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

sealed class DataState<out T> {
    abstract val data: T?

    data object Loading : DataState<Nothing>() {
        override val data: Nothing? get() = null
    }

    data class Success<T>(
        override val data: T,
    ) : DataState<T>()

    data class Error<T>(
        val exception: Throwable,
        override val data: T? = null,
    ) : DataState<T>() {
        val message: String get() = exception.message ?: "Unknown error"
    }
}

fun <T> Flow<T>.asDataStateFlow(
    exceptionMapper: suspend (Throwable) -> MifosException = ::defaultExceptionMapper,
): Flow<DataState<T>> =
    map<T, DataState<T>> { DataState.Success(it) }
        .onStart { emit(DataState.Loading) }
        .catch { emit(DataState.Error(exceptionMapper(it), null)) }

suspend fun <T> safeDataStateCall(
    dispatcher: CoroutineDispatcher,
    exceptionMapper: suspend (Throwable) -> MifosException = ::defaultExceptionMapper,
    block: suspend () -> T,
): DataState<T> {
    return withContext(dispatcher) {
        try {
            DataState.Success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            DataState.Error(exceptionMapper(e), null)
        }
    }
}

private fun defaultExceptionMapper(throwable: Throwable): MifosException {
    return if (throwable is MifosException) {
        throwable
    } else {
        MifosException.GenericError(
            throwable.message ?: "Unknown error",
            throwable,
        )
    }
}
