/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.data.mapper

import org.mifos.mobile.core.model.entity.MifosNotification

// fun MifosNotification.toEntity(): MifosNotificationEntity {
//    return MifosNotificationEntity(
//        timeStamp = timeStamp,
//        msg = msg,
//        read = read,
//    )
// }

fun MifosNotification.toModel(): MifosNotification {
    return MifosNotification(
        timeStamp = timeStamp,
        msg = msg,
        read = read,
    )
}
