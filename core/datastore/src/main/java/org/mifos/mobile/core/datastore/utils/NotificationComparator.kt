/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
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
