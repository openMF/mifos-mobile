/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.transformWhile
import kotlinx.coroutines.flow.update

inline fun <T : Any?, R : Any?> DataState<T>.map(
    transform: (T) -> R,
): DataState<R> = when (this) {
    is DataState.Success -> DataState.Success(transform(data))
    is DataState.Loading -> DataState.Loading
    is DataState.Pending -> DataState.Pending(transform(data))
    is DataState.Error -> DataState.Error(error, data?.let(transform))
    is DataState.NoNetwork -> DataState.NoNetwork(data?.let(transform))
}

inline fun <T : Any?, R : Any?> DataState<T>.mapNullable(
    transform: (T?) -> R,
): DataState<R> = when (this) {
    is DataState.Success -> DataState.Success(data = transform(data))
    is DataState.Loading -> DataState.Loading
    is DataState.Pending -> DataState.Pending(data = transform(data))
    is DataState.Error -> DataState.Error(error = error, data = transform(data))
    is DataState.NoNetwork -> DataState.NoNetwork(data = transform(data))
}

fun <T : Any?> Flow<DataState<T>>.takeUntilSuccess(): Flow<DataState<T>> = transformWhile {
    emit(it)
    it !is DataState.Success
}

fun <T : Any?> MutableStateFlow<DataState<T>>.updateToPendingOrLoading() {
    update { dataState ->
        dataState.data
            ?.let { data -> DataState.Pending(data = data) }
            ?: DataState.Loading
    }
}

fun <T1, T2, R> combineDataStates(
    dataState1: DataState<T1>,
    dataState2: DataState<T2>,
    transform: (t1: T1, t2: T2) -> R,
): DataState<R> {
    // Wraps the `transform` lambda to allow null data to be passed in. If either of the passed in
    // values are null, the regular transform will not be invoked and null is returned.
    val nullableTransform: (T1?, T2?) -> R? = { t1, t2 ->
        if (t1 != null && t2 != null) transform(t1, t2) else null
    }
    return when {
        // Error states have highest priority, fail fast.
        dataState1 is DataState.Error -> {
            DataState.Error(
                error = dataState1.error,
                data = nullableTransform(dataState1.data, dataState2.data),
            )
        }

        dataState2 is DataState.Error -> {
            DataState.Error(
                error = dataState2.error,
                data = nullableTransform(dataState1.data, dataState2.data),
            )
        }

        dataState1 is DataState.NoNetwork || dataState2 is DataState.NoNetwork -> {
            DataState.NoNetwork(nullableTransform(dataState1.data, dataState2.data))
        }

        // Something is still loading, we will wait for all the data.
        dataState1 is DataState.Loading || dataState2 is DataState.Loading -> DataState.Loading

        // Pending state for everything while any one piece of data is updating.
        dataState1 is DataState.Pending || dataState2 is DataState.Pending -> {
            @Suppress("UNCHECKED_CAST")
            DataState.Pending(transform(dataState1.data as T1, dataState2.data as T2))
        }

        // Both states are Success and have data
        else -> {
            @Suppress("UNCHECKED_CAST")
            DataState.Success(transform(dataState1.data as T1, dataState2.data as T2))
        }
    }
}

fun <T1, T2, T3, R> combineDataStates(
    dataState1: DataState<T1>,
    dataState2: DataState<T2>,
    dataState3: DataState<T3>,
    transform: (t1: T1, t2: T2, t3: T3) -> R,
): DataState<R> =
    dataState1
        .combineDataStatesWith(dataState2) { t1, t2 -> t1 to t2 }
        .combineDataStatesWith(dataState3) { t1t2Pair, t3 ->
            transform(t1t2Pair.first, t1t2Pair.second, t3)
        }

fun <T1, T2, T3, T4, R> combineDataStates(
    dataState1: DataState<T1>,
    dataState2: DataState<T2>,
    dataState3: DataState<T3>,
    dataState4: DataState<T4>,
    transform: (t1: T1, t2: T2, t3: T3, t4: T4) -> R,
): DataState<R> =
    dataState1
        .combineDataStatesWith(dataState2) { t1, t2 -> t1 to t2 }
        .combineDataStatesWith(dataState3) { t1t2Pair, t3 ->
            Triple(t1t2Pair.first, t1t2Pair.second, t3)
        }
        .combineDataStatesWith(dataState4) { t1t2t3Triple, t3 ->
            transform(t1t2t3Triple.first, t1t2t3Triple.second, t1t2t3Triple.third, t3)
        }

fun <T1, T2, R> DataState<T1>.combineDataStatesWith(
    dataState2: DataState<T2>,
    transform: (t1: T1, t2: T2) -> R,
): DataState<R> =
    combineDataStates(this, dataState2, transform)
