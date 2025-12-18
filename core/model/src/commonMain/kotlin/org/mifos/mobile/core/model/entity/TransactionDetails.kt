/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.entity.client.Type

@Serializable
data class TransactionDetails(

    val id: Long? = null,

    val officeId: Long? = null,

    val officeName: String? = null,

    @SerialName("transactionType")
    val type: Type? = null,

    val date: List<Int> = emptyList(),

    val currency: Currency? = null,

    val amount: Double? = null,

    val submittedOnDate: List<Int> = emptyList(),

    val reversed: Boolean? = null,

    val accountNo: String? = null,

    val manuallyReversed: Boolean? = null,

    val externalId: String? = null,

    val outstandingLoanBalance: Double? = null,

    val runningBalance: Double? = null,

    val principalPortion: Double? = null,

    val interestPortion: Double? = null,

    val feeChargesPortion: Double? = null,

    val penaltyChargesPortion: Double? = null,
) {
    val isCredit: Boolean
        get() {
            val typeLower = this.type?.value?.lowercase().orEmpty()
            return when {
                typeLower.contains("withdrawal") -> false
                typeLower.contains("disbursement") -> false
                typeLower.contains("repayment") -> false
                typeLower.contains("fee") -> false
                typeLower.contains("charge") -> false
                typeLower.contains("penalty") -> false
                typeLower.contains("transfer") && !typeLower.contains("incoming") -> false
                else -> true
            }
        }
}
