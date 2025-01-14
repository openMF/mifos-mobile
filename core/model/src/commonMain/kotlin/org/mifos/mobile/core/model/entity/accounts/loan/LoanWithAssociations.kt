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

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.Transaction

@Serializable
@Parcelize
data class LoanWithAssociations(
    val id: Int? = null,

    val accountNo: String? = null,

    val externalId: String? = null,

    val status: Status? = null,

    val clientId: Int? = null,

    val clientAccountNo: String? = null,

    val clientName: String? = null,

    val clientOfficeId: Int? = null,

    val loanProductId: Int? = null,

    val loanProductName: String? = null,

    @SerialName("isLoanProductLinkedToFloatingRate")
    val loanProductLinkedToFloatingRate: Boolean? = null,

    val loanType: LoanType? = null,

    val currency: Currency? = null,

    val principal: Double? = null,

    val approvedPrincipal: Double? = null,

    val proposedPrincipal: Double? = null,

    val termFrequency: Int? = null,

    val termPeriodFrequencyType: TermPeriodFrequencyType? = null,

    val numberOfRepayments: Int? = null,

    val repaymentEvery: Int? = null,

    val repaymentFrequencyType: RepaymentFrequencyType? = null,

    val interestRatePerPeriod: Int? = null,

    val interestRateFrequencyType: InterestRateFrequencyType? = null,

    @SerialName("isFloatingInterestRate")
    val floatingInterestRate: Boolean? = null,

    val amortizationType: AmortizationType? = null,

    val interestType: InterestType? = null,

    val interestCalculationPeriodType: InterestCalculationPeriodType? = null,

    val allowPartialPeriodInterestCalculation: Boolean? = null,

    val transactionProcessingStrategyId: Int? = null,

    val transactionProcessingStrategyName: String? = null,

    val syncDisbursementWithMeeting: Boolean? = null,

    val timeline: Timeline? = null,

    val summary: Summary? = null,

    val repaymentSchedule: RepaymentSchedule? = null,

    val feeChargesAtDisbursementCharged: Double? = null,

    val loanProductCounter: Int? = null,

    val multiDisburseLoan: Boolean? = null,

    val canDefineInstallmentAmount: Boolean? = null,

    val canDisburse: Boolean? = null,

    val canUseForTopup: Boolean? = null,

    @SerialName("isTopup")
    val topup: Boolean? = null,

    val closureLoanId: Int? = null,

    val inArrears: Boolean? = null,

    @SerialName("isNPA")
    val npa: Boolean? = null,

    val daysInMonthType: DaysInMonthType? = null,

    val daysInYearType: DaysInYearType? = null,

    @SerialName("isInterestRecalculationEnabled")
    val interestRecalculationEnabled: Boolean? = null,

    val interestRecalculationData: InterestRecalculationData? = null,

    val createStandingInstructionAtDisbursement: Boolean? = null,

    @SerialName("isvaliableInstallmentsAllowed")
    val valiableInstallmentsAllowed: Boolean? = null,

    val minimumGap: Int? = null,

    val maximumGap: Int? = null,

    val transactions: MutableList<Transaction?>? = arrayListOf(),

    val loanPurposeName: String? = null,
) : Parcelable
