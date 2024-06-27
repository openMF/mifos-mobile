package org.mifos.mobile.core.datastore

import com.raizlabs.android.dbflow.sql.language.SQLite
import io.reactivex.Observable
import org.mifos.mobile.core.datastore.utils.NotificationComparator
import org.mifos.mobile.core.datastore.model.Charge
import org.mifos.mobile.core.model.entity.Page
import org.mifos.mobile.core.datastore.model.MifosNotification
import org.mifos.mobile.core.datastore.model.MifosNotification_Table
import java.util.Collections
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Rajan Maurya on 02/03/17.
 */
@Singleton
class DatabaseHelper @Inject constructor() {
    fun syncCharges(charges: Page<Charge>?): Page<Charge>? {
        if (charges != null) {
            for (charge in charges.pageItems)
                charge.save()
        }
        return charges
    }

    fun clientCharges(): Page<Charge?> {
        val charges = SQLite.select()
            .from(Charge::class.java)
            .queryList()
        val chargePage = Page<Charge?>()
        chargePage.pageItems = charges
        return chargePage
    }

    fun notifications(): List<MifosNotification> {
        deleteOldNotifications()
        val notifications = SQLite.select()
            .from(MifosNotification::class.java)
            .queryList()
        Collections.sort(notifications, NotificationComparator())
        return notifications
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
