/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.savings

import kotlinx.serialization.Serializable

@Serializable
data class SavingsTransactionType(
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
