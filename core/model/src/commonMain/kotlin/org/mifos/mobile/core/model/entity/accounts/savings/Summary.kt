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
data class Summary(

    val currency: Currency? = null,

    val totalDeposits: Double? = null,

    val totalWithdrawals: Double? = null,

    val totalInterestEarned: Double? = null,

    val totalInterestPosted: Double? = null,

    val accountBalance: Double? = null,

    val totalOverdraftInterestDerived: Double? = null,

    val interestNotPosted: Double? = null,

    val lastInterestCalculationDate: List<Int> = emptyList(),
) : Parcelable
