/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.loan

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Summary(
    var principalDisbursed: Double = 0.toDouble(),

    var principalPaid: Double = 0.toDouble(),

    var interestCharged: Double = 0.toDouble(),

    var interestPaid: Double = 0.toDouble(),

    var feeChargesCharged: Double = 0.toDouble(),

    var penaltyChargesCharged: Double = 0.toDouble(),

    var penaltyChargesWaived: Double = 0.toDouble(),

    var totalExpectedRepayment: Double = 0.toDouble(),

    var interestWaived: Double = 0.toDouble(),

    var totalRepayment: Double = 0.toDouble(),

    var feeChargesWaived: Double = 0.toDouble(),

    var totalOutstanding: Double = 0.toDouble(),

    private var overdueSinceDate: List<Int>? = null,

    var currency: Currency? = null,
) : Parcelable {

    fun getOverdueSinceDate(): List<Int>? {
        return if (overdueSinceDate == null) {
            null
        } else {
            overdueSinceDate
        }
    }
}
