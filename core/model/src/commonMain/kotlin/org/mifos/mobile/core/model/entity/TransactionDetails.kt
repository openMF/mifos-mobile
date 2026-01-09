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

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDetails(

    val id: Long,
    val transactionName: String,
    val isCredit: Boolean,
    val amount: Double,
    val currencyCode: String,
    val date: List<Int>,
    val accountNo: String,
    val isReversed: Boolean,
    val balances: TransactionBalances,
)

@Serializable
data class TransactionBalances(
    val running: Double? = null,
    val principal: Double? = null,
    val interest: Double? = null,
    val fee: Double? = null,
    val penalty: Double? = null,
)
