/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.store

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import org.mobilenativefoundation.store.store5.StoreReadResponse

/**
 * Extensions for mapping [StoreReadResponse] flows to simpler types.
 *
 * Repositories can use these to unwrap Store responses internally while
 * keeping their public interface as `Flow<T>` or `Flow<Result<T>>`.
 */

/**
 * Maps a [StoreReadResponse] flow to `Flow<Result<Output>>`.
 *
 * - [StoreReadResponse.Data] → [Result.success]
 * - [StoreReadResponse.Error] → [Result.failure]
 * - [StoreReadResponse.Loading] and [StoreReadResponse.NoNewData] are filtered out.
 */
fun <Output : Any> Flow<StoreReadResponse<Output>>.mapToResult(): Flow<Result<Output>> {
    return filterNot { it is StoreReadResponse.Loading || it is StoreReadResponse.NoNewData }
        .map { response ->
            when (response) {
                is StoreReadResponse.Data -> Result.success(response.value)

                is StoreReadResponse.Error.Exception ->
                    Result.failure(response.error)

                is StoreReadResponse.Error.Message ->
                    Result.failure(RuntimeException(response.message))

                is StoreReadResponse.Error.Custom<*> ->
                    Result.failure(RuntimeException("Store error: ${response.error}"))

                else -> Result.failure(RuntimeException("Unexpected store response: $response"))
            }
        }
}

/**
 * Maps a [StoreReadResponse] flow to `Flow<Output>`, emitting only data values.
 *
 * Loading, error, and no-data responses are silently filtered out.
 * Use [mapToResult] when error handling is needed.
 */
fun <Output : Any> Flow<StoreReadResponse<Output>>.mapToData(): Flow<Output> {
    return map { response ->
        when (response) {
            is StoreReadResponse.Data -> response.value
            else -> null
        }
    }.filterNot { it == null }.map { it!! }
}

/**
 * Returns the first [StoreReadResponse.Data] value or throws on error.
 */
suspend fun <Output : Any> StoreReadResponse<Output>.requireData(): Output {
    return when (this) {
        is StoreReadResponse.Data -> value
        is StoreReadResponse.Error.Exception -> throw error
        is StoreReadResponse.Error.Message -> error(message)
        is StoreReadResponse.Error.Custom<*> -> error("Store error: $error")
        else -> error("No data available: $this")
    }
}
