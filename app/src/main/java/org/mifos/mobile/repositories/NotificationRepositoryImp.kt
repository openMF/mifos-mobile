package org.mifos.mobile.repositories


import io.reactivex.Observable
import org.mifos.mobile.api.DataManager
import org.mifos.mobile.models.notification.MifosNotification
import javax.inject.Inject

class NotificationRepositoryImp @Inject constructor(private val dataManager: DataManager) :
    NotificationRepository {
    override fun loadNotifications(): Observable<List<MifosNotification?>?> {
        return dataManager.notifications
    }
}