/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model

import com.google.gson.Gson
import retrofit2.HttpException

object MFErrorParser {
    const val LOG_TAG: String = "MFErrorParser"

    private val gson: Gson = Gson()

    fun parseError(serverResponse: String?): MifosError {
        return gson.fromJson(serverResponse, MifosError::class.java)
    }

    fun errorMessage(throwableError: Throwable): String? {
        var errorMessage = ""
        try {
            if (throwableError is HttpException) {
                errorMessage = throwableError.response()?.errorBody()?.string().toString()
                errorMessage = parseError(errorMessage).errors[0].defaultUserMessage ?: "Something went wrong!"
            } else {
                errorMessage = throwableError.toString()
            }
        } catch (throwable: Throwable) {
            // not able to catch error
        }
        return errorMessage
    }
}
