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

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.network.dto.currency.CurrencyResponseDto
import org.mifos.mobile.core.network.dto.savingsAccount.SavingsTransactionTypeResponseDto

@Serializable
data class SavingsTransactionResponseDto(
    val id: Int? = null,

    val transactionType: SavingsTransactionTypeResponseDto? = null,

    val accountId: Int? = null,

    val accountNo: String? = null,

    val date: List<Int> = emptyList(),

    val currency: CurrencyResponseDto? = null,

    val paymentDetailData: PaymentDetailsResponseDto? = null,

    val amount: Double? = null,

    val runningBalance: Double? = null,

    val reversed: Boolean? = null,

    val submittedOnDate: List<Int>? = null,

    val interestedPostedAsOn: Boolean? = null,

)
