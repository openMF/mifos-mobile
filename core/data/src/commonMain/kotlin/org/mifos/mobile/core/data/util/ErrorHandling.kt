/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.util

import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class ErrorResponse(
    val defaultUserMessage: String? = null,
    val errors: List<ErrorDetail>? = null,
)

@Serializable
data class ErrorDetail(
    val defaultUserMessage: String? = null,
)

/**
 * Generic function to extract error messages from API responses
 */
suspend fun extractErrorMessage(response: HttpResponse): String {
    val responseText = response.bodyAsText()
    return try {
        val json = Json { ignoreUnknownKeys = true }
        val errorResponse = json.decodeFromString<ErrorResponse>(responseText)
        errorResponse.errors?.firstOrNull()?.defaultUserMessage
            ?: errorResponse.defaultUserMessage
            ?: "Unknown error"
    } catch (e: Exception) {
        "Failed to parse error response"
    }
}
