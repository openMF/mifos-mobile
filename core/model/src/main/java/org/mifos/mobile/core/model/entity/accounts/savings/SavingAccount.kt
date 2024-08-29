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
import org.mifos.mobile.core.model.entity.accounts.Account
import org.mifos.mobile.core.model.entity.client.DepositType

@Parcelize
data class SavingAccount(

    var accountNo: String? = null,

    var productName: String? = null,

    var productId: Int? = null,

    var overdraftLimit: Long = 0,

    var minRequiredBalance: Long = 0,

    var accountBalance: Double = 0.toDouble(),

    var totalDeposits: Double = 0.toDouble(),

    var savingsProductName: String? = null,

    var clientName: String? = null,

    var savingsProductId: String? = null,

    var nominalAnnualInterestRate: Double = 0.toDouble(),

    var status: Status? = null,

    var currency: Currency? = null,

    var depositType: DepositType? = null,

    var lastActiveTransactionDate: List<Int>? = null,

    var timeLine: TimeLine? = null,
) : Parcelable, Account() {
    fun isRecurring(): Boolean {
        return this.depositType != null && (this.depositType?.isRecurring() == true)
    }
}
