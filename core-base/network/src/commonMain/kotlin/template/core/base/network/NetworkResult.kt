/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See See https://github.com/openMF/kmp-project-template/blob/main/LICENSE
 */
package template.core.base.network

/**
 * Represents the result of a network or remote operation, encapsulating either a success or an error.
 *
 * This is a sealed interface with two implementations:
 * - [Success] indicates the operation completed successfully and contains the resulting data.
 * - [Error] represents a failure and contains a [NetworkError] describing the error condition.
 *
 * @param D The type of data returned on success.
 * @param E The type of error returned on failure, constrained to [NetworkError].
 */
sealed interface NetworkResult<out D, out E : NetworkError> {

    /**
     * Represents a successful result.
     *
     * @param D The type of the successful response data.
     * @property data The actual result of the operation.
     */
    data class Success<out D>(val data: D) : NetworkResult<D, Nothing>

    /**
     * Represents a failed result due to a [NetworkError].
     *
     * @param E The specific type of [NetworkError] encountered.
     * @property error Details about the error that occurred.
     */
    data class Error<out E : NetworkError>(val error: E) : NetworkResult<Nothing, E>
}
