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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.data.repositoryImpl.NotificationRepositoryImp
import org.mifos.mobile.core.model.entity.MifosNotification
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@ExperimentalCoroutinesApi
class NotificationRepositoryImpTest {

    private lateinit var notificationRepositoryImp: NotificationRepository
    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        notificationRepositoryImp = NotificationRepositoryImp(
            testDispatcher,
        )
    }

    @Test
    fun testLoadNotifications_SuccessResponseReceivedFromDataManager_ReturnsSuccess() = runTest(testDispatcher) {
        val notifications = notificationRepositoryImp.loadNotifications()
        val item = notifications.drop(1).first()
        val data = assertIs<DataState.Success<List<MifosNotification>>>(item)
        assertEquals(emptyList(), data.data)
    }

    @Test
    fun testUnreadNotificationsCount_Successful() = runTest(testDispatcher) {
        val flow = notificationRepositoryImp.getUnReadNotificationCount()
        val item = flow.drop(1).first()
        val data = assertIs<DataState.Success<Int>>(item)
        assertEquals(0, data.data)
    }
}
