/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repository

import kotlinx.coroutines.flow.Flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.model.entity.User

interface UserAuthRepository {

    suspend fun registerUser(
        accountNumber: String?,
        authenticationMode: String?,
        email: String?,
        firstName: String?,
        lastName: String?,
        mobileNumber: String?,
        password: String?,
        username: String?,
    ): Flow<ResponseBody>

    suspend fun login(username: String, password: String): Flow<User>

    suspend fun verifyUser(authenticationToken: String?, requestId: String?): Flow<ResponseBody>

    suspend fun updateAccountPassword(
        newPassword: String,
        confirmPassword: String,
    ): Flow<ResponseBody>
}
