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
data class LoanTransactionDetailsResponseDto(
    val id: Long? = null,
    val amount: Double? = null,
    val date: List<Int>? = null,
    val manuallyReversed: Boolean? = null,
    val principalPortion: Double? = null,
    val outstandingLoanBalance: Double? = null,
    val interestPortion: Double? = null,
    val feeChargesPortion: Double? = null,
    val penaltyChargesPortion: Double? = null,
    val currency: CurrencyResponseDto? = null,
    @SerialName("transfer")
    val transfer: TransferDetailDto? = null,
    @SerialName("type")
    val loanType: LoanTransactionTypeResponseDto? = null,
)

@Serializable
data class LoanTransactionTypeResponseDto(
    val value: String? = null,
    val code: String? = null,
    val disbursement: Boolean = false,
    val repayment: Boolean = false,
    val waiveCharges: Boolean = false,
)
