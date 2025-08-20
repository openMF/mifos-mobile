/*
 * Copyright 2025 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.shareProductDetails

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Parcelize
@Serializable
data class Charge(
    val id: Int? = null,
    val name: String? = null,
    val active: Boolean? = null,
    val penalty: Boolean? = null,
    val currency: Currency? = null,
    val amount: Double? = null,
    val chargeTimeType: EnumOption? = null,
    val chargeAppliesTo: EnumOption? = null,
    val chargeCalculationType: EnumOption? = null,
    val chargePaymentMode: EnumOption? = null,
) : Parcelable
