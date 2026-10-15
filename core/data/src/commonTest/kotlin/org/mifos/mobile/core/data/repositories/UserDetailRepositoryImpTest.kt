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
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.UserDetailRepository
import org.mifos.mobile.core.data.repositoryImpl.UserDetailRepositoryImp
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.model.entity.notification.NotificationUserDetail
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.notification.NotificationUserDetailResponseDto
import org.mifos.mobile.core.network.dto.payloads.NotificationRegisterPayloadDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class UserDetailRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var userDetailRepository: UserDetailRepository

    private suspend fun createMockHttpResponse(content: String): HttpResponse {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond(content, HttpStatusCode.OK, headersOf("Content-Type", "application/json"))
                }
            }
        }
        return client.get("https://localhost")
    }

    @Test
    fun testRegisterNotification_Success() = runTest(testDispatcher) {
        val mockPayload = NotificationRegisterPayload(123L, "token")

        val fakeDataManager = object : DataManager() {
            override val notificationApi = object : BaseFakeNotificationService() {
                override suspend fun registerNotification(payload: NotificationRegisterPayloadDto?): HttpResponse {
                    return createMockHttpResponse("OK")
                }
            }
        }

        userDetailRepository = UserDetailRepositoryImp(
            dataManager = fakeDataManager,
            ioDispatcher = testDispatcher,
        )

        val result = userDetailRepository.registerNotification(mockPayload)

        val data = assertIs<DataState.Success<String>>(result)
        assertEquals("Notification Registered Successfully", data.data)
    }

    @Test
    fun testRegisterNotification_Error() = runTest(testDispatcher) {
        val errorMessage = "Failed to register notification"
        val mockPayload = NotificationRegisterPayload(123L, "token")

        val fakeDataManager = object : DataManager() {
            override val notificationApi = object : BaseFakeNotificationService() {
                override suspend fun registerNotification(payload: NotificationRegisterPayloadDto?): HttpResponse {
                    throw Exception(errorMessage)
                }
            }
        }

        userDetailRepository = UserDetailRepositoryImp(
            dataManager = fakeDataManager,
            ioDispatcher = testDispatcher,
        )

        val result = userDetailRepository.registerNotification(mockPayload)

        val error = assertIs<DataState.Error<String>>(result)
        assertEquals(errorMessage, error.exception.message)
    }

    @Test
    fun testGetUserNotificationId_Success() = runTest(testDispatcher) {
        val mockNotificationUserDetailResponseDto = NotificationUserDetailResponseDto(1)
        val mockId = 123L

        val fakeDataManager = object : DataManager() {
            override val notificationApi = object : BaseFakeNotificationService() {
                override fun getUserNotificationId(clientId: Long): Flow<NotificationUserDetailResponseDto> {
                    return flowOf(mockNotificationUserDetailResponseDto)
                }
            }
        }

        userDetailRepository = UserDetailRepositoryImp(
            dataManager = fakeDataManager,
            ioDispatcher = testDispatcher,
        )

        val flow = userDetailRepository.getUserNotificationId(mockId)

        val item = flow.drop(1).first()
        assertIs<DataState.Success<NotificationUserDetail>>(item)
    }

    @Test
    fun testGetUserNotificationId_Error() = runTest(testDispatcher) {
        val errorMessage = "Failed to get user notification"
        val mockId = 123L

        val fakeDataManager = object : DataManager() {
            override val notificationApi = object : BaseFakeNotificationService() {
                override fun getUserNotificationId(clientId: Long): Flow<NotificationUserDetailResponseDto> {
                    return flow { throw Exception(errorMessage) }
                }
            }
        }

        userDetailRepository = UserDetailRepositoryImp(
            dataManager = fakeDataManager,
            ioDispatcher = testDispatcher,
        )

        val flow = userDetailRepository.getUserNotificationId(mockId)

        val item = flow.drop(1).first()
        val error = assertIs<DataState.Error<NotificationUserDetail>>(item)
        assertEquals(errorMessage, error.exception.message)
    }

    @Test
    fun testUpdateRegisterNotification_Success() = runTest(testDispatcher) {
        val mockPayload = NotificationRegisterPayload(123L, "token")
        val mockId = 123L

        val fakeDataManager = object : DataManager() {
            override val notificationApi = object : BaseFakeNotificationService() {
                override suspend fun updateRegisterNotification(
                    clientId: Long,
                    payload: NotificationRegisterPayloadDto?,
                ): HttpResponse {
                    return createMockHttpResponse("OK")
                }
            }
        }

        userDetailRepository = UserDetailRepositoryImp(
            dataManager = fakeDataManager,
            ioDispatcher = testDispatcher,
        )

        val result = userDetailRepository.updateRegisterNotification(mockId, mockPayload)

        val data = assertIs<DataState.Success<String>>(result)
        assertEquals("Notification Updated Successfully", data.data)
    }

    @Test
    fun testUpdateRegisterNotificationError() = runTest(testDispatcher) {
        val errorMessage = "Failed to update register notification"
        val mockPayload = NotificationRegisterPayload(123L, "token")
        val mockId = 123L

        val fakeDataManager = object : DataManager() {
            override val notificationApi = object : BaseFakeNotificationService() {
                override suspend fun updateRegisterNotification(
                    clientId: Long,
                    payload: NotificationRegisterPayloadDto?,
                ): HttpResponse {
                    throw Exception(errorMessage)
                }
            }
        }

        userDetailRepository = UserDetailRepositoryImp(
            dataManager = fakeDataManager,
            ioDispatcher = testDispatcher,
        )

        val result = userDetailRepository.updateRegisterNotification(mockId, mockPayload)

        val error = assertIs<DataState.Error<String>>(result)
        assertEquals(errorMessage, error.exception.message)
    }
}
