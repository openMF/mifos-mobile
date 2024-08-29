/*
 * Copyright 2024 Mifos Initiative
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at https://mozilla.org/MPL/2.0/.
 *
 * See https://github.com/openMF/mobile-mobile/blob/master/LICENSE.md
 */
package org.mifos.mobile.core.model.entity.accounts.savings

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SavingsAccountApplicationPayload(

    var submittedOnDate: String? = null,

    var clientId: Int? = null,

    var productId: Int? = null,

    var locale: String = "en",

    var dateFormat: String = "dd MMMM yyyy",

) : Parcelable
