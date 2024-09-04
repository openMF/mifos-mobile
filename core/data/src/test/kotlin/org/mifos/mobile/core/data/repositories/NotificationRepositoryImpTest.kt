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
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.data.repositoryImpl.NotificationRepositoryImp
import org.mifos.mobile.core.datastore.model.MifosNotification
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.testing.util.MainDispatcherRule
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
@ExperimentalCoroutinesApi
class NotificationRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Mock
    lateinit var dataManager: DataManager

    private lateinit var notificationRepositoryImp: NotificationRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        notificationRepositoryImp = NotificationRepositoryImp(dataManager)
    }

    @Test
    fun testLoadNotifications_SuccessResponseReceivedFromDataManager_ReturnsSuccess() = runTest {
        val notification = mock(MifosNotification::class.java)
        val notificationList = List(5) { notification }
        `when`(
            dataManager.notifications(),
        ).thenReturn(notificationList)

        val notifications = notificationRepositoryImp.loadNotifications()

        notifications.test {
            assertEquals(notificationList, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testLoadNotifications_ErrorResponseReceivedFromDataManager_ReturnsError() = runTest {
        val dummyError = Exception("Dummy error")
        `when`(dataManager.notifications()).thenThrow(dummyError)

        val notifications = notificationRepositoryImp.loadNotifications()

        notifications.test {
            assert(Throwable("Dummy error") == awaitItem())
        }
    }
}
