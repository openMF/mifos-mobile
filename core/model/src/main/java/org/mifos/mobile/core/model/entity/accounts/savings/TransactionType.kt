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
data class TransactionType(
    var id: Int? = null,
    var code: String? = null,

    var value: String? = null,

    var deposit: Boolean? = null,

    var dividendPayout: Boolean? = null,

    var withdrawal: Boolean? = null,

    var interestPosting: Boolean? = null,

    var feeDeduction: Boolean? = null,

    var initiateTransfer: Boolean? = null,

    var approveTransfer: Boolean? = null,

    var withdrawTransfer: Boolean? = null,

    var rejectTransfer: Boolean? = null,

    var overdraftInterest: Boolean? = null,

    var writtenoff: Boolean? = null,

    var overdraftFee: Boolean? = null,

    var withholdTax: Boolean? = null,

    var escheat: Boolean? = null,

) : Parcelable
