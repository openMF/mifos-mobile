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

import org.mifos.mobile.core.model.entity.User
import org.mifospay.core.common.DataState

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
    ): DataState<String>

    suspend fun login(username: String, password: String): DataState<User>

    suspend fun verifyUser(authenticationToken: String?, requestId: String?): DataState<String>

    suspend fun updateAccountPassword(
        newPassword: String,
        confirmPassword: String,
    ): DataState<String>
}
