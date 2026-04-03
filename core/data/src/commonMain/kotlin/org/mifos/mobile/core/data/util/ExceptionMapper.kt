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
import org.mifos.mobile.core.common.MifosException

fun Throwable.toMifosException(): MifosException = when (this) {
    is MifosException -> this
    is ClientRequestException -> MifosException.ClientError(message, this)
    is ServerResponseException -> MifosException.ServerError(message, this)
    is IOException -> MifosException.NetworkError(message ?: "Network error", this)
    else -> MifosException.GenericError(message ?: "Unknown error", this)
}

suspend fun Throwable.toMifosExceptionSuspend(): MifosException = when (this) {
    is MifosException -> this
    is ClientRequestException -> {
        val errorMessage = extractErrorMessage(response)
        MifosException.ClientError(errorMessage, this)
    }
    is ServerResponseException -> MifosException.ServerError(message, this)
    is IOException -> MifosException.NetworkError(message ?: "Network error", this)
    else -> MifosException.GenericError(message ?: "Unknown error", this)
}
