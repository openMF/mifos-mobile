/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.templates.savings

import kotlinx.serialization.Serializable
import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize
import org.mifos.mobile.core.model.entity.Currency

@Serializable
@Parcelize
data class ChargeOptions(
    val id: Int? = null,
    val name: String? = null,
    val active: Boolean? = null,
    val penalty: Boolean? = null,
    val currency: Currency? = null,
    val amount: Float = 0f,
    val chargeTimeType: ChargeTimeType,
    val chargeAppliesTo: ChargeAppliesTo,
    val chargeCalculationType: ChargeCalculationType,
    val chargePaymentMode: ChargePaymentMode,
) : Parcelable
