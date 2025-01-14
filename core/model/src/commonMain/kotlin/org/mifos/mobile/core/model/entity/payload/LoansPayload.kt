/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.payload

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class LoansPayload(

    val clientId: Int? = null,

    val productId: Int? = null,

    val productName: String? = null,

    val principal: Double? = null,

    val loanTermFrequency: Int? = null,

    val loanTermFrequencyType: Int? = null,

    val loanType: String? = null,

    val numberOfRepayments: Int? = null,

    val repaymentEvery: Int? = null,

    val repaymentFrequencyType: Int? = null,

    val interestRatePerPeriod: Double? = null,

    val amortizationType: Int? = null,

    val interestType: Int? = null,

    val interestCalculationPeriodType: Int? = null,

    val transactionProcessingStrategyId: Int? = null,

    val expectedDisbursementDate: String? = null,

    val submittedOnDate: String? = null,

    val linkAccountId: Int? = null,

    val loanPurposeId: Int? = null,

    val loanPurpose: String? = null,

    val maxOutstandingLoanBalance: Double? = null,

    val currency: String? = null,

    val dateFormat: String = "dd MMMM yyyy",

    val locale: String = "en",
) : Parcelable
