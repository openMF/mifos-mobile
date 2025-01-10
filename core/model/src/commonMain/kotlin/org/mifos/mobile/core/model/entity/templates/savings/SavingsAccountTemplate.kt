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

/*
 * Created by saksham on 01/July/2018
 */

import org.mifos.mobile.core.model.Parcelable
import org.mifos.mobile.core.model.Parcelize

@Parcelize
data class SavingsAccountTemplate(
    val clientId: Int = 0,
    val clientName: String? = null,
    val withdrawalFeeForTransfers: Boolean? = null,
    val allowOverdraft: Boolean? = null,
    val enforceMinRequiredBalance: Boolean? = null,
    val withHoldTax: Boolean? = null,
    val isDormancyTrackingActive: Boolean? = null,
    val productOptions: ArrayList<ProductOptions> = arrayListOf(),
    val chargeOptions: ArrayList<ChargeOptions> = arrayListOf(),
) : Parcelable
