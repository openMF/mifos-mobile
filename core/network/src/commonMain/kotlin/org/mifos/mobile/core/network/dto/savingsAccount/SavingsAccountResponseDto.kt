/*
 * Copyright 2026 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.network.dto.savingsAccount

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto

@Serializable
data class SavingsAccountResponseDto(

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

    val status: SavingsStatusResponseDto? = null,

    val currency: CurrencyResponseDto? = null,

    val depositType: SavingsDepositTypeResponseDto? = null,

    val lastActiveTransactionDate: List<Int>? = null,

    val timeLine: SavingsTimeLineResponseDto? = null,
)
