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

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.model.entity.UpdatePasswordPayload
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.payload.LoginPayload
import org.mifos.mobile.core.model.entity.register.RegisterPayload
import org.mifos.mobile.core.model.entity.register.UserVerify
import org.mifos.mobile.core.network.DataManager
import org.mifospay.core.common.DataState

class UserAuthRepositoryImp(
    private val dataManager: DataManager,
    private val ioDispatcher: CoroutineDispatcher,
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
    ): DataState<String> {
        val registerPayload = RegisterPayload(
            accountNumber = accountNumber,
            authenticationMode = authenticationMode,
            email = email,
            firstName = firstName,
            lastName = lastName,
            mobileNumber = mobileNumber,
            password = password,
            username = username,
        )
        return try {
            withContext(ioDispatcher) {
                dataManager.registrationApi.registerUser(registerPayload)
                DataState.Success("User registered Successfully")
            }
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override suspend fun login(username: String, password: String): DataState<User> {
        val loginPayload = LoginPayload(
            username = username,
            password = password,
        )
        return try {
            withContext(ioDispatcher) {
                val user = dataManager.authenticationApi.authenticate(loginPayload)
                DataState.Success(user)
            }
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override suspend fun verifyUser(
        authenticationToken: String?,
        requestId: String?,
    ): DataState<String> {
        val userVerify = UserVerify(
            authenticationToken = authenticationToken,
            requestId = requestId,
        )
        return try {
            withContext(ioDispatcher) {
                dataManager.registrationApi.verifyUser(userVerify)
            }
            DataState.Success("User Verified Successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }

    override suspend fun updateAccountPassword(
        newPassword: String,
        confirmPassword: String,
    ): DataState<String> {
        val payload = UpdatePasswordPayload(
            password = newPassword,
            repeatPassword = confirmPassword,
        )
        return try {
            withContext(ioDispatcher) {
                dataManager.userDetailsApi.updateAccountPassword(payload)
            }
            DataState.Success("Password Updated Successfully")
        } catch (e: Exception) {
            DataState.Error(e, null)
        }
    }
}
