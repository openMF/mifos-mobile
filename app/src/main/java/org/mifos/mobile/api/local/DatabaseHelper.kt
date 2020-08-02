package org.mifos.mobile.api.local

import com.raizlabs.android.dbflow.sql.language.SQLite

import io.reactivex.Observable

import org.mifos.mobile.models.Charge
import org.mifos.mobile.models.Page
import org.mifos.mobile.models.notification.MifosNotification
import org.mifos.mobile.models.notification.MifosNotification_Table
import org.mifos.mobile.utils.NotificationComparator

import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Rajan Maurya on 02/03/17.
 */
@Singleton
class DatabaseHelper @Inject constructor() {
    fun syncCharges(charges: Page<Charge?>?): Observable<Page<Charge?>?>? {
        return Observable.defer {
            if (charges != null)
                for (charge in charges.pageItems)
                    charge?.save()
            Observable.just(charges)
        }
    }

    val clientCharges: Observable<Page<Charge?>?>?
        get() = Observable.defer {
            val charges = SQLite.select()
                    .from(Charge::class.java)
                    .queryList()
            val chargePage = Page<Charge?>()
            chargePage.pageItems = charges
            Observable.just(chargePage)
        }
    val notifications: Observable<List<MifosNotification?>?>?
        get() = Observable.defer {
            deleteOldNotifications()
            val notifications = SQLite.select()
                    .from(MifosNotification::class.java)
                    .queryList()
            Collections.sort(notifications, NotificationComparator())
            Observable.just(notifications)
        }
    val unreadNotificationsCount: Observable<Int>
        get() = Observable.defer {
            deleteOldNotifications()
            val count = SQLite.select()
                    .from(MifosNotification::class.java)
                    .where(MifosNotification_Table.read.eq(false))
                    .queryList().size
            Observable.just(count)
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