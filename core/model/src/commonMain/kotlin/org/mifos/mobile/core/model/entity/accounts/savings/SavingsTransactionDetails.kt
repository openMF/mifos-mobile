/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.savings

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SavingsTransactionDetails(
    val id: Long? = null,
    val accountNo: String? = null,
    val amount: Double? = null,
    val date: List<Int>? = null,
    val reversed: Boolean? = null,
    val runningBalance: Double? = null,
    val currency: Currency? = null,
    @SerialName("transactionType")
    val savingsType: SavingsTransactionType? = null,
)
