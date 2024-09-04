/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositories

import app.cash.turbine.test
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.data.repositoryImpl.UserAuthRepositoryImp
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.payload.LoginPayload
import org.mifos.mobile.core.model.entity.register.RegisterPayload
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.testing.util.FakeRemoteDataSource
import org.mifos.mobile.core.testing.util.FakeRemoteDataSource.userVerify
import org.mifos.mobile.core.testing.util.MainDispatcherRule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class UserAuthRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var userAuthRepositoryImp: UserAuthRepository
    private lateinit var mockUser: User

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        userAuthRepositoryImp = UserAuthRepositoryImp(dataManager)
        mockUser = FakeRemoteDataSource.user
    }

    @Test
    fun testRegisterUser_SuccessResponseReceivedFromDataManager_ReturnSuccessfulRegistration() =
        runTest {
            val successResponse = mock(ResponseBody::class.java)
            val registerPayload = RegisterPayload().apply {
                this.accountNumber = "accountNumber"
                this.authenticationMode = "authenticationMode"
                this.email = "email"
                this.firstName = "firstName"
                this.lastName = "lastName"
                this.mobileNumber = "mobileNumber"
                this.password = "password"
                this.username = "username"
            }

            Mockito.`when`(dataManager.registerUser(registerPayload)).thenReturn(successResponse)

            val result = userAuthRepositoryImp.registerUser(
                registerPayload.accountNumber,
                registerPayload.authenticationMode,
                registerPayload.email,
                registerPayload.firstName,
                registerPayload.lastName,
                registerPayload.mobileNumber,
                registerPayload.password,
                registerPayload.username,
            )
            result.test {
                assertEquals(successResponse, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(dataManager).registerUser(registerPayload)
        }

    @Test(expected = Exception::class)
    fun testRegisterUser_ErrorResponseReceivedFromDataManager_ReturnsUnsuccessfulRegistration() =
        runTest {
            val registerPayload = RegisterPayload().apply {
                this.accountNumber = "accountNumber"
                this.authenticationMode = "authenticationMode"
                this.email = "email"
                this.firstName = "firstName"
                this.lastName = "lastName"
                this.mobileNumber = "mobileNumber"
                this.password = "password"
                this.username = "username"
            }

            Mockito.`when`(dataManager.registerUser(registerPayload))
                .thenThrow(Exception("Error occurred"))

            val result = userAuthRepositoryImp.registerUser(
                registerPayload.accountNumber,
                registerPayload.authenticationMode,
                registerPayload.email,
                registerPayload.firstName,
                registerPayload.lastName,
                registerPayload.mobileNumber,
                registerPayload.password,
                registerPayload.username,
            )
            result.test {
                assertEquals(Throwable("Error occurred"), awaitError())
                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(dataManager).registerUser(registerPayload)
        }

    @Test
    fun testLogin_SuccessResponseReceivedFromDataManager_ReturnsUserSuccessfully() = runTest {
        val mockLoginPayload = LoginPayload().apply {
            this.username = "username"
            this.password = "password"
        }

        Mockito.`when`(
            dataManager.login(mockLoginPayload),
        ).thenReturn(mockUser)

        val result = userAuthRepositoryImp.login("username", "password")

        result.test {
            assertEquals(mockUser, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager).login(mockLoginPayload)
    }

    @Test(expected = Exception::class)
    fun testLogin_ErrorResponseReceivedFromDataManager_ReturnsError() = runTest {
        val mockLoginPayload = LoginPayload().apply {
            this.username = "username"
            this.password = "password"
        }
        Mockito.`when`(
            dataManager.login(mockLoginPayload),
        ).thenThrow(Exception("Error occurred"))

        val result = userAuthRepositoryImp.login("username", "password")
        result.test {
            assertEquals(Throwable("Error occurred"), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
        Mockito.verify(dataManager).login(mockLoginPayload)
    }

    @Test
    fun testVerifyUser_SuccessResponseReceivedFromDataManager_ReturnsSuccessfulRegistrationVerification() =
        runTest {
            val successResponse = mock(ResponseBody::class.java)
            Mockito.`when`(
                dataManager.verifyUser(userVerify),
            ).thenReturn(successResponse)

            val result =
                userAuthRepositoryImp.verifyUser(
                    userVerify.authenticationToken,
                    userVerify.requestId,
                )
            result.test {
                assertEquals(successResponse, awaitItem())
                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(dataManager).verifyUser(userVerify)
        }

    @Test(expected = Exception::class)
    fun testVerifyUser_ErrorResponseReceivedFromDataManager_ReturnsUnsuccessfulRegistrationVerification() =
        runTest {
            Mockito.`when`(
                dataManager.verifyUser(userVerify),
            ).thenThrow(Exception("Error occurred"))

            val result =
                userAuthRepositoryImp.verifyUser(
                    userVerify.authenticationToken,
                    userVerify.requestId,
                )
            result.test {
                assert(Throwable("Error occurred") == awaitError())
                cancelAndIgnoreRemainingEvents()
            }
            Mockito.verify(dataManager).verifyUser(userVerify)
        }
}
