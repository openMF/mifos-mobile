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
data class Periods(
    val period: Int? = null,

    val fromDate: List<Int> = emptyList(),

    val dueDate: List<Int> = emptyList(),

    val obligationsMetOnDate: List<Int> = emptyList(),

    val principalDisbursed: Double? = null,

    val complete: Boolean? = null,

    val daysInPeriod: Int? = null,

    val principalOriginalDue: Double? = null,

    val principalDue: Double? = null,

    val principalPaid: Double? = null,

    val principalWrittenOff: Double? = null,

    val principalOutstanding: Double? = null,

    val principalLoanBalanceOutstanding: Double? = null,

    val interestOriginalDue: Double? = null,

    val interestDue: Double? = null,

    val interestPaid: Double? = null,

    val interestWaived: Double? = null,

    val interestWrittenOff: Double? = null,

    val interestOutstanding: Double? = null,

    val feeChargesDue: Double? = null,

    val feeChargesPaid: Double? = null,

    val feeChargesWaived: Double? = null,

    val feeChargesWrittenOff: Double? = null,

    val feeChargesOutstanding: Double? = null,

    val penaltyChargesDue: Double? = null,

    val penaltyChargesPaid: Double? = null,

    val penaltyChargesWaived: Double? = null,

    val penaltyChargesWrittenOff: Double? = null,

    val penaltyChargesOutstanding: Double? = null,

    val totalOriginalDueForPeriod: Double? = null,

    val totalDueForPeriod: Double? = null,

    val totalPaidForPeriod: Double? = null,

    val totalPaidInAdvanceForPeriod: Double? = null,

    val totalPaidLateForPeriod: Double? = null,

    val totalWaivedForPeriod: Double? = null,

    val totalWrittenOffForPeriod: Double? = null,

    val totalOutstandingForPeriod: Double? = null,

    val totalOverdue: Double? = null,

    val totalActualCostOfLoanForPeriod: Double? = null,

    val totalInstallmentAmountForPeriod: Double? = null,

) : Parcelable
