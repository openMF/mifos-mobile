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

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class Summary(
    val principalDisbursed: Double = 0.0,

    val principalPaid: Double = 0.0,

    val interestCharged: Double = 0.0,

    val interestPaid: Double = 0.0,

    val feeChargesCharged: Double = 0.0,

    val penaltyChargesCharged: Double = 0.0,

    val penaltyChargesWaived: Double = 0.0,

    val totalExpectedRepayment: Double = 0.0,

    val interestWaived: Double = 0.0,

    val totalRepayment: Double = 0.0,

    val feeChargesWaived: Double = 0.0,

    val totalOutstanding: Double = 0.0,

    private val overdueSinceDate: List<Int>? = null,

    val currency: Currency? = null,
) : Parcelable {

    fun getOverdueSinceDate(): List<Int>? {
        return overdueSinceDate
    }
}
