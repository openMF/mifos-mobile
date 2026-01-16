/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.transaction

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class SavingsTransactionDetailsResponseDto(
    val id: Long? = null,
    val accountNo: String? = null,
    val amount: Double? = null,
    val date: List<Int>? = null,
    val reversed: Boolean? = null,
    val runningBalance: Double? = null,
    val currency: CurrencyResponseDto? = null,
    @SerialName("transactionType")
    val savingsType: SavingsTransactionTypeResponseDto? = null,
)

@Serializable
data class SavingsTransactionTypeResponseDto(
    val value: String? = null,
    val code: String? = null,
    val deposit: Boolean = false,
    val withdrawal: Boolean = false,
    val feeDeduction: Boolean = false,
    val initiateTransfer: Boolean = false,
    val approveTransfer: Boolean = false,
    val withdrawTransfer: Boolean = false,
    val rejectTransfer: Boolean = false,
)
