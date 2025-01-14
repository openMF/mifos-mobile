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
data class LoanAccount(
    val id: Long = 0,

    val loanProductId: Long = 0,

    val externalId: String? = null,

    val numberOfRepayments: Long = 0,

    val accountNo: String? = null,

    val productName: String? = null,

    val productId: Int? = null,

    val loanProductName: String? = null,

    val clientName: String? = null,

    val loanProductDescription: String? = null,

    val principal: Double = 0.0,

    val annualInterestRate: Double = 0.0,

    val status: Status? = null,

    val loanType: LoanType? = null,

    val loanCycle: Int? = null,

    val loanBalance: Double = 0.0,

    val amountPaid: Double = 0.0,

    val currency: Currency?,

    val inArrears: Boolean? = null,

    val summary: Summary? = null,

    val loanPurposeName: String? = null,

    val timeline: Timeline?,
) : Parcelable
