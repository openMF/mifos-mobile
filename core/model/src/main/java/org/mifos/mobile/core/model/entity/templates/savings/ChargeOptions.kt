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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.mifos.mobile.core.model.entity.Currency

@Parcelize
data class ChargeOptions(
    var id: Int? = null,
    var name: String? = null,
    var active: Boolean? = null,
    var penalty: Boolean? = null,
    var currency: Currency? = null,
    var amount: Float = 0.toFloat(),
    var chargeTimeType: ChargeTimeType,
    var chargeAppliesTo: ChargeAppliesTo,
    var chargeCalculationType: ChargeCalculationType,
    var chargePaymentMode: ChargePaymentMode,
) : Parcelable
