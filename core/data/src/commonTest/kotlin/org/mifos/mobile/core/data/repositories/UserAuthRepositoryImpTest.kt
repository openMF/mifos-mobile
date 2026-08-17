/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.repositories

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserAuthRepository
import org.mifos.mobile.core.data.repositoryImpl.UserAuthRepositoryImp
import org.mifos.mobile.core.model.entity.User
import org.mifos.mobile.core.model.entity.register.RegisterPayload
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.auth.UserDto
import org.mifos.mobile.core.network.dto.payloads.LoginPayloadDto
import org.mifos.mobile.core.network.dto.payloads.RegisterPayloadDto
import org.mifos.mobile.core.network.dto.payloads.UserVerifyPayloadDto
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class UserAuthRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataManager: DataManager
    private lateinit var userAuthRepositoryImp: UserAuthRepository
    private lateinit var mockUser: User

    private suspend fun createMockHttpResponse(
        content: String,
        status: HttpStatusCode = HttpStatusCode.OK,
    ): HttpResponse {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond(content, status, headersOf("Content-Type", "application/json"))
                }
            }
        }
        return client.get("https://localhost")
    }

    @BeforeTest
    fun setUp() {
        dataManager = object : DataManager() {}
        userAuthRepositoryImp = UserAuthRepositoryImp(dataManager, testDispatcher)
        mockUser = User()
    }

    @Test
    fun testRegisterUser_SuccessResponseReceivedFromDataManager_ReturnSuccessfulRegistration() =
        runTest(testDispatcher) {
            val successResponse: HttpResponse = createMockHttpResponse("OK")
            dataManager = object : DataManager() {
                override val registrationApi = object : BaseFakeRegistrationService() {
                    override suspend fun registerUser(registerPayload: RegisterPayloadDto?): HttpResponse {
                        return successResponse
                    }
                }
            }
            userAuthRepositoryImp = UserAuthRepositoryImp(dataManager, testDispatcher)

            val registerPayload = RegisterPayload(
                accountNumber = "accountNumber",
                authenticationMode = "authenticationMode",
                email = "email",
                firstName = "firstName",
                lastName = "lastName",
                mobileNumber = "mobileNumber",
                password = "password",
                username = "username",
            )

            val result = userAuthRepositoryImp.registerUser(
                registerPayload,
            )

            assertIs<DataState.Success<String>>(result)
        }

    @Test
    fun testRegisterUser_ErrorResponseReceivedFromDataManager_ReturnsUnsuccessfulRegistration() =
        runTest(testDispatcher) {
            dataManager = object : DataManager() {
                override val registrationApi = object : BaseFakeRegistrationService() {
                    override suspend fun registerUser(registerPayload: RegisterPayloadDto?): HttpResponse {
                        throw ClientRequestException(
                            createMockHttpResponse(
                                "{\"defaultUserMessage\":\"Error occurred\"}",
                                HttpStatusCode.BadRequest,
                            ),
                            "Error occurred",
                        )
                    }
                }
            }
            userAuthRepositoryImp = UserAuthRepositoryImp(dataManager, testDispatcher)

            val registerPayload = RegisterPayload(
                accountNumber = "accountNumber",
                authenticationMode = "authenticationMode",
                email = "email",
                firstName = "firstName",
                lastName = "lastName",
                mobileNumber = "mobileNumber",
                password = "password",
                username = "username",
            )

            val result = userAuthRepositoryImp.registerUser(
                registerPayload,
            )

            val error = assertIs<DataState.Error<String>>(result)
            assertEquals("Error occurred", error.exception.message)
        }

    @Test
    fun testLogin_SuccessResponseReceivedFromDataManager_ReturnsUserSuccessfully() = runTest(testDispatcher) {
        val mockAuthResponse = UserDto(
            base64EncodedAuthenticationKey = "token",
            username = "username",
            userId = 1L,
        )

        dataManager = object : DataManager() {
            override val authenticationApi = object : BaseFakeAuthenticationService() {
                override suspend fun authenticate(loginPayload: LoginPayloadDto): UserDto {
                    return mockAuthResponse
                }
            }
        }
        userAuthRepositoryImp = UserAuthRepositoryImp(dataManager, testDispatcher)

        val result = userAuthRepositoryImp.login("username", "password")

        val data = assertIs<DataState.Success<User>>(result)
        assertEquals("token", data.data.base64EncodedAuthenticationKey)
    }

    @Test
    fun testLogin_ErrorResponseReceivedFromDataManager_ReturnsError() = runTest(testDispatcher) {
        dataManager = object : DataManager() {
            override val authenticationApi = object : BaseFakeAuthenticationService() {
                override suspend fun authenticate(loginPayload: LoginPayloadDto): UserDto {
                    throw ClientRequestException(
                        createMockHttpResponse(
                            "{\"defaultUserMessage\":\"Error occurred\"}",
                            HttpStatusCode.BadRequest,
                        ),
                        "Error occurred",
                    )
                }
            }
        }
        userAuthRepositoryImp = UserAuthRepositoryImp(dataManager, testDispatcher)

        val result = userAuthRepositoryImp.login("username", "password")

        val error = assertIs<DataState.Error<User>>(result)
        assertEquals("Error occurred", error.exception.message)
    }

    @Test
    fun testVerifyUser_SuccessResponseReceivedFromDataManager_ReturnsSuccessfulRegistrationVerification() =
        runTest(testDispatcher) {
            val successResponse: HttpResponse = createMockHttpResponse("OK")
            dataManager = object : DataManager() {
                override val registrationApi = object : BaseFakeRegistrationService() {
                    override suspend fun verifyUser(userVerify: UserVerifyPayloadDto?): HttpResponse {
                        return successResponse
                    }
                }
            }
            userAuthRepositoryImp = UserAuthRepositoryImp(dataManager, testDispatcher)

            val result = userAuthRepositoryImp.verifyUser(
                "token",
                "requestId",
            )

            assertIs<DataState.Success<String>>(result)
        }

    @Test
    fun testVerifyUser_ErrorResponseReceivedFromDataManager_ReturnsUnsuccessfulRegistrationVerification() =
        runTest(testDispatcher) {
            dataManager = object : DataManager() {
                override val registrationApi = object : BaseFakeRegistrationService() {
                    override suspend fun verifyUser(userVerify: UserVerifyPayloadDto?): HttpResponse {
                        throw ClientRequestException(
                            createMockHttpResponse(
                                "{\"defaultUserMessage\":\"Error occurred\"}",
                                HttpStatusCode.BadRequest,
                            ),
                            "Error occurred",
                        )
                    }
                }
            }
            userAuthRepositoryImp = UserAuthRepositoryImp(dataManager, testDispatcher)

            val result = userAuthRepositoryImp.verifyUser(
                "token",
                "requestId",
            )

            val error = assertIs<DataState.Error<String>>(result)
            assertEquals("Error occurred", error.exception.message)
        }
}
