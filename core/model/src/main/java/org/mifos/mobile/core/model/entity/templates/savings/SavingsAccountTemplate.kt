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

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavingsAccountTemplate(
    var clientId: Int = 0,
    var clientName: String? = null,
    var withdrawalFeeForTransfers: Boolean? = null,
    var allowOverdraft: Boolean? = null,
    var enforceMinRequiredBalance: Boolean? = null,
    var withHoldTax: Boolean? = null,
    var isDormancyTrackingActive: Boolean? = null,
    var productOptions: ArrayList<ProductOptions> = ArrayList<ProductOptions>(),
    var chargeOptions: ArrayList<ChargeOptions> = ArrayList<ChargeOptions>(),
) : Parcelable
