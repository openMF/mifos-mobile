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
data class RepaymentSchedule(
    val currency: Currency? = null,

    val loanTermInDays: Int? = null,

    val totalPrincipalDisbursed: Double? = null,

    val totalPrincipalExpected: Double? = null,

    val totalPrincipalPaid: Double? = null,

    val totalInterestCharged: Double? = null,

    val totalFeeChargesCharged: Double? = null,

    val totalPenaltyChargesCharged: Double? = null,

    val totalWaived: Double? = null,

    val totalWrittenOff: Double? = null,

    val totalRepaymentExpected: Double? = null,

    val totalRepayment: Double? = null,

    val totalPaidInAdvance: Double? = null,

    val totalPaidLate: Double? = null,

    val totalOutstanding: Double? = null,

    val periods: List<Periods> = emptyList(),
) : Parcelable
