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

import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException
import org.mifos.mobile.core.common.MifosException

fun Throwable.toMifosException(): MifosException = when (this) {
    is MifosException -> this
    is ClientRequestException -> when (response.status.value) {
        400 -> MifosException.BadRequest(message, this)
        401 -> MifosException.Unauthorized(message, this)
        404 -> MifosException.NotFound(message, this)
        408 -> MifosException.RequestTimeout(message, this)
        429 -> MifosException.TooManyRequests(message, this)
        else -> MifosException.ClientError(message, this)
    }
    is ServerResponseException -> MifosException.ServerError(message, this)
    is IOException -> MifosException.NetworkError(message ?: "Network error", this)
    is SerializationException -> MifosException.SerializationError(
        message ?: "Serialization error",
        this,
    )
    else -> MifosException.GenericError(message ?: "Unknown error", this)
}

suspend fun Throwable.toMifosExceptionSuspend(): MifosException = when (this) {
    is MifosException -> this
    is ClientRequestException -> {
        val errorMessage = extractErrorMessage(response)
        when (response.status.value) {
            400 -> MifosException.BadRequest(errorMessage, this)
            401 -> MifosException.Unauthorized(errorMessage, this)
            404 -> MifosException.NotFound(errorMessage, this)
            408 -> MifosException.RequestTimeout(errorMessage, this)
            429 -> MifosException.TooManyRequests(errorMessage, this)
            else -> MifosException.ClientError(errorMessage, this)
        }
    }
    is ServerResponseException -> MifosException.ServerError(message, this)
    is IOException -> MifosException.NetworkError(message ?: "Network error", this)
    is SerializationException -> MifosException.SerializationError(
        message ?: "Serialization error",
        this,
    )
    else -> MifosException.GenericError(message ?: "Unknown error", this)
}
