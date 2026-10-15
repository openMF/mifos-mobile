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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.data.repositoryImpl.HomeRepositoryImp
import org.mifos.mobile.core.model.entity.MifosNotification
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.network.dto.accounts.AccountsResponseDto
import org.mifos.mobile.core.network.dto.client.ClientResponseDto
import org.mifos.mobile.core.network.services.ClientService
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class HomeRepositoryImpTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var dataManager: DataManager
    private lateinit var notificationRepository: NotificationRepository
    private lateinit var homeRepositoryImp: HomeRepositoryImp

    private var isError = false
    private lateinit var fakeHttpResponse: HttpResponse

    @BeforeTest
    fun setUp() = runTest(testDispatcher) {
        isError = false

        val client = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    respond("image/png,base64string")
                }
            }
        }
        fakeHttpResponse = client.get("")

        val fakeClientService = object : BaseFakeClientService() {
            override fun getClientAccounts(clientId: Long): Flow<AccountsResponseDto> {
                if (isError) return flow { throw Exception("Failed to fetch client accounts") }
                return flowOf(AccountsResponseDto())
            }

            override fun getClientForId(clientId: Long): Flow<ClientResponseDto> {
                if (isError) return flow { throw Exception("Failed to fetch current client") }
                return flowOf(ClientResponseDto())
            }

            override fun getClientImage(clientId: Long): Flow<HttpResponse> {
                if (isError) return flow { throw Exception("Failed to fetch client image") }
                return flowOf(fakeHttpResponse)
            }
        }

        dataManager = object : DataManager() {
            override val clientsApi: ClientService = fakeClientService
        }

        notificationRepository = object : NotificationRepository {
            override fun loadNotifications(): Flow<DataState<List<MifosNotification>>> =
                flowOf(DataState.Success(emptyList()))
            override fun getUnReadNotificationCount(): Flow<DataState<Int>> {
                if (isError) return flowOf(DataState.Error(Exception("Failed to fetch unread notifications count")))
                return flowOf(DataState.Success(5))
            }
            override suspend fun saveNotification(notification: MifosNotification) {}
            override suspend fun deleteOldNotifications() {}
            override suspend fun updateReadStatus(notification: MifosNotification, isRead: Boolean) {}
        }

        homeRepositoryImp = HomeRepositoryImp(dataManager, notificationRepository, testDispatcher)
    }

    @Test
    fun testClientAccounts_Successful() = runTest(testDispatcher) {
        val flow = homeRepositoryImp.clientAccounts(1L)
        val item = flow.drop(1).first()
        assertIs<DataState.Success<ClientAccounts>>(item)
    }

    @Test
    fun testCurrentClient_Successful() = runTest(testDispatcher) {
        val flow = homeRepositoryImp.currentClient(1L)
        val item = flow.drop(1).first()
        assertIs<DataState.Success<Client>>(item)
    }

    @Test
    fun testClientImage_Successful() = runTest(testDispatcher) {
        val flow = homeRepositoryImp.clientImage(1L)
        val item = flow.drop(1).first()
        val data = assertIs<DataState.Success<String>>(item)
        assertEquals("base64string", data.data)
    }

    @Test
    fun testUnreadNotificationsCount_Successful() = runTest(testDispatcher) {
        val flow = homeRepositoryImp.unreadNotificationsCount()
        val item = flow.first()
        val data = assertIs<DataState.Success<Int>>(item)
        assertEquals(5, data.data)
    }

    @Test
    fun testClientAccounts_Error() = runTest(testDispatcher) {
        isError = true
        val flow = homeRepositoryImp.clientAccounts(1L)
        val item = flow.drop(1).first()
        val error = assertIs<DataState.Error<ClientAccounts>>(item)
        assertEquals("Failed to fetch client accounts", error.exception.message)
    }

    @Test
    fun testCurrentClient_Error() = runTest(testDispatcher) {
        isError = true
        val flow = homeRepositoryImp.currentClient(1L)
        val item = flow.drop(1).first()
        val error = assertIs<DataState.Error<Client>>(item)
        assertEquals("Failed to fetch current client", error.exception.message)
    }

    @Test
    fun testClientImage_Error() = runTest(testDispatcher) {
        isError = true
        val flow = homeRepositoryImp.clientImage(1L)
        val item = flow.drop(1).first()
        val error = assertIs<DataState.Error<String>>(item)
        assertEquals("Failed to fetch client image", error.exception.message)
    }

    @Test
    fun testUnreadNotificationsCount_Error() = runTest(testDispatcher) {
        isError = true
        val flow = homeRepositoryImp.unreadNotificationsCount()
        val item = flow.first()
        val error = assertIs<DataState.Error<Int>>(item)
        assertEquals("Failed to fetch unread notifications count", error.exception.message)
    }
}
