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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Transactions(
    var id: Int? = null,

    var transactionType: TransactionType? = null,

    var accountId: Int? = null,

    var accountNo: String? = null,

    var date: List<Int> = ArrayList(),

    var currency: Currency? = null,

    var paymentDetailData: PaymentDetailData? = null,

    var amount: Double? = null,

    var runningBalance: Double? = null,

    var reversed: Boolean? = null,

    var submittedOnDate: List<Int>? = null,

    var interestedPostedAsOn: Boolean? = null,

) : Parcelable
