package org.mifos.mobile.repositories

import io.reactivex.Observable
import org.mifos.mobile.models.notification.MifosNotification

interface NotificationRepository {

    fun loadNotifications(): Observable<List<MifosNotification?>?>?
}