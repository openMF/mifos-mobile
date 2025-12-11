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

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.savings.PaymentDetailData
import org.mifos.mobile.core.model.entity.client.Type

@Serializable
@Parcelize
data class TransactionDetails(

    val id: Long? = null,

    val officeId: Long? = null,

    val officeName: String? = null,

    @OptIn(ExperimentalSerializationApi::class)
    @JsonNames("transactionType")
    val type: Type,

    val date: List<Int> = emptyList(),

    val currency: Currency? = null,

    val amount: Double? = null,

    val submittedOnDate: List<Int> = emptyList(),

    val reversed: Boolean? = null,

    val accountNo: String? = null,

    val paymentDetailData: PaymentDetailData? = null,

    val manuallyReversed: Boolean? = null,

    val externalId: String? = null,

    val outstandingLoanBalance: Double? = null,

    val runningBalance: Double? = null,

    val principalPortion: Double? = null,

    val interestPortion: Double? = null,

    val feeChargesPortion: Double? = null,

    val penaltyChargesPortion: Double? = null,
) : Parcelable
