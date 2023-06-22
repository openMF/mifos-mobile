package org.mifos.mobile

import io.reactivex.Observable
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.MifosNotification

class NotificationRepository(private val dataManager: DataManager?) {

    fun loadNotifications() : Observable<List<MifosNotification?>?>? {
        return dataManager?.notifications
    }
}