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

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Serializable
@Parcelize
data class Transactions(
    val id: Int? = null,

    val transactionType: TransactionType? = null,

    val accountId: Int? = null,

    val accountNo: String? = null,

    val date: List<Int> = emptyList(),

    val currency: Currency? = null,

    val paymentDetailData: PaymentDetailData? = null,

    val amount: Double? = null,

    val runningBalance: Double? = null,

    val reversed: Boolean? = null,

    val submittedOnDate: List<Int>? = null,

    val interestedPostedAsOn: Boolean? = null,

) : Parcelable
