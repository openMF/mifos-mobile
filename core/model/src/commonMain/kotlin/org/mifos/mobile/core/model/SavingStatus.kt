/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model

import kotlinx.serialization.Serializable

@Serializable
enum class SavingStatus(
    val status: String,
    val sortOrder: Int,
) {
    ACTIVE("Active", 0),
    APPROVED("Approved", 1),
    SUBMIT_AND_PENDING_APPROVAL("Submitted and pending approval", 2),
    INACTIVE("Inactive", 3),
    CLOSED("Closed", 4),
    UNKNOWN("Unknown", Int.MAX_VALUE),
    ;

    companion object {
        fun fromStatus(status: String): SavingStatus {
            return entries.find { it.status.equals(status, ignoreCase = true) }
                ?: UNKNOWN
        }
    }
}
