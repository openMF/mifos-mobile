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
data class RepaymentSchedule(
    var currency: Currency? = null,

    var loanTermInDays: Int? = null,

    var totalPrincipalDisbursed: Double? = null,

    var totalPrincipalExpected: Double? = null,

    var totalPrincipalPaid: Double? = null,

    var totalInterestCharged: Double? = null,

    var totalFeeChargesCharged: Double? = null,

    var totalPenaltyChargesCharged: Double? = null,

    var totalWaived: Double? = null,

    var totalWrittenOff: Double? = null,

    var totalRepaymentExpected: Double? = null,

    var totalRepayment: Double? = null,

    var totalPaidInAdvance: Double? = null,

    var totalPaidLate: Double? = null,

    var totalOutstanding: Double? = null,

    var periods: List<Periods> = ArrayList(),

) : Parcelable
