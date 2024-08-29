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
import org.mifos.mobile.core.model.entity.client.DepositType

@Parcelize
data class SavingsWithAssociations(

    var id: Long? = null,

    var accountNo: String? = null,

    var depositType: DepositType? = null,

    var externalId: String? = null,

    var clientId: Int? = null,

    var clientName: String? = null,

    var savingsProductId: Int? = null,

    var savingsProductName: String? = null,

    var fieldOfficerId: Int? = null,

    var status: Status? = null,

    var timeline: TimeLine? = null,

    var currency: Currency? = null,

    internal var nominalAnnualInterestRate: Double? = null,

    var minRequiredOpeningBalance: Double? = null,

    var lockinPeriodFrequency: Double? = null,

    var withdrawalFeeForTransfers: Boolean? = null,

    var allowOverdraft: Boolean? = null,

    var enforceMinRequiredBalance: Boolean? = null,

    var withHoldTax: Boolean? = null,

    var lastActiveTransactionDate: List<Int>? = null,

    var dormancyTrackingActive: Boolean? = null,

    var summary: Summary? = null,

    var transactions: List<Transactions> = ArrayList(),

) : Parcelable {

    fun isRecurring(): Boolean {
        return this.depositType != null && (this.depositType?.isRecurring() == true)
    }

    fun setNominalAnnualInterestRate(nominalAnnualInterestRate: Double?) {
        this.nominalAnnualInterestRate = nominalAnnualInterestRate
    }

    fun getNominalAnnualInterestRate(): Double {
        return nominalAnnualInterestRate!!
    }

    fun setNominalAnnualInterestRate(nominalAnnualInterestRate: Double) {
        this.nominalAnnualInterestRate = nominalAnnualInterestRate
    }
}
