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
data class Summary(

    var currency: Currency? = null,

    var totalDeposits: Double? = null,

    var totalWithdrawals: Double? = null,

    var totalInterestEarned: Double? = null,

    var totalInterestPosted: Double? = null,

    var accountBalance: Double? = null,

    var totalOverdraftInterestDerived: Double? = null,

    var interestNotPosted: Double? = null,

    var lastInterestCalculationDate: List<Int> = ArrayList(),

) : Parcelable
