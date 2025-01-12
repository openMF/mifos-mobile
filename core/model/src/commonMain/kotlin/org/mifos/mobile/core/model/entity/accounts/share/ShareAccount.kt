/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.share

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.accounts.savings.Currency

@Serializable
@Parcelize
data class ShareAccount(

    val id: Long = 0,

    val accountNo: String? = null,

    val totalApprovedShares: Int? = null,

    val totalPendingForApprovalShares: Int? = null,

    val productId: Int? = null,

    val productName: String? = null,

    val shortProductName: String? = null,

    val status: Status? = null,

    val currency: Currency? = null,

    val timeline: Timeline? = null,

) : Parcelable
