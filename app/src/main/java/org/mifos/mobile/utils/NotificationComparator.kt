package org.mifos.mobile.utils

import org.mifos.mobile.models.notification.MifosNotification
import java.util.*

/**
 * Created by dilpreet on 14/9/17.
 */
class NotificationComparator : Comparator<MifosNotification> {
    override fun compare(mifosNotification1: MifosNotification, mifosNotification2: MifosNotification): Int {
        return when {
            mifosNotification2.timeStamp < mifosNotification1.timeStamp -> -1
            mifosNotification1.timeStamp > mifosNotification2.timeStamp -> 1
            else -> 0
        }
    }
}