/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.testing.fake

import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.register.RegisterPayload

/**
 * Fake implementation of [UserAuthRepository] for testing.
 *
 * Usage:
 * ```kotlin
 * val fakeRepo = FakeUserAuthRepository()
 *
 * // Set success response
 * fakeRepo.setLoginResult(DataState.Success(testUser))
 *
 * // Set error response
 * fakeRepo.setLoginResult(DataState.Error(Exception("Invalid credentials")))
 *
 * // Use in tests
 * val viewModel = LoginViewModel(fakeRepo)
 * ```
 */
class FakeUserAuthRepository : UserAuthRepository {

    private var loginResult: DataState<User> = DataState.Success(createDefaultUser())
    private var registerResult: DataState<String> = DataState.Success("Registration successful")
    private var verifyResult: DataState<String> = DataState.Success("Verification successful")
    private var updatePasswordResult: DataState<String> = DataState.Success("Password updated")

    // Track method calls for verification
    var loginCallCount = 0
        private set
    var lastLoginUsername: String? = null
        private set
    var lastLoginPassword: String? = null
        private set

    fun setLoginResult(result: DataState<User>) {
        loginResult = result
    }

    fun setRegisterResult(result: DataState<String>) {
        registerResult = result
    }

    fun setVerifyResult(result: DataState<String>) {
        verifyResult = result
    }

    fun setUpdatePasswordResult(result: DataState<String>) {
        updatePasswordResult = result
    }

    fun reset() {
        loginResult = DataState.Success(createDefaultUser())
        registerResult = DataState.Success("Registration successful")
        verifyResult = DataState.Success("Verification successful")
        updatePasswordResult = DataState.Success("Password updated")
        loginCallCount = 0
        lastLoginUsername = null
        lastLoginPassword = null
    }

    override suspend fun registerUser(registerPayload: RegisterPayload): DataState<String> {
        return registerResult
    }

    override suspend fun login(username: String, password: String): DataState<User> {
        loginCallCount++
        lastLoginUsername = username
        lastLoginPassword = password
        return loginResult
    }

    override suspend fun verifyUser(
        authenticationToken: String?,
        requestId: String?,
    ): DataState<String> {
        return verifyResult
    }

    override suspend fun updateAccountPassword(
        newPassword: String,
        confirmPassword: String,
    ): DataState<String> {
        return updatePasswordResult
    }

    private fun createDefaultUser(): User {
        return User(
            userId = 1L,
            username = "testuser",
            base64EncodedAuthenticationKey = "test-auth-key",
        )
    }
}
