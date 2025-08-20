/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.payload

import kotlinx.serialization.Serializable

@Serializable
data class ShareApplicationPayload(
    val productId: Int,
    val unitPrice: Double,
    val requestedShares: Int,
    val submittedDate: String,
    val savingsAccountId: Int,
    val applicationDate: String,
    val locale: String = "en",
    val dateFormat: String = "dd MMMM yyyy",
    val clientId: Long,
)
