/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositoryImpl

import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.common.MifosException
import org.mifos.mobile.core.data.mapper.auth.toModel
import org.mifos.mobile.core.data.mapper.payloads.toDto
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.model.entity.UpdatePasswordPayload
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.payload.LoginPayload
import org.mifos.mobile.core.model.entity.register.RegisterPayload
import org.mifos.mobile.core.model.entity.register.UserVerify
import org.mifos.mobile.core.network.DataManager

class UserAuthRepositoryImp(
    private val dataManager: DataManager,
    ioDispatcher: CoroutineDispatcher,
) : BaseRepository(ioDispatcher), UserAuthRepository {

    override suspend fun registerUser(
        registerPayload: RegisterPayload,
    ): DataState<String> = safeCall {
        dataManager.registrationApi
            .registerUser(registerPayload.toDto())
            .bodyAsText()
    }

    override suspend fun login(username: String, password: String): DataState<User> = safeCall {
        val loginPayload = LoginPayload(
            username = username,
            password = password,
        ).toDto()

        val user = dataManager.authenticationApi
            .authenticate(loginPayload)
            .toModel()

        if (user.base64EncodedAuthenticationKey != null) {
            user
        } else {
            throw MifosException.ClientError("Invalid Credentials")
        }
    }

    override suspend fun verifyUser(
        authenticationToken: String?,
        requestId: String?,
    ): DataState<String> = safeCall {
        val userVerify = UserVerify(
            authenticationToken = authenticationToken,
            requestId = requestId,
        ).toDto()

        dataManager.registrationApi
            .verifyUser(userVerify)
            .bodyAsText()
    }

    override suspend fun updateAccountPassword(
        newPassword: String,
        confirmPassword: String,
    ): DataState<String> = safeCall {
        val payload = UpdatePasswordPayload(
            password = newPassword,
            repeatPassword = confirmPassword,
        ).toDto()

        dataManager.userDetailsApi
            .updateAccountPassword(payload)
            .bodyAsText()
    }
}
