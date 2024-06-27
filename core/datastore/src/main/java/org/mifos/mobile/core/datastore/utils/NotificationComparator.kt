package org.mifos.mobile.core.datastore.utils

import org.mifos.mobile.core.datastore.model.MifosNotification

/**
 * Created by dilpreet on 14/9/17.
 */
class NotificationComparator : Comparator<MifosNotification> {
    override fun compare(
        firstNotification: MifosNotification,
        secondNotification: MifosNotification,
    ): Int {
        return when {
            // comparator function logic to sort notifications in the descending order of their timeStamp :
            firstNotification.timeStamp > secondNotification.timeStamp -> -1
            firstNotification.timeStamp < secondNotification.timeStamp -> 1
            else -> 0
        }
    }
}
