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

sealed class MifosException(
    message: String,
    cause: Throwable? = null,
) : Exception(message, cause) {

    class BadRequest(
        message: String = "Bad request",
        cause: Throwable? = null,
    ) : MifosException(message, cause)

    class Unauthorized(
        message: String = "Unauthorized",
        cause: Throwable? = null,
    ) : MifosException(message, cause)

    class NotFound(
        message: String = "Not found",
        cause: Throwable? = null,
    ) : MifosException(message, cause)

    class RequestTimeout(
        message: String = "Request timeout",
        cause: Throwable? = null,
    ) : MifosException(message, cause)

    class TooManyRequests(
        message: String = "Too many requests",
        cause: Throwable? = null,
    ) : MifosException(message, cause)

    class ServerError(
        message: String = "Server error",
        cause: Throwable? = null,
    ) : MifosException(message, cause)

    class ClientError(
        message: String,
        cause: Throwable? = null,
    ) : MifosException(message, cause)

    class SerializationError(
        message: String = "Serialization error",
        cause: Throwable? = null,
    ) : MifosException(message, cause)

    class NetworkError(
        message: String = "Network error",
        cause: Throwable? = null,
    ) : MifosException(message, cause)

    class GenericError(
        message: String,
        cause: Throwable? = null,
    ) : MifosException(message, cause)
}
