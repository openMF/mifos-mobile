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
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mifos.mobile.core.data.repository.UserDetailRepository
import org.mifos.mobile.core.data.repositoryImpl.UserDetailRepositoryImp
import org.mifos.mobile.core.model.entity.notification.NotificationRegisterPayload
import org.mifos.mobile.core.model.entity.notification.NotificationUserDetail
import org.mifos.mobile.core.network.DataManager
import org.mifos.mobile.core.testing.util.MainDispatcherRule
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class UserDetailRepositoryImpTest {

    @get:Rule
    val coroutineTestRule = MainDispatcherRule()

    @Mock
    private lateinit var mockDataManager: DataManager

    private lateinit var userDetailRepository: UserDetailRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        userDetailRepository = UserDetailRepositoryImp(mockDataManager)
    }

    @Test
    fun testRegisterNotification_Success() = runTest {
        val mockResponseBody: ResponseBody = mock(ResponseBody::class.java)
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        `when`(mockDataManager.registerNotification(mockPayload)).thenReturn(mockResponseBody)

        val flow = userDetailRepository.registerNotification(mockPayload)

        flow.test {
            assertEquals(mockResponseBody, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testRegisterNotification_Error() = runTest {
        val errorMessage = "Failed to register notification"
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        `when`(mockDataManager.registerNotification(mockPayload)).thenThrow(
            Exception(
                errorMessage,
            ),
        )

        val flow = userDetailRepository.registerNotification(mockPayload)

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testGetUserNotificationId_Success() = runTest {
        val mockNotificationUserDetail: NotificationUserDetail =
            mock(NotificationUserDetail::class.java)
        val mockId = 123L
        `when`(mockDataManager.getUserNotificationId(mockId)).thenReturn(mockNotificationUserDetail)

        val flow = userDetailRepository.getUserNotificationId(mockId)

        flow.test {
            assertEquals(mockNotificationUserDetail, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testGetUserNotificationId_Error() = runTest {
        val errorMessage = "Failed to get user notification"
        val mockId = 123L
        `when`(mockDataManager.getUserNotificationId(mockId)).thenThrow(
            Exception(
                errorMessage,
            ),
        )

        val flow = userDetailRepository.getUserNotificationId(mockId)

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun testUpdateRegisterNotification_Success() = runTest {
        val mockResponseBody: ResponseBody = mock(ResponseBody::class.java)
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        val mockId = 123L
        `when`(mockDataManager.updateRegisterNotification(mockId, mockPayload)).thenReturn(
            mockResponseBody,
        )

        val flow = userDetailRepository.updateRegisterNotification(mockId, mockPayload)

        flow.test {
            assertEquals(mockResponseBody, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test(expected = Exception::class)
    fun testUpdateRegisterNotificationError() = runTest {
        val errorMessage = "Failed to update register notification"
        val mockPayload: NotificationRegisterPayload = mock(NotificationRegisterPayload::class.java)
        val mockId = 123L
        `when`(mockDataManager.updateRegisterNotification(mockId, mockPayload)).thenThrow(
            Exception(errorMessage),
        )

        val flow = userDetailRepository.updateRegisterNotification(mockId, mockPayload)

        flow.test {
            assertEquals(Throwable(errorMessage), awaitError())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
