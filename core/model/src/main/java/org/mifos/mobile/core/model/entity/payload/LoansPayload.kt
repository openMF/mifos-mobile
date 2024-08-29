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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by nellyk on 2/20/2016.
 */

@Parcelize
data class LoansPayload(

    var clientId: Int? = null,

    var productId: Int? = null,

    var productName: String? = null,

    var principal: Double? = null,

    var loanTermFrequency: Int? = null,

    var loanTermFrequencyType: Int? = null,

    var loanType: String? = null,

    var numberOfRepayments: Int? = null,

    var repaymentEvery: Int? = null,

    var repaymentFrequencyType: Int? = null,

    var interestRatePerPeriod: Double? = null,

    var amortizationType: Int? = null,

    var interestType: Int? = null,

    var interestCalculationPeriodType: Int? = null,

    var transactionProcessingStrategyId: Int? = null,

    var expectedDisbursementDate: String? = null,

    var submittedOnDate: String? = null,

    var linkAccountId: Int? = null,

    var loanPurposeId: Int? = null,

    var loanPurpose: String? = null,

    var maxOutstandingLoanBalance: Double? = null,

    var currency: String? = null,

    var dateFormat: String = "dd MMMM yyyy",

    var locale: String = "en",
) : Parcelable
