/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.ResponseBody
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.model.entity.UpdatePasswordPayload
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.payload.LoginPayload
import org.mifos.mobile.core.model.entity.register.RegisterPayload
import org.mifos.mobile.core.model.entity.register.UserVerify
import org.mifos.mobile.core.network.DataManager
import javax.inject.Inject

class UserAuthRepositoryImp @Inject constructor(
    private val dataManager: DataManager,
) : UserAuthRepository {

    override suspend fun registerUser(
        accountNumber: String?,
        authenticationMode: String?,
        email: String?,
        firstName: String?,
        lastName: String?,
        mobileNumber: String?,
        password: String?,
        username: String?,
    ): Flow<ResponseBody> {
        val registerPayload = RegisterPayload().apply {
            this.accountNumber = accountNumber
            this.authenticationMode = authenticationMode
            this.email = email
            this.firstName = firstName
            this.lastName = lastName
            this.mobileNumber = mobileNumber
            this.password = password
            this.username = username
        }
        return flow {
            emit(dataManager.registerUser(registerPayload))
        }
    }

    override suspend fun login(username: String, password: String): Flow<User> {
        val loginPayload = LoginPayload(
            username = username,
            password = password,
        )

        return flow {
            emit(dataManager.login(loginPayload))
        }
    }

    override suspend fun verifyUser(
        authenticationToken: String?,
        requestId: String?,
    ): Flow<ResponseBody> {
        val userVerify = UserVerify().apply {
            this.authenticationToken = authenticationToken
            this.requestId = requestId
        }
        return flow {
            emit(dataManager.verifyUser(userVerify))
        }
    }

    override suspend fun updateAccountPassword(
        newPassword: String,
        confirmPassword: String,
    ): Flow<ResponseBody> {
        val payload = UpdatePasswordPayload().apply {
            this.password = newPassword
            this.repeatPassword = confirmPassword
        }

        return flow {
            emit(dataManager.updateAccountPassword(payload))
        }
    }
}
