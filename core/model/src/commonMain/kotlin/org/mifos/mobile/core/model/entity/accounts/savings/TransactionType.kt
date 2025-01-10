/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.savings

import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Parcelize
data class TransactionType(
    val id: Int? = null,
    val code: String? = null,

    val value: String? = null,

    val deposit: Boolean? = null,

    val dividendPayout: Boolean? = null,

    val withdrawal: Boolean? = null,

    val interestPosting: Boolean? = null,

    val feeDeduction: Boolean? = null,

    val initiateTransfer: Boolean? = null,

    val approveTransfer: Boolean? = null,

    val withdrawTransfer: Boolean? = null,

    val rejectTransfer: Boolean? = null,

    val overdraftInterest: Boolean? = null,

    val writtenoff: Boolean? = null,

    val overdraftFee: Boolean? = null,

    val withholdTax: Boolean? = null,

    val escheat: Boolean? = null,

) : Parcelable
