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
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.data.repositoryImpl.HomeRepositoryImp
import org.mifos.mobile.core.model.entity.client.Client
import org.mifos.mobile.core.model.entity.client.ClientAccounts
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.testing.util.MainDispatcherRule
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class HomeRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Mock
    lateinit var dataManager: DataManager

    @Mock
    lateinit var notificationRepository: NotificationRepository

    private lateinit var homeRepositoryImp: HomeRepositoryImp

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        homeRepositoryImp = HomeRepositoryImp(dataManager, notificationRepository)
    }

    @Test
    fun testClientAccounts_Successful() = runTest {
        val mockClientAccounts: ClientAccounts = mock(ClientAccounts::class.java)

        `when`(dataManager.clientAccounts()).thenReturn(mockClientAccounts)

        val flow = homeRepositoryImp.clientAccounts()

        flow.test {
            assertEquals(mockClientAccounts, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testCurrentClient_Successful() = runTest {
        val mockClient: Client = mock(Client::class.java)

        `when`(dataManager.currentClient()).thenReturn(mockClient)

        val flow = homeRepositoryImp.currentClient()

        flow.test {
            assertEquals(mockClient, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testClientImage_Successful() = runTest {
        val mockResponseBody: ResponseBody = mock(ResponseBody::class.java)

        `when`(dataManager.clientImage()).thenReturn(mockResponseBody)

        val flow = homeRepositoryImp.clientImage()

        flow.test {
            assertEquals(mockResponseBody, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testUnreadNotificationsCount_Successful() = runTest {
        val mockUnreadCount = 5

        `when`(notificationRepository.getUnReadNotificationCount()).thenReturn(flowOf(mockUnreadCount))

        val flow = homeRepositoryImp.unreadNotificationsCount()

        flow.test {
            assertEquals(mockUnreadCount, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testClientAccounts_Error() = runTest {
        val errorMessage = "Failed to fetch client accounts"
        `when`(dataManager.clientAccounts())
            .thenThrow(Exception(errorMessage))
        val flow = homeRepositoryImp.clientAccounts()

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testCurrentClient_Error() = runTest {
        val errorMessage = "Failed to fetch current client"
        `when`(dataManager.currentClient())
            .thenThrow(Exception(errorMessage))

        val flow = homeRepositoryImp.currentClient()

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testClientImage_Error() = runTest {
        val errorMessage = "Failed to fetch client image"
        `when`(dataManager.clientImage())
            .thenThrow(Exception(errorMessage))
        val flow = homeRepositoryImp.clientImage()

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testUnreadNotificationsCount_Error() = runTest {
        val errorMessage = "Failed to fetch unread notifications count"

        `when`(notificationRepository.getUnReadNotificationCount())
            .thenThrow(Exception(errorMessage))

        val flow = homeRepositoryImp.unreadNotificationsCount()

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
