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
data class SavingsWithAssociations(

    val id: Long? = null,

    val accountNo: String? = null,

    val depositType: DepositType? = null,

    val externalId: String? = null,

    val clientId: Int? = null,

    val clientName: String? = null,

    val savingsProductId: Int? = null,

    val savingsProductName: String? = null,

    val fieldOfficerId: Int? = null,

    val status: Status? = null,

    val timeline: TimeLine? = null,

    val currency: Currency? = null,

    internal val nominalAnnualInterestRate: Double? = null,

    val minRequiredOpeningBalance: Double? = null,

    val lockinPeriodFrequency: Double? = null,

    val withdrawalFeeForTransfers: Boolean? = null,

    val allowOverdraft: Boolean? = null,

    val enforceMinRequiredBalance: Boolean? = null,

    val withHoldTax: Boolean? = null,

    val lastActiveTransactionDate: List<Int>? = null,

    val dormancyTrackingActive: Boolean? = null,

    val summary: Summary? = null,

    val transactions: List<Transactions> = emptyList(),

) : Parcelable {

    fun isRecurring(): Boolean {
        return this.depositType != null && this.depositType.isRecurring()
    }

    fun getNominalAnnualInterestRate(): Double {
        return nominalAnnualInterestRate!!
    }

    fun setNominalAnnualInterestRate(nominalAnnualInterestRate: Double?): SavingsWithAssociations {
        return this.copy(
            nominalAnnualInterestRate = nominalAnnualInterestRate,
        )
    }
}
