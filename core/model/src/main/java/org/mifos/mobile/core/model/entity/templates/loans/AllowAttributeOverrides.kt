/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.loans

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by Rajan Maurya on 16/07/16.
 */

@Parcelize
data class AllowAttributeOverrides(

    var amortizationType: Boolean? = null,

    var interestType: Boolean? = null,

    var transactionProcessingStrategyId: Boolean? = null,

    var interestCalculationPeriodType: Boolean? = null,

    var inArrearsTolerance: Boolean? = null,

    var repaymentEvery: Boolean? = null,

    var graceOnPrincipalAndInterestPayment: Boolean? = null,

    var graceOnArrearsAgeing: Boolean? = null,

) : Parcelable
