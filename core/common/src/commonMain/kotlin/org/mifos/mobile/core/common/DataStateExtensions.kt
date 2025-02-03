/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformWhile

inline fun <T : Any?, R : Any?> DataState<T>.map(
    transform: (T) -> R,
): DataState<R> = when (this) {
    is DataState.Success -> DataState.Success(transform(data))
    is DataState.Loading -> DataState.Loading
    is DataState.Error -> DataState.Error(exception, data?.let(transform))
}

inline fun <T : Any?, R : Any?> DataState<T>.mapNullable(
    transform: (T?) -> R,
): DataState<R> = when (this) {
    is DataState.Success -> DataState.Success(data = transform(data))
    is DataState.Loading -> DataState.Loading
    is DataState.Error -> DataState.Error(exception = exception, data = transform(data))
}

fun <T : Any?> Flow<DataState<T>>.takeUntilResultSuccess(): Flow<DataState<T>> = transformWhile {
    emit(it)
    it !is DataState.Success
}

fun <T1, T2, R> combineResults(
    dataState1: DataState<T1>,
    dataState2: DataState<T2>,
    transform: (t1: T1, t2: T2) -> R,
): DataState<R> {
    val nullableTransform: (T1?, T2?) -> R? = { t1, t2 ->
        if (t1 != null && t2 != null) transform(t1, t2) else null
    }

    return when {
        // Error states have highest priority, fail fast.
        dataState1 is DataState.Error -> {
            DataState.Error(
                exception = dataState1.exception,
                data = nullableTransform(dataState1.data, dataState2.data),
            )
        }

        dataState2 is DataState.Error -> {
            DataState.Error(
                exception = dataState2.exception,
                data = nullableTransform(dataState1.data, dataState2.data),
            )
        }

        // Something is still loading, we will wait for all the data.
        dataState1 is DataState.Loading || dataState2 is DataState.Loading -> DataState.Loading

        // Pending state for everything while any one piece of data is updating.
        // Both states are _root_ide_package_.org.mifos.mobile.core.common.Result.Success and have data
        else -> {
            @Suppress("UNCHECKED_CAST")
            DataState.Success(transform(dataState1.data as T1, dataState2.data as T2))
        }
    }
}

fun <T1, T2, T3, R> combineResults(
    dataState1: DataState<T1>,
    dataState2: DataState<T2>,
    dataState3: DataState<T3>,
    transform: (t1: T1, t2: T2, t3: T3) -> R,
): DataState<R> =
    dataState1
        .combineResultsWith(dataState2) { t1, t2 -> t1 to t2 }
        .combineResultsWith(dataState3) { t1t2Pair, t3 ->
            transform(t1t2Pair.first, t1t2Pair.second, t3)
        }

fun <T1, T2, T3, T4, R> combineResults(
    dataState1: DataState<T1>,
    dataState2: DataState<T2>,
    dataState3: DataState<T3>,
    dataState4: DataState<T4>,
    transform: (t1: T1, t2: T2, t3: T3, t4: T4) -> R,
): DataState<R> =
    dataState1
        .combineResultsWith(dataState2) { t1, t2 -> t1 to t2 }
        .combineResultsWith(dataState3) { t1t2Pair, t3 ->
            Triple(t1t2Pair.first, t1t2Pair.second, t3)
        }
        .combineResultsWith(dataState4) { t1t2t3Triple, t3 ->
            transform(t1t2t3Triple.first, t1t2t3Triple.second, t1t2t3Triple.third, t3)
        }

fun <T1, T2, R> DataState<T1>.combineResultsWith(
    dataState2: DataState<T2>,
    transform: (t1: T1, t2: T2) -> R,
): DataState<R> =
    combineResults(this, dataState2, transform)
