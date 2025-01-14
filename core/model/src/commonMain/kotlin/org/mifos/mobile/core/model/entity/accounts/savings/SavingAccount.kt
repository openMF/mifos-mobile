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
import org.mifos.mobile.core.model.entity.client.DepositType

@Serializable
@Parcelize
data class SavingAccount(

    val id: Long = 0,

    val accountNo: String? = null,

    val productName: String? = null,

    val productId: Int? = null,

    val overdraftLimit: Long = 0,

    val minRequiredBalance: Long = 0,

    val accountBalance: Double = 0.0,

    val totalDeposits: Double = 0.0,

    val savingsProductName: String? = null,

    val clientName: String? = null,

    val savingsProductId: String? = null,

    val nominalAnnualInterestRate: Double = 0.0,

    val status: Status? = null,

    val currency: Currency? = null,

    val depositType: DepositType? = null,

    val lastActiveTransactionDate: List<Int>? = null,

    val timeLine: TimeLine? = null,
) : Parcelable {
    fun isRecurring(): Boolean {
        return this.depositType != null && this.depositType.isRecurring()
    }
}
