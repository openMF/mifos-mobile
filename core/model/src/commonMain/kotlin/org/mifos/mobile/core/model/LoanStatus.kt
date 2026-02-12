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

enum class LoanStatus(
    val status: String,
    val sortOrder: Int,
) {
    ACTIVE("Active", 0),

    DISBURSED("Disbursed", 1),

    APPROVED("Approved", 2),

    SUBMIT_AND_PENDING_APPROVAL("Submitted and pending approval", 3),

    OVERPAID("Overpaid", 4),

    MATURED("Matured", 5),

    CLOSED("Closed", 6),

    CLOSED_OBLIGATIONS_MET("Closed (obligations met)", 7),

    REJECTED("Rejected", 8),

    WITHDRAWN("Withdrawn by applicant", 9),

    UNKNOWN("Unknown", Int.MAX_VALUE),
    ;

    companion object {
        fun fromStatus(status: String?): LoanStatus {
            return entries.firstOrNull {
                it.status.equals(status, ignoreCase = true)
            } ?: UNKNOWN
        }
    }
}
