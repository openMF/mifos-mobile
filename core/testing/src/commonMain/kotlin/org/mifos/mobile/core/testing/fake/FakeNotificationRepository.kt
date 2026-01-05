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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.mifos.mobile.core.common.DataState
import org.mifos.mobile.core.data.repository.NotificationRepository
import org.mifos.mobile.core.model.entity.MifosNotification

/**
 * Fake implementation of [NotificationRepository] for testing.
 *
 * Usage:
 * ```kotlin
 * val fakeRepo = FakeNotificationRepository()
 *
 * // Set notifications
 * fakeRepo.setNotifications(DataState.Success(testNotifications))
 *
 * // Set unread count
 * fakeRepo.setUnreadCount(5)
 *
 * // Use in tests
 * val viewModel = NotificationViewModel(fakeRepo)
 * ```
 */
class FakeNotificationRepository : NotificationRepository {

    private val notificationsState = MutableStateFlow<DataState<List<MifosNotification>>>(
        DataState.Success(emptyList()),
    )
    private val unreadCountState = MutableStateFlow<DataState<Int>>(
        DataState.Success(0),
    )

    private val savedNotifications = mutableListOf<MifosNotification>()

    // Track method calls for verification
    var saveCallCount = 0
        private set
    var deleteOldCallCount = 0
        private set
    var updateReadStatusCallCount = 0
        private set

    fun setNotifications(result: DataState<List<MifosNotification>>) {
        notificationsState.value = result
    }

    fun emitNotificationsLoading() {
        notificationsState.value = DataState.Loading
    }

    fun emitNotificationsSuccess(notifications: List<MifosNotification>) {
        notificationsState.value = DataState.Success(notifications)
    }

    fun emitNotificationsError(error: Throwable) {
        notificationsState.value = DataState.Error(error)
    }

    fun setUnreadCount(count: Int) {
        unreadCountState.value = DataState.Success(count)
    }

    fun getSavedNotifications(): List<MifosNotification> = savedNotifications.toList()

    fun reset() {
        notificationsState.value = DataState.Success(emptyList())
        unreadCountState.value = DataState.Success(0)
        savedNotifications.clear()
        saveCallCount = 0
        deleteOldCallCount = 0
        updateReadStatusCallCount = 0
    }

    override fun loadNotifications(): Flow<DataState<List<MifosNotification>>> {
        return notificationsState.asStateFlow()
    }

    override fun getUnReadNotificationCount(): Flow<DataState<Int>> {
        return unreadCountState.asStateFlow()
    }

    override suspend fun saveNotification(notification: MifosNotification) {
        saveCallCount++
        savedNotifications.add(notification)
    }

    override suspend fun deleteOldNotifications() {
        deleteOldCallCount++
    }

    override suspend fun updateReadStatus(notification: MifosNotification, isRead: Boolean) {
        updateReadStatusCallCount++
    }
}
