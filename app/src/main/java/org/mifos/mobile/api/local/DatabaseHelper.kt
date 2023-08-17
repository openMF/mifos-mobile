package org.mifos.mobile.api.local

import com.raizlabs.android.dbflow.sql.language.SQLite
import io.reactivex.Observable
import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.models.notification.MifosNotification_Table
import org.mifos.mobile.utils.NotificationComparator
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Rajan Maurya on 02/03/17.
 */
@Singleton
class DatabaseHelper @Inject constructor() {
    fun syncCharges(charges: Page<Charge?>?): Response<Page<Charge?>?> {
        if (charges != null) {
            for (charge in charges.pageItems)
                charge?.save()
        }
        return Response.success(charges)
    }

    fun clientCharges(): Response<Page<Charge?>?> {
        val charges = SQLite.select()
            .from(Charge::class.java)
            .queryList()
        val chargePage = Page<Charge?>()
        chargePage.pageItems = charges
        return Response.success(chargePage)
    }

    val notifications: Observable<List<MifosNotification?>?>
        get() = Observable.defer {
            deleteOldNotifications()
            val notifications = SQLite.select()
                .from(MifosNotification::class.java)
                .queryList()
            Collections.sort(notifications, NotificationComparator())
            Observable.just(notifications)
        }
    fun unreadNotificationsCount(): Int {
        deleteOldNotifications()
        return SQLite.select()
            .from(MifosNotification::class.java)
            .where(MifosNotification_Table.read.eq(false))
            .queryList().size
    }

    private fun deleteOldNotifications() {
        Observable.defer<Void> {
            val thirtyDaysInSeconds: Long = 2592000
            val thirtyDaysFromCurrentTimeInSeconds = System.currentTimeMillis() / 1000 -
                    thirtyDaysInSeconds
            SQLite.delete(MifosNotification::class.java)
                .where(MifosNotification_Table.timeStamp.lessThan(thirtyDaysFromCurrentTimeInSeconds * 1000))
                .execute()
            null
        }
    }
}
